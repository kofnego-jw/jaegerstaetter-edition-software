package at.kulinz.jaegerstaetter.datamodel.existdb;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RepoHelperTest {

    @Test
    public void test() throws Exception {
        LocalDateTime creation = LocalDateTime.of(2022, 4, 22, 12, 0);
        LocalDateTime deletion = LocalDateTime.of(2022, 4, 22, 13, 15);
        TeiDocument doc1 = new TeiDocument("base/test.txt", null, creation);
        TeiDocument doc2 = doc1.nextVersion(null, deletion);
        Assertions.assertEquals(doc1.getFilePath(), RepoHelper.teiDocumentToCurrentUrl(doc1));
        Assertions.assertEquals(doc1.getFilePath(), RepoHelper.teiDocumentToCurrentUrl(doc2));
        Assertions.assertEquals("base/test.txt_versions/001_202204221200-202204221315",
                RepoHelper.teiDocumentToVersionedUrl(doc1));
        Assertions.assertEquals("base/test.txt_versions/002_202204221315",
                RepoHelper.teiDocumentToVersionedUrl(doc2));

    }
}
