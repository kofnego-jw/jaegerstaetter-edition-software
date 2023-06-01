package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;

import java.io.FileNotFoundException;
import java.util.List;

public interface RegistryService {

    /**
     * @return a list of all corporation entries
     */
    List<RegistryEntryCorporation> getCorporationRegistry() throws FileNotFoundException, FrontendModelException;

    /**
     * @return a list of all person entries
     */
    List<RegistryEntryPerson> getPersonRegistry() throws FileNotFoundException, FrontendModelException;

    /**
     * @return a list of all place entries
     */
    List<RegistryEntryPlace> getPlaceRegistry() throws FileNotFoundException, FrontendModelException;

    /**
     * @return a list of all saint entries
     */
    List<RegistryEntrySaint> getSaintRegistry() throws FileNotFoundException, FrontendModelException;

    /**
     * Get an index entry of a corporation
     *
     * @param corporationKey the key of the corporation
     * @return an index entry
     */
    IndexEntryCorporation getCorporationIndex(String corporationKey) throws FileNotFoundException, FrontendModelException;

    /**
     * Get an index entry of a person
     *
     * @param personKey the key of the person
     * @return an index entry
     */
    IndexEntryPerson getPersonIndex(String personKey) throws FileNotFoundException, FrontendModelException;

    /**
     * Get an index entry of a place
     *
     * @param placeKey the key of the place
     * @return an index entry
     */
    IndexEntryPlace getPlaceIndex(String placeKey) throws FileNotFoundException, FrontendModelException;

    /**
     * Get an index entry of a saint
     *
     * @param saintKey the key of the saint
     * @return an index entry
     */
    IndexEntrySaint getSaintIndex(String saintKey) throws FileNotFoundException, FrontendModelException;


}
