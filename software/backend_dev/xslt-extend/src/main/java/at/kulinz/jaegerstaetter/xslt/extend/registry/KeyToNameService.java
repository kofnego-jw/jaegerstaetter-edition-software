package at.kulinz.jaegerstaetter.xslt.extend.registry;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryType;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import at.kulinz.jaegerstaetter.metadata.registry.repository.DataRepository;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class KeyToNameService {

    private final File registryJsonFile;
    private final LoadingCache<String, DataRepository> repositoryCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public DataRepository load(String key) throws Exception {
                    JsonFileRepository repository = new JsonFileRepository(registryJsonFile);
                    return repository;
                }
            });

    public KeyToNameService(File registryJsonFile) {
        this.registryJsonFile = registryJsonFile;
    }

    private RegistryType getRegistryType(String type) {
        return type == null ? RegistryType.PERSON :
                switch (type.toLowerCase()) {
                    case "organisation" -> RegistryType.CORPORATION;
                    case "place" -> RegistryType.PLACE;
                    default -> RegistryType.PERSON;
                };
    }

    public String getReadableNameByKey(String key, String type) throws Exception {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        RegistryType registryType = getRegistryType(type);
        return switch (registryType) {
            case CORPORATION -> getCorporationName(key);
            case PLACE -> getPlaceName(key);
            case PERSON, SAINT -> getReadablePersonName(key);
            case BIBLE -> key;
        };
    }

    public String getOfficialNameByKey(String key, String type) throws Exception {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        RegistryType registryType = getRegistryType(type);
        return switch (registryType) {
            case CORPORATION -> getCorporationName(key);
            case PLACE -> getPlaceName(key);
            case PERSON, SAINT -> getOfficialPersonName(key);
            case BIBLE -> key;
        };
    }

    private DataRepository getDataRepository() throws Exception {
        return this.repositoryCache.get("data");
    }

    private String getCorporationName(String key) throws Exception {
        return getDataRepository().findCorporationByKey(key)
                .map(ci -> ci.getReadableName()).orElse(key);
    }

    private String getPlaceName(String key) throws Exception {
        return getDataRepository().findPlaceByKey(key).map(PlaceInfo::getReadableName).orElse(key);
    }

    private String getReadablePersonName(String key) throws Exception {
        String myKey = key.startsWith("#") ? key.substring(1) : key;
        DataRepository repository = getDataRepository();
        return repository.findAllPersonInfos().stream().filter(pi -> pi.key.equals(myKey))
                .findAny().map(PersonInfo::getGeneratedReadableName)
                .orElse(repository.findAllSaintInfos().stream().filter(si -> key.equals(si.key)).findAny().map(SaintInfo::getReadableName).orElse(""));
    }

    private String getOfficialPersonName(String key) throws Exception {
        String myKey = key.startsWith("#") ? key.substring(1) : key;
        DataRepository repository = getDataRepository();
        return repository.findAllPersonInfos().stream().filter(pi -> pi.key.equals(myKey))
                .findAny().map(PersonInfo::getGeneratedOfficialName)
                .orElse(repository.findAllSaintInfos().stream().filter(si -> key.equals(si.key)).findAny().map(SaintInfo::getReadableName).orElse(""));
    }

}
