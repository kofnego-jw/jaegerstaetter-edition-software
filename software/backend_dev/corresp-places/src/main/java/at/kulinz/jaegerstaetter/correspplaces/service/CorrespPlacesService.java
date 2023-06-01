package at.kulinz.jaegerstaetter.correspplaces.service;

import at.kulinz.jaegerstaetter.correspplaces.CorrespPlacesException;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CorrespInfo;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.DocumentInfo;

import java.util.List;
import java.util.Optional;

public interface CorrespPlacesService {

    void reset();

    void analyzeDocument(TeiDocument documents) throws CorrespPlacesException;

    List<CorrespInfo> getAllCorresPlaces();

    Optional<CorrespInfo> findByDocumentInfo(DocumentInfo documentInfo);

    void load() throws CorrespPlacesException;

    void save() throws CorrespPlacesException;

}
