package at.kulinz.jaegerstaetter.metadata.webapp.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;
import at.kulinz.jaegerstaetter.metadata.registry.repository.DataRepository;
import at.kulinz.jaegerstaetter.metadata.registry.repository.TeiExporter;
import at.kulinz.jaegerstaetter.metadata.webapp.dto.RegistryPreviewDTO;
import at.kulinz.jaegerstaetter.metadata.webapp.dto.TeiDocumentDTO;
import at.kulinz.jaegerstaetter.tei.registry.service.RegistryRepository;
import net.sf.saxon.s9api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TeiRegistryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeiRegistryService.class);

    private static final String XSLT_PATH = "/at/kulinz/jaegerstaetter/metadata/webapp/registryPreview.xsl";

    private final RegistryRepository teiRegistryRepository;

    private final DataRepository dataRepository;

    private final TeiExporter teiExporter;

    private final XsltExecutable previewExecutable;

    private final Processor processor;

    @Autowired
    public TeiRegistryService(RegistryRepository metadataRegistryRepository, DataRepository dataRepository, TeiExporter teiExporter) throws Exception {
        this.teiRegistryRepository = metadataRegistryRepository;
        this.dataRepository = dataRepository;
        this.teiExporter = teiExporter;
        processor = new Processor(false);
        StreamSource xsltSource = new StreamSource(getClass().getResourceAsStream(XSLT_PATH));
        previewExecutable = processor.newXsltCompiler().compile(xsltSource);
    }

    public RegistryPreviewDTO previewRegistry(VocabularyType type, String key) throws Exception {
        Optional<TeiDocument> registryDocument = teiRegistryRepository.getIngestRegistryXmlDocument();
        if (registryDocument.isEmpty()) {
            return new RegistryPreviewDTO(type, key, "<p>No registry entry found. Is this entry already published?</p>");
        }
        return preview(type, key, registryDocument.get().getContent());
    }

    private RegistryPreviewDTO preview(VocabularyType type, String key, byte[] xml) throws Exception {
        XsltTransformer previewTransformer = previewExecutable.load();
        previewTransformer.clearParameters();
        previewTransformer.setParameter(new QName("type"), new XdmAtomicValue(type.name().toLowerCase()));
        previewTransformer.setParameter(new QName("key"), new XdmAtomicValue(key));
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xml));
        previewTransformer.setSource(xmlSource);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializer serializer = processor.newSerializer(baos);
        previewTransformer.setDestination(serializer);
        previewTransformer.transform();
        String html = baos.toString(StandardCharsets.UTF_8);
        return new RegistryPreviewDTO(type, key, html);
    }

    public synchronized TeiDocumentDTO publishRegistryData(int oldVersionNumber) throws Exception {
        Optional<TeiDocument> old = teiRegistryRepository.getIngestRegistryXmlDocument();
        byte[] newVersionXml = teiExporter.exportToTei(this.dataRepository);
        byte[] newVersionJson = teiExporter.exportToJson(this.dataRepository);
        LOGGER.info("New version created");
        List<TeiDocument> newDocs = teiRegistryRepository.updateRegistryToIngest(newVersionXml, newVersionJson);
        LOGGER.info("updated");
        return TeiDocumentDTO.fromTeiDocument(newDocs.get(0));
    }

    public TeiDocumentDTO previewCurrentVersion() throws Exception {
        byte[] content = teiExporter.exportToTei(this.dataRepository);
        return TeiDocumentDTO.fromTeiDocument(new TeiDocument("PREVIEW", content, LocalDateTime.now()));
    }

    public List<TeiDocumentDTO> getAllRegistryVersions() {
        return TeiDocumentDTO.fromTeiDocuments(teiRegistryRepository.getEditionRegistryXmlDocumentVersions());
    }

    public TeiDocumentDTO getRegistryDocument() {
        return TeiDocumentDTO.fromTeiDocument(teiRegistryRepository.getEditionRegistryXmlDocument()
                .orElse(new TeiDocument("", "Cannot find registry document.".getBytes(StandardCharsets.UTF_8), LocalDateTime.now())));
    }
}
