package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.correspplaces.service.CorrespPlacesService;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.ResourceService;
import at.kulinz.jaegerstaetter.pdfgenerator.service.PdfGenerator;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class PreviewResourceService implements ResourceService {

    private final ResourceServiceImpl delegate;

    public PreviewResourceService(ExistDBTeiRepository previewRepository, MetadataService metadataService,
                                  TransformationService transformationService, FacsimileService facsimileService,
                                  PdfGenerator pdfGenerator, CorrespPlacesService previewCorrespPlacesService, JaegerstaetterConfig config) {
        this.delegate = new ResourceServiceImpl(previewRepository, metadataService, transformationService, facsimileService, pdfGenerator, previewCorrespPlacesService, config);
    }

    @Override
    @Cacheable(value = "preview.resource.content")
    public byte[] getContent(String resourceId) throws FileNotFoundException {
        return delegate.getContent(resourceId);
    }

    @Override
    public ResourceFWDTO fromTeiDocumentFW(TeiDocumentFW fw) {
        return delegate.fromTeiDocumentFW(fw);
    }

    @Override
    @Cacheable("preview.resource.list")
    public List<ResourceFWDTO> listAllResources() {
        return delegate.listAllResources();
    }

    @Override
    public ResourceFWDTO findResourceFWByIdAndDocumentId(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        return delegate.findResourceFWByIdAndDocumentId(resourceId, date);
    }

    @Override
    @Cacheable("preview.resource.latestfw")
    public ResourceFWDTO findLatestResourceFWByIdAndDocumentId(String resourceId) throws FrontendModelException, FileNotFoundException {
        return delegate.findResourceFWByIdAndDocumentId(resourceId, null);
    }

    @Override
    public ResourceDTO findResourceById(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        return delegate.findResourceById(resourceId, date);
    }

    @Override
    @Cacheable("preview.resource.latest")
    public ResourceDTO findLatestResourceById(String resourceId) throws FileNotFoundException, FrontendModelException {
        return delegate.findResourceById(resourceId, null);
    }

    @Override
    public byte[] getXmlRepresentation(String resourceId, boolean includeHeader) throws FrontendModelException, FileNotFoundException {
        return delegate.getXmlRepresentation(resourceId, includeHeader);
    }

    @Override
    public byte[] getXmlRepresentation(String resourceId, String dating, boolean includeHeader) throws FileNotFoundException, FrontendModelException {
        return delegate.getXmlRepresentation(resourceId, dating, includeHeader);
    }

    @Override
    public byte[] getXmlRepresentation(String resourceId, Integer version) throws FileNotFoundException {
        return delegate.getXmlRepresentation(resourceId, version);
    }

    @Override
    public byte[] getHtmlNormalizedRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlNormalizedRepresentation(resourceId);
    }

    @Override
    public byte[] getHtmlNormalizedRepresentation(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlNormalizedRepresentation(resourceId, date);
    }

    @Override
    public byte[] getHtmlNormalizedRepresentationWithHighlight(String resourceId, SearchRequest searchRequest) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlNormalizedRepresentationWithHighlight(resourceId, searchRequest);
    }

    @Override
    public byte[] getHtmlNormalizedRepresentationWithMarkRsRequest(String resourceId, MarkRsRequest markRsRequest) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlNormalizedRepresentationWithMarkRsRequest(resourceId, markRsRequest);
    }

    @Override
    public byte[] getHtmlNormalizedRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlNormalizedRepresentation(resourceId, version);
    }

    @Override
    public byte[] getHtmlDiplomaticRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlDiplomaticRepresentation(resourceId);
    }

    @Override
    public byte[] getHtmlDiplomaticRepresentation(String resourceId, String date) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlDiplomaticRepresentation(resourceId, date);
    }

    @Override
    public byte[] getHtmlDiplomaticRepresentation(String resourceId, Integer version) throws FileNotFoundException, FrontendModelException {
        return delegate.getHtmlDiplomaticRepresentation(resourceId, version);
    }

    @Override
    public byte[] getFacsimile(String facResourceId) throws FileNotFoundException {
        return delegate.getFacsimile(facResourceId);
    }

    @Override
    public byte[] getPdfNormRepresentation(String resourceId) throws FileNotFoundException, FrontendModelException {
        return delegate.getPdfNormRepresentation(resourceId);
    }

    @Override
    public NoteResourceDTO getNoteResource(String resourceId, String noteId, String date) throws FileNotFoundException, FrontendModelException {
        return delegate.getNoteResource(resourceId, noteId, date);
    }

    @Override
    @Cacheable("preview.resource.latestnote")
    public NoteResourceDTO getLatestNoteResource(String resourceId, String noteId) throws FileNotFoundException, FrontendModelException {
        return delegate.getNoteResource(resourceId, noteId, null);
    }

    @Override
    public CorrespInfo getCorrespInfo(String resourceId, String anchorName) {
        return delegate.getCorrespInfo(resourceId, anchorName);
    }
}
