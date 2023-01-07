package top.autuan.sms;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import top.autuan.web.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SmsComponent {
    private RedissonClient redissonClient;
    private final RandomGenerator RANDOM_GENERATOR;
    private final SmsProps props;

    public SmsComponent(RedissonClient redissonClient,
                        SmsProps props) {
        this.redissonClient = redissonClient;
        this.props = props;
        this.RANDOM_GENERATOR = new RandomGenerator("0123456789", props.getLength());
    }

    public void sendCode(String mobile) {
        String key = props.getPrefix() + mobile;
        RBucket<String> bucket = redissonClient.getBucket(key);

        // 上一条未过期，不发新消息
        if (bucket.isExists()) {
            String tip = props.getRepeat();
            tip = UnicodeUtil.toString(tip);
//            StrUtil.
//            throw new BusinessException("您已获取过验证码,验证码4小时内有效");
            throw new BusinessException(tip);
        }

        String code = RANDOM_GENERATOR.generate();
        bucket.set(code, 4, TimeUnit.HOURS);

//        String template = "您的验证码是{}。如非本人操作，请忽略本短信,4小时内有效。【权益转让中心】";
        String template = props.getTemplate();
        String content = StrUtil.format(template, code);

        send(mobile, content);
    }

    public Boolean verify(String mobile, String userInput) {
        String key = props.getPrefix() + mobile;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (bucket.isExists()) {
            return bucket.get().equals(userInput);
        }
        return false;
    }

    public void send(String mobile, String content) {
        Map<String, Object> param = new HashMap<>(4);
        param.put("username", props.getUsername());
        param.put("password", props.getPassword());
        param.put("mobile", mobile);
        param.put("content", content);

        HttpUtil.get(props.getUrl(), param);
    }
}
