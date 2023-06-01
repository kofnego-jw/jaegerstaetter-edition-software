package at.kulinz.jaegerstaetter.existdbinit.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import at.kulinz.jaegerstaetter.tei.edition.connector.repoobj.PhotoDocumentJsonRepo;
import at.kulinz.jaegerstaetter.tei.registry.service.RegistryRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Component
public class ExistDBInitService {

    private static final String START_XML = "/existinit/start.xml";

    private final ExistDBTeiRepository previewRepository;
    private final ExistDBTeiRepository editionRepository;
    private final ExistDBTeiNotVersionedRepository commonRepository;
    private final ExistDBTeiNotVersionedRepository ingestRepository;

    private final JaegerstaetterConfig jaegerstaetterConfig;

    @Autowired
    public ExistDBInitService(JaegerstaetterConfig config) throws Exception {
        this.previewRepository = config.previewRepository();
        this.editionRepository = config.editionRepository();
        this.commonRepository = config.commonRepository();
        this.ingestRepository = config.ingestRepository();
        this.jaegerstaetterConfig = config;
    }

    /**
     * Create following directories if not exists:
     *
     * @throws Exception
     */
    public void createExistDBCollectionsIfNecessary() throws Exception {
        String registryJson = (jaegerstaetterConfig.getExistEditionRegistryDirName().endsWith("/") ?
                jaegerstaetterConfig.getExistEditionRegistryDirName() :
                jaegerstaetterConfig.getExistEditionRegistryDirName() + "/") + RegistryRepository.JSON_FILENAME;

        // Create Common directories
        this.commonRepository.init();
        this.commonRepository.mkdirs(jaegerstaetterConfig.getExistCommonPhotodocumentImageDirName());
        this.commonRepository.mkdirs(jaegerstaetterConfig.getExistCommonPhotodocumentExcelDirName());
        this.commonRepository.mkdirs(jaegerstaetterConfig.getExistCommonFacsimileDirName());
        Optional<TeiDocument> photoDocJson = this.commonRepository.retrieveByFilePath(jaegerstaetterConfig.getExistCommonPhotodocumentJsonFile());
        if (photoDocJson.isEmpty()) {
            PhotoDocumentJsonRepo jsonRepo = new PhotoDocumentJsonRepo(Collections.emptyList());
            this.commonRepository.createNewDocument(jaegerstaetterConfig.getExistCommonPhotodocumentJsonFile(), jsonRepo.toJson(), LocalDateTime.now());
        }

        // Create Edition directories
        this.editionRepository.init();
        this.editionRepository.mkdirs(jaegerstaetterConfig.getExistEditionDataDirName());
        this.editionRepository.mkdirs(jaegerstaetterConfig.getExistEditionBiographyDirName());
        this.editionRepository.mkdirs(jaegerstaetterConfig.getExistEditionRegistryDirName());
        this.editionRepository.mkdirs(jaegerstaetterConfig.getExistEditionCommentDocDirName());
        Optional<TeiDocument> editionStartXml = this.editionRepository.retrieveByFilePath(jaegerstaetterConfig.getExistEditionCommentDocStartXmlName());
        if (editionStartXml.isEmpty()) {
            byte[] sampleStart = getJarResource(START_XML);
            this.editionRepository.createNewDocument(jaegerstaetterConfig.getExistEditionCommentDocStartXmlName(), sampleStart, LocalDateTime.now());
        }
        File temp = new File(jaegerstaetterConfig.workingDir(), "inittemp/" + RegistryRepository.JSON_FILENAME);
        JsonFileRepository jsonFileRepository = new JsonFileRepository(temp);
        jsonFileRepository.writeData();
        Optional<TeiDocument> editionRegistryJson = this.editionRepository.retrieveByFilePath(registryJson);
        if (editionRegistryJson.isEmpty()) {
            byte[] sampleRegistry = FileUtils.readFileToByteArray(temp);
            this.editionRepository.createNewDocument(registryJson, sampleRegistry, LocalDateTime.now());
        }

        // Create Preview directories
        this.previewRepository.init();
        this.previewRepository.mkdirs(jaegerstaetterConfig.getExistEditionDataDirName());
        this.previewRepository.mkdirs(jaegerstaetterConfig.getExistEditionBiographyDirName());
        this.previewRepository.mkdirs(jaegerstaetterConfig.getExistEditionRegistryDirName());
        this.previewRepository.mkdirs(jaegerstaetterConfig.getExistEditionCommentDocDirName());
        Optional<TeiDocument> previewStartXml = this.previewRepository.retrieveByFilePath(jaegerstaetterConfig.getExistEditionCommentDocStartXmlName());
        if (previewStartXml.isEmpty()) {
            byte[] sampleStart = getJarResource(START_XML);
            this.previewRepository.createNewDocument(jaegerstaetterConfig.getExistEditionCommentDocStartXmlName(), sampleStart, LocalDateTime.now());
        }
        Optional<TeiDocument> previewRegistryJson = this.previewRepository.retrieveByFilePath(registryJson);
        if (previewRegistryJson.isEmpty()) {
            byte[] sampleRegistry = FileUtils.readFileToByteArray(temp);
            this.previewRepository.createNewDocument(registryJson, sampleRegistry, LocalDateTime.now());
        }

        // Create Ingest directories
        this.ingestRepository.init();
        this.ingestRepository.mkdirs(jaegerstaetterConfig.getExistEditionDataDirName());
        this.ingestRepository.mkdirs(jaegerstaetterConfig.getExistEditionBiographyDirName());
        this.ingestRepository.mkdirs(jaegerstaetterConfig.getExistEditionRegistryDirName());
        this.ingestRepository.mkdirs(jaegerstaetterConfig.getExistEditionCommentDocDirName());
        Optional<TeiDocument> ingestStartXml = this.ingestRepository.retrieveByFilePath(jaegerstaetterConfig.getExistEditionCommentDocStartXmlName());
        if (ingestStartXml.isEmpty()) {
            byte[] sampleStart = getJarResource(START_XML);
            this.ingestRepository.createNewDocument(jaegerstaetterConfig.getExistEditionCommentDocStartXmlName(), sampleStart, LocalDateTime.now());
        }
        Optional<TeiDocument> ingestRegistryJson = this.ingestRepository.retrieveByFilePath(registryJson);
        if (ingestRegistryJson.isEmpty()) {
            byte[] sampleRegistry = FileUtils.readFileToByteArray(temp);
            this.ingestRepository.createNewDocument(registryJson, sampleRegistry, LocalDateTime.now());
        }

    }

    private byte[] getJarResource(String path) throws Exception {
        return IOUtils.toByteArray(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }
}
