package at.kulinz.jaegerstaetter.xmlservice.service;

import at.kulinz.jaegerstaetter.xmlservice.XmlServiceException;
import net.sf.saxon.s9api.XsltExecutable;

import javax.xml.transform.Source;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface XsltService {

    void registerExtensionProviders(List<SaxonExtensionProvider> extensions) throws XmlServiceException;

    XsltExecutable createXsltExecutable(Source xsltStream) throws XmlServiceException;

    byte[] transform(InputStream xmlStream, Source xsltStream, Map<String, String> parameters) throws XmlServiceException;

    byte[] transform(InputStream xmlStream, XsltExecutable executable, Map<String, String> parameters) throws XmlServiceException;

}
