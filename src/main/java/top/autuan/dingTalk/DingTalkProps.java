package top.autuan.dingTalk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ding-talk")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkProps {
    private String url;
}
