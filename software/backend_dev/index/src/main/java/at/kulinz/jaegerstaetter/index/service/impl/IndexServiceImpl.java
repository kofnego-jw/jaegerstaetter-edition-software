package at.kulinz.jaegerstaetter.index.service.impl;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexField;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexFieldname;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexSortedField;
import at.kulinz.jaegerstaetter.index.IndexException;
import at.kulinz.jaegerstaetter.index.service.IndexService;
import at.kulinz.jaegerstaetter.lucenebase.IndexConstants;
import at.kulinz.jaegerstaetter.lucenebase.JaegerstaetterAnalyzer;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexServiceImpl implements IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    private static final FieldType KEYWORD;

    static {
        KEYWORD = new FieldType();
        KEYWORD.setStored(true);
        KEYWORD.setIndexOptions(IndexOptions.DOCS);
        KEYWORD.setTokenized(false);
        KEYWORD.freeze();
    }

    private final File luceneDir;

    public IndexServiceImpl(File luceneDir) {
        this.luceneDir = luceneDir;
    }

    private static String joinStringList(List<String> strings) {
        if (strings == null) {
            return "";
        }
        return String.join(" / ", strings);
    }

    public Directory getDirectory() throws Exception {
        return FSDirectory.open(luceneDir.toPath());
    }

    private Analyzer getAnalyzer() {
        return JaegerstaetterAnalyzer.getAnalyzer();
    }

    private IndexWriterConfig getIndexWriterConfig() {
        Analyzer analyzer = getAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setCommitOnClose(true);
        return config;
    }

    private IndexWriter getIndexWriter() throws Exception {
        Directory directory = getDirectory();
        IndexWriterConfig config = getIndexWriterConfig();
        return new IndexWriter(directory, config);
    }


    private IndexableField createField(IndexFieldname fieldname, String content) {
        return switch (fieldname.fieldType) {
            case STRING -> new StringField(fieldname.fieldname, content, Field.Store.YES);
            case TEXT -> new TextField(fieldname.fieldname, content, Field.Store.YES);
            case BOOLEAN -> new StringField(fieldname.fieldname, content, Field.Store.NO);
            case KEYWORD -> new StoredField(fieldname.fieldname, content);
            case KEYWORD_INDEXED -> new StringField(fieldname.fieldname, content, Field.Store.YES);
        };
    }

    private List<IndexableField> createField(IndexField field) {
        if (field == null || field.contents == null) {
            return Collections.emptyList();
        }
        return field.contents
                .stream()
                .map(content -> createField(field.fieldname, content))
                .collect(Collectors.toList());
    }

    private Document createDocument(String resourceId, String documentId, List<IndexField> fields) {
        if (StringUtils.isBlank(resourceId)) {
            return null;
        }
        if (StringUtils.isBlank(documentId)) {
            documentId = resourceId;
        }
        Document document = new Document();
        document.add(new StoredField(IndexConstants.ID_FIELDNAME, documentId, KEYWORD));
        document.add(new StoredField(IndexConstants.RESOURCEID_FIELDNAME, resourceId));
        document.add(new StoredField(IndexConstants.DOCUMENTID_FIELDNAME, documentId));
        for (IndexField field : fields) {
            List<IndexableField> indexableFields = createField(field);
            indexableFields.forEach(document::add);
            if (field.contents != null && !field.contents.isEmpty()) {
                Stream.of(IndexSortedField.values()).filter(sf -> sf.mainField == field.fieldname)
                        .forEach(sf -> {
                            SortedDocValuesField sortField = new SortedDocValuesField(sf.fieldname, new BytesRef(field.contents.get(0)));
                            document.add(sortField);
                        });
            }
        }
        return document;
    }

    @Override
    public synchronized void updateLuceneIndex(String resourceId, String documentId, List<IndexField> fields) {
        try (IndexWriter indexWriter = getIndexWriter()) {
            Document document = createDocument(resourceId, documentId, fields);
            if (document == null) {
                return;
            }
            Term idTerm = new Term(IndexConstants.ID_FIELDNAME, documentId);
            indexWriter.updateDocument(idTerm, document);
            indexWriter.commit();
        } catch (Exception e) {
            LOGGER.warn("Error during indexing.", e);
        }
    }

    @Override
    public void close() throws Exception {
        getDirectory().close();
    }

    @Override
    public void clearIndex() throws IndexException {
        LOGGER.warn("Clear index.");
        try (IndexWriter indexWriter = getIndexWriter()) {
            indexWriter.deleteAll();
            indexWriter.commit();
        } catch (Exception e) {
            throw new IndexException("Cannot clear index.", e);
        }
    }
}
