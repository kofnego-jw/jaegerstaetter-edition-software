package at.kulinz.jaegerstaetter.index.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexField;
import at.kulinz.jaegerstaetter.index.IndexException;

import java.util.List;

public interface IndexService extends AutoCloseable {

    void updateLuceneIndex(String resourceId, String documentId, List<IndexField> fields) throws IndexException;

    default void updateLuceneIndex(IndexDocument indexDocument) throws IndexException {
        updateLuceneIndex(indexDocument.resourceId, indexDocument.documentId, indexDocument.fields);
    }

    default void updateLuceneIndex(List<IndexDocument> docs) throws IndexException {
        if (docs == null) {
            return;
        }
        for (IndexDocument doc : docs) {
            updateLuceneIndex(doc);
        }
    }

    void clearIndex() throws IndexException;
}
