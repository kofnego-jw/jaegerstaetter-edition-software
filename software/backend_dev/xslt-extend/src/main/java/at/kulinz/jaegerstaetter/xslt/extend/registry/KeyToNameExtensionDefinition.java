package at.kulinz.jaegerstaetter.xslt.extend.registry;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

/**
 * Definition of the highlighting function.
 */
public class KeyToNameExtensionDefinition extends ExtensionFunctionDefinition {

    /**
     * StructuredQName for the function
     */
    private static final StructuredQName FUNCTION_QNAME = new StructuredQName(QNameConstants.DEFAULT_PREFIX,
            QNameConstants.NAMESPACE_URL, QNameConstants.HIGHLIGHT_FUNCTIONNAME);

    /**
     * Argument: up to 3 strings.
     */
    private static final SequenceType[] ARGUMENT_TYPES = new SequenceType[]{
            SequenceType.SINGLE_STRING, // Key-Value
            SequenceType.SINGLE_STRING, // Type of registry
            SequenceType.SINGLE_STRING // Use Official?
    };

    /**
     * Result type: any sequence
     */
    private static final SequenceType RESULT_TYPE = SequenceType.SINGLE_STRING;

    /**
     * HighlightingService
     */
    private final KeyToNameService keyToNameService;

    /**
     * function object holder. will be initialized in the constructor.
     */
    private final KeyToNameXsltExtensionFunction functionObjectHolder;

    public KeyToNameExtensionDefinition(KeyToNameService keyToNameService) {
        super();
        this.keyToNameService = keyToNameService;
        this.functionObjectHolder = new KeyToNameXsltExtensionFunction(this.keyToNameService);
    }

    public KeyToNameService getKeyToNameService() {
        return this.keyToNameService;
    }


    @Override
    public SequenceType[] getArgumentTypes() {
        return ARGUMENT_TYPES;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return FUNCTION_QNAME;
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return RESULT_TYPE;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return this.functionObjectHolder;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return ARGUMENT_TYPES.length - 1;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return ARGUMENT_TYPES.length;
    }


}
