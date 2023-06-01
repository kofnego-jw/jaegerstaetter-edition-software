package at.kulinz.jaegerstaetter.tei.registry.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;

import java.util.List;
import java.util.Optional;

public interface RegistryRepository {

    String XML_FILENAME = "registry.xml";
    String JSON_FILENAME = "registry.json";


    Optional<TeiDocument> getIngestRegistryJsonDocument();

    Optional<TeiDocument> getIngestRegistryXmlDocument();

    Optional<TeiDocument> getEditionRegistryJsonDocument();

    Optional<TeiDocument> getEditionRegistryXmlDocument();

    List<TeiDocument> getEditionRegistryJsonDocumentVersions();

    List<TeiDocument> getEditionRegistryXmlDocumentVersions();


    List<TeiDocument> updateRegistryToIngest(byte[] xml, byte[] json) throws Exception;
}
