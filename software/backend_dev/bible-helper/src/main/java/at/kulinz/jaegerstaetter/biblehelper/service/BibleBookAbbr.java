package at.kulinz.jaegerstaetter.biblehelper.service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BibleBookAbbr {

    public static final String ABBR = "Gen|" +
            "Ex|" +
            "Lev|" +
            "Num|" +
            "Dtn|" +
            "Jos|" +
            "Ri|" +
            "Rut|" +
            "1 Sam|" +
            "2 Sam|" +
            "1 Kön|" +
            "2 Kön|" +
            "1 Chr|" +
            "2 Chr|" +
            "Esr|" +
            "Neh|" +
            "Tob|" +
            "Jdt|" +
            "Est|" +
            "Ps|" +
            "Spr|" +
            "Ijob|" +
            "Koh|" +
            "Hld|" +
            "Weish|" +
            "Sir|" +
            "Jes|" +
            "Jer|" +
            "Klgl|" +
            "Bar|" +
            "Ez|" +
            "Dan|" +
            "Hos|" +
            "Joel|" +
            "Am|" +
            "Obd|" +
            "Jon|" +
            "Mi|" +
            "Nah|" +
            "Hab|" +
            "Zef|" +
            "Hag|" +
            "Sach|" +
            "Mal|" +
            "1 Makk|" +
            "2 Makk|" +
            "3 Makk|" +
            "4 Makk|" +
            "Mt|" +
            "Mk|" +
            "Lk|" +
            "Joh|" +
            "Apg|" +
            "Röm|" +
            "1 Kor|" +
            "2 Kor|" +
            "Gal|" +
            "Eph|" +
            "Phil|" +
            "Kol|" +
            "1 Thess|" +
            "2 Thess|" +
            "1 Tim|" +
            "2 Tim|" +
            "Tit|" +
            "Phlm|" +
            "Hebr|" +
            "Jak|" +
            "1 Petr|" +
            "2 Petr|" +
            "1 Joh|" +
            "2 Joh|" +
            "3 Joh|" +
            "Jud|" +
            "Offb";

    public static final List<String> BOOK_ABBR_LIST = Stream.of(ABBR.split("\\|")).collect(Collectors.toList());

    public static final Pattern BIBLE_PASSAGE_PATTERN = Pattern.compile("(" + ABBR.replace(" ", "\\s*") +
            ")\\s+(" +
            "\\d+([,.])\\s*\\d+\\s*-\\s*\\d+([,.])\\s*\\d+|" +
            "\\d+([,.])\\s*\\d+\\s*-\\s*\\d+|" +
            "\\d+\\s*-\\s*\\d+([,.])\\s*\\d+|" +
            "\\d+\\s*-\\s*\\d+|" +
            "\\d+([,.])\\s*\\d+|" +
            "\\d+" +
            ")");

    // public static final Pattern BIBLE_PASSAGE_PATTERN = Pattern.compile("(" + ABBR.replace(" ", "\\s*") +
    //        ")\\s+(\\d+(\\s*-\\s*\\d+,\\s*\\d+|,\\s*\\d+|,\\s*\\d+-\\d+|,\\s*\\d+-\\d+,\\s*\\d+|\\s*-\\s*\\d+))");

}
