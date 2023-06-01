package at.kulinz.jaegerstaetter.search.service;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LuceneSortTest {

    private static final File TEST_LUCENE_DIR = new File("target/test/lucenesort");

    @BeforeAll
    public static void setUpDir() throws Exception {
        if (TEST_LUCENE_DIR.exists()) {
            FileUtils.deleteDirectory(TEST_LUCENE_DIR);
        }
        TEST_LUCENE_DIR.mkdirs();
    }

    @BeforeEach
    public void inserData() throws Exception {
        try (FSDirectory directory = FSDirectory.open(TEST_LUCENE_DIR.toPath())) {
            IndexWriterConfig config = new IndexWriterConfig();
            IndexWriter writer = new IndexWriter(directory, config);

            Document d1 = new Document();
            d1.add(new StoredField("id", "A"));
            d1.add(new SortedDocValuesField("_id_sort", new BytesRef("A")));
            writer.addDocument(d1);
            Document d2 = new Document();
            d2.add(new StoredField("id", "C"));
            d2.add(new SortedDocValuesField("_id_sort", new BytesRef("C")));
            Document d3 = new Document();
            writer.addDocument(d2);
            d3.add(new StoredField("id", "B"));
            d3.add(new SortedDocValuesField("_id_sort", new BytesRef("B")));
            writer.addDocument(d3);
            Document d4 = new Document();
            d4.add(new StoredField("id", "BB"));
            d4.add(new SortedDocValuesField("_id_sort", new BytesRef("BB")));
            writer.addDocument(d4);
            writer.commit();
        }

    }

    @Test
    public void test() throws Exception {
        try (FSDirectory directory = FSDirectory.open(TEST_LUCENE_DIR.toPath())) {
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Query q = new MatchAllDocsQuery();
            TopFieldDocs results = searcher.search(q, 10, new Sort(new SortField("_id_sort", SortField.Type.STRING, true)));
            System.out.println(results.totalHits);
            for (ScoreDoc d : results.scoreDocs) {
                System.out.println(reader.document(d.doc).getField("id"));
                System.out.println(reader.document(d.doc).getField("_id_sort"));
            }
        }
    }
}
