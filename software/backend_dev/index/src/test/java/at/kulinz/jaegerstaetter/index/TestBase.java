package at.kulinz.jaegerstaetter.index;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
public abstract class TestBase {
}
