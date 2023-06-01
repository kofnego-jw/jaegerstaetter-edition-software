package at.kulinz.jaegerstaetter.stylesheets.service;

public class TransformationConstants {

    public static final String XSLT_RESOURCE_PREFIX = "/xsltstylesheets/";
    public static final String XSLT_RESOURCE_PATTERN = XSLT_RESOURCE_PREFIX + "**";

    public static final String HTML_NORM_STYLESHEET = "representation/norm.xsl";
    public static final String HTML_NORM_WITH_SEARCHHIGHLIGHT_STYLESHEET = "representation/norm_search.xsl";
    public static final String HTML_DIPLO_STYLESHEET = "representation/diplo.xsl";

    public static final String XML_STYLESHEET = "representation/toXml.xsl";
    public static final String XML_PARAM_INCLUDE_HEADER_KEY = "includeHeader";
    public static final String XML_PARAM_VERSIONS_KEY = "versions";

    public static final String METADATA_METADATARESULT_STYLESHEET = "metadata/metadata.xsl";

    public static final String BIOGRAPHY_BIOGRAPHYRESULT_STYLESHEET = "biography/biographyResult.xsl";
    public static final String BIOGRAPHY_HTML_STYLESHEET = "biography/toHtml.xsl";
    public static final String BIOGRAPHY_TOC_STYLESHEET = "biography/toToc.xsl";
    public static final String BIOGRAPHY_PRINTHTML_STYLESHEET = "biography/biographyToPrintHtml.xsl";

    public static final String PARAM_PORT_NUMBER = "port";

    public static final String TOC_STYLESHEET = "representation/toc.xsl";
    public static final String LUCENE_INDEXDOCUMENT_STYLESHEET = "lucene/indexdoc.xsl";

    public static final String COMMENT_DOC_HTML_STYLESHEET = "commentdoc/toHtml.xsl";
    public static final String COMMENT_DOC_TOC_STYLESHEET = "commentdoc/toToc.xsl";

    public static final String NOTE_STYLESHEET = "representation/note.xsl";

    public static final String PRINT_HTML_STYLESHEET = "representation/printHtml.xsl";

    public static final String PARAM_SEARCH_JSON = "searchJson";
    public static final String PARAM_HIGHLIGHT_STARTTAG = "startTag";
    public static final String PARAM_HIGHLIGHT_ENDTAG = "endTag";

    public static final String PARAM_MARK_RS_TYPE = "markRsType";
    public static final String PARAM_MARK_RS_KEY = "markRsKey";

    public static final String DEFAULT_VAL_HIGHLIGHT_STARTTAG = "<span class=\"highlight\">";
    public static final String DEFAULT_VAL_HIGHLIGHT_ENDTAG = "</span>";

    public static final String NOTE_ID = "noteId";

    public static final String DOC_DESC_STYLESHEET = "metadata/docDesc.xsl";
    public static final String PARAM_FILENAME = "filename";

    public static final String BIOGRAPHY_INDEX_HTML_STYLESHEET = "commentdoc/biographyIndex.xsl";

    public static final String SPECIAL_CORRESP_HTML_STYLESHEET = "commentdoc/specialCorresp.xsl";

}
