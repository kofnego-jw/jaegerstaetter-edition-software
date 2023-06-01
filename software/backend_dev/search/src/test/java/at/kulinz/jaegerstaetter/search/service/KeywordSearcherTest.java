package at.kulinz.jaegerstaetter.search.service;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class KeywordSearcherTest {
    private static final File LUCENE_DIR = new File("target/test/luceneDir");

    @BeforeEach
    public void setup() throws Exception {
        if (LUCENE_DIR.exists()) {
            FileUtils.deleteDirectory(LUCENE_DIR);
        }
        LUCENE_DIR.mkdirs();
    }

    private Analyzer analyzer() {
        return new KeywordAnalyzer();
    }

    private FieldType fieldType() {
        FieldType type = new FieldType();
        type.setTokenized(false);
        type.setIndexOptions(IndexOptions.DOCS);
        type.setStored(true);
        return type;
    }

    private IndexableField field(String value) {
        //Field field = new Field("keys", value, fieldType());
        StringField field = new StringField("keys", value, Field.Store.YES);
        return field;
    }

    private IndexWriterConfig config() {
        IndexWriterConfig config = new IndexWriterConfig();
        config.setCommitOnClose(true);
        return config;
    }


    @Test
    public void test() throws Exception {
        FSDirectory directory = FSDirectory.open(LUCENE_DIR.toPath());
        Document doc1 = new Document();
        doc1.add(new StoredField("id", "doc1"));
        doc1.add(field("key one"));
        doc1.add(field("key two"));

        Document doc2 = new Document();
        doc2.add(new StoredField("id", "doc2"));
        doc2.add(field("key one more"));
        doc2.add(field("key two"));

        IndexWriter writer = new IndexWriter(directory, config());
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        writer.close();

        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("keys", analyzer());
        Query query = parser.parse("key one");
        TopDocs topDocs = searcher.search(query, 100);
        Assertions.assertEquals(1L, topDocs.totalHits.value);

        query = parser.parse("key two");
        topDocs = searcher.search(query, 100);
        for (ScoreDoc doc : topDocs.scoreDocs) {
            Document indexed = reader.document(doc.doc);
            System.out.println("hit in: " + indexed.getField("id"));
            IndexableField[] keys = indexed.getFields("keys");
            System.out.println(keys.length);
            for (IndexableField key : keys) {
                System.out.println(key.stringValue());
            }
        }
        System.out.println("finished");

    }
}
