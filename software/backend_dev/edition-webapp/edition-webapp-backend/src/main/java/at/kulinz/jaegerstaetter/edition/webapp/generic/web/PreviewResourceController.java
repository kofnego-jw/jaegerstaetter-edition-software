package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.ResourceListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.StringListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.ResourceControllerApi;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexResourceService;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.PreviewResourceService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Profile("preview")
public class PreviewResourceController implements ResourceControllerApi {

    private static final String XML_MIMETYPE = URLConnection.guessContentTypeFromName("sample.xml");
    private static final String HTML_MIMETYPE = URLConnection.guessContentTypeFromName("sample.html");
    private static final String JPEG_MIMETYPE = URLConnection.guessContentTypeFromName("sample.jpg");
    private static final String PDF_MIMETYPE = URLConnection.guessContentTypeFromName("sample.pdf");

    @Autowired
    private PreviewResourceService previewResourceService;
    private final LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(3600, TimeUnit.SECONDS)
            .build(new CacheLoader<>() {
                @Override
                public Object load(String key) {
                    switch (key) {
                        case "resourceList" -> {
                            List<ResourceFWDTO> list = previewResourceService.listAllResources();
                            return ControllerMsgHelper.fromResourceList(list);
                        }
                        default -> {
                        }
                    }
                    return null;
                }
            });
    @Autowired
    private PreviewIndexResourceService previewIndexResourceService;

    @Override
    public ResourceListMsg toc() {
        return new ResourceListMsg(this.previewIndexResourceService.listAllResources());
    }

    @Override
    public ResourceDTO metadata(String resourceId, String date) throws Exception {
        if (date == null) {
            return previewResourceService.findLatestResourceById(resourceId);
        }
        return previewResourceService.findResourceById(resourceId, date);
    }

    @Override
    public void getXml(String filePath, String date, boolean includeHeader, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] xml = StringUtils.isBlank(date) ? previewResourceService.getXmlRepresentation(filePath, includeHeader) :
                previewResourceService.getXmlRepresentation(filePath, date, includeHeader);
        ControllerHelper.sendContent(response, XML_MIMETYPE, xml);
    }

    @Override
    public void getDiplo(String filePath, String date, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = StringUtils.isBlank(date) ? previewResourceService.getHtmlDiplomaticRepresentation(filePath) :
                previewResourceService.getHtmlDiplomaticRepresentation(filePath, date);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getNorm(String filePath, String date, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = StringUtils.isBlank(date) ? previewResourceService.getHtmlNormalizedRepresentation(filePath) :
                previewResourceService.getHtmlNormalizedRepresentation(filePath, date);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getNormWithSearchHighlight(String resourceId, SearchRequest searchRequest, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = previewResourceService.getHtmlNormalizedRepresentationWithHighlight(resourceId, searchRequest);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getNormWithMarkRsRequest(String resourceId, MarkRsRequest markRsRequest, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = previewResourceService.getHtmlNormalizedRepresentationWithMarkRsRequest(resourceId, markRsRequest);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getFacsimile(String filePath, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] facsimile = previewResourceService.getFacsimile(filePath);
        ControllerHelper.sendContent(response, JPEG_MIMETYPE, facsimile);
    }

    @Override
    public void getNormAsPdf(String resourceId, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] pdf = previewResourceService.getPdfNormRepresentation(resourceId);
        ControllerHelper.sendContent(response, PDF_MIMETYPE, pdf);
        ;
    }

    @Override
    public StringListMsg getKeyValue(String type) {
        return new StringListMsg(previewIndexResourceService.getKeyValues(type));
    }

    @Override
    public NoteResourceDTO getNote(String resourceId, String noteId, String date) throws Exception {
        if (date == null) {
            return previewResourceService.getLatestNoteResource(resourceId, noteId);
        }
        return previewResourceService.getNoteResource(resourceId, noteId, date);
    }

    @Override
    public CorrespInfo getCorrespPlaces(String filename, String anchorName) throws Exception {
        return previewResourceService.getCorrespInfo(filename, anchorName);
    }
}
