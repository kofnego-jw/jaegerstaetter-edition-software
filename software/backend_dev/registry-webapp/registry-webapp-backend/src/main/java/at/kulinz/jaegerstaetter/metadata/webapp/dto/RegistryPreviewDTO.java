package at.kulinz.jaegerstaetter.metadata.webapp.dto;

import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;

public class RegistryPreviewDTO {

    public final VocabularyType type;

    public final String key;

    public final String html;

    public RegistryPreviewDTO(VocabularyType type, String key, String html) {
        this.type = type;
        this.key = key;
        this.html = html;
    }
}
