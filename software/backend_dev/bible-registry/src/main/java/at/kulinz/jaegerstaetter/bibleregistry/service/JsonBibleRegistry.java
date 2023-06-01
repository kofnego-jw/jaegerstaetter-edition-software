package at.kulinz.jaegerstaetter.bibleregistry.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonBibleRegistry {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public final List<JsonBibleIndexEntry> entries;

    @JsonCreator
    public JsonBibleRegistry(@JsonProperty("entries") List<JsonBibleIndexEntry> entries) {
        this.entries = entries;
    }

    public static JsonBibleRegistry fromDocument(byte[] content) throws Exception {
        return OBJECT_MAPPER.readValue(content, JsonBibleRegistry.class);
    }

    public static byte[] toDocument(JsonBibleRegistry registry) throws Exception {
        return OBJECT_MAPPER.writeValueAsBytes(registry);
    }
}
