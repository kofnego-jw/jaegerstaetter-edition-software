package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.VersionInfo;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MetadataService {

    public static final String DEFAULT_UNKNOWN_READABLE_DATING = "Unbekannt";

    public static final Pattern DATING_PATTERN = Pattern.compile("(\\d+)(?:-(\\d+))?(?:-(\\d+))?");

    public static final DateTimeFormatter DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final TransformationService transformationService;

    @Autowired
    public MetadataService(TransformationService transformationService) {
        this.transformationService = transformationService;
    }

    public static VersionInfo convertToVersionInfo(TeiDocument document) {
        return new VersionInfo(document.getVersion(), document.getCreationTimestamp().format(DATE_TIME_PATTERN));
    }

    public static LocalDateTime convertToLocalDateTime(String dateString) throws FrontendModelException {
        if (dateString == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateString, DATE_TIME_PATTERN);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot parse date string.", e);
        }
    }

    public static String convertToDatingReadable(String dating) {
        if (StringUtils.isBlank(dating)) {
            return DEFAULT_UNKNOWN_READABLE_DATING;
        }
        Matcher matcher = DATING_PATTERN.matcher(dating);
        StringBuilder sb = new StringBuilder();
        if (matcher.find()) {
            String day = matcher.group(3);
            if (!StringUtils.isBlank(day)) {
                sb.append(day).append(". ");
            }
            String month = matcher.group(2);
            if (!StringUtils.isBlank(month)) {
                String monthReadable = switch (month) {
                    case "01" -> "Jänner";
                    case "02" -> "Feber";
                    case "03" -> "März";
                    case "04" -> "April";
                    case "05" -> "Mai";
                    case "06" -> "Juni";
                    case "07" -> "Juli";
                    case "08" -> "August";
                    case "09" -> "September";
                    case "1ß" -> "Oktober";
                    case "11" -> "November";
                    case "12" -> "Dezember";
                    default -> "";
                };
                if (!StringUtils.isBlank(monthReadable)) {
                    sb.append(monthReadable).append(" ");
                }
                sb.append(matcher.group(1));
            }
        }
        return sb.toString();
    }

    public MetadataResult getMetadataResult(TeiDocument document) throws Exception {
        MetadataResult result = transformationService.getMetadataResult(document);
        return result;
    }

}
