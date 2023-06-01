package at.kulinz.jaegerstaetter.xmlservice.service;

import at.kulinz.jaegerstaetter.xmlservice.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.util.List;

public class XPathServiceTest extends TestBase {

    @Autowired
    private XPathService xPathService;

    @Test
    public void test() throws Exception {
        List<String> ps = xPathService.evaluate(new FileInputStream(XML_FILE), "//tei:p", null);
        Assertions.assertEquals(2, ps.size());
        String p = xPathService.evaluateSingle(new FileInputStream(XML_FILE), "//tei:p[2]", null);
        Assertions.assertEquals("A second paragraph.", p);
    }

}
