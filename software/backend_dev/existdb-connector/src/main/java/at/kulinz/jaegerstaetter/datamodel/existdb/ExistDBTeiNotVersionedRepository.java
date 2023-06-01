package at.kulinz.jaegerstaetter.datamodel.existdb;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExistDBTeiNotVersionedRepository extends ExistDBTeiRepository {


    public ExistDBTeiNotVersionedRepository(String protocol, String host, int port, String davBaseDir, String username, String password) {
        super(protocol, host, port, davBaseDir, username, password);
    }


    @Override
    public Optional<TeiDocumentFW> retrieveFWByFilePath(String filePath) {
        LocalDateTime modified;
        try {
            modified = lastModified(filePath);
        } catch (Exception e) {
            modified = LocalDateTime.now();
        }
        TeiDocumentFW fw = new TeiDocumentFW(filePath, 1, filePath, modified, null);
        return Optional.of(fw);
    }

    @Override
    public TeiDocument saveUnderUrl(TeiDocument document, String url) throws DataModelException {
        byte[] content = document.getContent();
        davSave(url, content, true);
        document.setUrl(url);
        return document;
    }

    @Override
    public TeiDocument createNewDocument(String filePath, byte[] content, LocalDateTime timestamp) throws DataModelException {
        TeiDocument doc = new TeiDocument(filePath, content, timestamp);
        doc.setUrl(filePath);
        saveCurrent(doc);
        return doc;
    }

    @Override
    public TeiDocument addNewVersion(TeiDocument oldVersion, TeiDocument newVersion) throws DataModelException {
        return createNewDocument(oldVersion.getFilePath(), newVersion.getContent(), newVersion.getCreationTimestamp());
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePath(String filePath) {
        return retrieveByUrl(filePath);
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePathAndTimestamp(String filePath, LocalDateTime timestamp) {
        return retrieveByFilePath(filePath);
    }

    @Override
    public Optional<TeiDocument> retrieveByUrl(String url) {
        try {
            byte[] content = davLoad(url);
            String davUrl = toDavUrl(url);
            LocalDateTime modified = lastModified(url);
            String path = davUrl.substring(getWebDavBase().length());
            TeiDocument document = new TeiDocument(path, content, modified);
            document.setUrl(url);
            return Optional.of(document);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePathAndVersion(String filePath, Integer version) {
        return retrieveByFilePath(filePath);
    }

    @Override
    public List<TeiDocument> retrieveAllVersions(String filePath) {
        Optional<TeiDocument> docOpt = retrieveByFilePath(filePath);
        if (docOpt.isEmpty()) {
            return Collections.emptyList();
        }
        return List.of(docOpt.get());
    }

}
