package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryType;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchResult;

import java.util.List;

public interface SearchService {

    /**
     * Performs a search with the given parameter
     *
     * @param request the search request
     * @return a search result
     * @throws FrontendModelException if any exception happens
     */
    SearchResult search(SearchRequest request) throws FrontendModelException;

    /**
     * Searches for occurrences of a given registry entry
     *
     * @param type the type of the registry
     * @param key  the key value
     * @return a List of ResourceFWDTO
     * @throws FrontendModelException if any exception happens
     */
    List<ResourceFWDTO> listOccurrence(RegistryType type, String key) throws FrontendModelException;
}
