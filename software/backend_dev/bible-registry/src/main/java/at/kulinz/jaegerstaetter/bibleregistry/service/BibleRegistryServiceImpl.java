package at.kulinz.jaegerstaetter.bibleregistry.service;

import at.kulinz.jaegerstaetter.biblehelper.BiblePassage;
import at.kulinz.jaegerstaetter.biblehelper.service.BiblePassageExtractor;
import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexEntryBiblePassages;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryBibleBook;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.RegistryEntryBiblePosition;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceFWDTO;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.BibleRegistryService;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.ResourceService;
import at.kulinz.jaegerstaetter.index.service.IndexResourceService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import net.sf.saxon.s9api.XPathExecutable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BibleRegistryServiceImpl implements BibleRegistryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BibleRegistryServiceImpl.class);

    private final ResourceService resourceService;

    private final ExistDBTeiRepository repository;

    private final XPathService xPathService;

    private final String bibleRegistryFilePath;

    private final XPathExecutable bibleNoteXPath;

    private final IndexResourceService indexResourceService;

    public BibleRegistryServiceImpl(ResourceService resourceService, ExistDBTeiRepository repository, XPathService xPathService, IndexResourceService indexResourceService,
                                    JaegerstaetterConfig config) throws Exception {
        this.resourceService = resourceService;
        this.repository = repository;
        this.xPathService = xPathService;
        this.indexResourceService = indexResourceService;
        this.bibleNoteXPath = createBibleNoteXPath();
        String dir = config.getExistEditionRegistryDirName();
        if (!dir.endsWith("/")) {
            dir = dir + "/";
        }
        this.bibleRegistryFilePath = dir + "bible.json";
    }

    private XPathExecutable createBibleNoteXPath() throws Exception {
        return this.xPathService.createXPathExecutable("//tei:note[contains(@subtype, 'bible')]/normalize-space()", null);
    }

    public void indexDocuments() throws FrontendModelException {
        try {
            List<ResourceFWDTO> files = resourceService.listAllResources();
            Map<String, List<BiblePassage>> resultMap = new HashMap<>();
            for (ResourceFWDTO file : files) {
                try {
                    byte[] content = resourceService.getContent(file.id);
                    List<String> list = xPathService.evaluate(new ByteArrayInputStream(content), this.bibleNoteXPath);
                    List<BiblePassage> passages = list.stream().flatMap(s -> BiblePassageExtractor.getPassages(s).stream())
                            .collect(Collectors.toList());
                    resultMap.put(file.id, passages);
                } catch (Exception e) {
                    LOGGER.error("Cannot process document.", e);
                }
            }
            List<JsonBibleIndexEntry> entries = fromMap(resultMap);
            save(new JsonBibleRegistry(entries));
        } catch (Exception e) {
            LOGGER.error("Cannot save bible registry.", e);
            throw new FrontendModelException("Cannot save bible registry.", e);
        }
    }

    private void save(JsonBibleRegistry registry) throws Exception {
        byte[] content = JsonBibleRegistry.toDocument(registry);
        Optional<TeiDocument> exists = this.repository.retrieveByFilePath(this.bibleRegistryFilePath);
        if (exists.isEmpty()) {
            repository.createNewDocument(this.bibleRegistryFilePath, content, LocalDateTime.now());
        } else {
            TeiDocument prev = exists.get();
            TeiDocument next = prev.nextVersion(content, LocalDateTime.now());
            repository.addNewVersion(prev, next);
        }
    }

    private JsonBibleRegistry load() throws Exception {
        Optional<TeiDocument> exists = repository.retrieveByFilePath(this.bibleRegistryFilePath);
        if (exists.isPresent()) {
            return JsonBibleRegistry.fromDocument(exists.get().getContent());
        }
        return new JsonBibleRegistry(Collections.emptyList());
    }

    private List<JsonBibleIndexEntry> fromMap(Map<String, List<BiblePassage>> map) {
        Map<BiblePassage, List<String>> reversed = new HashMap<>();
        for (Map.Entry<String, List<BiblePassage>> entry : map.entrySet()) {
            List<BiblePassage> values = entry.getValue();
            for (BiblePassage pass : values) {
                List<String> exists = reversed.get(pass);
                if (exists == null) {
                    exists = new ArrayList<>();
                }
                exists.add(entry.getKey());
                reversed.put(pass, exists);
            }
        }
        return reversed.entrySet().stream().map(entry -> {
            BiblePassage pass = entry.getKey();
            return new JsonBibleIndexEntry(pass.bookAbbr, pass.position, pass.orderString, entry.getValue());
        }).sorted(JsonBibleIndexEntry.COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public List<RegistryEntryBibleBook> getBibleBooks() throws FrontendModelException {
        try {
            JsonBibleRegistry repository = load();
            return repository.entries.stream().map(entry -> entry.book)
                    .collect(Collectors.toSet())
                    .stream()
                    .map(book -> {
                        int count = (int) repository.entries.stream().filter(entry -> entry.book.equals(book)).count();
                        return BiblePassageExtractor.fromBookAbbr(book, count);
                    })
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Cannot read repository");
            throw new FrontendModelException("Cannot read repository.", e);
        }
    }

    @Override
    public IndexEntryBiblePassages getBibleResources(String book) throws FrontendModelException {
        if (StringUtils.isBlank(book)) {
            return new IndexEntryBiblePassages(null, Collections.emptyList());
        }
        try {
            JsonBibleRegistry repository = load();
            int count = (int) repository.entries.stream().filter(entry -> entry.book.equals(book)).count();
            RegistryEntryBibleBook bibleBook = BiblePassageExtractor.fromBookAbbr(book, count);
            List<RegistryEntryBiblePosition> positions = repository.entries.stream().filter(entry -> book.equals(entry.book))
                    .map(this::fromIndex)
                    .sorted()
                    .collect(Collectors.toList());
            return new IndexEntryBiblePassages(bibleBook, positions);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot get resources.", e);
        }
    }

    private RegistryEntryBiblePosition fromIndex(JsonBibleIndexEntry index) {
        List<ResourceFWDTO> resources = index.documentIds.stream().map(id ->
                indexResourceService.fetchById(id).orElse(null)
        ).filter(Objects::nonNull).collect(Collectors.toList());
        return new RegistryEntryBiblePosition(index.book, index.position, index.orderString, resources);
    }
}
