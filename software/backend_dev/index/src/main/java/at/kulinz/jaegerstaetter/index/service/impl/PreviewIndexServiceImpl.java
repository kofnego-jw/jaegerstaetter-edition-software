package at.kulinz.jaegerstaetter.index.service.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexField;
import at.kulinz.jaegerstaetter.index.IndexException;
import at.kulinz.jaegerstaetter.index.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PreviewIndexServiceImpl implements IndexService {
    private final IndexServiceImpl delegate;

    @Autowired
    public PreviewIndexServiceImpl(JaegerstaetterConfig config) {
        this.delegate = new IndexServiceImpl(config.previewLuceneDir());
    }

    @Override
    public void updateLuceneIndex(String resourceId, String documentId, List<IndexField> fields) {
        delegate.updateLuceneIndex(resourceId, documentId, fields);
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    @Override
    public void clearIndex() throws IndexException {
        delegate.clearIndex();
    }
}
