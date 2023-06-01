package at.kulinz.jaegerstaetter.index.service;

import at.kulinz.jaegerstaetter.index.TestBase;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexServiceImpl;
import at.kulinz.jaegerstaetter.lucenebase.IndexConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class IndexServiceTest extends TestBase {


    @Autowired
    private PreviewIndexServiceImpl indexService;

    @Autowired
    private File previewLuceneDir;

    @BeforeEach
    public void eraseIndex() throws Exception {
        indexService.clearIndex();
    }

    @Test
    public void index() throws Exception {
        indexService.updateLuceneIndex(TestConstants.DOC_V1_01);
        indexService.updateLuceneIndex(TestConstants.DOC_V1_02_0A);
        indexService.updateLuceneIndex(TestConstants.DOC_V1_02_0B);
        indexService.updateLuceneIndex(TestConstants.DOC_V2_01);

        FSDirectory directory = FSDirectory.open(previewLuceneDir.toPath());
        IndexReader reader = DirectoryReader.open(directory);
        for (int i = 0; i < reader.numDocs(); i++) {
            Document document = reader.document(i);
            System.out.println(document.getField(IndexConstants.ID_FIELDNAME));
        }

        Assertions.assertEquals(3, reader.numDocs());
    }
}
