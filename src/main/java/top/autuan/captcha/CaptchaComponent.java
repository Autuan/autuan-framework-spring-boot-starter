package top.autuan.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

public class CaptchaComponent {
    private final RandomGenerator RANDOM_GENERATOR;
    private final String PREFIX;
    private RedissonClient redissonClient;

    public CaptchaComponent(RedissonClient redissonClient, String prefix, Integer length) {
        this.redissonClient = redissonClient;
        this.PREFIX = prefix;
        this.RANDOM_GENERATOR = new RandomGenerator("0123456789", length);
    }

    public String generatorBase64(String identify) {
        ByteArrayOutputStream captcha = generator(identify);
        return Base64.encode(captcha.toByteArray());
    }

    public ByteArrayOutputStream generator(String identify) {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(RANDOM_GENERATOR);

        // 图形验证码写出，可以写出到文件，也可以写出到流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        lineCaptcha.write(outputStream);

        String code = lineCaptcha.getCode();
        String key = PREFIX + identify;
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(code, 5, TimeUnit.MINUTES);

        return outputStream;
    }

    public Boolean verifyCaptcha(String identify, String userInput) {
        if (StrUtil.hasBlank(identify, userInput)) {
            return false;
        }
        String key = PREFIX + identify;
        RBucket<String> bucket = redissonClient.getBucket(key);
        boolean exists = bucket.isExists();
        if (exists) {
            String code = bucket.get();
            boolean isEq = userInput.equals(code);
            if (isEq) {
                bucket.delete();
            }
            return isEq;
        } else {
            return false;
        }

    }
}
