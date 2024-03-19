package top.autuan.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.redis"
)

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisProps {
    private Integer database = 0;
    private String host;
    private Integer port;
    private String password;

}
