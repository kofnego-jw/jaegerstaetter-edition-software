package at.kulinz.jaegerstaetter.xslt.extend.registry;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * Function implementation for uibk:keytoname.
 * The implementation uses a cache: JsonStrings->SearchRequest will be cached
 */
public class KeyToNameXsltExtensionFunction extends ExtensionFunctionCall {

    /**
     * highlighting service
     */
    private final KeyToNameService keyToNameService;

    public KeyToNameXsltExtensionFunction(KeyToNameService service) {
        super();
        this.keyToNameService = service;
    }

    private Sequence convertToSequence(String s) {
        return new StringValue(s);
    }

    @Override
    public Sequence call(XPathContext context, Sequence[] arguments)
            throws XPathException {
        String key, type, useOfficial;
        if (arguments.length < 2) {
            return convertToSequence("");
        }
        try (SequenceIterator iterator = arguments[0].iterate()) {
            key = iterator.next().getStringValue();
        }
        try (SequenceIterator iterator = arguments[1].iterate()) {
            type = iterator.next().getStringValue();
        }
        if (arguments.length > 2) {
            try (SequenceIterator iterator = arguments[2].iterate()) {
                useOfficial = iterator.next().getStringValue();
            }
        } else {
            useOfficial = "true";
        }
        boolean official = useOfficial.equalsIgnoreCase("true");

        try {
            String name = official ?
                    this.keyToNameService.getOfficialNameByKey(key, type) :
                    this.keyToNameService.getReadableNameByKey(key, type);
            return convertToSequence(name);
        } catch (Exception e) {
            throw new XPathException("Cannot get name from key.", e);
        }
    }

}
