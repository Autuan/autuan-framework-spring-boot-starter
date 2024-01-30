package top.autuan.autu;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import top.autuan.web.Result;

import java.util.Arrays;
import java.util.List;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

public class TotpAuthComponent {
    private SecretGenerator secretGenerator;

    private RecoveryCodeGenerator recoveryCodeGenerator;

    private QrGenerator qrGenerator;

    @Autowired
    private CodeVerifier verifier;

    public List<String> recoveryCodes() {
        String[] codes = recoveryCodeGenerator.generateCodes(6);
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


//    public String setupDevice() throws QrGenerationException {
//        return setupDevice();
//    }
    public String setupDevice(String secret,String companyEmail,String appName) throws QrGenerationException {
//        String secret = secretGenerator.generate();
//        String companyEmail = "example@example.com";
//        String appName = "AppName";

        QrData data = new QrData.Builder()
                .label(companyEmail)
                .secret(secret)
                .issuer(appName)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        String qrCodeImage = getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );
        return qrCodeImage;
    }
}
