package at.kulinz.jaegerstaetter.datamodel.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TeiDocumentFW {

    private String filePath;

    private Integer version;

    private String url;

    private LocalDateTime creationTimestamp;

    private LocalDateTime deletionTimestamp;

    public TeiDocumentFW(String filePath, Integer version, LocalDateTime creationTimestamp) {
        this(filePath, version, null, creationTimestamp, null);
    }

    public TeiDocumentFW(String filePath, Integer version, String url, LocalDateTime creationTimestamp, LocalDateTime deletionTimestamp) {
        this.filePath = filePath;
        this.version = version;
        this.url = url;
        this.creationTimestamp = creationTimestamp;
        this.deletionTimestamp = deletionTimestamp;
    }

    public TeiDocumentFW() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public LocalDateTime getDeletionTimestamp() {
        return deletionTimestamp;
    }

    public void setDeletionTimestamp(LocalDateTime deletionTimestamp) {
        this.deletionTimestamp = deletionTimestamp;
    }

    public boolean isDeleted() {
        return this.deletionTimestamp != null;
    }

    public String getName() {
        String path = getFilePath();
        if (path.contains("/")) {
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeiDocumentFW that = (TeiDocumentFW) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "TeiDocumentFW{" +
                "filePath='" + filePath + '\'' +
                ", version=" + version +
                ", url='" + url + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", deletionTimestamp=" + deletionTimestamp +
                '}';
    }
}
