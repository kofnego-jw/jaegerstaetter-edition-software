package at.kulinz.jaegerstaetter.xslt.extend.lucene;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchField;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchFieldParam;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchOccur;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameSaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.saxon.s9api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CustomLuceneFunctionTest {

    private static final File XSLT_FILE = new File("src/test/resources/customFn.xsl");
    private static final File XML_FILE = new File("src/test/resources/test.xml");

    private static final File REGISTRY_JSON = new File("src/test/resources/registry/data.json");

    private KeyToNameService getKeyToNameService() throws Exception {
        return new KeyToNameService(REGISTRY_JSON);
    }

    @Test
    public void test() throws Exception {
        Processor processor = new Processor(false);
        SaxonExtensionProvider provider = new HighlightSaxonExtensionProvider(new HighlightingServiceImpl());
        SaxonExtensionProvider keytonameProvider = new KeyToNameSaxonExtensionProvider(getKeyToNameService());
        processor.registerExtensionFunction(provider.getExtensionFunctionDefinition());
        processor.registerExtensionFunction(keytonameProvider.getExtensionFunctionDefinition());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializer serializer = processor.newSerializer(baos);
        SearchRequest req = new SearchRequest(List.of(
                new SearchFieldParam(SearchField.ALL, "test folg*", SearchOccur.SHOULD)
        ), 0, 10, null, true);
        String json = new ObjectMapper().writeValueAsString(req);
        XsltCompiler compiler = processor.newXsltCompiler();
        XsltExecutable xsltExecutable = compiler.compile(new StreamSource(XSLT_FILE));
        XsltTransformer transformer = xsltExecutable.load();
        transformer.setSource(new StreamSource(XML_FILE));
        transformer.setParameter(new QName("searchJson"), new XdmAtomicValue(json));
        transformer.setDestination(serializer);
        transformer.transform();
        String xmlResult = baos.toString(StandardCharsets.UTF_8);
        Assertions.assertTrue(xmlResult.contains("<p>Das ist ein <span class=\"highlight\">Test</span> mit <span class=\"highlight\">Folgen</span>.</p>"));
        Assertions.assertTrue(xmlResult.contains("<p>Das ist ein <span class=\"highlight\">Test</span> mit Testfolgend.</p>"));
        Assertions.assertTrue(xmlResult.contains("name=\"Jägerstätter, Franz\""));
    }

}
