package top.autuan.sms;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SmsProps.class)
public class SmsConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "sms",name = "enable",havingValue = "http")
    SmsComponentHttpImpl smsComponent(@Autowired RedissonClient redissonClient, SmsProps prop){
        return new SmsComponentHttpImpl(redissonClient, prop);
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnProperty(prefix = "sms",name = "enable",havingValue = "tencent")
    TencentCloudSmsImpl tencentCloudSmsComponent(@Autowired RedissonClient redissonClient, SmsProps prop){
        return new TencentCloudSmsImpl(redissonClient, prop);
    }
}
