package at.kulinz.jaegerstaetter.edition.webapp.preview;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.edition.admin.AdminConfig;
import at.kulinz.jaegerstaetter.edition.webapp.generic.EditionBackendConfig;
import at.kulinz.jaegerstaetter.frontendmodel.SwaggerConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@Import({AdminConfig.class, EditionBackendConfig.class, SwaggerConfig.class})
public class PreviewApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewApplication.class);

    private AtomicLong lastCopied = new AtomicLong(0L);

    @Autowired
    private JaegerstaetterConfig jaegerstaetterConfig;


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PreviewApplication.class);
        application.setAdditionalProfiles("preview");
        ConfigurableApplicationContext ctx = application.run();
        CacheManager cacheManager = ctx.getBean(CacheManager.class);
        clearCache(cacheManager);
        System.out.println("Spring Boot Application 'Jägerstätter Edition Preview and Admin' run with " + ctx.getBeanDefinitionCount() + " beans.");
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

    @Scheduled(fixedDelay = 5000)
    public void copyStylesheets() throws Exception {
        long now = System.currentTimeMillis();
        File stylesheetDir = new File("issues/backend_dev/xslt-stylesheets/src/main/resources/xsltstylesheets");
        if (!stylesheetDir.exists()) {
            // System.out.println("Cannot find stylesheet dir.");
            return;
        }
        long lastModified = this.lastCopied.get();
        File outputDir = this.jaegerstaetterConfig.xsltDir();
        for (File f : stylesheetDir.listFiles()) {
            copyIfChanged(f, stylesheetDir, outputDir, lastModified);
        }
        this.lastCopied.set(now);
    }

    private void copyIfChanged(File file, File base, File outputDir, long lastModified) throws Exception {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                copyIfChanged(child, base, outputDir, lastModified);
            }
        } else if (file.lastModified() > lastModified) {
            System.out.println("Copy stylesheet: " + file.getName());
            String relPath = file.getAbsolutePath().substring(base.getAbsolutePath().length());
            if (relPath.startsWith("/")) {
                relPath = relPath.substring(1);
            }
            File out = new File(outputDir, relPath);
            FileUtils.copyFile(file, out);
        }
    }
}
