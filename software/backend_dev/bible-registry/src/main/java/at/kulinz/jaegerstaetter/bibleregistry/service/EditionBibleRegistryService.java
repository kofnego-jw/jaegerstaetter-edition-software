package at.kulinz.jaegerstaetter.bibleregistry.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexEntryBiblePassages;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryBibleBook;
import at.kulinz.jaegerstaetter.index.service.impl.EditionIndexResourceService;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.EditionResourceService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class EditionBibleRegistryService {

    private final BibleRegistryServiceImpl delegate;

    @Autowired
    public EditionBibleRegistryService(EditionResourceService editionResourceService,
                                       ExistDBTeiRepository editionRepository, XPathService xPathService,
                                       EditionIndexResourceService editionIndexResourceService,
                                       JaegerstaetterConfig config) throws Exception {
        this.delegate = new BibleRegistryServiceImpl(editionResourceService, editionRepository, xPathService, editionIndexResourceService, config);
    }

    public void indexDocuments() throws FrontendModelException {
        delegate.indexDocuments();
    }

    public List<RegistryEntryBibleBook> getBibleBooks() throws FrontendModelException {
        return delegate.getBibleBooks();
    }

    public IndexEntryBiblePassages getBibleResources(String book) throws FileNotFoundException, FrontendModelException {
        return delegate.getBibleResources(book);
    }
}
