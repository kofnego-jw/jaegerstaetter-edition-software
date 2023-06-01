package at.kulinz.jaegerstaetter.edition.webapp.edition;

import at.kulinz.jaegerstaetter.edition.webapp.generic.EditionBackendConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableCaching
@Import({EditionBackendConfig.class})
public class EditionApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EditionApplication.class);
        application.setAdditionalProfiles("edition");
        ConfigurableApplicationContext ctx = application.run();
        CacheManager cacheManager = ctx.getBean(CacheManager.class);
        clearCache(cacheManager);
        System.out.println("Spring Boot Application 'Jägerstätter Edition' run with " + ctx.getBeanDefinitionCount() + " beans.");
    }

    private static void clearCache(CacheManager manager) {
        if (manager != null) {
            for (String s : manager.getCacheNames()) {
                Cache cache = manager.getCache(s);
                if (cache != null) {
                    cache.clear();
                }
            }
        }
    }


}
