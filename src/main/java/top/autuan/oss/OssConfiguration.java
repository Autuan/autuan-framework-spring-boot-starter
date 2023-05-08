package top.autuan.oss;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.captcha.CaptchaProps;
import top.autuan.oss.aliyun.AliYunOSSComponent;
import top.autuan.redis.RedisConfiguration;

@Configuration
@EnableConfigurationProperties(OssProps.class)
public class OssConfiguration {

    @Bean
    @ConditionalOnExpression("${oss.enable:false} && '${oss.type}'=='aliyun'")
    AliYunOSSComponent aliYunOSSComponent(OssProps props){
        return new AliYunOSSComponent(props.getEndpoint(),props.getBucketName(),props.getKey(),props.getSecret());
    }
}
