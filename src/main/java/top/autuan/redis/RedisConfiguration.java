package top.autuan.redis;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import top.autuan.captcha.CaptchaComponent;
import top.autuan.redis.anno.ProjectCacheAspect;
import top.autuan.redis.anno.ProjectCacheEvictAspect;
import top.autuan.redis.anno.ProjectCachePutAspect;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(RedisProps.class)
public class RedisConfiguration {
    @Bean
    @Order(1)
    @Primary
    public RedissonClient redissonClient(RedisProps props){
        String host = props.getHost();
        Integer port = props.getPort();
        String address = "redis://" + host + ":" + port;
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer()
                .setAddress(address)
                .setPassword(props.getPassword())
                .setConnectionMinimumIdleSize(10)
                .setDatabase(props.getDatabase())
        ;
        RedissonClient client = Redisson.create(config);

        return client;
    }

    @Bean
    @Order(2)
    public ProjectCacheAspect projectCacheAspect(RedissonClient redissonClient) {
        return new ProjectCacheAspect(redissonClient);
    }
    @Bean
    @Order(3)
    public ProjectCacheEvictAspect projectCacheEvictAspect(RedissonClient redissonClient) {
        return new ProjectCacheEvictAspect(redissonClient);
    }
    @Bean
    @Order(4)
    public ProjectCachePutAspect projectCachePutAspect(RedissonClient redissonClient) {
        return new ProjectCachePutAspect(redissonClient);
    }

}
