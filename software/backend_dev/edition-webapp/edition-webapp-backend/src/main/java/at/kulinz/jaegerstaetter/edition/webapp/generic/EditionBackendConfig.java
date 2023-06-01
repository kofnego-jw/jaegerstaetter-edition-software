package at.kulinz.jaegerstaetter.edition.webapp.generic;

import at.kulinz.jaegerstaetter.bibleregistry.BibleRegistryConfig;
import at.kulinz.jaegerstaetter.biography.BiographyConfig;
import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.correspplaces.CorrespPlacesConfig;
import at.kulinz.jaegerstaetter.pdfgenerator.PdfGeneratorConfig;
import at.kulinz.jaegerstaetter.search.SearchConfig;
import at.kulinz.jaegerstaetter.tei.edition.connector.EditionConnectorConfig;
import at.kulinz.jaegerstaetter.tei.registry.RegistryConnectorConfig;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootConfiguration
@Import({JaegerstaetterConfig.class, EditionConnectorConfig.class, SearchConfig.class, RegistryConnectorConfig.class, BiographyConfig.class, BibleRegistryConfig.class,
        PdfGeneratorConfig.class, CorrespPlacesConfig.class})
@ComponentScan
public class EditionBackendConfig {


    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        PolymorphicTypeValidator subTypeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(List.class)
                .allowIfSubType(Map.class)
                .allowIfSubType(Set.class)
                .allowIfSubType("at.kulinz.jaegerstaetter.frontendmodel.dtoobj.")
                .build();
//        ObjectMapper objectMapper = JsonMapper.builder()
//                .activateDefaultTyping(subTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
//                .build();
//        objectMapper.registerModule(new JavaTimeModule());
//        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//        RedisSerializationContext.SerializationPair<?> jsonSerializer =
//                RedisSerializationContext.SerializationPair.fromSerializer(new GenericToStringSerializer<Object>());
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
                        // .serializeValuesWith(new )
                        .disableCachingNullValues())
                .build();
    }

}
