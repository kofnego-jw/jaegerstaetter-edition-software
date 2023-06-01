package at.kulinz.jaegerstaetter.xmlservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

@SpringBootTest
@ContextConfiguration(classes = {XmlServiceConfig.class})
public abstract class TestBase {

    public static final File XML_FILE = new File("src/test/resources/test.xml");
    public static final File XSLT_FILE = new File("src/test/resources/test.xsl");
}
