package top.autuan.auth;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import top.autuan.auth.entity.AuthSetupResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

public class TotpAuthComponent {
    private SecretGenerator secretGenerator;
    private RecoveryCodeGenerator recoveryCodeGenerator;
//  @Autowired
    private CodeVerifier verifier;

    private QrGenerator qrGenerator;

    private Integer digits;
    private Integer period;

    public TotpAuthComponent(Integer digits, Integer period) {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        RecoveryCodeGenerator recoveryCodeGenerator = new RecoveryCodeGenerator();
        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(HashingAlgorithm.SHA1), new SystemTimeProvider());

        this.secretGenerator = secretGenerator;
        this.recoveryCodeGenerator = recoveryCodeGenerator;
        this.qrGenerator = qrGenerator;
        this.verifier = verifier;

//        digits = Optional.ofNullable(digits).orElse(6);
        this.digits = digits;
//        period = Optional.ofNullable(period).orElse(30);
        this.period = period;
    }




    public Boolean verify(String code, String secret) {
        return verifier.isValidCode(secret, code);
    }

    public String generateSecret() {
        String secret = secretGenerator.generate();
        return secret;
    }

    /**
     *
     * @param title 标题 即 issuer
     * @param subtitle 副标题 即 label 可以放提醒或用户名/手机号
     * @return
     * @throws QrGenerationException
     */
    public AuthSetupResult setup(String title, String subtitle) throws QrGenerationException {
        String secret = generateSecret();
        return setup(secret, title,subtitle);
    }

    public AuthSetupResult setup(String secret, String appName, String subtitle) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .issuer(appName)
                .label(subtitle)
                .secret(secret)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(digits)
                .period(period)
                .build();

        String qrCodeImage = getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );
        return AuthSetupResult.builder()
                .imageBase64(qrCodeImage)
                .secret(secret)
                .build();
    }

        public List<String> recoveryCodes(Integer recoverNum) {
        Integer amount = Optional.ofNullable(recoverNum).orElse(6);
        String[] codes = recoveryCodeGenerator.generateCodes(amount);
        List<String> list = Arrays.asList(codes);
        return list;
    }
}
