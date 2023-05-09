package top.autuan.oss;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OssProps.class)
public class OssConfiguration {

    @Bean
    @ConditionalOnExpression("${oss.enable:false} && '${oss.type}'=='aliyun'")
    AliYunOSSComponent aliYunOSSComponent(OssProps props){
        return new AliYunOSSComponent(props.getEndpoint(),props.getBucketName(),props.getKey(),props.getSecret());
    }
}
