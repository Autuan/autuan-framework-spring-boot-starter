package top.autuan.wecom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wecom")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WecomProps {
    private String url;
}
