package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.bibleregistry.service.PreviewBibleRegistryService;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.RegistryControllerApi;
import at.kulinz.jaegerstaetter.tei.registry.service.impl.PreviewRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@Profile("preview")
public class PreviewRegistryController implements RegistryControllerApi {

    @Autowired
    private PreviewBibleRegistryService bibleRegistryService;

    @Autowired
    private PreviewRegistryService registryService;

    @Override
    public List<RegistryEntryCorporation> listCorporations() throws FileNotFoundException, FrontendModelException {
        return registryService.getCorporationRegistry();
    }

    @Override
    public IndexEntryCorporation listCorporationResources(String corporationKey) throws FileNotFoundException, FrontendModelException {
        return registryService.getCorporationIndex(corporationKey);
    }

    @Override
    public List<RegistryEntryPerson> listPersons() throws FileNotFoundException, FrontendModelException {
        return registryService.getPersonRegistry();
    }

    @Override
    public IndexEntryPerson listPersonResources(String personKey) throws FileNotFoundException, FrontendModelException {
        return registryService.getPersonIndex(personKey);
    }

    @Override
    public List<RegistryEntryPlace> listPlaces() throws FileNotFoundException, FrontendModelException {
        return registryService.getPlaceRegistry();
    }

    @Override
    public IndexEntryPlace listPlaceResources(String placeKey) throws FileNotFoundException, FrontendModelException {
        return registryService.getPlaceIndex(placeKey);
    }

    @Override
    public List<RegistryEntrySaint> listSaints() throws FileNotFoundException, FrontendModelException {
        return registryService.getSaintRegistry();
    }

    @Override
    public IndexEntrySaint listSaintResources(String saintKey) throws FileNotFoundException, FrontendModelException {
        return registryService.getSaintIndex(saintKey);
    }

    @Override
    public List<RegistryEntryBibleBook> listBibleBooks() throws FrontendModelException {
        return bibleRegistryService.getBibleBooks();
    }

    @Override
    public IndexEntryBiblePassages listBiblePassageResource(String book) throws FrontendModelException {
        return bibleRegistryService.getBibleResources(book);
    }
}
