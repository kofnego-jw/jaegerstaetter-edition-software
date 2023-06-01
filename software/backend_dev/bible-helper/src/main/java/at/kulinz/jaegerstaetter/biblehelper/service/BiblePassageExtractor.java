package at.kulinz.jaegerstaetter.biblehelper.service;

import at.kulinz.jaegerstaetter.biblehelper.BiblePassage;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryBibleBook;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiblePassageExtractor {

    private static final Pattern POS_PATTERN = Pattern.compile("(\\d+)(?:[,.]\\s*(\\d+))?");

    public static List<BiblePassage> getPassages(String text) {
        if (text == null) {
            return Collections.emptyList();
        }
        List<BiblePassage> passages = new ArrayList<>();
        Matcher mat = BibleBookAbbr.BIBLE_PASSAGE_PATTERN.matcher(text);
        while (mat.find()) {
            String position = mat.group(2).trim();
            String book = findBookAbbr(mat.group(1).trim());
            BiblePassage passage = new BiblePassage(
                    book, position,
                    toOrderString(book, position));
            passages.add(passage);
        }
        return passages;
    }

    public static String findBookAbbr(String abbr) {
        if (StringUtils.isBlank(abbr)) {
            return "";
        }
        String test = abbr.toLowerCase().replace(" ", "");
        for (String book : BibleBookAbbr.BOOK_ABBR_LIST) {
            if (book.toLowerCase().replace(" ", "").equals(test)) {
                return book;
            }
        }
        return "";
    }

    public static String toOrderString(String book, String pos) {
        String bookPos = String.format("%03d", BibleBookAbbr.BOOK_ABBR_LIST.indexOf(book) + 1) + "|";
        if (StringUtils.isBlank(pos)) {
            return bookPos + "000|000";
        }
        Matcher mat = POS_PATTERN.matcher(pos);
        if (mat.find()) {
            String chap = String.format("%03d", Integer.parseInt(mat.group(1)));
            String ver = "000";
            if (mat.group(2) != null) {
                ver = String.format("%03d", Integer.parseInt(mat.group(2)));
            }
            return bookPos + chap + "|" + ver;
        }
        return bookPos + "000|000";
    }

    public static RegistryEntryBibleBook fromBookAbbr(String abbr, int counter) {
        String orderString = toOrderString(abbr, null);
        return new RegistryEntryBibleBook(abbr, orderString, abbr, counter);
    }

}
