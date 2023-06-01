package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class XsltHelper {

    private static final String STYLESHEET_CLASSPATH = "/at/kulinz/jaegerstaetter/metadata/registry/jsonRepo2Tei.xsl";

    private final Processor processor = new Processor(false);

    private final XsltExecutable xsltExecutable;

    private final XmlMapper xmlMapper = new XmlMapper();

    public XsltHelper() throws MetadataException {
        try {
            StreamSource xslt = new StreamSource(getClass().getResourceAsStream(STYLESHEET_CLASSPATH));
            this.xsltExecutable = this.processor.newXsltCompiler().compile(xslt);
        } catch (Exception e) {
            throw new MetadataException("Cannot compile xslt.", e);
        }
    }

    public byte[] createTei(JsonRepository jsonRepository) throws MetadataException {
        try {
            XsltTransformer transformer = xsltExecutable.load();
            byte[] content = xmlMapper.writeValueAsBytes(jsonRepository);
            StreamSource xml = new StreamSource(new ByteArrayInputStream(content));
            transformer.setSource(xml);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Serializer dest = processor.newSerializer(out);
            transformer.setDestination(dest);
            transformer.transform();
            return out.toByteArray();
        } catch (Exception e) {
            throw new MetadataException("Cannot create tei document from json.", e);
        }
    }
}
