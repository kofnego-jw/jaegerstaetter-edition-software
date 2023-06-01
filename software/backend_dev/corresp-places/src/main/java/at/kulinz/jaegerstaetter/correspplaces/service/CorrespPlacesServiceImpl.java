package at.kulinz.jaegerstaetter.correspplaces.service;

import at.kulinz.jaegerstaetter.correspplaces.CorrespPlacesException;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CorrespInfo;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.DocumentInfo;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.sf.saxon.s9api.XPathExecutable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CorrespPlacesServiceImpl implements CorrespPlacesService, AutoCloseable {

    private final List<CorrespInfo> correspInfos = new ArrayList<>();

    private final XPathExecutable correspXPath;

    private final XPathService xPathService;

    private final File correspSavingLocation;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CorrespPlacesServiceImpl(XPathService xPathService, File correspSavingLocation) throws CorrespPlacesException {
        this.xPathService = xPathService;
        this.correspSavingLocation = correspSavingLocation;
        this.correspXPath = createCorrespXPath(xPathService);
        this.load();
    }

    @Override
    public void load() throws CorrespPlacesException {
        if (this.correspSavingLocation.exists()) {
            try {
                CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, CorrespInfo.class);
                List<CorrespInfo> list = objectMapper.readValue(this.correspSavingLocation, collectionType);
                this.reset();
                this.correspInfos.addAll(list);
            } catch (Exception e) {
                throw new CorrespPlacesException("Cannot read value.", e);
            }
        }
    }

    @Override
    public void save() throws CorrespPlacesException {
        try {
            objectMapper.writeValue(this.correspSavingLocation, this.correspInfos);
        } catch (Exception e) {
            throw new CorrespPlacesException("Cannot save corresp places.", e);
        }
    }

    @Override
    public void close() throws CorrespPlacesException {
        this.save();
    }

    private XPathExecutable createCorrespXPath(XPathService xPathService) throws CorrespPlacesException {
        try {
            return xPathService.createXPathExecutable("//(tei:div|tei:anchor)/@corresp", null);
        } catch (Exception e) {
            throw new CorrespPlacesException("Cannot create xpath executable.", e);
        }
    }

    @Override
    public void reset() {
        this.correspInfos.clear();
    }

    @Override
    public List<CorrespInfo> getAllCorresPlaces() {
        return this.correspInfos;
    }

    @Override
    public void analyzeDocument(TeiDocument doc) throws CorrespPlacesException {
        byte[] content = doc.getContent();
        try {
            List<String> places = xPathService.evaluate(new ByteArrayInputStream(content), this.correspXPath);
            for (String link : places) {
                Optional<DocumentInfo> toInfo = createDocumentInfo(link);
                if (toInfo.isPresent()) {
                    DocumentInfo fromInfo = new DocumentInfo(doc.getName(), linkStringToId(link));
                    addToPool(toInfo.get(), fromInfo);
                }
            }
        } catch (Exception e) {
            throw new CorrespPlacesException("Cannot evaluate for corresp places.", e);
        }
    }

    private synchronized void addToPool(DocumentInfo toInfo, DocumentInfo fromInfo) {
        Optional<CorrespInfo> inDb = correspInfos.stream().filter(x -> x.contains(toInfo)).findAny();
        if (inDb.isPresent()) {
            inDb.get().addDocumentInfo(fromInfo);
        } else {
            String id = linkStringToId(toInfo.filename + "#" + toInfo.anchorName);
            CorrespInfo ci = new CorrespInfo(id, List.of(toInfo, fromInfo));
            correspInfos.add(ci);
        }
    }

    private static String linkStringToId(String link) {
        return link.replaceAll("[^A-Za-z0-9]", "_");
    }

    private static Optional<DocumentInfo> createDocumentInfo(String linkString) {
        if (linkString.contains("#")) {
            String filename = linkString.substring(0, linkString.lastIndexOf("#"));
            String id = linkString.substring(linkString.lastIndexOf("#") + 1);
            return Optional.of(new DocumentInfo(filename, id));
        }
        return Optional.empty();
    }


    @Override
    public Optional<CorrespInfo> findByDocumentInfo(DocumentInfo documentInfo) {
        if (documentInfo == null) {
            return Optional.empty();
        }
        return this.correspInfos
                .stream()
                .filter(ci -> ci.contains(documentInfo))
                .findAny();
    }
}
