package at.kulinz.jaegerstaetter.index.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceType;
import at.kulinz.jaegerstaetter.index.TestBase;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexResourceService;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class IndexResourceServiceTest extends TestBase {

    @Autowired
    private PreviewIndexServiceImpl indexService;

    @Autowired
    private PreviewIndexResourceService indexResourceService;

    @BeforeEach
    public void clearAndInserNewData() throws Exception {
        indexService.clearIndex();
        indexService.updateLuceneIndex(TestConstants.RES1);
        indexService.updateLuceneIndex(TestConstants.RES2);
        indexService.updateLuceneIndex(TestConstants.RES3);
    }

    @Test
    public void test() throws Exception {
        Assertions.assertTrue(indexResourceService.fetchById("doc0").isEmpty());
        Optional<ResourceFWDTO> doc1Opt = indexResourceService.fetchById("doc1");
        Assertions.assertTrue(doc1Opt.isPresent());
        ResourceFWDTO fw1 = doc1Opt.get();
        Assertions.assertEquals("doc1", fw1.id);
        Assertions.assertEquals("doc1-title", fw1.title);
        Assertions.assertEquals("doc1-dating", fw1.dating);
        Assertions.assertEquals("doc1-datingReadable", fw1.datingReadable);
        Assertions.assertEquals(ResourceType.LETTER, fw1.type);
        Assertions.assertEquals("doc1-summary", fw1.summary);
    }
}
