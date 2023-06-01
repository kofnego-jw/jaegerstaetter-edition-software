package at.kulinz.jaegerstaetter.biography.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.Biography;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.BiographyFW;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexEntryPerson;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryPerson;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.BiographyService;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.PhotoDocumentService;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.RegistryService;
import at.kulinz.jaegerstaetter.pdfgenerator.service.PdfGenerator;
import at.kulinz.jaegerstaetter.stylesheets.model.BiographyResult;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BiographyServiceImpl implements BiographyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiographyService.class);

    private static final String P0001 = "p_0001";
    private static final String P0002 = "p_0002";
    private static final Comparator<TeiDocumentFW> BIOGRAPHY_COMPARATOR = (fw1, fw2) -> {
        String fn1 = fw1.getName().toLowerCase();
        String fn2 = fw2.getName().toLowerCase();
        if (fn1.equals(fn2)) {
            return 0;
        }
        if (fn1.contains("_fj.xml")) {
            return -1;
        }
        if (fn2.contains("_fj.xml")) {
            return 1;
        }
        if (fn1.contains("_faj.xml")) {
            return -1;
        }
        if (fn2.contains("_faj.xml")) {
            return 1;
        }
        return fn1.compareTo(fn2);
    };

    private final ExistDBTeiRepository editionRepository;
    private final PhotoDocumentService photoDocumentService;
    private final RegistryService registryService;
    private final TransformationService transformationService;
    private final XPathService xPathService;
    private final PdfGenerator pdfGenerator;
    private final String biographyDir;

    public BiographyServiceImpl(ExistDBTeiRepository editionRepository,
                                RegistryService registryService,
                                PhotoDocumentService photoDocumentService,
                                TransformationService transformationService,
                                XPathService xPathService,
                                PdfGenerator pdfGenerator,
                                JaegerstaetterConfig config) {
        this.editionRepository = editionRepository;
        this.registryService = registryService;
        this.transformationService = transformationService;
        this.photoDocumentService = photoDocumentService;
        this.xPathService = xPathService;
        this.pdfGenerator = pdfGenerator;
        this.biographyDir = config.getExistEditionBiographyDirName().endsWith("/") ? config.getExistEditionBiographyDirName() :
                config.getExistEditionBiographyDirName() + "/";
    }

    private String convertPersonKeyToPath(String personKey) {
        return URLEncoder.encode(personKey, StandardCharsets.UTF_8);
    }

    private String convertPathToPersonKey(String path) {
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }

    private BiographyFW toBiographyFW(TeiDocumentFW fw) {
        try {
            BiographyResult biographyResult = transformationService.getBiographyResult(editionRepository.retrieveByFilePath(fw.getFilePath()).get());
            List<RegistryEntryPerson> persons = registryService.getPersonRegistry()
                    .stream().filter(registry -> biographyResult.persNameKeys.contains(registry.key))
                    .collect(Collectors.toList());
            return new BiographyFW(persons, biographyResult.filename, biographyResult.title);
        } catch (Exception e) {
            LOGGER.error("Cannot create BiographyFW.", e);
            return null;
        }
    }

    @Override
    public List<BiographyFW> listBiographies() {
        return editionRepository.retrieveAllUnder(biographyDir, false)
                .stream().sorted(BIOGRAPHY_COMPARATOR)
                .map(this::toBiographyFW)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private byte[] getBiographyHTML(TeiDocument document) throws FrontendModelException {
        try {
            return transformationService.biographyToHtml(document);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot get HTML representation.");
        }
    }

    private String getBiographyToc(TeiDocument document) throws FrontendModelException {
        try {
            byte[] bytes = transformationService.biographyToToc(document);
            String content = new String(bytes, StandardCharsets.UTF_8);
            if (StringUtils.isBlank(content) || StringUtils.isBlank(content.replaceAll("<.*?>", ""))) {
                return "";
            }
            return content;
        } catch (Exception e) {
            throw new FrontendModelException("Cannot get HTML representation.");
        }
    }

    @Override
    public Biography getBiography(String filename) throws FileNotFoundException, FrontendModelException {
        TeiDocument doc = getTeiDocument(filename);
        BiographyFW fw = toBiographyFW(doc);
        List<IndexEntryPerson> index =
                fw.persons.stream().map(entry -> {
                    try {
                        return registryService.getPersonIndex(entry.key);
                    } catch (Exception e) {
                        return null;
                    }
                }).collect(Collectors.toList());
        String content = new String(getBiographyHTML(doc), StandardCharsets.UTF_8);
        String toc = getBiographyToc(doc);
        String author = getBiographyAuthor(doc);
        return new Biography(index, fw.filename, fw.title, content, toc, author);
    }

    public String getBiographyAuthor(TeiDocument doc) {
        try {
            List<String> authors = xPathService.evaluate(new ByteArrayInputStream(doc.getContent()), "//tei:titleStmt/tei:author", null);
            return String.join("; ", authors);
        } catch (Exception e) {
            return "Unbekannt";
        }
    }

    @Override
    public byte[] getImage(String id) throws FileNotFoundException, FrontendModelException {
        return this.photoDocumentService.getPhotoDocument("", id);
    }

    @Override
    public byte[] getPdf(String filename) throws FileNotFoundException, FrontendModelException {
        TeiDocument teiDoc = getTeiDocument(filename);
        try {
            return this.pdfGenerator.generateBiographyPdf(teiDoc);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot create PDF.", e);
        }
    }

    private TeiDocument getTeiDocument(String filename) throws FileNotFoundException {
        if (StringUtils.isBlank(filename)) {
            throw new FileNotFoundException("Cannot find biography.");
        }
        String myFilename = filename.replaceAll("\\.\\./", "__/");
        Optional<TeiDocument> doc = editionRepository.retrieveByFilePath(biographyDir + "/" + myFilename);
        if (doc.isEmpty()) {
            throw new FileNotFoundException("Cannot find biography.");
        }
        return doc.get();
    }
}
