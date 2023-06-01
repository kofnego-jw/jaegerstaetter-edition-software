package at.kulinz.jaegerstaetter.tei.edition.connector;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.TocList;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.tei.edition.test.MetadataServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Comparator;
import java.util.stream.Stream;

public class ResourceServiceImplTest extends TestBase {

    @Autowired
    TransformationService transformationService;

    @Test
    public void test() throws Exception {
        Stream.of(MetadataServiceTest.DATA.listFiles(x -> x.getName().endsWith(".xml")))
                .sorted(Comparator.comparing(File::getName))
                .forEach(f -> {
                    try {
                        TocList list = transformationService.toTocEntries(MetadataServiceTest.mock(f));
                        System.out.println(f.getName() + ": " + list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
