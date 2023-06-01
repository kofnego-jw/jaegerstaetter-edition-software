package at.kulinz.jaegerstaetter.stylesheets.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MarkRsRequest;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.VersionInfoList;
import at.kulinz.jaegerstaetter.stylesheets.model.BiographyResult;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xmlservice.service.XsltService;
import net.sf.saxon.s9api.XsltExecutable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
// @Profile({"preview", "edition"})
public class NoCacheTransformationServiceImpl extends TransformationServiceImpl {

    @Autowired
    public NoCacheTransformationServiceImpl(List<SaxonExtensionProvider> saxonExtensionProviderList,
                                            File xsltDir, XsltService xsltService,
                                            ExistDBTeiNotVersionedRepository commonRepository,
                                            JaegerstaetterConfig config) throws Exception {
        super(saxonExtensionProviderList, xsltDir, xsltService, commonRepository, config);
    }

    @Override
    public byte[] toNormalized(TeiDocument document) throws Exception {
        XsltExecutable normalizedExecutable = createExecutable(TransformationConstants.HTML_NORM_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedExecutable, null);
    }

    @Override
    public byte[] toDiplomatic(TeiDocument document) throws Exception {
        XsltExecutable diplomaticExecutable = createExecutable(TransformationConstants.HTML_DIPLO_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), diplomaticExecutable, null);
    }

    @Override
    public byte[] toXml(TeiDocument document, boolean includeHeader, VersionInfoList list) throws Exception {
        XsltExecutable toXmlExecutable = createExecutable(TransformationConstants.XML_STYLESHEET);
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.XML_PARAM_INCLUDE_HEADER_KEY, Boolean.toString(includeHeader));
        params.put(TransformationConstants.XML_PARAM_VERSIONS_KEY, JacksonXmlHelper.toXML(list));
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), toXmlExecutable, params);
    }


    @Override
    public byte[] toNormalizedWithSearchHighlight(TeiDocument document, SearchRequest searchRequest) throws Exception {
        XsltExecutable normalizedWithSearchExecutable = createExecutable(TransformationConstants.HTML_NORM_WITH_SEARCHHIGHLIGHT_STYLESHEET);
        Map<String, String> params = createNormWithHighlightParams(searchRequest);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedWithSearchExecutable, params);
    }

    @Override
    public byte[] toNormalizedWithMarkRsHighlight(TeiDocument document, MarkRsRequest markRsRequest) throws Exception {
        XsltExecutable normalizedExecutable = createExecutable(TransformationConstants.HTML_NORM_STYLESHEET);
        Map<String, String> params = createNormWithMarkRsParams(markRsRequest);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedExecutable, params);
    }

    @Override
    public IndexDocument getIndexDocument(TeiDocument document, String documentId) throws Exception {
        String docId = StringUtils.isBlank(documentId) ? document.getFilePath() : documentId;
        Map<String, String> params = Map.of("filePath", document.getFilePath(),
                "documentId", docId);
        XsltExecutable luceneExecutable = createExecutable(TransformationConstants.LUCENE_INDEXDOCUMENT_STYLESHEET);
        byte[] xml = xsltService.transform(new ByteArrayInputStream(document.getContent()), luceneExecutable, params);
        return JacksonXmlHelper.fromInputStreamToIndexDocument(new ByteArrayInputStream(xml));
    }


    @Override
    public byte[] commentDocToHtml(TeiDocument document) throws Exception {
        XsltExecutable commentDocHtmlExecutable = createExecutable(TransformationConstants.COMMENT_DOC_HTML_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), commentDocHtmlExecutable, null);
    }

    @Override
    public byte[] commentDocToToc(TeiDocument document) throws Exception {
        XsltExecutable commentDocTocExecutable = createExecutable(TransformationConstants.COMMENT_DOC_TOC_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), commentDocTocExecutable, null);
    }


    @Override
    public byte[] toPrintHtml(TeiDocument document) throws Exception {
        XsltExecutable printHtmlExecutable = createExecutable(TransformationConstants.PRINT_HTML_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), printHtmlExecutable, null);
    }

    @Override
    public byte[] toNoteContent(TeiDocument document, String noteId) throws Exception {
        XsltExecutable noteExecutable = createExecutable(TransformationConstants.NOTE_STYLESHEET);
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.NOTE_ID, noteId);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), noteExecutable, params);
    }

    @Override
    public MetadataResult getMetadataResult(TeiDocument document) throws Exception {
        XsltExecutable metadataExecutable = createExecutable(TransformationConstants.METADATA_METADATARESULT_STYLESHEET);
        String filename = document.getFilePath();
        if (filename.contains("/")) {
            filename = filename.substring(filename.lastIndexOf("/") + 1);
        }
        Map<String, String> params = Map.of("fileName", filename);
        byte[] result = xsltService.transform(new ByteArrayInputStream(document.getContent()), metadataExecutable, params);
        return JacksonXmlHelper.fromInputStreamToMetadataResult(new ByteArrayInputStream(result));
    }

    @Override
    public BiographyResult getBiographyResult(TeiDocument document) throws Exception {
        XsltExecutable metadataExecutable = createExecutable(TransformationConstants.BIOGRAPHY_BIOGRAPHYRESULT_STYLESHEET);
        String filename = document.getName();
        Map<String, String> params = Map.of("fileName", filename);
        byte[] result = xsltService.transform(new ByteArrayInputStream(document.getContent()), metadataExecutable, params);
        return JacksonXmlHelper.fromInputStreamToBiographyResult(new ByteArrayInputStream(result));
    }

    @Override
    public byte[] biographyToHtml(TeiDocument document) throws Exception {
        XsltExecutable normalizedExecutable = createExecutable(TransformationConstants.BIOGRAPHY_HTML_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedExecutable, null);
    }

    @Override
    public byte[] biographyToToc(TeiDocument document) throws Exception {
        XsltExecutable normalizedExecutable = createExecutable(TransformationConstants.BIOGRAPHY_TOC_STYLESHEET);
        return xsltService.transform(new ByteArrayInputStream(document.getContent()), normalizedExecutable, null);
    }

    @Override
    public byte[] biographyToPrintHtml(TeiDocument doc, int portNumber) throws Exception {
        XsltExecutable biographyPrintExecutable = createExecutable(TransformationConstants.BIOGRAPHY_PRINTHTML_STYLESHEET);
        Map<String, String> params = new HashMap<>();
        params.put(TransformationConstants.PARAM_PORT_NUMBER, Integer.toString(portNumber));
        return xsltService.transform(new ByteArrayInputStream(doc.getContent()), biographyPrintExecutable, params);
    }

    @Override
    public DocDescResult docDescResult(TeiDocument document) throws Exception {
        XsltExecutable docDescExecutable = createExecutable(TransformationConstants.DOC_DESC_STYLESHEET);
        String filename = document.getName();
        Map<String, String> params = Map.of(TransformationConstants.PARAM_FILENAME, filename);
        byte[] result = xsltService.transform(new ByteArrayInputStream(document.getContent()), docDescExecutable, params);
        return JacksonXmlHelper.fromInputStreamToDocDescResult(new ByteArrayInputStream(result));
    }

    @Override
    public byte[] biographyIndexToHtml(TeiDocument doc) throws Exception {
        XsltExecutable biographyIndexExecutable = createExecutable(TransformationConstants.BIOGRAPHY_INDEX_HTML_STYLESHEET);
        Map<String, String> params = new HashMap<>();
        return xsltService.transform(new ByteArrayInputStream(doc.getContent()), biographyIndexExecutable, params);
    }

    @Override
    public byte[] specialCorrespToHtml(TeiDocument doc) throws Exception {
        XsltExecutable specialCorrespExecutable = createExecutable(TransformationConstants.SPECIAL_CORRESP_HTML_STYLESHEET);
        Map<String, String> params = new HashMap<>();
        return xsltService.transform(new ByteArrayInputStream(doc.getContent()), specialCorrespExecutable, params);
    }

}
