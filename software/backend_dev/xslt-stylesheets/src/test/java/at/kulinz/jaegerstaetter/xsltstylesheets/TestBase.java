package at.kulinz.jaegerstaetter.xsltstylesheets;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {StylesheetsConfig.class})
public abstract class TestBase {

    @MockBean
    @Qualifier("editionRespository")
    public ExistDBTeiRepository editionRepository;

    @MockBean
    @Qualifier("commonRepository")
    public ExistDBTeiNotVersionedRepository commonRepository;
}
