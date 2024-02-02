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
import org.springframework.beans.factory.annotation.Autowired;
import top.autuan.auth.entity.AuthSetupResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

public class TotpAuthComponent {
    private SecretGenerator secretGenerator;
    private RecoveryCodeGenerator recoveryCodeGenerator;

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

    @Autowired
    private CodeVerifier verifier;

    public List<String> recoveryCodes(Integer recoverNum) {
        Integer amount = Optional.ofNullable(recoverNum).orElse(6);
        String[] codes = recoveryCodeGenerator.generateCodes(amount);
        List<String> list = Arrays.asList(codes);
        return list;
    }

    public Boolean verify(String code, String secret) {
        return verifier.isValidCode(secret, code);
    }

    public String generateSecret() {
        String secret = secretGenerator.generate();
        return secret;
    }

    public AuthSetupResult setupDevice(String companyEmail, String appName) throws QrGenerationException {
        String secret = generateSecret();
        return setupDevice(secret, companyEmail, appName);
    }

    public AuthSetupResult setupDevice(String secret, String companyEmail, String appName) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .label(companyEmail)
                .secret(secret)
                .issuer(appName)
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
}
