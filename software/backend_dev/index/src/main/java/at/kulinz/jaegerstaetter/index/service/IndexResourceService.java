package at.kulinz.jaegerstaetter.index.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import org.apache.lucene.document.Document;

import java.util.List;
import java.util.Optional;

public interface IndexResourceService extends AutoCloseable {

    ResourceFWDTO fromLuceneDoc(Document document);

    Optional<ResourceFWDTO> fetchById(String id);

    List<ResourceFWDTO> listAllResources();

    /**
     * Returns the key values for resource filters
     *
     * @param type the type, could be: "author", "recipient", "place" or "objecttype"
     * @return a list of keys, sorted (author and recipient Jägerstätter first)
     */
    List<String> getKeyValues(String type);

}
