package at.kulinz.jaegerstaetter.xslt.extend.lucene;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.xslt.extend.XsltExtendException;

public interface HighlightingService {

    /**
     * Highlights a passage of text, may not highlight at all.
     *
     * @param request         the search request
     * @param textToHighlight the text to be highlighted
     * @param startTag        the start tag
     * @param endTag          the end tag
     * @return a text decorated with start and endtag if found
     * @throws XsltExtendException if any exception happens
     */
    String highlightText(SearchRequest request, String textToHighlight, String startTag, String endTag) throws XsltExtendException;

    String highlightText(String searchRequestJson, String textToHighlight, String startTag, String endTag) throws XsltExtendException;


}
