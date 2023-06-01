package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.correspplaces.service.CorrespPlacesService;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.ResourceService;
import at.kulinz.jaegerstaetter.pdfgenerator.service.PdfGenerator;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);
    private final ExistDBTeiRepository repository;
    private final MetadataService metadataService;
    private final TransformationService transformationService;
    private final PdfGenerator pdfGenerator;
    private final CorrespPlacesService correspPlacesService;

    private final FacsimileService facsimileService;

    private final String dataFilePath;

    public ResourceServiceImpl(ExistDBTeiRepository repository, MetadataService metadataService,
                               TransformationService transformationService, FacsimileService facsimileService,
                               PdfGenerator pdfGenerator, CorrespPlacesService correspPlacesService, JaegerstaetterConfig config) {
        this.repository = repository;
        this.metadataService = metadataService;
        this.transformationService = transformationService;
        this.facsimileService = facsimileService;
        this.pdfGenerator = pdfGenerator;
        this.correspPlacesService = correspPlacesService;
        String existEditionDataDirName = config.getExistEditionDataDirName();
        this.dataFilePath = existEditionDataDirName.endsWith("/") ? existEditionDataDirName : existEditionDataDirName + "/";
    }

    private String convertFilePathToId(String filePath) {
        if (filePath.startsWith(dataFilePath)) {
            return filePath.substring(dataFilePath.length());
        }
        if (filePath.startsWith("/")) {
            return filePath;
        }
        return "/" + filePath;
    }

    private String convertIdToFilePath(String id) {
        if (id.startsWith("/")) {
            return id.substring(1);
        }
        return dataFilePath + id;
    }

    @Override
    public byte[] getContent(String resourceId) throws FileNotFoundException {
        String filePath = convertIdToFilePath(resourceId);
        Optional<TeiDocument> teiDocument = repository.retrieveByFilePath(filePath);
        if (teiDocument.isEmpty()) {
            throw new FileNotFoundException("Cannot find the document.");
        }
        return teiDocument.get().getContent();
    }

    @Override
    public ResourceFWDTO fromTeiDocumentFW(TeiDocumentFW fw) {
        if (fw == null) {
            return null;
        }
        try {
            TeiDocument document = fw instanceof TeiDocument ?
                    (TeiDocument) fw :
                    retrieveByFilePathAndVersion(fw.getFilePath(), fw.getVersion());
            MetadataResult metadataResult = metadataService.getMetadataResult(document);
            String datingReadable = MetadataService.convertToDatingReadable(metadataResult.dating);
            List<EditionCorpus> corpus = metadataResult.corpus != null ?
                    metadataResult.corpus.stream().map(EditionCorpus::valueOf)
                            .collect(Collectors.toList()) : Collections.emptyList();
            List<EditionTimePeriod> periods = metadataResult.periods != null ?
                    metadataResult.periods.stream().map(EditionTimePeriod::valueOf)
                            .collect(Collectors.toList()) : Collections.emptyList();
            List<String> authors = metadataResult.authors != null ? metadataResult.authors : Collections.emptyList();
            List<String> recipients = metadataResult.recipients != null ? metadataResult.recipients : Collections.emptyList();
            List<String> places = metadataResult.places != null ? metadataResult.places : Collections.emptyList();
            List<String> objectTypes = metadataResult.objectTypes != null ? metadataResult.objectTypes : Collections.emptyList();
            return new ResourceFWDTO(convertFilePathToId(fw.getFilePath()), metadataResult.title,
                    metadataResult.dating, datingReadable, metadataResult.type, metadataResult.summary,
                    corpus, periods, authors, recipients, places, objectTypes, metadataResult.signature, metadataResult.altSignature);
        } catch (Exception e) {
            LOGGER.error("Cannot create ResourceFWDTO.", e);
            return null;
        }
    }

    @Override
    public List<ResourceFWDTO> listAllResources() {
        List<TeiDocumentFW> fws = repository.retrieveAllUnder(dataFilePath, true)
                .stream().sorted((Comparator.comparing(TeiDocumentFW::getFilePath))).collect(Collectors.toList());
        return fws.stream().map(this::fromTeiDocumentFW)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceFWDTO findResourceFWByIdAndDocumentId(String filePath, String date) throws FileNotFoundException, FrontendModelException {
        TeiDocument document = retrieveByFilePathAndDate(convertIdToFilePath(filePath), date);
        return fromTeiDocumentFW(document);
    }

    @Override
    public ResourceFWDTO findLatestResourceFWByIdAndDocumentId(String resourceId) throws FrontendModelException, FileNotFoundException {
        return this.findResourceFWByIdAndDocumentId(resourceId, null);
    }

    @Override
    public ResourceDTO findResourceById(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndDate(convertIdToFilePath(resourceId), date);
        try {
            MetadataResult mr = metadataService.getMetadataResult(doc);
            List<EditionCorpus> corpus = mr.corpus != null ?
                    mr.corpus.stream().map(EditionCorpus::valueOf)
                            .collect(Collectors.toList()) : Collections.emptyList();
            List<EditionTimePeriod> periods = mr.periods != null ?
                    mr.periods.stream().map(EditionTimePeriod::valueOf)
                            .collect(Collectors.toList()) : Collections.emptyList();
            List<String> authors = mr.authors != null ? mr.authors : Collections.emptyList();
            List<String> recipients = mr.recipients != null ? mr.recipients : Collections.emptyList();
            List<String> places = mr.places != null ? mr.places : Collections.emptyList();
            List<String> objectTypes = mr.objectTypes != null ? mr.objectTypes : Collections.emptyList();
            String datingReadable = MetadataService.convertToDatingReadable(mr.dating);
            List<VersionInfo> versions = repository.retrieveAllVersions(convertIdToFilePath(resourceId))
                    .stream()
                    .map(MetadataService::convertToVersionInfo)
                    .sorted(VersionInfo.SORTER_DESC)
                    .collect(Collectors.toList());
            byte[] norm = transformationService.toNormalized(doc);
            byte[] dipl = transformationService.toDiplomatic((doc));
            List<String> prevLetterIds = mr.prevLetterIds;
            List<String> nextLetterIds = mr.nextLetterIds;
            List<String> relatedLetterIds = mr.relatedLetterIds;
            List<ResourceFWDTO> prevLetters = getResourceFWDTOByIds(prevLetterIds);
            List<ResourceFWDTO> nextLetters = getResourceFWDTOByIds(nextLetterIds);
            List<ResourceFWDTO> relatedLetters = getResourceFWDTOByIds(relatedLetterIds);
            TocList tocEntries = transformationService.toTocEntries(doc);
            return new ResourceDTO(convertFilePathToId(doc.getFilePath()), mr.title, mr.dating, datingReadable, mr.type, mr.summary,
                    corpus, periods, authors, recipients, places, objectTypes, mr.signature, mr.altSignature, mr.facsimileIds,
                    mr.metadataGroups, versions, new String(norm, StandardCharsets.UTF_8), new String(dipl, StandardCharsets.UTF_8),
                    prevLetters, nextLetters, relatedLetters, tocEntries);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot retrieve metadata from document.", e);
        }
    }

    @Override
    public ResourceDTO findLatestResourceById(String resourceId) throws FileNotFoundException, FrontendModelException {
        return this.findResourceById(resourceId, null);
    }

    private List<ResourceFWDTO> getResourceFWDTOByIds(List<String> ids) {
        List<ResourceFWDTO> letters = Collections.emptyList();
        if (ids != null) {
            letters = ids.stream().map(letterId -> {
                try {
                    return findResourceFWByIdAndDocumentId(letterId, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return letters;
    }

    private TeiDocument retrieveByFilePathAndDate(String filePath, String dating) throws FileNotFoundException, FrontendModelException {
        if (dating == null) {
            return retrieveByFilePath(filePath);
        }
        LocalDateTime dateTime = MetadataService.convertToLocalDateTime(dating);
        Optional<TeiDocument> docOpt = repository.retrieveByFilePathAndTimestamp(filePath, dateTime);
        if (docOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find a document using the file path and date provided.");
        }
        return docOpt.get();
    }

    private TeiDocument retrieveByFilePathAndVersion(String filePath, Integer version) throws FileNotFoundException {
        Optional<TeiDocument> docOpt = repository.retrieveByFilePathAndVersion(filePath, version);
        if (docOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find a document with the file path and the version provided.");
        }
        return docOpt.get();
    }

    private TeiDocument retrieveByFilePath(String filePath) throws FileNotFoundException {
        Optional<TeiDocument> docOpt = repository.retrieveByFilePath(filePath);
        if (docOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find a document using the file path: " + filePath);
        }
        return docOpt.get();
    }

    @Override
    public byte[] getXmlRepresentation(String resourceId, boolean includeHeader) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePath(convertIdToFilePath(resourceId));
        List<VersionInfo> versions = repository.retrieveAllVersions(convertIdToFilePath(resourceId))
                .stream()
                .map(MetadataService::convertToVersionInfo)
                .sorted(VersionInfo.SORTER_DESC)
                .collect(Collectors.toList());
        VersionInfoList list = new VersionInfoList(versions);
        try {
            return transformationService.toXml(doc, includeHeader, list);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot get the xml representation.", e);
        }
    }

    @Override
    public byte[] getPdfNormRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException {
        if (StringUtils.isBlank(resourceId)) {
            throw new FileNotFoundException();
        }
        String id = resourceId.endsWith(".xml") ? resourceId : resourceId + ".xml";
        TeiDocument doc = retrieveByFilePath(convertIdToFilePath(id));
        try {
            return pdfGenerator.generatePdf(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot generate PDF.", e);
        }
    }

    @Override
    public byte[] getXmlRepresentation(String resourceId, String dating, boolean includeHeader) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndDate(convertIdToFilePath(resourceId), dating);
        List<VersionInfo> versions = repository.retrieveAllVersions(convertIdToFilePath(resourceId))
                .stream()
                .map(MetadataService::convertToVersionInfo)
                .sorted(VersionInfo.SORTER_DESC)
                .collect(Collectors.toList());
        try {
            return transformationService.toXml(doc, includeHeader, new VersionInfoList(versions));
        } catch (Exception e) {
            throw new FrontendModelException("Cannot get XML representation.", e);
        }
    }

    @Override
    public byte[] getXmlRepresentation(String resourceId, Integer version) throws FileNotFoundException {
        TeiDocument doc = retrieveByFilePathAndVersion(convertIdToFilePath(resourceId), version);
        return doc.getContent();
    }

    @Override
    public byte[] getHtmlNormalizedRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePath(convertIdToFilePath(resourceId));
        try {
            return transformationService.toNormalized(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public byte[] getHtmlNormalizedRepresentation(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndDate(convertIdToFilePath(resourceId), date);
        try {
            return transformationService.toNormalized(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public byte[] getHtmlNormalizedRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndVersion(convertIdToFilePath(resourceId), version);
        try {
            return transformationService.toNormalized(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public byte[] getHtmlNormalizedRepresentationWithHighlight(String resourceId, SearchRequest searchRequest) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePath(convertIdToFilePath(resourceId));
        try {
            return transformationService.toNormalizedWithSearchHighlight(doc, searchRequest);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform.", e);
        }
    }

    @Override
    public byte[] getHtmlNormalizedRepresentationWithMarkRsRequest(String resourceId, MarkRsRequest markRsRequest) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePath(convertIdToFilePath(resourceId));
        try {
            return transformationService.toNormalizedWithMarkRsHighlight(doc, markRsRequest);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform.", e);
        }
    }

    // $S$EEIR9XBf2Q9LK42nqwr1yh/VvjEAsMzADdaN7IDLk/l6NLKdiEjo

    @Override
    public byte[] getHtmlDiplomaticRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePath(convertIdToFilePath(resourceId));
        try {
            return transformationService.toDiplomatic(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public byte[] getHtmlDiplomaticRepresentation(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndDate(convertIdToFilePath(resourceId), date);
        try {
            return transformationService.toDiplomatic(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public byte[] getHtmlDiplomaticRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndVersion(convertIdToFilePath(resourceId), version);
        try {
            return transformationService.toDiplomatic(doc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public NoteResourceDTO getNoteResource(String resourceId, String noteId, String date) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = retrieveByFilePathAndDate(convertIdToFilePath(resourceId), date);
        try {
            byte[] noteContent = transformationService.toNoteContent(doc, noteId);
            return new NoteResourceDTO(resourceId, noteId, new String(noteContent, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new FrontendModelException("Cannot transform data.", e);
        }
    }

    @Override
    public NoteResourceDTO getLatestNoteResource(String resourceId, String noteId) throws FileNotFoundException, FrontendModelException {
        return this.getNoteResource(resourceId, noteId, null);
    }

    @Override
    public byte[] getFacsimile(String facResourceId) throws FileNotFoundException {
        return facsimileService.getFacsimile(facResourceId);
    }

    @Override
    public CorrespInfo getCorrespInfo(String resourceId, String anchorName) {
        DocumentInfo di = new DocumentInfo(resourceId, anchorName);
        return correspPlacesService.findByDocumentInfo(di).orElse(new CorrespInfo("", Collections.emptyList()));
    }
}
