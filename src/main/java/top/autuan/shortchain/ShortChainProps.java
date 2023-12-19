package top.autuan.shortchain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "short-chain")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortChainProps {
    private String enable;

    private Boolean recursion;
}
