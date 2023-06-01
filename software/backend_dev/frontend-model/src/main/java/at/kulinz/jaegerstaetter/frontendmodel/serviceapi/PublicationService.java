package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;

/**
 * Data ingest service
 */
public interface PublicationService {

    /**
     * Clone all data from the edition to the preview directory
     *
     * @param password the password, if invalid, nothing should happen
     * @throws Exception when the password is invalid
     */
    void cloneEditionToPreview(String password) throws Exception;

    /**
     * Clone the edition and ingest data to the preview directory
     *
     * @param password the password
     * @throws Exception when the password is invalid
     */
    void cloneEditionAndIngestToPreview(String password) throws Exception;

    /**
     * Ingest data to the edition
     *
     * @param password the password
     * @throws Exception when the password is invalid
     */
    void ingestToEdition(String password) throws Exception;

    /**
     * Get the statistics of the preview data
     *
     * @return a StatReport object
     * @throws Exception if exception happens
     */
    StatReport getStatistics() throws Exception;
}
