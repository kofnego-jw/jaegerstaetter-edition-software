package at.kulinz.jaegerstaetter.photodoc.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroup;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentItem;
import at.kulinz.jaegerstaetter.photodoc.TestBase;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class PhotoDocExcelImportServiceTest extends TestBase {

    private static final File EXCEL = new File("src/test/resources/Fotos_Dokumente_Auswahl.xlsx");
    @Autowired
    private PhotoDocExcelImportService photoDocExcelImportService;

    private static TeiDocument mockTeiDocument(File file) throws Exception {
        byte[] content = FileUtils.readFileToByteArray(file);
        return new TeiDocument("/ingest/photodoc/" + file.getName(), content, LocalDateTime.now());
    }

    @BeforeAll
    public static void checkExcelFile() {
        Assumptions.assumeTrue(EXCEL.exists());
    }

    @Test
    public void test() throws Exception {
        Assertions.assertNotNull(photoDocExcelImportService);
        List<PhotoDocumentGroup> groups = photoDocExcelImportService.fromExcel(mockTeiDocument(EXCEL));
        for (PhotoDocumentGroup group : groups) {
            System.out.println(group.groupKey);
            System.out.println(group.groupTitle);
            for (PhotoDocumentItem item : group.items) {
                System.out.println(item.id + " " + item.signature + ": " + item.content + " / " + item.jpegs);
            }
        }
    }
}
