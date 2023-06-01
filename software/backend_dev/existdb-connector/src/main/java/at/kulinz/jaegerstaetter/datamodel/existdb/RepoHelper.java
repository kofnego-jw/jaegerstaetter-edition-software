package at.kulinz.jaegerstaetter.datamodel.existdb;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepoHelper {

    private static final Pattern VERSIONED_URL_PATTERN = Pattern.compile("^(.*)_versions/(\\d+)_(\\d+)(?:-(\\d+))?$");

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public static String findBase(String s) {
        if (s.contains("/")) {
            return s.substring(0, s.lastIndexOf("/") + 1);
        }
        return "";
    }

    public static String findName(String s) {
        if (s.contains("/")) {
            return s.substring(s.lastIndexOf("/") + 1);
        }
        return s;
    }

    public static String teiDocumentToCurrentUrl(TeiDocument doc) throws DataModelException {
        if (doc == null) {
            throw new DataModelException("Cannot create Url for null doc.");
        }
        return doc.getFilePath();
    }

    private static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    private static LocalDateTime fromString(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }

    public static String pathToVersionedUrl(String path) {
        return path + "_versions/";
    }

    public static String teiDocumentToVersionedUrl(TeiDocument doc) throws DataModelException {
        if (doc == null) {
            throw new DataModelException("Cannot create Url for null doc.");
        }
        String path = doc.getFilePath();
        String base = pathToVersionedUrl(path);
        String name = String.format("%03d", doc.getVersion()) + "_" +
                formatDate(doc.getCreationTimestamp());
        if (doc.isDeleted()) {
            name = name + "-" + formatDate(doc.getDeletionTimestamp());
        }
        return base + name;
    }

    public static TeiDocument createDocumentFromVersionedUrl(String versionedUrl, byte[] content) throws DataModelException {
        TeiDocumentFW fw = createDocumentFWFromVersionedUrl(versionedUrl);
        if (fw.getVersion() == 0) {
            throw new DataModelException("Cannot get version information from '" + versionedUrl + "'.");
        }
        return TeiDocument.fromTeiDocumentFW(fw, content);
    }

    public static TeiDocumentFW createDocumentFWFromVersionedUrl(String versionedUrl) {
        Matcher mat = VERSIONED_URL_PATTERN.matcher(versionedUrl);
        if (!mat.find()) {
            return new TeiDocumentFW(versionedUrl, 0, versionedUrl, LocalDateTime.now(), LocalDateTime.now());
        }
        String path = mat.group(1);
        String versionStr = mat.group(2);
        String creationStr = mat.group(3);
        String deletionStr = mat.group(4);
        Integer version = Integer.parseInt(versionStr, 10);
        LocalDateTime creation = fromString(creationStr);
        LocalDateTime deletion = fromString(deletionStr);
        return new TeiDocumentFW(path, version, versionedUrl, creation, deletion);
    }
}
