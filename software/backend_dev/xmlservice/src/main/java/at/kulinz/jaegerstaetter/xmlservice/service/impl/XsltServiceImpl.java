package at.kulinz.jaegerstaetter.xmlservice.service.impl;

import at.kulinz.jaegerstaetter.xmlservice.XmlServiceException;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xmlservice.service.XsltService;
import net.sf.saxon.s9api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class XsltServiceImpl implements XsltService {

    private final Processor processor = new Processor(false);

    private final XsltCompiler xsltCompiler = processor.newXsltCompiler();

    private final CopyOnWriteArrayList<String> registeredExtensions = new CopyOnWriteArrayList<>();

    @Autowired
    public XsltServiceImpl() {
    }

    @Override
    public synchronized void registerExtensionProviders(List<SaxonExtensionProvider> extensions) throws XmlServiceException {
        if (extensions != null) {
            extensions.forEach(provider -> {
                String id = provider.getExtensionId();
                if (!registeredExtensions.contains(id)) {
                    this.processor.registerExtensionFunction(provider.getExtensionFunctionDefinition());
                    registeredExtensions.add(id);
                }
            });
        }
    }

    @Override
    public XsltExecutable createXsltExecutable(Source xsltStream) throws XmlServiceException {
        try {
            return this.xsltCompiler.compile(xsltStream);
        } catch (Exception e) {
            throw new XmlServiceException("Cannot compile xslt source.", e);
            //return null;
        }
    }

    @Override
    public byte[] transform(InputStream xml, XsltExecutable executable, Map<String, String> parameters) throws XmlServiceException {
        XsltTransformer transformer = executable.load();
        transformer.setSource(new StreamSource(xml));
        if (parameters != null) {
            parameters.forEach((key, value) -> transformer.setParameter(new QName(key), new XdmAtomicValue(value)));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Serializer serializer = processor.newSerializer(baos);
        transformer.setDestination(serializer);
        try {
            transformer.transform();
        } catch (Exception e) {
            throw new XmlServiceException("Cannot transform.", e);
        }
        String result = baos.toString(StandardCharsets.UTF_8);
        result = Normalizer.normalize(result, Normalizer.Form.NFC);
        return result.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] transform(InputStream xmlStream, Source xsltStream, Map<String, String> parameters) throws XmlServiceException {
        XsltExecutable executable = createXsltExecutable(xsltStream);
        return transform(xmlStream, executable, parameters);
    }

}
