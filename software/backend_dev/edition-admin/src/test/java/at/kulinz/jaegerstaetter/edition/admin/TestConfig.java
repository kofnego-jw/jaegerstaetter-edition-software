package at.kulinz.jaegerstaetter.edition.admin;

import at.kulinz.jaegerstaetter.testhelper.TestHelperConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({AdminConfig.class, TestHelperConfig.class})
public class TestConfig {
    
}
