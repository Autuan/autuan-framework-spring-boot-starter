package top.autuan.oss;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oss")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OssProps {
    private String endpoint;
    private String bucketName;
    private String key;
    private String secret;
}
