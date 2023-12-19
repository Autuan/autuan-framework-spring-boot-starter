package top.autuan.shortchain;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.captcha.CaptchaProps;
import top.autuan.dingTalk.DingTalkComponent;
import top.autuan.dingTalk.DingTalkProps;
import top.autuan.dingTalk.ProjectProps;
import top.autuan.redis.RedisConfiguration;

import java.util.Optional;

@Configuration
@AutoConfigureAfter(RedisConfiguration.class)
@EnableConfigurationProperties(ShortChainProps.class)
public class ShortChainConfiguration {
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "short-chain", name = "enable")
    ShortChainComponent shortChainComponent(@Autowired RedissonClient redissonClient,ShortChainProps props) {
        return new ShortChainComponent(redissonClient, Optional.ofNullable(props.getRecursion()).orElse(false));
    }
}
