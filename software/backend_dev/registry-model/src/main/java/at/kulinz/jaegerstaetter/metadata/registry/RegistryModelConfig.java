package at.kulinz.jaegerstaetter.metadata.registry;

import at.kulinz.jaegerstaetter.metadata.authority.VocabularySearchConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@ComponentScan(basePackages = {"at.kulinz,jaegerstaetter.metadata.registry"})
@Import({VocabularySearchConfiguration.class})
public class RegistryModelConfig {

}
