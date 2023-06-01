package at.kulinz.jaegerstaetter.tei.registry.service.impl;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.RegistryService;
import at.kulinz.jaegerstaetter.search.service.impl.PreviewSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class PreviewRegistryService implements RegistryService {

    private final RegistryServiceImpl delegate;

    @Autowired
    public PreviewRegistryService(PreviewRegistryRepository registryRepository, PreviewSearchService previewSearchService) {
        this.delegate = new RegistryServiceImpl(registryRepository, previewSearchService);
    }

    @Override
    public List<RegistryEntryCorporation> getCorporationRegistry() throws FileNotFoundException, FrontendModelException {
        return delegate.getCorporationRegistry();
    }

    @Override
    public List<RegistryEntryPerson> getPersonRegistry() throws FileNotFoundException, FrontendModelException {
        return delegate.getPersonRegistry();
    }

    @Override
    public List<RegistryEntryPlace> getPlaceRegistry() throws FileNotFoundException, FrontendModelException {
        return delegate.getPlaceRegistry();
    }

    @Override
    public List<RegistryEntrySaint> getSaintRegistry() throws FileNotFoundException, FrontendModelException {
        return delegate.getSaintRegistry();
    }

    @Override
    public IndexEntryCorporation getCorporationIndex(String corporationKey) throws FileNotFoundException, FrontendModelException {
        return delegate.getCorporationIndex(corporationKey);
    }

    @Override
    public IndexEntryPerson getPersonIndex(String personKey) throws FileNotFoundException, FrontendModelException {
        return delegate.getPersonIndex(personKey);
    }

    @Override
    public IndexEntryPlace getPlaceIndex(String placeKey) throws FileNotFoundException, FrontendModelException {
        return delegate.getPlaceIndex(placeKey);
    }

    @Override
    public IndexEntrySaint getSaintIndex(String saintKey) throws FileNotFoundException, FrontendModelException {
        return delegate.getSaintIndex(saintKey);
    }
}
