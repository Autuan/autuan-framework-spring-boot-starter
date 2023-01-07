package top.autuan.captcha;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.redis.RedisConfiguration;

@Configuration
@AutoConfigureAfter(RedisConfiguration.class)
@EnableConfigurationProperties(CaptchaProps.class)
public class CaptchaConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "captcha",name = "enable")
    CaptchaComponent captchaComponent(@Autowired RedissonClient redissonClient,CaptchaProps prop){
        return new CaptchaComponent(redissonClient, prop.getPrefix(), prop.getLength());
    }

}
