package at.kulinz.jaegerstaetter.frontendmodel.dtomsg;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchResult;

public class SearchResultMsg extends BasicMsg {

    public final SearchResult searchResult;

    public SearchResultMsg(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public SearchResultMsg(String message) {
        super(message);
        this.searchResult = null;
    }
}
