package at.kulinz.jaegerstaetter.formaldesc;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FilePathMockRepository extends ExistDBTeiRepository {

    private final File baseDir;

    public FilePathMockRepository(File baseDir) {
        super("", "", 0, "", "", "");
        this.baseDir = baseDir;
    }

    @Override
    public TeiDocument saveUnderUrl(TeiDocument document, String url) throws DataModelException {
        throw new UnsupportedOperationException();
    }

    @Override
    public TeiDocument createNewDocument(String filePath, byte[] content, LocalDateTime timestamp) throws DataModelException {
        throw new UnsupportedOperationException();
    }

    @Override
    public TeiDocument addNewVersion(TeiDocument oldVersion, TeiDocument newVersion) throws DataModelException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<TeiDocument> retrieveByUrl(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePath(String filePath) {
        File f = new File(baseDir, filePath);
        if (f.exists()) {
            return Optional.of(mock(f));
        }
        return Optional.empty();
    }

    private TeiDocument mock(File f) {
        String filePath = f.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        byte[] content;
        try {
            content = FileUtils.readFileToByteArray(f);
        } catch (Exception e) {
            content = new byte[0];
        }
        return new TeiDocument(filePath, content, LocalDateTime.now());
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePathAndVersion(String filePath, Integer version) {
        return Optional.empty();
    }

    @Override
    public List<TeiDocument> retrieveAllVersions(String filePath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeiDocumentFW> retrieveAllUnder(String filePath, boolean recursive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeiDocumentFW> retrieveAll() {
        throw new UnsupportedOperationException();
    }
}
