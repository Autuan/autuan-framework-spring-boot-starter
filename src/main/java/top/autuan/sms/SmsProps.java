package top.autuan.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsProps {
    private String url;

    private String username;
    private String password;

    private String appId;
    private String secretId;
    private String secretKey;

    private Integer length;
    private String prefix;

    private String repeat = "您已获取过验证码,验证码4小时内有效";
    private String template = "您的验证码是{}。如非本人操作，请忽略本短信,4小时内有效。";
}
