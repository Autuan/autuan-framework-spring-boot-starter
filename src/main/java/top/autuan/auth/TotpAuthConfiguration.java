package top.autuan.auth;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.captcha.CaptchaComponent;
import top.autuan.captcha.CaptchaProps;
import top.autuan.redis.RedisConfiguration;

@Configuration
@AutoConfigureAfter(RedisConfiguration.class)
@EnableConfigurationProperties(TotpAuthProps.class)
public class TotpAuthConfiguration {

    @Bean
//    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "totp",name = "enable")
    TotpAuthComponent totpAuthComponent(TotpAuthProps prop){
        return new TotpAuthComponent( prop.getDigits(), prop.getPeriod());
    }
}
