package at.kulinz.jaegerstaetter.datamodel.model;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;

import java.time.LocalDateTime;

public class TeiDocument extends TeiDocumentFW {

    private byte[] content;

    public TeiDocument() {
        super();
    }

    public TeiDocument(String filePath, byte[] content, LocalDateTime creationTimestamp) {
        this(filePath, 1, creationTimestamp, content);
    }

    protected TeiDocument(String filePath, Integer version, LocalDateTime creationTimestamp, byte[] content) {
        super(filePath, version, creationTimestamp);
        this.content = content;
    }

    public TeiDocument(String filePath, Integer version, String url, LocalDateTime creationTimestamp, LocalDateTime deletionTimestamp, byte[] content) {
        super(filePath, version, url, creationTimestamp, deletionTimestamp);
        this.content = content;
    }

    public static TeiDocument fromTeiDocumentFW(TeiDocumentFW fw, byte[] content) {
        return new TeiDocument(fw.getFilePath(), fw.getVersion(), fw.getUrl(), fw.getCreationTimestamp(),
                fw.getDeletionTimestamp(), content);
    }

    public byte[] getContent() {
        return content;
    }

    protected void setContent(byte[] content) {
        this.content = content;
    }

    public TeiDocument nextVersion(byte[] content, LocalDateTime newVersionCreationTimestamp) throws DataModelException {
        if (this.getDeletionTimestamp() != null) {
            throw new DataModelException("Cannot create new version from an already deleted document.");
        }
        this.setDeletionTimestamp(newVersionCreationTimestamp);
        return new TeiDocument(this.getFilePath(), this.getVersion() + 1, newVersionCreationTimestamp, content);
    }
}
