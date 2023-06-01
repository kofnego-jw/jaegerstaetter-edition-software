package at.kulinz.jaegerstaetter.biblehelper;

import at.kulinz.jaegerstaetter.biblehelper.service.BiblePassageExtractor;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import at.kulinz.jaegerstaetter.xmlservice.service.impl.XPathServiceImpl;
import net.sf.saxon.s9api.XPathExecutable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiblePassageExtractorTest {

    public static final File DATA_DIR = new File("../../../daten/XML_Briefe, Vorlage&Index");

    private XPathService service;

    private XPathExecutable selector;

    @BeforeEach
    public void setupSelector() throws Exception {
        this.service = new XPathServiceImpl();
        this.selector = service.createXPathExecutable("//tei:note[contains(@subtype,'bible')]/normalize-space()", null);
    }


    @Test
    public void testNumberedBookAbbr() {
        List<BiblePassage> passages = BiblePassageExtractor.getPassages("Joh 1,5-2,2");
        Assertions.assertEquals(1, passages.size());
        Assertions.assertEquals("Joh", passages.get(0).bookAbbr);
        Assertions.assertEquals("1,5-2,2", passages.get(0).position);

        passages = BiblePassageExtractor.getPassages("1Joh 1-2.4");
        Assertions.assertEquals(1, passages.size());
        Assertions.assertEquals("1 Joh", passages.get(0).bookAbbr);
        Assertions.assertEquals("1-2.4", passages.get(0).position);

        passages = BiblePassageExtractor.getPassages("Ketter: Hebr                             10,26. Bei " +
                "Ketter lautet der                                     Beginn stattdessen: Gemeint ist die " +
                "Sünde des Abfalls vom                                     Christentum bzw. die Sünde wider den " +
                "Heiligen Geist: der                                     erkannten Wahrheit widerstreben.\n");
        Assertions.assertEquals(1, passages.size());
        Assertions.assertEquals("Hebr", passages.get(0).bookAbbr);


    }

    @Test
    public void listAllPlacesInData() {
        File[] files = DATA_DIR.listFiles(file -> file.getName().endsWith(".xml"));
        List<File> fileList = Stream.of(Objects.requireNonNull(files)).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());
        for (File xml : fileList) {
            List<String> notes;
            try {
                notes = service.evaluate(new FileInputStream(xml), selector);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            List<BiblePassage> passages =
                    notes.stream().flatMap(note -> BiblePassageExtractor.getPassages(note).stream()).collect(Collectors.toList());
            System.out.println("File: " + xml.getName());
            System.out.println("      " + passages.stream().map(pass -> pass.bookAbbr + " " + pass.position + " " + pass.orderString)
                    .collect(Collectors.joining("\n      ")));
        }
    }
}
