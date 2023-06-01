package at.kulinz.jaegerstaetter.tei.registry.service.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.tei.registry.service.RegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EditionRegistryRepository implements RegistryRepository {

    private final RegistryRepositoryImpl delegate;

    @Autowired
    public EditionRegistryRepository(ExistDBTeiNotVersionedRepository ingestRepository, ExistDBTeiRepository editionRepository, JaegerstaetterConfig config) {
        this.delegate = new RegistryRepositoryImpl(ingestRepository, editionRepository, config);
    }

    public TeiDocument update(String filePath, byte[] content) throws Exception {
        return delegate.update(filePath, content);
    }

    @Override
    public Optional<TeiDocument> getIngestRegistryJsonDocument() {
        return delegate.getIngestRegistryJsonDocument();
    }

    @Override
    public Optional<TeiDocument> getIngestRegistryXmlDocument() {
        return delegate.getIngestRegistryXmlDocument();
    }

    @Override
    public Optional<TeiDocument> getEditionRegistryJsonDocument() {
        return delegate.getEditionRegistryJsonDocument();
    }

    @Override
    public Optional<TeiDocument> getEditionRegistryXmlDocument() {
        return delegate.getEditionRegistryXmlDocument();
    }

    @Override
    public List<TeiDocument> getEditionRegistryJsonDocumentVersions() {
        return delegate.getEditionRegistryJsonDocumentVersions();
    }

    @Override
    public List<TeiDocument> getEditionRegistryXmlDocumentVersions() {
        return delegate.getEditionRegistryXmlDocumentVersions();
    }

    @Override
    public List<TeiDocument> updateRegistryToIngest(byte[] xml, byte[] json) throws Exception {
        return delegate.updateRegistryToIngest(xml, json);
    }

}
