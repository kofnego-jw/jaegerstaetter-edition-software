package at.kulinz.jaegerstaetter.xmlservice.service;

import at.kulinz.jaegerstaetter.xmlservice.XmlServiceException;
import net.sf.saxon.s9api.XPathExecutable;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface XPathService {

    Map<String, String> DEFAULT_NS_DECL = Map.of("tei", "http://www.tei-c.org/ns/1.0");

    XPathExecutable createXPathExecutable(String xpath, Map<String, String> nsDecl) throws XmlServiceException;

    String evaluateSingle(InputStream xml, String xpath, Map<String, String> nsDecl) throws XmlServiceException;

    String evaluateSingle(InputStream xml, XPathExecutable executable) throws XmlServiceException;

    List<String> evaluate(InputStream xml, String xpath, Map<String, String> nsDecl) throws XmlServiceException;

    List<String> evaluate(InputStream xml, XPathExecutable executable) throws XmlServiceException;
}
