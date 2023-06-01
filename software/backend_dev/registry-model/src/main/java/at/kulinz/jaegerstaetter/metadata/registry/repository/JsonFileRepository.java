package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Collections;

public class JsonFileRepository extends AbstractDataRepository implements DataRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final File repositoryFile;

    private JsonRepository jsonRepository;

    public JsonFileRepository(File repositoryFile) throws MetadataException {
        this.repositoryFile = repositoryFile;
        this.readRepos();
    }

    static JsonRepository readRepoInternal(File file) throws MetadataException {
        try {
            return OBJECT_MAPPER.readValue(file, JsonRepository.class);
        } catch (Exception e) {
            throw new MetadataException("Cannot read json file.", e);
        }
    }

    private void readRepos() throws MetadataException {
        if (!this.repositoryFile.exists()) {
            this.jsonRepository = new JsonRepository(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
            this.writeData();
        } else {
            this.jsonRepository = readRepoInternal(this.repositoryFile);
        }
    }

    @Override
    public JsonRepository getJsonRepository() {
        return this.jsonRepository;
    }

    @Override
    public synchronized void writeData() throws MetadataException {
        try {
            File parent = this.repositoryFile.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new MetadataException("Cannot create parent file.");
            }
            OBJECT_MAPPER.writeValue(this.repositoryFile, this.jsonRepository.sorted());
        } catch (Exception e) {
            throw new MetadataException("Cannot write json file.", e);
        }
    }

}
