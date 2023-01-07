package top.autuan.dingTalk;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.captcha.CaptchaComponent;
import top.autuan.captcha.CaptchaProps;


@Configuration
@EnableConfigurationProperties({DingTalkProps.class,ProjectProps.class})
public class DingTalkConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "ding-talk",name = "enable")
    DingTalkComponent dingTalkComponent( DingTalkProps prop,ProjectProps projectProps){
        return new DingTalkComponent(prop,projectProps);
    }
}
