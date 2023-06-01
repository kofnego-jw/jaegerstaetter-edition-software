package at.kulinz.jaegerstaetter.biblehelper;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Objects;

public class BiblePassage {

    public static final Comparator<BiblePassage> COMPARATOR = (p1, p2) -> {
        if (p1 == null || StringUtils.isBlank(p1.orderString)) {
            if (p2 == null || StringUtils.isBlank(p2.orderString)) {
                return 0;
            }
            return 1;
        }
        if (p2 == null || StringUtils.isBlank(p2.orderString)) {
            return -1;
        }
        return p1.orderString.compareTo(p2.orderString);
    };

    public final String bookAbbr;
    public final String position;
    public final String orderString;

    public BiblePassage(String bookAbbr, String position, String orderString) {
        this.bookAbbr = bookAbbr;
        this.position = position;
        this.orderString = orderString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiblePassage passage = (BiblePassage) o;
        return Objects.equals(bookAbbr, passage.bookAbbr) && Objects.equals(position, passage.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookAbbr, position);
    }

    @Override
    public String toString() {
        return "BiblePassage{" +
                "bookAbbr='" + bookAbbr + '\'' +
                ", position='" + position + '\'' +
                ", orderString='" + orderString + '\'' +
                '}';
    }
}
