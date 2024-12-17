package top.autuan.rank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rank")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankProps {
    private String enable;


}
