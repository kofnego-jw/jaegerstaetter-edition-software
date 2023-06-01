package at.kulinz.jaegerstaetter.photodoc.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.tei.edition.connector.repoobj.PhotoDocumentJsonRepo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PhotoDocExcelImportService {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    private static PhotoColor toColor(String val) {
        if (StringUtils.isBlank(val)) {
            return null;
        }
        return switch (val.toLowerCase()) {
            case "s-w" -> PhotoColor.BLACK_WHITE;
            case "farbe" -> PhotoColor.COLOR;
            default -> null;
        };
    }

    private static PhotoType toType(String val) {
        if (StringUtils.isBlank(val)) {
            return null;
        }
        return switch (val.toLowerCase()) {
            case "repro" -> PhotoType.REPRODUCTION;
            case "neg" -> PhotoType.NEGATIVE;
            case "pos" -> PhotoType.POSITIVE;
            default -> null;
        };
    }

    private static String normalizeJpgEntry(String s) {
        return s.replaceAll("\\.(jpg|JPG)$", "");
    }

    private static List<String> fromJpgEntryToJpegs(String jpg) {
        if (StringUtils.isBlank(jpg)) {
            return Collections.emptyList();
        }
        if (!jpg.contains(";")) {
            return List.of(normalizeJpgEntry(jpg));
        }
        return Stream.of(jpg.split("\\s*;\\s*")).filter(StringUtils::isNotBlank)
                .map(PhotoDocExcelImportService::normalizeJpgEntry).collect(Collectors.toList());
    }

    public PhotoDocumentJsonRepo fromExcel(List<TeiDocument> excelSSs) throws FrontendModelException {
        List<PhotoDocumentGroup> groups = new ArrayList<>();
        try {
            for (TeiDocument doc : excelSSs) {
                groups.addAll(fromExcel(doc));
            }
        } catch (Exception e) {
            throw new FrontendModelException("Cannot process Excel files.", e);
        }
        return new PhotoDocumentJsonRepo(groups);
    }

    public List<PhotoDocumentGroup> fromExcel(TeiDocument excel) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(excel.getContent());
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        List<PhotoDocumentGroup> groups = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            PhotoDocumentGroup group = fromSheet(sheet, i);
            if (group != null) {
                groups.add(group);
            }
        }
        return groups;
    }

    private Map<String, Integer> containsAllHeaders(Row row, PhotoDocumentGroupType type) {
        if (row == null) {
            return null;
        }
        Set<String> allHeaders = switch (type) {
            case DOCUMENT -> new HashSet<>(PhotoDocExcelConstants.ALL_DOCUMENT_HEADERS);
            default -> new HashSet<>(PhotoDocExcelConstants.ALL_PHOTOS_HEADERS);
        };
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : row) {
            if (cell == null || cell.getCellType() == null) {
                continue;
            }
            String strValue = DATA_FORMATTER.formatCellValue(cell).toLowerCase().trim();
            if (!StringUtils.isBlank(strValue)) {
                allHeaders.remove(strValue);
                map.put(strValue, cell.getColumnIndex());
            }
        }
        if (allHeaders.isEmpty()) {
            return map;
        }
        if (allHeaders.size() == 1 && allHeaders.contains(PhotoDocExcelConstants.JPEGS_COLUMN.toLowerCase())) {
            Integer sigIndex = map.get(PhotoDocExcelConstants.SIGNATURE_COLUMN.toLowerCase());
            map.put(PhotoDocExcelConstants.JPEGS_COLUMN, sigIndex);
            return map;
        }
        System.out.println("Not found: " + allHeaders);
        return null;
    }

    private PhotoDocumentGroup fromSheet(Sheet sheet, int sheetNumber) {
        String sheetName = sheet.getSheetName();
        if (StringUtils.isBlank(sheetName)) {
            sheetName = Integer.toString(sheetNumber + 1);
        }
        PhotoDocumentGroupType type = sheetName.toLowerCase().startsWith("fotos") ? PhotoDocumentGroupType.PHOTO : PhotoDocumentGroupType.DOCUMENT;
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            return null;
        }
        String title = sheetName;
        if (title.contains("_")) {
            title = title.substring(title.indexOf("_") + 1);
        }
        if (StringUtils.isBlank(title)) {
            return null;
        }
        int headerRowNum = 0;
        Map<String, Integer> headerMap = null;
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Map<String, Integer> hMap = containsAllHeaders(row, type);
            if (hMap != null) {
                headerRowNum = i;
                headerMap = hMap;
                break;
            }
        }
        if (headerMap == null) {
            return null;
        }
        List<PhotoDocumentItem> items = new ArrayList<>();
        for (int i = headerRowNum + 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            PhotoDocumentItem item = createItem(row, headerMap);
            if (item != null) {
                items.add(item);
            }
        }
        return new PhotoDocumentGroup(type, sheetName, title, items);
    }

    private String getValueAsString(Row row, Map<String, Integer> headerMap, String header) {
        Integer cellNum = headerMap.get(header);
        if (cellNum == null) {
            return null;
        }
        if (row == null) {
            return null;
        }
        String strValue = DATA_FORMATTER.formatCellValue(row.getCell(cellNum));
        return StringUtils.isBlank(strValue) ? null : strValue;
    }

    private PhotoDocumentItem createItem(Row row, Map<String, Integer> headerMap) {
        String id = String.format("%03d", row.getRowNum());
        String signature = getValueAsString(row, headerMap, PhotoDocExcelConstants.SIGNATURE_COLUMN);
        if (StringUtils.isAnyBlank(id, signature)) {
            return null;
        }
        String title = getValueAsString(row, headerMap, PhotoDocExcelConstants.TITLE_COLUMN);
        String dating = getValueAsString(row, headerMap, PhotoDocExcelConstants.DATING_COLUMN);
        String notBefore = dating;
        String notAfter = dating;
        String place = getValueAsString(row, headerMap, PhotoDocExcelConstants.PLACE_COLUMN);
        String provenience = getValueAsString(row, headerMap, PhotoDocExcelConstants.PROVENIENCE_COLUMN);
        String content = getValueAsString(row, headerMap, PhotoDocExcelConstants.CONTENT_COLUMN);
        String copyright = getValueAsString(row, headerMap, PhotoDocExcelConstants.COPYRIGHT_COLUMN);
        String jpg = getValueAsString(row, headerMap, PhotoDocExcelConstants.JPEGS_COLUMN);
        if (StringUtils.isBlank(jpg)) {
            jpg = signature;
        }
        String pageCount = getValueAsString(row, headerMap, PhotoDocExcelConstants.PAGESCOUNT_COLUMN);
        if (StringUtils.isBlank(pageCount)) {
            pageCount = "1";
        }
        List<String> jpgs = fromJpgEntryToJpegs(jpg);
        return new PhotoDocumentItem(id, signature, title, dating, notBefore, notAfter, place, provenience, content, copyright, pageCount, jpgs);
    }

}
