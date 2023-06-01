package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexEntryBiblePassages;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryBibleBook;

import java.util.List;

public interface BibleRegistryService {

    void indexDocuments() throws FrontendModelException;

    List<RegistryEntryBibleBook> getBibleBooks() throws FrontendModelException;

    IndexEntryBiblePassages getBibleResources(String book) throws FrontendModelException;
}
