package at.kulinz.jaegerstaetter.xmlservice.service;

import net.sf.saxon.lib.ExtensionFunctionDefinition;

/**
 * This interface should be implemented by all bundles that provides an extension function.
 * <p>
 * Created by joseph on 25.02.16.
 */
public interface SaxonExtensionProvider {

    /**
     * The implementation should provide an id for each function. Best practise:
     * NamespaceURI:FunctionName.
     * E.g. "http://www.uibk.ac.at/igwee/ns:diff
     *
     * @return the ID of the function
     */
    String getExtensionId();

    /**
     * @return the function definition
     */
    ExtensionFunctionDefinition getExtensionFunctionDefinition();

}
