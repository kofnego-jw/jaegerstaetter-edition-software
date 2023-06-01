package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.ResourceListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.StringListMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.ResourceControllerApi;
import at.kulinz.jaegerstaetter.index.service.impl.EditionIndexResourceService;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.EditionResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.net.URLConnection;

@RestController
@Profile("edition")
public class EditionResourceController implements ResourceControllerApi {

    private static final String XML_MIMETYPE = URLConnection.guessContentTypeFromName("sample.xml");
    private static final String HTML_MIMETYPE = URLConnection.guessContentTypeFromName("sample.html");
    private static final String JPEG_MIMETYPE = URLConnection.guessContentTypeFromName("sample.jpg");
    private static final String PDF_MIMETYPE = URLConnection.guessContentTypeFromName("sample.pdf");

    @Autowired
    private EditionResourceService editionResourceService;

    @Autowired
    private EditionIndexResourceService editionIndexResourceService;

    @Override
    public ResourceListMsg toc() {
        return new ResourceListMsg(editionIndexResourceService.listAllResources());
    }

    @Override
    public ResourceDTO metadata(String resourceId, String date) throws Exception {
        if (date == null) {
            return editionResourceService.findLatestResourceById(resourceId);
        }
        return editionResourceService.findResourceById(resourceId, date);
    }

    @Override
    public void getXml(String filePath, String date, boolean includeHeader, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] xml = StringUtils.isBlank(date) ? editionResourceService.getXmlRepresentation(filePath, includeHeader) :
                editionResourceService.getXmlRepresentation(filePath, date, includeHeader);
        ControllerHelper.sendContent(response, XML_MIMETYPE, xml);
    }

    @Override
    public void getDiplo(String filePath, String date, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = StringUtils.isBlank(date) ? editionResourceService.getHtmlDiplomaticRepresentation(filePath) :
                editionResourceService.getHtmlDiplomaticRepresentation(filePath, date);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getNorm(String filePath, String date, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = StringUtils.isBlank(date) ? editionResourceService.getHtmlNormalizedRepresentation(filePath) :
                editionResourceService.getHtmlNormalizedRepresentation(filePath, date);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getNormAsPdf(String resourceId, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] pdf = editionResourceService.getPdfNormRepresentation(resourceId);
        ControllerHelper.sendContent(response, PDF_MIMETYPE, pdf);
    }

    @Override
    public void getNormWithSearchHighlight(String resourceId, SearchRequest searchRequest, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = editionResourceService.getHtmlNormalizedRepresentationWithHighlight(resourceId, searchRequest);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getNormWithMarkRsRequest(String resourceId, MarkRsRequest markRsRequest, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] html = editionResourceService.getHtmlNormalizedRepresentationWithMarkRsRequest(resourceId, markRsRequest);
        ControllerHelper.sendContent(response, HTML_MIMETYPE, html);
    }

    @Override
    public void getFacsimile(String filePath, HttpServletResponse response) throws FileNotFoundException, FrontendModelException {
        byte[] facsimile = editionResourceService.getFacsimile(filePath);
        ControllerHelper.sendContent(response, JPEG_MIMETYPE, facsimile);
    }

    @Override
    public StringListMsg getKeyValue(String type) {
        return new StringListMsg(editionIndexResourceService.getKeyValues(type));
    }

    @Override
    public NoteResourceDTO getNote(String resourceId, String noteId, String date) throws Exception {
        if (date == null) {
            return editionResourceService.getLatestNoteResource(resourceId, noteId);
        }
        return editionResourceService.getNoteResource(resourceId, noteId, date);
    }

    @Override
    public CorrespInfo getCorrespPlaces(String filename, String anchorName) throws Exception {
        return editionResourceService.getCorrespInfo(filename, anchorName);
    }
}
