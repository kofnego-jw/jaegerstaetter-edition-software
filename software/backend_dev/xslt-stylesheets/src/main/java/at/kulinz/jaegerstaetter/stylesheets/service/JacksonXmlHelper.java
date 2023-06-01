package at.kulinz.jaegerstaetter.stylesheets.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.TocList;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.VersionInfoList;
import at.kulinz.jaegerstaetter.stylesheets.model.BiographyResult;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JacksonXmlHelper {

    private static final XmlMapper MAPPER = new XmlMapper();

    public static String toXML(MetadataResult result) throws Exception {
        byte[] bytes = MAPPER.writeValueAsBytes(result);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String toXML(DocDescResult result) throws Exception {
        byte[] bytes = MAPPER.writeValueAsBytes(result);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static MetadataResult fromInputStreamToMetadataResult(InputStream xmlStream) throws Exception {
        return MAPPER.readValue(xmlStream, MetadataResult.class);
    }

    public static DocDescResult fromInputStreamToDocDescResult(InputStream xmlStream) throws Exception {
        return MAPPER.readValue(xmlStream, DocDescResult.class);
    }

    public static BiographyResult fromInputStreamToBiographyResult(InputStream xmlStream) throws Exception {
        return MAPPER.readValue(xmlStream, BiographyResult.class);
    }

    public static String toXML(TocList tocList) throws Exception {
        byte[] bytes = MAPPER.writeValueAsBytes(tocList);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String toXML(VersionInfoList list) throws Exception {
        byte[] bytes = MAPPER.writeValueAsBytes(list);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static TocList fromInputStreamToTocList(InputStream xmlStream) throws Exception {
        return MAPPER.readValue(xmlStream, TocList.class);
    }

    public static String toXML(IndexDocument document) throws Exception {
        byte[] bytes = MAPPER.writeValueAsBytes(document);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static IndexDocument fromInputStreamToIndexDocument(InputStream xmlStream) throws Exception {
        return MAPPER.readValue(xmlStream, IndexDocument.class);
    }
}
