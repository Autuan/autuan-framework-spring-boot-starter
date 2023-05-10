package top.autuan.shortchain;

import cn.hutool.core.util.RandomUtil;
import com.google.common.hash.Hashing;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import top.autuan.web.exception.BusinessException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShortChainComponent {
    public static final String base62Chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private RedissonClient redissonClient;


    public ShortChainComponent(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // 生成一个不重复的短链/短码
    public String generateShortCode(String bloomFilterKey,String originData) {
        // 一个系统可能有多个短链，所以需要指定 bloomFilterKey 如: 用户邀请码  文章公告短链 分享短链
        bloomFilterKey = Optional.ofNullable(bloomFilterKey).orElse("bloom:filter:key");

        List<String> list = new ArrayList<>();
        // 一次性生成20个 尽量避免递归
        for (int i = 0; i < 20; i++) {
            String randomStr = RandomUtil.randomString(12);
            originData += randomStr;

            // 通过 Guava 进行 MurmurHash 计算
            long hash = Hashing.murmur3_32().hashString(originData, StandardCharsets.UTF_8).padToLong();
            // 转为 62 进制
            String hash62 = convertToBase62(hash);
            list.add(hash62);
        }

        if (true) {
            RBloomFilter<Object> filter = redissonClient.getBloomFilter(bloomFilterKey);
            // 避免过滤器未初始化
            if(!filter.isExists() ){
                filter.tryInit(2048L, 0.03);
            }

            for (String val : list) {
                if (filter.contains(val)) {
                    continue;
                } else {
                    // 不存在，存入 redis
                    filter.add(val);
                    return val;
                }
            }
        }

        throw new BusinessException("generator error repeat 20 times");
    }

    private static String convertToBase62(long decimal) {
        String result = "";
        do {
            result = base62Chars.charAt((int) (decimal % 62)) + result;
            decimal /= 62;
        } while (decimal > 0);

        return result;
    }
}
