package at.kulinz.jaegerstaetter.search.service.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryType;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchResult;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.SearchService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class MetadataSearchService implements SearchService {

    private final SearchServiceImpl delegate;

    public MetadataSearchService(JaegerstaetterConfig config) {
        this.delegate = new SearchServiceImpl(new File(config.workingDir(), "registry_lucene"));
    }

    @Override
    public SearchResult search(SearchRequest query) throws FrontendModelException {
        return delegate.search(query);
    }

    @Override
    public List<ResourceFWDTO> listOccurrence(RegistryType type, String key) throws FrontendModelException {
        return delegate.listOccurrence(type, key);
    }
}
