package at.kulinz.jaegerstaetter.index.service.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.index.service.IndexResourceService;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PreviewIndexResourceService implements IndexResourceService {

    private final IndexResourceServiceImpl delegate;

    @Autowired
    public PreviewIndexResourceService(JaegerstaetterConfig config) {
        this.delegate = new IndexResourceServiceImpl(config.previewLuceneDir());
    }

    @Override
    public List<ResourceFWDTO> listAllResources() {
        return delegate.listAllResources();
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    @Override
    public Optional<ResourceFWDTO> fetchById(String id) {
        return delegate.fetchById(id);
    }

    @Override
    public ResourceFWDTO fromLuceneDoc(Document document) {
        return delegate.fromLuceneDoc(document);
    }

    @Override
    public List<String> getKeyValues(String type) {
        return delegate.getKeyValues(type);
    }
}
