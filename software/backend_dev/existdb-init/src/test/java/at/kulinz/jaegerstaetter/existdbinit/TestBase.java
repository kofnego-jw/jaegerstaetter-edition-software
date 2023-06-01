package at.kulinz.jaegerstaetter.existdbinit;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {ExistDBInitConfig.class})
public abstract class TestBase {
}
