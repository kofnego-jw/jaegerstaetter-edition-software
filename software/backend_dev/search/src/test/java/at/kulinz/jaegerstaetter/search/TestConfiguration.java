package at.kulinz.jaegerstaetter.search;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"at.ac.uibk.fiba.ficker.search.service"})
@Import({SearchConfig.class})
public class TestConfiguration {

}
