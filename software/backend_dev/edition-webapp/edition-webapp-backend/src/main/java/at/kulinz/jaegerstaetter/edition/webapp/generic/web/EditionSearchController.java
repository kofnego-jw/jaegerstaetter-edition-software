package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.SearchResultMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchResult;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.SearchControllerApi;
import at.kulinz.jaegerstaetter.search.service.impl.EditionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("edition")
public class EditionSearchController implements SearchControllerApi {

    @Autowired
    private EditionSearchService searchService;

    @Override
    public SearchResultMsg search(SearchRequest request) throws FrontendModelException {
        SearchResult result = searchService.search(request);
        return ControllerMsgHelper.fromSearchResult(result);
    }
}
