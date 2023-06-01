package at.kulinz.jaegerstaetter.xsltstylesheets.uniform;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import at.kulinz.jaegerstaetter.xsltstylesheets.TestBase;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniformTest extends TestBase {

    public static final File DATA_DIR = new File("../../../daten/XML_Briefe, Vorlage&Index");

    @Autowired
    private XPathService xPathService;

    private ExistDBTeiNotVersionedRepository commonRepository = new ExistDBTeiNotVersionedRepository("http", "localhost", 8080, "/exist/webdav/db/ffji/common/", "admin", "");
    private ExistDBTeiRepository previewRepository = new ExistDBTeiRepository("http", "localhost", 8080,
            "/exist/webdav/db/ffji/preview/data/", "admin", "");

    @Test
    public void findUnicodeText() throws Exception {
        File[] xmls = DATA_DIR.listFiles(x -> x.getName().endsWith(".xml"));
        for (File f : xmls) {

        }
    }

    @Test
    public void test() throws Exception {
        File[] xmls = DATA_DIR.listFiles(x -> x.getName().endsWith(".xml"));
        Assertions.assertNotNull(xmls);
        // Diplomatic Transcription: All Elements
        // String xpath = "distinct-values(//tei:div[@type='diplomatische_Umschrift']//tei:*/name())";
        //String xpath = "distinct-values(//tei:div[@type='diplomatische_Umschrift']//tei:*/@*/name())";
        //String xpath = "distinct-values(//tei:ref/@target)";
        //String xpath = "distinct-values(//tei:text//tei:*/@facs)";
        // Normalized Transcription: All Elements
        String xpath = "distinct-values(//tei:div[@type='Lesefassung']//tei:ref/@target[not(starts-with(.,'http'))])";
        //String xpath = "distinct-values(//tei:div[@type='diplomatische_Umschrift']//tei:*/@*/name())";
        // String xpath = "distinct-values(//tei:ref/@target)";
        //String xpath = "distinct-values(//tei:text//tei:*/@facs)";
        Map<String, HitNote> notes = new HashMap<>();
        for (File xml : xmls) {
            List<String> ns = xPathService.evaluate(new FileInputStream(xml), xpath, null);
            ns.forEach(n -> {
                List<String> targets = new ArrayList<>();
                if (n.contains(";")) {
                    targets.addAll(Stream.of(n.split("\\s*;\\s*")).collect(Collectors.toList()));
                } else {
                    targets.add(n);
                }
                for (String target : targets) {
                    HitNote hitNote = notes.get(target);
                    if (hitNote == null) {
                        hitNote = new HitNote(target);
                        notes.put(target, hitNote);
                    }
                    hitNote.addCount();
                    hitNote.addAppearance(xml);
                }
                /* HitNote hitNote = notes.get(n);
                if (hitNote == null) {
                    hitNote = new HitNote(n);
                    notes.put(n, hitNote);
                }
                hitNote.addCount();
                hitNote.addAppearance(xml); */
            });
        }
        notes.values().stream().sorted().forEach(x -> {
            if (!destAvailable(x.value)) {
                System.out.println(x.value + "\t" + x.count + "\t" + x.appearances);
                System.out.println();
            }
        });
    }

    private boolean destAvailable(String s) {
        if (s.endsWith(".jpg")) {
            return isAvailable(s);
        } else if (s.endsWith(".xml")) {
            return isXmlAvailable(s);
        }
        return false;
    }

    private boolean hasDestination(String s) {
        String[] ss = s.split("#");
        String filename = ss[0];
        String xmlId = ss[1];
        File data = new File(DATA_DIR, filename);
        try {
            String c = FileUtils.readFileToString(data, StandardCharsets.UTF_8);
            return c.contains("xml:id=\"" + xmlId + "\"");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isXmlAvailable(String s) {
        return previewRepository.retrieveByFilePath(s).isPresent();
    }

    private boolean isAvailable(String s) {
        if (!s.endsWith(".jpg")) {
            s = s + ".jpg";
        }
        Optional<TeiDocument> opt = commonRepository.retrieveByUrl("/facsimiles/" + s);
        if (opt.isPresent()) {
            return true;
        }
        opt = commonRepository.retrieveByFilePath("/photodocument/images/" + s);
        return opt.isPresent();
    }

    static class HitNote implements Comparable<HitNote> {
        private final String value;
        private int count;
        private List<File> appearances = new ArrayList<>();

        @Override
        public String toString() {
            return "HitNote{" +
                    "value='" + value + '\'' +
                    ", count=" + count +
                    ", appearances=" + appearances +
                    '}';
        }

        @Override
        public int compareTo(@NotNull HitNote o) {
            return this.value.compareTo(o.value);
        }

        public HitNote(String value) {
            this.value = value;
            this.count = 0;
        }

        public void addCount() {
            this.count += 1;
        }

        public void addAppearance(File f) {
            if (!this.appearances.contains(f)) {
                this.appearances.add(f);
            }
        }

        public String getValue() {
            return value;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<File> getAppearances() {
            return appearances;
        }

        public void setAppearances(List<File> appearances) {
            this.appearances = appearances;
        }
    }

}
