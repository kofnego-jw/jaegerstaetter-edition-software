package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroup;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentGroupType;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PhotoDocumentItem;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.PhotoDocumentService;
import at.kulinz.jaegerstaetter.tei.edition.connector.repoobj.PhotoDocumentJsonRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PhotoDocumentServiceImpl implements PhotoDocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoDocumentServiceImpl.class);

    private final String photoDocumentJsonPath;

    private final String photoDocumentImageDir;

    private final ExistDBTeiNotVersionedRepository commonRepository;

    private final FacsimileService facsimileService;

    @Autowired
    public PhotoDocumentServiceImpl(ExistDBTeiNotVersionedRepository commonRepository,
                                    JaegerstaetterConfig config,
                                    FacsimileService facsimileService) {
        this.commonRepository = commonRepository;
        this.photoDocumentImageDir = config.getExistCommonPhotodocumentImageDirName().endsWith("/") ?
                config.getExistCommonPhotodocumentImageDirName() : config.getExistCommonPhotodocumentImageDirName() + "/";
        this.photoDocumentJsonPath = config.getExistCommonPhotodocumentJsonFile();
        this.facsimileService = facsimileService;
    }

    private byte[] getJsonDocument() throws Exception {
        return this.commonRepository.retrieveByFilePath(photoDocumentJsonPath).map(TeiDocument::getContent)
                .orElseThrow(() -> new Exception("Cannot get json document."));
    }

    @Override
    @Cacheable("common.photodoc.list")
    public List<PhotoDocumentGroup> listAllPhotoDocuments() {
        try {
            return PhotoDocumentJsonRepo.readJson(getJsonDocument()).photoDocumentGroups;
        } catch (Exception e) {
            LOGGER.error("Cannot read json value.", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable("common.photodoc.groups.photo")
    public List<PhotoDocumentGroup> listPhotoGroups() {
        return listAllPhotoDocuments().stream().filter(g -> g.type == PhotoDocumentGroupType.PHOTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable("common.photodoc.groups.doc")
    public List<PhotoDocumentGroup> listDocumentGroups() {
        return listAllPhotoDocuments().stream().filter(g -> g.type == PhotoDocumentGroupType.DOCUMENT).collect(Collectors.toList());
    }

    @Override
    @Cacheable("common.photodoc.groupbykey")
    public List<PhotoDocumentItem> listByGroup(String groupKey) throws FileNotFoundException {
        List<PhotoDocumentGroup> list = listAllPhotoDocuments();
        Optional<PhotoDocumentGroup> opt = list.stream().filter(group -> groupKey.equals(group.groupKey)).findAny();
        if (opt.isEmpty()) {
            throw new FileNotFoundException("Cannot find photo document item.");
        }
        return opt.get().items;
    }

    @Override
    public byte[] getPhotoDocument(String groupKey, String id) throws FileNotFoundException {
        if (!id.endsWith(".jpg")) {
            id = id + ".jpg";
        }
        String path = this.photoDocumentImageDir + id;

        Optional<TeiDocument> opt = commonRepository.retrieveByFilePath(path);
        if (opt.isEmpty()) {
            id = id.replace(".jpg", ".JPG");
            path = this.photoDocumentImageDir + id;
            opt = commonRepository.retrieveByFilePath(path);
            if (opt.isEmpty()) {
                // Search in Facsimile
                id = id.replace(".JPG", ".jpg");
                return facsimileService.getFacsimile(id);
            }
        }
        return opt.get().getContent();
    }
}
