package at.kulinz.jaegerstaetter.metadata.webapp.dto;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeiDocumentDTO {
    public final String documentPath;
    public final LocalDateTime creationTimestamp;

    public final Integer version;
    public final String xmlContent;

    public TeiDocumentDTO(String documentPath, LocalDateTime creationTimestamp, Integer version, String xmlContent) {
        this.documentPath = documentPath;
        this.creationTimestamp = creationTimestamp;
        this.version = version;
        this.xmlContent = xmlContent;
    }

    public static TeiDocumentDTO fromTeiDocument(TeiDocument doc) {
        if (doc == null) {
            return null;
        }
        String content = new String(doc.getContent(), StandardCharsets.UTF_8);
        return new TeiDocumentDTO(doc.getFilePath(), doc.getCreationTimestamp(), doc.getVersion(), content);
    }

    public static List<TeiDocumentDTO> fromTeiDocuments(List<TeiDocument> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream().map(TeiDocumentDTO::fromTeiDocument).collect(Collectors.toList());
    }
}
