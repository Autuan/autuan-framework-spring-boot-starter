package top.autuan.captcha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "captcha"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaProps {
    private String prefix;
    private Integer length;
}
