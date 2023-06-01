package at.kulinz.jaegerstaetter.xmlservice.service.impl;

import at.kulinz.jaegerstaetter.xmlservice.XmlServiceException;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import net.sf.saxon.s9api.*;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class XPathServiceImpl implements XPathService {
    private final Processor processor = new Processor(false);

    private final DocumentBuilder documentBuilder = processor.newDocumentBuilder();

    private final XPathCompiler xPathCompiler = processor.newXPathCompiler();

    public XPathServiceImpl() {
        DEFAULT_NS_DECL.forEach(xPathCompiler::declareNamespace);
    }

    @Override
    public XPathExecutable createXPathExecutable(String xpath, Map<String, String> nsDecl) throws XmlServiceException {
        try {
            if (nsDecl != null) {
                nsDecl.forEach(xPathCompiler::declareNamespace);
            }
            return xPathCompiler.compile(xpath);
        } catch (Exception e) {
            throw new XmlServiceException("Cannot compile xpath.");
        }
    }

    @Override
    public String evaluateSingle(InputStream xml, String xpath, Map<String, String> nsDecl) throws XmlServiceException {
        XPathExecutable executable = createXPathExecutable(xpath, nsDecl);
        return evaluateSingle(xml, executable);
    }

    @Override
    public String evaluateSingle(InputStream xml, XPathExecutable executable) throws XmlServiceException {
        try {
            XPathSelector selector = executable.load();
            XdmNode node = documentBuilder.build(new StreamSource(xml));
            selector.setContextItem(node);
            XdmItem item = selector.evaluateSingle();
            if (item == null || item.isEmpty()) {
                return "";
            }
            return item.getStringValue();
        } catch (Exception e) {
            throw new XmlServiceException("Cannot evaluate xpath.", e);
        }
    }

    @Override
    public List<String> evaluate(InputStream xml, String xpath, Map<String, String> nsDecl) throws XmlServiceException {
        XPathExecutable executable = createXPathExecutable(xpath, nsDecl);
        return evaluate(xml, executable);
    }

    @Override
    public List<String> evaluate(InputStream xml, XPathExecutable executable) throws XmlServiceException {
        try {
            XPathSelector selector = executable.load();
            XdmNode node = documentBuilder.build(new StreamSource(xml));
            selector.setContextItem(node);
            XdmValue results = selector.evaluate();
            return results.stream()
                    .map(XdmItem::getStringValue)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new XmlServiceException("Cannot evaluate xpath.", e);
        }
    }
}
