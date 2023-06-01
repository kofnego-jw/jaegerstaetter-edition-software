package at.kulinz.jaegerstaetter.integration.metadata;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MetadataGroup;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MetadataRecord;
import at.kulinz.jaegerstaetter.integration.TestBase;
import at.kulinz.jaegerstaetter.integration.TestConstants;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.xmlservice.service.SaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameExtensionDefinition;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameSaxonExtensionProvider;
import at.kulinz.jaegerstaetter.xslt.extend.registry.KeyToNameService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListAllTitlesTest extends TestBase {

    @Autowired
    private TransformationService transformationService;

    @Autowired
    private List<SaxonExtensionProvider> saxonExtensionProviderList;

    private static final String getMetadata(MetadataResult result, String groupName, String fieldname) {
        if (result == null) {
            return "";
        }
        Optional<MetadataGroup> any = result.metadataGroups.stream().filter(g -> g.groupKey.equals(groupName)).findAny();
        if (any.isEmpty()) {
            return "";
        }
        Optional<MetadataRecord> recOpt = any.get().records.stream().filter(r -> r.fieldname.equals(fieldname)).findAny();
        if (recOpt.isEmpty()) {
            return "";
        }
        return recOpt.get().content;
    }

    @Test
    public void test() throws Exception {
        System.out.println(saxonExtensionProviderList);
        Optional<SaxonExtensionProvider> any = saxonExtensionProviderList.stream().filter(x -> x instanceof KeyToNameSaxonExtensionProvider)
                .findAny();
        KeyToNameSaxonExtensionProvider provider = (KeyToNameSaxonExtensionProvider) any.get();
        KeyToNameExtensionDefinition extension = (KeyToNameExtensionDefinition) provider.getExtensionFunctionDefinition();
        KeyToNameService service = extension.getKeyToNameService();

        System.out.println(service.getReadableNameByKey("#P_0025", "person"));

        for (File file : Stream.of(TestConstants.DATA_DIRECTORY.listFiles(x -> x.getName().endsWith(".xml")))
                .sorted(Comparator.comparing(File::getName)).collect(Collectors.toList())) {
            try {
                MetadataResult result = transformationService.getMetadataResult(new TeiDocument(file.getName(), FileUtils.readFileToByteArray(file), LocalDateTime.now()));
                System.out.println(file.getName());
                System.out.println("  " + getMetadata(result, "correspDesc", "Absendedatum"));
            } catch (Exception e) {
                System.err.println(file.getName());
            }
        }
    }


}
