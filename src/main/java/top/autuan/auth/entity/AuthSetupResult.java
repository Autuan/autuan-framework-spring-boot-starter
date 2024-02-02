package top.autuan.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSetupResult {
    /**
     * 二维码图片的base64编码
     */
    private String imageBase64;

    /**
     * auth secret
     */
    private String secret;
}
