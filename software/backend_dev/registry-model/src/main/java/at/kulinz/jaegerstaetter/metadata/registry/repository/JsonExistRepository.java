package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

public class JsonExistRepository extends AbstractDataRepository implements DataRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ExistDBTeiRepository existDBTeiRepository;
    private final String dataFilePath;
    private JsonRepository jsonRepository;

    public JsonExistRepository(ExistDBTeiRepository existDBTeiRepository, String dataFilePath) throws MetadataException {
        this.existDBTeiRepository = existDBTeiRepository;
        this.dataFilePath = dataFilePath;
        this.readRepos();
    }

    private TeiDocument getRepositoryFile() throws MetadataException {
        return existDBTeiRepository.retrieveByFilePath(dataFilePath).orElseThrow(() -> new MetadataException("Cannot read file from exist db."));
    }

    private boolean repositoryFileExists() {
        Optional<TeiDocumentFW> doc = existDBTeiRepository.retrieveFWByFilePath(dataFilePath);
        return doc.isPresent();
    }

    private JsonRepository readRepoInternal() throws MetadataException {
        TeiDocument repositoryFile = getRepositoryFile();
        try {
            return OBJECT_MAPPER.readValue(repositoryFile.getContent(), JsonRepository.class);
        } catch (IOException e) {
            throw new MetadataException("Cannot read JSON data.", e);
        }
    }

    private void readRepos() throws MetadataException {
        if (!this.repositoryFileExists()) {
            this.jsonRepository = new JsonRepository(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
            try {
                byte[] content = OBJECT_MAPPER.writeValueAsBytes(this.jsonRepository);
                this.existDBTeiRepository.createNewDocument(this.dataFilePath, content, LocalDateTime.now());
            } catch (Exception e) {
                throw new MetadataException("Cannot create data file in exist.", e);
            }
        } else {
            this.jsonRepository = this.readRepoInternal();
        }
    }

    @Override
    JsonRepository getJsonRepository() {
        return this.jsonRepository;
    }

    private void writeDataInternal(byte[] content) throws MetadataException {
        try {
            TeiDocument repositoryFile = getRepositoryFile();
            TeiDocument nextVersion = repositoryFile.nextVersion(content, LocalDateTime.now());
            existDBTeiRepository.addNewVersion(repositoryFile, nextVersion);
        } catch (Exception e) {
            throw new MetadataException("Cannot save to exist.", e);
        }
    }

    @Override
    synchronized void writeData() throws MetadataException {
        try {
            byte[] content = OBJECT_MAPPER.writeValueAsBytes(this.jsonRepository.sorted());
            this.writeDataInternal(content);
        } catch (Exception e) {
            throw new MetadataException("Cannot write json file.", e);
        }
    }

}
