package at.kulinz.jaegerstaetter.metadata.registry.repository;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonNameHelper {

    public static final Pattern PREFERREDNAME_WITH_MAIDENNAME = Pattern.compile("\\s*((?:[^,]+,)?\\s*geb\\.\\s*[^,]+)(?:,\\s*([^(]+))?(?:\\((.*)\\))?\\s*$");

    public static final Pattern PREFERRED_NAME_PATTERN = Pattern.compile("^\\s*([^,(]+)(?:,\\s*([^(]+))?(?:\\((.*)\\))?\\s*$");

    public static NameTriple convertToNameTriple(String preferredName) {
        if (StringUtils.isBlank(preferredName)) {
            return NameTriple.EMPTY;
        }
        Matcher mat = PREFERREDNAME_WITH_MAIDENNAME.matcher(preferredName);
        if (mat.find()) {
            return new NameTriple(mat.group(1), mat.group(2), mat.group(3));
        }
        mat = PREFERRED_NAME_PATTERN.matcher(preferredName);
        if (mat.find()) {
            return new NameTriple(mat.group(1), mat.group(2), mat.group(3));
        }
        return NameTriple.EMPTY;
    }

    public static String blankOrTrim(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        return s.trim();
    }

}
