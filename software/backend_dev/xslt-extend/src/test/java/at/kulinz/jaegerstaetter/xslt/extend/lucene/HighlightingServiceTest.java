package at.kulinz.jaegerstaetter.xslt.extend.lucene;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchField;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchFieldParam;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchOccur;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HighlightingServiceTest {

    private HighlightingService service = new HighlightingServiceImpl();

    @Test
    public void test() throws Exception {

        SearchRequest test = new SearchRequest(List.of(new SearchFieldParam(SearchField.ALL, "test", SearchOccur.SHOULD)), 0, 10, null, true);

        String s = service.highlightText(test, "Das ist ein Test mit Testfolgen.", "<span class='highlight'>", "</span>");
        Assertions.assertEquals("Das ist ein <span class='highlight'>Test</span> mit Testfolgen.", s);

        test = new SearchRequest(List.of(new SearchFieldParam(SearchField.ALL, "test*", SearchOccur.SHOULD)), 0, 10, null, true);
        s = service.highlightText(test, "Das ist ein Test mit Testfolgen.", "<span class='highlight'>", "</span>");
        Assertions.assertEquals("Das ist ein <span class='highlight'>Test</span> mit <span class='highlight'>Testfolgen</span>.", s);

        test = new SearchRequest(List.of(new SearchFieldParam(SearchField.ALL, "test", SearchOccur.MUST),
                new SearchFieldParam(SearchField.TRANSCRIPT, "folgen", SearchOccur.SHOULD)), 0, 10, null, true);
        s = service.highlightText(test, "Das ist ein Test mit Folgen.", "<span class='highlight'>", "</span>");
        Assertions.assertEquals("Das ist ein <span class='highlight'>Test</span> mit <span class='highlight'>Folgen</span>.", s);

        test = new SearchRequest(List.of(new SearchFieldParam(SearchField.ALL, "test folgen", SearchOccur.MUST)), 0, 10, null, true);
        s = service.highlightText(test, "Das ist ein Test mit Folgen.", "<span class='highlight'>", "</span>");

        Assertions.assertEquals("Das ist ein <span class='highlight'>Test</span> mit <span class='highlight'>Folgen</span>.", s);

    }

}
