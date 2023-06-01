package at.kulinz.jaegerstaetter.edition.admin.service;

import at.kulinz.jaegerstaetter.bibleregistry.service.EditionBibleRegistryService;
import at.kulinz.jaegerstaetter.bibleregistry.service.PreviewBibleRegistryService;
import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.correspplaces.service.CorrespPlacesService;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexField;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexFieldname;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.ResourceService;
import at.kulinz.jaegerstaetter.index.service.IndexService;
import at.kulinz.jaegerstaetter.index.service.impl.EditionIndexServiceImpl;
import at.kulinz.jaegerstaetter.index.service.impl.PreviewIndexServiceImpl;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.EditionResourceService;
import at.kulinz.jaegerstaetter.tei.edition.connector.impl.PreviewResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IndexControlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexControlService.class);

    private final TransformationService transformationService;

    private final EditionIndexServiceImpl editionIndexService;

    private final ExistDBTeiRepository editionRepository;

    private final EditionResourceService editionResourceService;

    private final EditionBibleRegistryService editionBibleRegistryService;

    private final PreviewIndexServiceImpl previewIndexService;

    private final ExistDBTeiRepository previewRepository;

    private final PreviewResourceService previewResourceService;

    private final PreviewBibleRegistryService previewBibleRegistryService;

    private final CorrespPlacesService previewCorrespPlacesService;

    private final CorrespPlacesService editionCorrespPlacesService;

    private final String dataDirname;

    public IndexControlService(TransformationService transformationService,
                               EditionIndexServiceImpl editionIndexService,
                               ExistDBTeiRepository editionRepository,
                               EditionResourceService editionResourceService,
                               EditionBibleRegistryService editionBibleRegistryService,
                               PreviewIndexServiceImpl previewIndexService,
                               ExistDBTeiRepository previewRepository,
                               PreviewResourceService previewResourceService,
                               PreviewBibleRegistryService previewBibleRegistryService,
                               CorrespPlacesService previewCorrespPlacesService,
                               CorrespPlacesService editionCorrespPlacesService,
                               JaegerstaetterConfig config) {
        this.transformationService = transformationService;
        this.editionIndexService = editionIndexService;
        this.previewIndexService = previewIndexService;
        this.editionRepository = editionRepository;
        this.previewRepository = previewRepository;
        this.editionResourceService = editionResourceService;
        this.previewResourceService = previewResourceService;
        this.editionBibleRegistryService = editionBibleRegistryService;
        this.previewBibleRegistryService = previewBibleRegistryService;
        this.previewCorrespPlacesService = previewCorrespPlacesService;
        this.editionCorrespPlacesService = editionCorrespPlacesService;
        this.dataDirname = config.getExistEditionDataDirName().endsWith("/") ? config.getExistEditionDataDirName() : config.getExistEditionDataDirName() + "/";
    }

    public void clearEditionIndex() throws FrontendModelException {
        try {
            this.editionIndexService.clearIndex();
        } catch (Exception e) {
            throw new FrontendModelException("Cannot clear edition index.", e);
        }
    }

    public void clearPreviewIndex() throws FrontendModelException {
        try {
            this.previewIndexService.clearIndex();
        } catch (Exception e) {
            throw new FrontendModelException("Cannot clear preview index.", e);
        }
    }

    public void reindexAllEditionDocuments() throws FrontendModelException {
        clearEditionIndex();
        editionCorrespPlacesService.reset();
        reindexAllDocuments(editionRepository, editionIndexService, editionResourceService, editionCorrespPlacesService);
        editionBibleRegistryService.indexDocuments();
    }

    public void reindexAllPreviewDocuments() throws FrontendModelException {
        clearPreviewIndex();
        previewCorrespPlacesService.reset();
        reindexAllDocuments(previewRepository, previewIndexService, previewResourceService, previewCorrespPlacesService);
        previewBibleRegistryService.indexDocuments();
    }

    private void reindexAllDocuments(ExistDBTeiRepository repository,
                                     IndexService indexService,
                                     ResourceService resourceService,
                                     CorrespPlacesService correspPlacesService) throws FrontendModelException {
        List<TeiDocumentFW> docs = repository.retrieveAllUnder(dataDirname, true);
        for (TeiDocumentFW fw : docs) {
            TeiDocument doc;
            try {
                doc = repository.retrieveByFilePath(fw.getFilePath()).orElseThrow();
                indexDocument(indexService, resourceService, doc);
                correspPlacesService.analyzeDocument(doc);
            } catch (Exception e) {
                LOGGER.warn("Cannot index document.", e);
            }
        }
        try {
            correspPlacesService.save();
        } catch (Exception e) {
            throw new FrontendModelException("Cannot save corresp places.", e);
        }
    }

    public void indexDocument(IndexService indexService, ResourceService resourceService, TeiDocument document) throws FrontendModelException {
        try {
            IndexDocument indexDocument = transformationService.getIndexDocument(document, document.getFilePath());
            ResourceFWDTO resourceFWDTO = resourceService.fromTeiDocumentFW(document);
            addResourceFWFields(indexDocument, resourceFWDTO);
            indexService.updateLuceneIndex(indexDocument);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot index document.", e);
        }
    }

    private void addResourceFWFields(IndexDocument document, ResourceFWDTO fw) {
        List<String> corpora = fw.corpora.stream().map(Enum::name).collect(Collectors.toList());
        List<String> periods = fw.periods.stream().map(Enum::name).collect(Collectors.toList());
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_ID, List.of(fw.id)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_TITLE, List.of(fw.title)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_DATING, List.of(fw.dating)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_DATING_READABLE, List.of(fw.datingReadable)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_SIGNATURE, List.of(fw.signature)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_ALTSIGNATURE, List.of(fw.altSignature)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_TYPE, List.of(fw.type == null ? "" : fw.type.name())));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_SUMMARY, List.of(fw.summary)));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_TOC, List.of(Boolean.TRUE.toString())));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_CORPUS, corpora));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_PERIOD, periods));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_AUTHOR, fw.authors));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_RECIPIENT, fw.recipients));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_PLACE, fw.places));
        document.fields.add(new IndexField(IndexFieldname.RESOURCE_OBJECTTYPE, fw.objectTypes));
    }
}
