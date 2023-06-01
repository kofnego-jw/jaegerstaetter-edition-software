package at.ac.uibk.fiba.wang;

import at.kulinz.jaegerstaetter.xmlservice.TestBase;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class XPathWorkerTest extends TestBase {

    private static final File DATA_DIR = new File("../../../daten/XML_Briefe, Vorlage&Index");
    @Autowired
    private XPathService xPathService;

    @BeforeAll
    public static void setup() {
        Assumptions.assumeTrue(DATA_DIR.exists());
    }

    @Test
    public void test_listAllElementNames() throws Exception {
        HashSet<String> nameSet = new HashSet<>();
        for (File xml : Objects.requireNonNull(DATA_DIR.listFiles(x -> x.getName().endsWith(".xml")))) {
            try {
                List<String> results = xPathService.evaluate(new FileInputStream(xml), "//tei:body//tei:div[@type='diplomatische_Umschrift']//tei:p//tei:*/name()", null);
                nameSet.addAll(results);
            } catch (Exception e) {
                System.err.println("Cannot process file: " + xml.getName());
            }
        }
        nameSet.stream().sorted().forEach(System.out::println);
    }

}
