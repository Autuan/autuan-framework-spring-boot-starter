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

public class SmsComponentHttpImpl implements SmsComponent {
    private RedissonClient redissonClient;
    private final RandomGenerator RANDOM_GENERATOR;
    private final SmsProps props;

    public SmsComponentHttpImpl(RedissonClient redissonClient,
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
            throw new BusinessException(tip);
        }

        String code = RANDOM_GENERATOR.generate();
        bucket.set(code, 4, TimeUnit.HOURS);

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
