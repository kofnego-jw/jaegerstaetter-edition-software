package at.kulinz.jaegerstaetter.photodoc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {PhotoDocConfig.class})
public abstract class TestBase {
}
