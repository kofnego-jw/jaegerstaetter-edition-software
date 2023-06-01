package at.kulinz.jaegerstaetter.tei.registry.service.impl;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.RegistryService;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.SearchService;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonRepository;
import at.kulinz.jaegerstaetter.tei.registry.service.RegistryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RegistryServiceImpl implements RegistryService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RegistryRepository registryRepository;

    private final SearchService searchService;

    public RegistryServiceImpl(RegistryRepository registryRepository, SearchService searchService) {
        this.registryRepository = registryRepository;
        this.searchService = searchService;
    }

    private JsonRepository fromJson() throws FileNotFoundException, FrontendModelException {
        Optional<TeiDocument> jsonOpt = registryRepository.getEditionRegistryJsonDocument();
        if (jsonOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find published registry data.");
        }
        TeiDocument json = jsonOpt.get();
        try {
            return OBJECT_MAPPER.readValue(json.getContent(), JsonRepository.class);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot read json value.", e);
        }
    }

    @Override
    public List<RegistryEntryCorporation> getCorporationRegistry() throws FileNotFoundException, FrontendModelException {
        return fromJson().corporationInfoList
                .stream().map(RegistryEntryCorporation::fromCorporationInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistryEntryPerson> getPersonRegistry() throws FileNotFoundException, FrontendModelException {
        return fromJson().personInfoList
                .stream().map(RegistryEntryPerson::fromPersonInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistryEntryPlace> getPlaceRegistry() throws FileNotFoundException, FrontendModelException {
        return fromJson().placeInfoList
                .stream().map(RegistryEntryPlace::fromPlaceInfo)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistryEntrySaint> getSaintRegistry() throws FileNotFoundException, FrontendModelException {
        return fromJson().saintInfoList
                .stream().map(RegistryEntrySaint::fromSaintInfo)
                .collect(Collectors.toList());
    }

    @Override
    public IndexEntryCorporation getCorporationIndex(String corporationKey) throws FileNotFoundException, FrontendModelException {
        RegistryEntryCorporation registryEntry = getCorporationRegistry().stream().filter(entry -> entry.key.equals(corporationKey))
                .findAny().orElseThrow(() -> new FileNotFoundException("Cannot find registry entry."));
        List<ResourceFWDTO> hits = searchService.listOccurrence(RegistryType.CORPORATION, corporationKey);
        return new IndexEntryCorporation(registryEntry, hits);
    }

    @Override
    public IndexEntryPerson getPersonIndex(String personKey) throws FileNotFoundException, FrontendModelException {
        RegistryEntryPerson registryEntry = getPersonRegistry().stream().filter(entry -> entry.key.equals(personKey))
                .findAny().orElseThrow(() -> new FileNotFoundException("Cannot find person entry."));
        List<ResourceFWDTO> hits = searchService.listOccurrence(RegistryType.PERSON, personKey);
        return new IndexEntryPerson(registryEntry, hits);
    }

    @Override
    public IndexEntryPlace getPlaceIndex(String placeKey) throws FileNotFoundException, FrontendModelException {
        RegistryEntryPlace registryEntry = getPlaceRegistry().stream().filter(entry -> entry.key.equals(placeKey))
                .findAny().orElseThrow(() -> new FileNotFoundException("Cannot find place entry."));
        List<ResourceFWDTO> hits = searchService.listOccurrence(RegistryType.PLACE, placeKey);
        return new IndexEntryPlace(registryEntry, hits);
    }

    @Override
    public IndexEntrySaint getSaintIndex(String saintKey) throws FileNotFoundException, FrontendModelException {
        RegistryEntrySaint registryEntry = getSaintRegistry().stream().filter(entry -> entry.key.equals(saintKey))
                .findAny().orElseThrow(() -> new FileNotFoundException("Cannot find saint entry."));
        List<ResourceFWDTO> hits = searchService.listOccurrence(RegistryType.SAINT, saintKey);
        return new IndexEntrySaint(registryEntry, hits);
    }
}
