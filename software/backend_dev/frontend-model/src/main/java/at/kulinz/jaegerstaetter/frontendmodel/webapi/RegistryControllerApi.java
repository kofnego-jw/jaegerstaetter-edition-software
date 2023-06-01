package at.kulinz.jaegerstaetter.frontendmodel.webapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RequestMapping("/api/registry")
@RestController
public interface RegistryControllerApi {

    @GetMapping("/corporation/")
    List<RegistryEntryCorporation> listCorporations() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/corporation/{key}")
    IndexEntryCorporation listCorporationResources(@PathVariable("key") String corporationKey) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/person/")
    List<RegistryEntryPerson> listPersons() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/person/{key}")
    IndexEntryPerson listPersonResources(@PathVariable("key") String personKey) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/place/")
    List<RegistryEntryPlace> listPlaces() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/place/{key}")
    IndexEntryPlace listPlaceResources(@PathVariable("key") String placeKey) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/saint/")
    List<RegistryEntrySaint> listSaints() throws FileNotFoundException, FrontendModelException;

    @GetMapping("/saint/{key}")
    IndexEntrySaint listSaintResources(@PathVariable("key") String saintKey) throws FileNotFoundException, FrontendModelException;

    @GetMapping("/bible/")
    List<RegistryEntryBibleBook> listBibleBooks() throws FrontendModelException;

    @GetMapping("/bible/{book}")
    IndexEntryBiblePassages listBiblePassageResource(@PathVariable("book") String book) throws FileNotFoundException, FrontendModelException;


}
