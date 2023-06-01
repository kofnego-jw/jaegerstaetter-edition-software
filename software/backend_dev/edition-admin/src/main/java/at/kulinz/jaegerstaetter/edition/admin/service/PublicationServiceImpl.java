package at.kulinz.jaegerstaetter.edition.admin.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.formaldesc.service.FormalDescService;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.PublicationService;
import at.kulinz.jaegerstaetter.photodoc.service.PhotoDocExcelImportService;
import at.kulinz.jaegerstaetter.tei.edition.connector.repoobj.PhotoDocumentJsonRepo;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PublicationServiceImpl implements PublicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationServiceImpl.class);

    private final JaegerstaetterConfig config;
    private final ExistDBTeiRepository editionRepository;
    private final ExistDBTeiRepository previewRepository;
    private final ExistDBTeiNotVersionedRepository ingestRepository;
    private final ExistDBTeiNotVersionedRepository commonRepository;
    private final IndexControlService indexControlService;
    private final PhotoDocExcelImportService photoDocExcelImportService;
    private final FormalDescService formalDescService;
    private final String dataFilePath;

    @Autowired(required = false)
    private CacheManager cacheManager;

    public PublicationServiceImpl(JaegerstaetterConfig config,
                                  ExistDBTeiRepository editionRepository,
                                  ExistDBTeiRepository previewRepository,
                                  ExistDBTeiNotVersionedRepository ingestRepository,
                                  ExistDBTeiNotVersionedRepository commonRepository,
                                  IndexControlService indexControlService,
                                  PhotoDocExcelImportService photoDocExcelImportService,
                                  FormalDescService formalDescService) {
        this.config = config;
        this.editionRepository = editionRepository;
        this.previewRepository = previewRepository;
        this.ingestRepository = ingestRepository;
        this.commonRepository = commonRepository;
        this.indexControlService = indexControlService;
        this.photoDocExcelImportService = photoDocExcelImportService;
        this.formalDescService = formalDescService;
        String existEditionDataDirName = config.getExistEditionDataDirName();
        this.dataFilePath = existEditionDataDirName.endsWith("/") ? existEditionDataDirName : existEditionDataDirName + "/";
    }

    private void addImagesToCache() throws Exception {

    }

    private void evictCache() throws Exception {
        if (this.cacheManager != null) {
            for (String name : this.cacheManager.getCacheNames()) {
                Objects.requireNonNull(this.cacheManager.getCache(name)).clear();
            }
        }
    }

    private void checkPassword(String password) throws Exception {
        if (StringUtils.isBlank(config.getAdminPassword())) {
            return;
        }
        if (!config.getAdminPassword().equals(password)) {
            throw new Exception("Password invalid.");
        }
    }

    @Override
    public void cloneEditionToPreview(String password) throws Exception {
        checkPassword(password);
        previewRepository.deleteAll();
        Sardine sardine = SardineFactory.begin(config.getExistUsername(), config.getExistPassword());
        if (sardine.exists(editionRepository.getSardineUrl())) {
            sardine.copy(editionRepository.getSardineUrl(), previewRepository.getSardineUrl());
        }
        addImagesToCache();
        this.indexControlService.reindexAllPreviewDocuments();
        evictCache();
    }

    @Override
    public void cloneEditionAndIngestToPreview(String password) throws Exception {
        this.cloneEditionToPreview(password);
        addNewVersionIfDifferent(this.previewRepository, this.ingestRepository);
        addImagesToCache();
        createPhotoDocumentJson();
        this.indexControlService.reindexAllPreviewDocuments();
    }

    @Override
    public void ingestToEdition(String password) throws Exception {
        checkPassword(password);
        addImagesToCache();
        addNewVersionIfDifferent(this.editionRepository, this.ingestRepository);
        createPhotoDocumentJson();
        this.indexControlService.reindexAllEditionDocuments();
    }

    private void createPhotoDocumentJson() throws Exception {
        List<TeiDocumentFW> fws = commonRepository.retrieveAllUnder(config.getExistCommonPhotodocumentExcelDirName(), false);
        List<TeiDocument> excels = fws.stream()
                .filter(fw -> fw.getFilePath().endsWith(".xlsx"))
                .map(fw -> commonRepository.retrieveByFilePath(fw.getFilePath()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        PhotoDocumentJsonRepo repo = photoDocExcelImportService.fromExcel(excels);
        byte[] repoBytes = repo.toJson();
        commonRepository.createNewDocument(config.getExistCommonPhotodocumentJsonFile(), repoBytes, LocalDateTime.now());
    }

    private boolean noDiff(byte[] one, byte[] two) {
        return Arrays.equals(one, two);
    }

    private void addNewVersionIfDifferent(ExistDBTeiRepository destRepo, ExistDBTeiRepository startRepo) throws Exception {
        List<TeiDocumentFW> docFWs = startRepo.retrieveAll();
        for (TeiDocumentFW doc : docFWs) {
            String path = doc.getFilePath();
            Optional<TeiDocument> startOpt = startRepo.retrieveByFilePath(path);
            if (startOpt.isEmpty()) {
                continue;
            }
            Optional<TeiDocument> destOpt = destRepo.retrieveByFilePath(path);
            TeiDocument startDoc = startOpt.get();
            if (destOpt.isPresent()) {
                TeiDocument oldVersion = destOpt.get();
                if (noDiff(startDoc.getContent(), oldVersion.getContent())) {
                    continue;
                }
                LOGGER.info("Add new version for: {}", startDoc.getFilePath());
                TeiDocument newVersion = oldVersion.nextVersion(startDoc.getContent(), startDoc.getCreationTimestamp());
                destRepo.addNewVersion(oldVersion, newVersion);
            } else {
                LOGGER.info("Create new document for: {}", startDoc.getFilePath());
                destRepo.createNewDocument(path, startDoc.getContent(), startDoc.getCreationTimestamp());
            }
        }
    }

    @Override
    public StatReport getStatistics() throws Exception {
        List<String> ids = previewRepository.retrieveAllUnder(dataFilePath, false)
                .stream().map(TeiDocumentFW::getFilePath).sorted().collect(Collectors.toList());
        return formalDescService.createEditionStats(ids);
    }
}
