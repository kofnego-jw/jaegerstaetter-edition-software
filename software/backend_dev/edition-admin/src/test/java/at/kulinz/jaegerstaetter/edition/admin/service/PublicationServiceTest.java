package at.kulinz.jaegerstaetter.edition.admin.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.edition.admin.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PublicationServiceTest extends TestBase {

    @Autowired
    private PublicationServiceImpl service;

    @Autowired
    private ExistDBTeiNotVersionedRepository ingestRepository;

    @Autowired
    private ExistDBTeiRepository editionRepository;

    @Autowired
    private ExistDBTeiRepository previewRepository;

    @Autowired
    private JaegerstaetterConfig config;


    @BeforeEach
    public void setUp() throws Exception {
        super.testExistDBConnection();
        ingestRepository.deleteAll();
        editionRepository.deleteAll();
        previewRepository.deleteAll();

    }

    @Test
    public void test() throws Exception {
        System.out.println(previewRepository.getSardineUrl());
/*
        // Add to Ingest
        ingestRepository.createNewDocument("test1.xml", "<v1/>".getBytes(StandardCharsets.UTF_8), LocalDateTime.now());
        TeiDocument test2Doc = ingestRepository.createNewDocument("fol1/test2.xml", "<test/>".getBytes(StandardCharsets.UTF_8), LocalDateTime.now());

        // Clone to Preview
        service.cloneEditionAndIngestToPreview(config.getAdminPassword());
        System.out.println(ingestRepository.retrieveAll());
        System.out.println(previewRepository.retrieveAll());

        // Clone to Edition
        service.ingestToEdition(config.getAdminPassword());
        System.out.println(editionRepository.retrieveAll());

        // Add to Ingest
        ingestRepository.createNewDocument("fol1/test3.xml", "<test/>".getBytes(StandardCharsets.UTF_8), LocalDateTime.now());

        // Clone to Preview
        service.cloneEditionAndIngestToPreview(config.getAdminPassword());
        System.out.println(ingestRepository.retrieveAll());
        System.out.println(previewRepository.retrieveAll());

        // Clone to Edition
        service.ingestToEdition(config.getAdminPassword());
        System.out.println(editionRepository.retrieveAll());

        // Update in Ingest
        ingestRepository.addNewVersion(test2Doc, test2Doc.nextVersion("<test changed='true'/>".getBytes(StandardCharsets.UTF_8), LocalDateTime.now()));


        // Clone to Preview
        service.cloneEditionAndIngestToPreview(config.getAdminPassword());
        System.out.println(ingestRepository.retrieveAll());
        System.out.println(previewRepository.retrieveAll());
        System.out.println(new String(previewRepository.retrieveByFilePath(test2Doc.getFilePath()).get().getContent(), StandardCharsets.UTF_8));

        // Clone to Edition
        service.ingestToEdition(config.getAdminPassword());
        System.out.println(editionRepository.retrieveAll()); */
    }
}
