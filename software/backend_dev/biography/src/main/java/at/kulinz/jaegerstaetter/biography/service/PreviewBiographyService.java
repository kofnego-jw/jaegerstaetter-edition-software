package at.kulinz.jaegerstaetter.biography.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.Biography;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.BiographyFW;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.BiographyService;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.PhotoDocumentService;
import at.kulinz.jaegerstaetter.pdfgenerator.service.PdfGenerator;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.tei.registry.service.impl.PreviewRegistryService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class PreviewBiographyService implements BiographyService {

    private final BiographyServiceImpl delegate;

    public PreviewBiographyService(ExistDBTeiRepository previewRepository,
                                   PreviewRegistryService previewRegistryservice,
                                   PhotoDocumentService photoDocumentService,
                                   TransformationService transformationService,
                                   XPathService xPathService,
                                   PdfGenerator pdfGenerator,
                                   JaegerstaetterConfig config) {
        this.delegate = new BiographyServiceImpl(previewRepository, previewRegistryservice, photoDocumentService, transformationService, xPathService, pdfGenerator, config);
    }

    @Cacheable(value = "preview.biography.list")
    @Override
    public List<BiographyFW> listBiographies() {
        return this.delegate.listBiographies();
    }

    @Cacheable(value = "preview.biography")
    @Override
    public Biography getBiography(String filename) throws FrontendModelException, FileNotFoundException {
        return this.delegate.getBiography(filename);
    }

    @Override
    public byte[] getImage(String filename) throws FileNotFoundException, FrontendModelException {
        return this.delegate.getImage(filename);
    }

    @Override
    public byte[] getPdf(String filename) throws FileNotFoundException, FrontendModelException {
        return this.delegate.getPdf(filename);
    }
}
