package at.kulinz.jaegerstaetter.bibleregistry.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexEntryBiblePassages;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryBibleBook;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexResourceService;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.PreviewResourceService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PreviewBibleRegistryService {

    private final BibleRegistryServiceImpl delegate;

    @Autowired
    public PreviewBibleRegistryService(PreviewResourceService previewResourceService,
                                       ExistDBTeiRepository previewRepository, XPathService xPathService,
                                       PreviewIndexResourceService previewIndexResourceService,
                                       JaegerstaetterConfig config) throws Exception {
        this.delegate = new BibleRegistryServiceImpl(previewResourceService, previewRepository, xPathService, previewIndexResourceService, config);
    }

    public void indexDocuments() throws FrontendModelException {
        delegate.indexDocuments();
    }

    public List<RegistryEntryBibleBook> getBibleBooks() throws FrontendModelException {
        return delegate.getBibleBooks();
    }

    public IndexEntryBiblePassages getBibleResources(String book) throws FrontendModelException {
        return delegate.getBibleResources(book);
    }
}
