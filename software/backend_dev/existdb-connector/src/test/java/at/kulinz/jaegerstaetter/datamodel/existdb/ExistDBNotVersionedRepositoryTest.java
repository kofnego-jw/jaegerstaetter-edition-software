package at.kulinz.jaegerstaetter.datamodel.existdb;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ExistDBNotVersionedRepositoryTest {

    private ExistDBTeiNotVersionedRepository repository;

    @BeforeAll
    public static void createTestRepo() {
        Sardine sardine = SardineFactory.begin(TestConstants.USERNAME, TestConstants.PASSWORD);
        try {
            sardine.list(TestConstants.TESTURL);
            sardine.delete(TestConstants.TEST_BASE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Exception e = null;
        try {
            sardine.createDirectory(TestConstants.TEST_BASE_URL);
            sardine.put(TestConstants.TEST_BASE_URL + "test.txt", "test".getBytes(StandardCharsets.UTF_8));
            sardine.delete(TestConstants.TEST_BASE_URL + "test.txt");
        } catch (Exception ex) {
            e = ex;
        }
        Assumptions.assumeTrue(e == null);
    }

    @AfterAll
    public static void deleteTestRepo() {
        Sardine sardine = SardineFactory.begin(TestConstants.USERNAME, TestConstants.PASSWORD);
        Exception test = null;
        try {
            sardine.delete(TestConstants.TEST_BASE_URL);
        } catch (Exception e) {
            test = e;
        }
        Assumptions.assumeTrue(test == null);
    }

    @BeforeEach
    public void setUpRepository() {
        this.repository = new ExistDBTeiNotVersionedRepository(TestConstants.TEST_PROTOCOL,
                TestConstants.TEST_HOST, TestConstants.TEST_PORT, TestConstants.TEST_DAV_BASE,
                TestConstants.USERNAME, TestConstants.PASSWORD);
    }

    @Test
    public void test02_create() throws Exception {
        LocalDateTime creation = LocalDateTime.of(2022, 4, 22, 12, 0);
        TeiDocument newDocument = repository.createNewDocument("test.xml", "<v1/>".getBytes(StandardCharsets.UTF_8), creation);
        Assertions.assertNotNull(newDocument.getUrl());
        TeiDocument retrieved = repository.retrieveByUrl(newDocument.getUrl()).get();
        Assertions.assertEquals(newDocument, retrieved);
        Assertions.assertArrayEquals("<v1/>".getBytes(StandardCharsets.UTF_8), retrieved.getContent());
    }

    @Test
    public void test03_createNewVersion() throws Exception {
        TeiDocument retrieved = repository.retrieveByFilePath("test.xml").get();
        Assertions.assertFalse(retrieved.isDeleted());
        LocalDateTime update = LocalDateTime.of(2022, 4, 22, 14, 15);
        TeiDocument nextVersion = retrieved.nextVersion("<v2/>".getBytes(StandardCharsets.UTF_8), update);
        TeiDocument nextInDb = repository.addNewVersion(retrieved, nextVersion);
        Assertions.assertEquals("test.xml", nextInDb.getUrl());
        List<TeiDocument> allVersions = repository.retrieveAllVersions("test.xml");
        Assertions.assertEquals(1, allVersions.size());

        Optional<TeiDocument> ver2 = repository.retrieveByFilePathAndVersion("test.xml", 2);
        Assertions.assertTrue(ver2.isPresent());
        Assertions.assertEquals(nextInDb, ver2.get());
        Assertions.assertFalse(repository.retrieveByFilePathAndVersion("test.xml", 3).isEmpty());
        List<TeiDocumentFW> all = repository.retrieveAll();
        System.out.println(all);
        Assertions.assertEquals(1, all.size());
        repository.delete("test.xml");
        allVersions = repository.retrieveAllVersions("test.xml");
        Assertions.assertTrue(allVersions.isEmpty());
    }

    @Test
    public void test05_randomAddFilesAndDirectories() throws Exception {
        LocalDateTime create = LocalDateTime.of(2022, 4, 22, 12, 0);
        LocalDateTime update = LocalDateTime.of(2022, 4, 22, 20, 0);
        Random random = new Random();
        int counter = 1;
        int dirs = random.nextInt(10) + 3;
        for (int i = 1; i <= dirs; i++) {
            String parentpath = String.format("%03d", i) + "/";
            int subDirs = random.nextInt(2);
            for (int subi = 0; subi <= subDirs; subi++) {
                String dirPath = parentpath + String.format("%03d", subi) + "/";
                int dirFiles = random.nextInt(2) + 1;
                for (int subFile = 0; subFile < dirFiles; subFile++) {
                    addFile(dirPath, counter++, create);
                }
            }
            int subFiles = random.nextInt(2) + 1;
            for (int subFile = 0; subFile < subFiles; subFile++) {
                addFile(parentpath, counter++, create);
            }
        }
        int files = random.nextInt(2) + 1;
        for (int i = 0; i < files; i++) {
            addFile("", counter++, create);
        }

        List<TeiDocumentFW> all = repository.retrieveAll();
        all.forEach(d -> System.out.println(d.getUrl()));
        Assertions.assertEquals(counter - 1, all.size());
        List<TeiDocumentFW> changed = new ArrayList<>();
        while (random.nextInt(10) % 10 != 0) {
            TeiDocumentFW toChange;
            do {
                toChange = all.get(random.nextInt(all.size()));
            } while (changed.contains(toChange));
            addNewVersion(toChange, update);
            changed.add(toChange);
        }
        List<TeiDocumentFW> all2 = repository.retrieveAll();
        List<String> changedPaths = changed.stream()
                .map(TeiDocumentFW::getFilePath)
                .collect(Collectors.toList());
        all2.stream().filter(d -> !d.getFilePath().equals("test.txt")).forEach(doc -> {
            String path = doc.getFilePath();
            if (path.equals("test.xml")) {
                return;
            }
            if (changedPaths.contains(path)) {
                Assertions.assertEquals(1, doc.getVersion());
            } else {
                Assertions.assertEquals(1, doc.getVersion());
            }
        });
    }

    private void addNewVersion(TeiDocumentFW old, LocalDateTime update) throws Exception {
        TeiDocument doc = TeiDocument.fromTeiDocumentFW(old, "".getBytes(StandardCharsets.UTF_8));
        TeiDocument next = doc.nextVersion("def".getBytes(StandardCharsets.UTF_8), update);
        repository.addNewVersion(doc, next);
    }

    private void addFile(String parentPath, int counter, LocalDateTime create) throws Exception {
        String filePath = parentPath + String.format("%03d.txt", counter);
        repository.createNewDocument(filePath, "abc".getBytes(StandardCharsets.UTF_8), create);
    }

}
