package at.kulinz.jaegerstaetter.index;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({IndexConfig.class})
public class TestConfiguration {
}
