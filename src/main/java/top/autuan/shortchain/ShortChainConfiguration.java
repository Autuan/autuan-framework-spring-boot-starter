package top.autuan.shortchain;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import top.autuan.dingTalk.DingTalkComponent;
import top.autuan.dingTalk.DingTalkProps;
import top.autuan.dingTalk.ProjectProps;

public class ShortChainConfiguration {
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "short-chain", name = "enable")
    ShortChainComponent shortChainComponent(@Autowired RedissonClient redissonClient) {
        return new ShortChainComponent(redissonClient);
    }
}
