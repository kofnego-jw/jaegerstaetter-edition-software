package at.kulinz.jaegerstaetter.tei.registry.service.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.tei.registry.service.RegistryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RegistryRepositoryImpl implements RegistryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryRepository.class);

    private static final Comparator<TeiDocument> BY_VERSION_REVERSED = Comparator.comparing(TeiDocument::getVersion).reversed();

    private final ExistDBTeiNotVersionedRepository ingestRepository;

    private final ExistDBTeiRepository publicationRepository;

    private final String jsonFilePath;

    private final String xmlFilePath;

    public RegistryRepositoryImpl(ExistDBTeiNotVersionedRepository ingestRepository, ExistDBTeiRepository publicationRepository, JaegerstaetterConfig config) {
        this.ingestRepository = ingestRepository;
        this.publicationRepository = publicationRepository;
        String base = config.getExistEditionRegistryDirName().endsWith("/") ? config.getExistEditionRegistryDirName() :
                config.getExistEditionRegistryDirName() + "/";
        this.jsonFilePath = base + JSON_FILENAME;
        this.xmlFilePath = base + XML_FILENAME;
    }

    private LocalDateTime now() {
        return LocalDateTime.now();
    }

    public synchronized TeiDocument update(String filePath, byte[] content) throws Exception {
        List<TeiDocument> oldVersion = ingestRepository.retrieveAllVersions(filePath);
        if (oldVersion.isEmpty()) {
            LOGGER.info("Create new document.");
            return ingestRepository.createNewDocument(filePath, content, now());
        } else {
            LOGGER.info("Create new version");
            TeiDocument old = oldVersion.stream().sorted(BY_VERSION_REVERSED).collect(Collectors.toList()).get(0);
            TeiDocument newVersion = old.nextVersion(content, now());
            LOGGER.info("New Version in update registry created");
            return ingestRepository.addNewVersion(old, newVersion);
        }
    }

    @Override
    public Optional<TeiDocument> getIngestRegistryJsonDocument() {
        return ingestRepository.retrieveByFilePath(jsonFilePath);
    }

    @Override
    public Optional<TeiDocument> getIngestRegistryXmlDocument() {
        return ingestRepository.retrieveByFilePath(xmlFilePath);
    }

    @Override
    public Optional<TeiDocument> getEditionRegistryJsonDocument() {
        return publicationRepository.retrieveByFilePath(jsonFilePath);
    }

    @Override
    public Optional<TeiDocument> getEditionRegistryXmlDocument() {
        return publicationRepository.retrieveByFilePath(xmlFilePath);
    }

    @Override
    public List<TeiDocument> getEditionRegistryJsonDocumentVersions() {
        return publicationRepository.retrieveAllVersions(jsonFilePath);
    }

    @Override
    public List<TeiDocument> getEditionRegistryXmlDocumentVersions() {
        return publicationRepository.retrieveAllVersions(xmlFilePath);
    }

    @Override
    public List<TeiDocument> updateRegistryToIngest(byte[] xml, byte[] json) throws Exception {
        TeiDocument xmlDocument = update(xmlFilePath, xml);
        TeiDocument jsonDocument = update(jsonFilePath, json);
        return List.of(xmlDocument, jsonDocument);
    }
}
