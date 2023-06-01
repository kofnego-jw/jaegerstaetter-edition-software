package at.kulinz.jaegerstaetter.search.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexServiceImpl;
import at.kulinz.jaegerstaetter.search.TestBase;
import at.kulinz.jaegerstaetter.search.service.impl.PreviewSearchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class SearchServiceTest extends TestBase {

    @Autowired
    PreviewIndexServiceImpl indexingService;

    @Autowired
    PreviewSearchService searchingService;

    @BeforeEach
    public void index() throws Exception {
        indexingService.updateLuceneIndex(TestConstants.DOC_V1_01);
        indexingService.updateLuceneIndex(TestConstants.DOC_V1_02_0A);
        indexingService.updateLuceneIndex(TestConstants.DOC_V1_02_0B);
    }

    @AfterEach
    public void eraseIndex() throws Exception {
        indexingService.clearIndex();
    }


    @Test
    public void searching() throws Exception {
        Mockito.when(editionResourceService.findResourceFWByIdAndDocumentId("01", null))
                .thenReturn(new ResourceFWDTO("01", "title", "dating", "datingReadable", ResourceType.LETTER, "summary text",
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), "", ""));
        Mockito.when(editionResourceService.findResourceFWByIdAndDocumentId("02", null))
                .thenReturn(new ResourceFWDTO("02", "title", "dating", "datingReadable", ResourceType.LETTER, "summary text",
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), "", ""));

        SearchRequest req1 = new SearchRequest(Collections.emptyList(), 0, 10, null, true);
        SearchResult result1 = searchingService.search(req1);
        Assertions.assertEquals(0, result1.totalHits);
        SearchRequest req2 = new SearchRequest(List.of(
                new SearchFieldParam(SearchField.TRANSCRIPT, "two", SearchOccur.MUST)
        ), 0, 10, null, true);
        SearchResult result2 = searchingService.search(req2);
        Assertions.assertEquals(2, result2.totalHits);
    }
}
