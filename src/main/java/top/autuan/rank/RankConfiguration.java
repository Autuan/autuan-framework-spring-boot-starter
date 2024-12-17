package top.autuan.rank;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.redis.RedisConfiguration;
import top.autuan.shortchain.ShortChainComponent;
import top.autuan.shortchain.ShortChainProps;

import java.util.Optional;

@Configuration
@AutoConfigureAfter(RedisConfiguration.class)
@EnableConfigurationProperties(RankProps.class)
public class RankConfiguration {
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "rank", name = "enable")
    RankComponent rankComponent(@Autowired RedissonClient redissonClient, RankProps props) {
        System.out.println("rank props enable "+props.getEnable());

        return new RankComponent(redissonClient
                );
    }
}
