package at.kulinz.jaegerstaetter.xslt.extend.lucene;

import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import net.sf.saxon.lib.ExtensionFunctionDefinition;

/**
 * The ExtensionProvider for Highlight function.
 */
public class HighlightSaxonExtensionProvider implements SaxonExtensionProvider {

    public static final String EXTENSION_ID = QNameConstants.NAMESPACE_URL + ":" + QNameConstants.HIGHLIGHT_FUNCTIONNAME;

    private final HighlightingService highlightingService;

    public HighlightSaxonExtensionProvider(HighlightingService highlightingService) {
        this.highlightingService = highlightingService;
    }

    @Override
    public String getExtensionId() {
        return EXTENSION_ID;
    }

    @Override
    public ExtensionFunctionDefinition getExtensionFunctionDefinition() {
        return new HighlightingXsltExtensionDefinition(highlightingService);
    }
}
