package at.kulinz.jaegerstaetter.metadata.webapp;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.metadata.registry.RegistryModelConfig;
import at.kulinz.jaegerstaetter.metadata.registry.repository.DataRepository;
import at.kulinz.jaegerstaetter.metadata.registry.repository.JsonFileRepository;
import at.kulinz.jaegerstaetter.tei.registry.RegistryConnectorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.File;

@SpringBootApplication
@Import({RegistryModelConfig.class, RegistryConnectorConfig.class})
public class MetadataWebapp {

    @Autowired
    private JaegerstaetterConfig jaegerstaetterConfig;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MetadataWebapp.class);
        application.setAdditionalProfiles("metadata");
        ConfigurableApplicationContext ctx = application.run();
        System.out.println("Spring Boot Application 'Jägerstätter Registry' run with " + ctx.getBeanDefinitionCount() + " Beans.");
    }

    @Bean
    public DataRepository dataRepository() throws Exception {
        File repoFile = jaegerstaetterConfig.getRegistryJsonFileDataRepository();
        File parentFile = repoFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new Exception("Cannot create metadata repository file.");
        }
        return new JsonFileRepository(repoFile);
    }

}
