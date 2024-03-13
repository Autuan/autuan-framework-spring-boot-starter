package top.autuan.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "totp"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotpAuthProps {
    // 验证码长度
    private Integer digits = 6;
    // 时间片长度
    private Integer period = 30;
}
