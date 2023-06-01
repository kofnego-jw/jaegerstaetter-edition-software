package at.kulinz.jaegerstaetter.stylesheets.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.stylesheets.model.BiographyResult;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xmlservice.service.XsltService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.saxon.s9api.XsltExecutable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//@Component
//@Profile("!preview")
public class TransformationServiceImpl implements TransformationService {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected final XsltService xsltService;
    private final File xsltDir;
    private final ExistDBTeiNotVersionedRepository commonRepository;

    private XsltExecutable metadataExecutable;

    private XsltExecutable normalizedExecutable;

    private XsltExecutable diplomaticExecutable;

    private XsltExecutable luceneExecutable;

    private XsltExecutable commentDocHtmlExecutable;

    private XsltExecutable commentDocTocExecutable;

    private XsltExecutable normalizedWithSearchResultExecutable;

    private XsltExecutable tocExecutable;

    private XsltExecutable noteExecutable;

    private XsltExecutable printHtmlExecutable;

    private XsltExecutable biographyResultExecutable;

    private XsltExecutable biographyHtmlExecutable;

    private XsltExecutable biographyTocExecutable;

    private XsltExecutable biographyPrintExecutable;

    private XsltExecutable toXmlExecutable;

    private XsltExecutable docDescExecutable;

    private XsltExecutable biographyIndexHtmlExecutable;

    private XsltExecutable specialCorrespHtmlExecutable;

    @Autowired
    public TransformationServiceImpl(List<SaxonExtensionProvider> saxonExtensionProviderList,
                                     File xsltDir, XsltService xsltService,
                                     ExistDBTeiNotVersionedRepository commonRepository,
                                     JaegerstaetterConfig config) throws Exception {
        this.xsltDir = xsltDir;
        this.xsltService = xsltService;
        this.xsltService.registerExtensionProviders(saxonExtensionProviderList);
        this.commonRepository = commonRepository;
        initializeStylesheetDir(config.getExistCommonXsltDirName(), false);
        createXsltExecutables();
    }

    private void initializeStylesheetDir(String xsltDirName, boolean useExistDb) throws Exception {
        // Copy stylesheets in jar file to xslt dir
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] luceneResources = patternResolver.getResources(TransformationConstants.XSLT_RESOURCE_PATTERN);
        for (Resource r : luceneResources) {
            if (!r.isReadable()) {
                continue;
            }
            String uri = r.getURI().toString();
            String filename = uri.substring(uri.indexOf(TransformationConstants.XSLT_RESOURCE_PREFIX) +
                    TransformationConstants.XSLT_RESOURCE_PREFIX.length());
            File output = new File(xsltDir, filename);
            FileUtils.copyInputStreamToFile(r.getInputStream(), output);
        }

        if (useExistDb) {
            // If there are any files in commonsRepo / xslt / (lucene|metadata|presentation) copy them
            List<TeiDocumentFW> all = commonRepository.retrieveAll();
            List<TeiDocumentFW> luceneFiles = all.stream().filter(fw -> fw.getFilePath().startsWith(xsltDirName + "/lucene/"))
                    .collect(Collectors.toList());
            for (TeiDocumentFW fw : luceneFiles) {
                Optional<TeiDocument> teiOpt = commonRepository.retrieveByFilePath(fw.getFilePath());
                if (teiOpt.isPresent()) {
                    copyToXsltWithFilePath(teiOpt.get());
                }
            }
            List<TeiDocumentFW> metadataFiles = all.stream().filter(fw -> fw.getFilePath().startsWith(xsltDirName + "/metadata/"))
                    .collect(Collectors.toList());
            for (TeiDocumentFW fw : metadataFiles) {
                Optional<TeiDocument> teiOpt = commonRepository.retrieveByFilePath(fw.getFilePath());
                if (teiOpt.isPresent()) {
                    copyToXsltWithFilePath(teiOpt.get());
                }
            }
            List<TeiDocumentFW> presentationFiles = all.stream().filter(fw -> fw.getFilePath().startsWith(xsltDirName + "/representation/"))
                    .collect(Collectors.toList());
            for (TeiDocumentFW fw : presentationFiles) {
                Optional<TeiDocument> teiOpt = commonRepository.retrieveByFilePath(fw.getFilePath());
                if (teiOpt.isPresent()) {
                    copyToXsltWithFilePath(teiOpt.get());
                }
            }
        }
    }

    private void createXsltExecutables() throws Exception {
        this.metadataExecutable = createExecutable(TransformationConstants.METADATA_METADATARESULT_STYLESHEET);
        this.normalizedExecutable = createExecutable(TransformationConstants.HTML_NORM_STYLESHEET);
        this.diplomaticExecutable = createExecutable(TransformationConstants.HTML_DIPLO_STYLESHEET);
        this.luceneExecutable = createExecutable(TransformationConstants.LUCENE_INDEXDOCUMENT_STYLESHEET);
        this.commentDocHtmlExecutable = createExecutable(TransformationConstants.COMMENT_DOC_HTML_STYLESHEET);
        this.commentDocTocExecutable = createExecutable(TransformationConstants.COMMENT_DOC_TOC_STYLESHEET);
        this.normalizedWithSearchResultExecutable = createExecutable(TransformationConstants.HTML_NORM_WITH_SEARCHHIGHLIGHT_STYLESHEET);
        this.tocExecutable = createExecutable(TransformationConstants.TOC_STYLESHEET);
        this.noteExecutable = createExecutable(TransformationConstants.NOTE_STYLESHEET);
        this.printHtmlExecutable = createExecutable(TransformationConstants.PRINT_HTML_STYLESHEET);
        this.biographyResultExecutable = createExecutable(TransformationConstants.BIOGRAPHY_BIOGRAPHYRESULT_STYLESHEET);
        this.biographyHtmlExecutable = createExecutable(TransformationConstants.BIOGRAPHY_HTML_STYLESHEET);
        this.biographyTocExecutable = createExecutable(TransformationConstants.BIOGRAPHY_TOC_STYLESHEET);
        this.biographyPrintExecutable = createExecutable(TransformationConstants.BIOGRAPHY_PRINTHTML_STYLESHEET);
        this.toXmlExecutable = createExecutable(TransformationConstants.XML_STYLESHEET);
        this.docDescExecutable = createExecutable(TransformationConstants.DOC_DESC_STYLESHEET);
        this.biographyIndexHtmlExecutable = createExecutable(TransformationConstants.BIOGRAPHY_INDEX_HTML_STYLESHEET);
        this.specialCorrespHtmlExecutable = createExecutable(TransformationConstants.SPECIAL_CORRESP_HTML_STYLESHEET);
    }

    protected XsltExecutable createExecutable(String path) throws Exception {
        File xslt = new File(xsltDir, path).getCanonicalFile();
        return xsltService.createXsltExecutable(new StreamSource(xslt));
    }

    private void copyToXsltWithFilePath(TeiDocument teiDoc) throws Exception {
        String filePath = teiDoc.getFilePath();
        File dest = new File(xsltDir, filePath.substring(filePath.indexOf("xslt/")));
        FileUtils.writeByteArrayToFile(dest, teiDoc.getContent());
    }

    @Override
    public MetadataResult getMetadataResult(TeiDocument document) throws Exception {
        String filename = document.getFilePath();
        if (filename.contains("/")) {
            filename = filename.substring(filename.lastIndexOf("/") + 1);
        }
        Map<String, String> params = Map.of("fileName", filename);
        byte[] xml = xsltService.transform(new ByteArrayInputStream(document.getContent()), metadataExecutable, params);
        return JacksonXmlHelper.fromInputStreamToMetadataResult(new ByteArrayInputStream(xml));
    }

    @Override
    public BiographyResult getBiographyResult(TeiDocument document) throws Exception {
        String filename = document.getFilePath();
        if (filename.contains("/")) {
            filename = filename.substring(filename.lastIndexOf("/") + 1);
        }
        Map<String, String> params = Map.of("fileName", filename);
        byte[] xml = xsltService.transform(new ByteArrayInputStream(document.getContent()), biographyResultExecutable, params);
        return JacksonXmlHelper.fromInputStreamToBiographyResult(new ByteArrayInputStream(xml));
    }

    @Override
    public byte[] toNormalized(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedExecutable, null);
    }

    @Override
    public byte[] toNormalizedWithSearchHighlight(TeiDocument document, SearchRequest searchRequest) throws Exception {
        Map<String, String> params = createNormWithHighlightParams(searchRequest);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedWithSearchResultExecutable, params);
    }

    @Override
    public byte[] toNormalizedWithMarkRsHighlight(TeiDocument document, MarkRsRequest markRsRequest) throws Exception {
        Map<String, String> params = createNormWithMarkRsParams(markRsRequest);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedExecutable, params);
    }

    @Override
    public byte[] toXml(TeiDocument document, boolean includeHeader, VersionInfoList list) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.XML_PARAM_INCLUDE_HEADER_KEY, Boolean.toString(includeHeader));
        params.put(TransformationConstants.XML_PARAM_VERSIONS_KEY, JacksonXmlHelper.toXML(list));
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), toXmlExecutable, params);
    }

    protected Map<String, String> createNormWithHighlightParams(SearchRequest searchRequest) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.PARAM_SEARCH_JSON, OBJECT_MAPPER.writeValueAsString(searchRequest));
        params.put(TransformationConstants.PARAM_HIGHLIGHT_STARTTAG, TransformationConstants.DEFAULT_VAL_HIGHLIGHT_STARTTAG);
        params.put(TransformationConstants.PARAM_HIGHLIGHT_ENDTAG, TransformationConstants.DEFAULT_VAL_HIGHLIGHT_ENDTAG);
        return params;
    }

    protected Map<String, String> createNormWithMarkRsParams(MarkRsRequest markRsRequest) {
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.PARAM_MARK_RS_TYPE, markRsRequest.type.toString());
        params.put(TransformationConstants.PARAM_MARK_RS_KEY, markRsRequest.key);
        return params;
    }

    @Override
    public byte[] toDiplomatic(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), diplomaticExecutable, null);
    }

    @Override
    public byte[] toNoteContent(TeiDocument document, String noteId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.NOTE_ID, noteId);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), noteExecutable, params);
    }

    @Override
    public IndexDocument getIndexDocument(TeiDocument document, String documentId) throws Exception {
        String docId = StringUtils.isBlank(documentId) ? document.getFilePath() : documentId;
        Map<String, String> params = Map.of("filePath", document.getFilePath(),
                "documentId", docId);
        byte[] xml = xsltService.transform(new ByteArrayInputStream(document.getContent()), luceneExecutable, params);
        return JacksonXmlHelper.fromInputStreamToIndexDocument(new ByteArrayInputStream(xml));
    }

    @Override
    public byte[] commentDocToHtml(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), commentDocHtmlExecutable, null);
    }

    @Override
    public byte[] commentDocToToc(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), commentDocTocExecutable, null);
    }

    @Override
    public TocList toTocEntries(TeiDocument document) throws Exception {
        byte[] xml = xsltService.transform(new ByteArrayInputStream(document.getContent()), tocExecutable, null);
        return JacksonXmlHelper.fromInputStreamToTocList(new ByteArrayInputStream(xml));
    }

    @Override
    public byte[] toPrintHtml(TeiDocument doc) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(doc.getContent()), printHtmlExecutable, null);
    }

    @Override
    public byte[] biographyToHtml(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), biographyHtmlExecutable, null);
    }

    @Override
    public byte[] biographyToToc(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), biographyTocExecutable, null);
    }

    @Override
    public byte[] biographyToPrintHtml(TeiDocument doc, int portNumber) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.PARAM_PORT_NUMBER, Integer.toString(portNumber));
        return xsltService.transform(new ByteArrayInputStream(doc.getContent()), biographyPrintExecutable, params);
    }

    @Override
    public DocDescResult docDescResult(TeiDocument document) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.PARAM_FILENAME, document.getName());
        byte[] result = xsltService.transform(new ByteArrayInputStream(document.getContent()), docDescExecutable, params);
        return JacksonXmlHelper.fromInputStreamToDocDescResult(new ByteArrayInputStream(result));
    }

    @Override
    public byte[] biographyIndexToHtml(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), biographyIndexHtmlExecutable, null);
    }

    @Override
    public byte[] specialCorrespToHtml(TeiDocument document) throws Exception {
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), specialCorrespHtmlExecutable, null);
    }
}
