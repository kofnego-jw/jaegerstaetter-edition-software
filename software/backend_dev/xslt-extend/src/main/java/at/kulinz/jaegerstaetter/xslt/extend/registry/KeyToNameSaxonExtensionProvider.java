package at.kulinz.jaegerstaetter.xslt.extend.registry;

import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import net.sf.saxon.lib.ExtensionFunctionDefinition;

/**
 * The ExtensionProvider for Highlight function.
 */
public class KeyToNameSaxonExtensionProvider implements SaxonExtensionProvider {

    public static final String EXTENSION_ID = QNameConstants.NAMESPACE_URL + ":" + QNameConstants.HIGHLIGHT_FUNCTIONNAME;

    private final KeyToNameService keyToNameService;

    public KeyToNameSaxonExtensionProvider(KeyToNameService keyToNameService) {
        this.keyToNameService = keyToNameService;
    }

    @Override
    public String getExtensionId() {
        return EXTENSION_ID;
    }

    @Override
    public ExtensionFunctionDefinition getExtensionFunctionDefinition() {
        return new KeyToNameExtensionDefinition(this.keyToNameService);
    }
}
