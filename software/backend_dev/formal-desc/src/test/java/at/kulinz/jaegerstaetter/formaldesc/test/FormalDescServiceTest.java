package at.kulinz.jaegerstaetter.formaldesc.test;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.formaldesc.FilePathMockRepository;
import at.kulinz.jaegerstaetter.formaldesc.TestBase;
import at.kulinz.jaegerstaetter.formaldesc.service.FormalDescService;
import at.kulinz.jaegerstaetter.formaldesc.service.impl.FormalDescServiceImpl;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormalDescServiceTest extends TestBase {

    private static final File BASE_DIR = new File("../../../daten/XML_Briefe, Vorlage&Index");

    @Autowired
    TransformationService transformationService;


    @Test
    public void test() throws Exception {
        ExistDBTeiRepository mockRepo = new FilePathMockRepository(BASE_DIR);
        FormalDescService service = new FormalDescServiceImpl(transformationService, mockRepo);
        File[] files = BASE_DIR.listFiles(x -> x.getName().endsWith(".xml"));
        if (files != null) {
            List<String> ids = Stream.of(files).sorted().map(File::getName).collect(Collectors.toList());
            StatReport stats = service.createEditionStats(ids);
            System.out.println(stats);
        }
    }
}
