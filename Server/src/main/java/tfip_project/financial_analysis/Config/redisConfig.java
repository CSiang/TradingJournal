package tfip_project.financial_analysis.Config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class redisConfig {
    
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

    @Value("${spring.redis.username}")
    private String redisUsername;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private Optional<Integer> redisDatabase;


    @Bean
    @Scope("singleton")
    public RedisTemplate<String, String> redisTemplate(){

        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost.trim());
        // config.setHostName(redisHost);

        config.setPort(redisPort.get()); // Remember to use .get() because this is Optional class.

        if( !redisUsername.isEmpty() && !redisPassword.isEmpty()){
            // config.setUsername(redisUsername);
            // config.setPassword(redisPassword);
            config.setUsername(redisUsername.trim());
            config.setPassword(redisPassword.trim());
        }

        config.setDatabase(redisDatabase.get());

        final JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder().build();

        final JedisConnectionFactory jedisFac= new JedisConnectionFactory(config, jedisConfig);
        jedisFac.afterPropertiesSet();

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }



}
