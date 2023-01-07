package top.autuan.wecom;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.autuan.dingTalk.ProjectProps;


@Configuration
@EnableConfigurationProperties({WecomProps.class, ProjectProps.class})
public class WecomConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "wecom", name = "enable")
    WecomComponent wecomComponent(WecomProps prop, ProjectProps projectProps) {
        return new WecomComponent(prop, projectProps);
    }
}
