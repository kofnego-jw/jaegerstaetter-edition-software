package at.kulinz.jaegerstaetter.xmlservice.service;

import at.kulinz.jaegerstaetter.xmlservice.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;

public class XsltServiceTest extends TestBase {

    @Autowired
    private XsltService xsltService;

    @Test
    public void test() throws Exception {
        byte[] result = xsltService.transform(new FileInputStream(XML_FILE), new StreamSource(XSLT_FILE), null);
        Assertions.assertTrue(result.length > 200);
    }
}
