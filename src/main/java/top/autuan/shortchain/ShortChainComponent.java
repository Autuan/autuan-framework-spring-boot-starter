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
import java.util.concurrent.TimeUnit;

public class ShortChainComponent {
    public static final String base62Chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final boolean RECURSION;

    private final int DAY_TO_LIVE ;
    private RedissonClient redissonClient;

    public ShortChainComponent(RedissonClient redissonClient, boolean recursion, int dayToLive) {
        this.redissonClient = redissonClient;
        this.RECURSION = recursion;
        this.DAY_TO_LIVE = dayToLive;
    }

    private static String convertToBase62(long decimal) {
        String result = "";
        do {
            result = base62Chars.charAt((int) (decimal % 62)) + result;
            decimal /= 62;
        } while (decimal > 0);

        return result;
    }

    // 生成一个不重复的短链/短码
    public String generateShortCode( String originData) {
        return generateShortCode(null, originData, RECURSION);
    }

    public String generateShortCode(String bloomFilterKey, String originData) {
        return generateShortCode(bloomFilterKey, originData, RECURSION);
    }

    public String generateShortCode(String bloomFilterKey, String originData, boolean isRecursion) {
        // 一个系统可能有多个短链，所以需要指定 bloomFilterKey 如: 用户邀请码  文章公告短链 分享短链
        bloomFilterKey = Optional.ofNullable(bloomFilterKey).orElse("bloom:filter:key");
        String reflectionKey = bloomFilterKey+ ":reflection:";

        List<String> list = new ArrayList<>();
        // 一次性生成20个 尽量避免递归
        for (int i = 0; i < 20; i++) {
            String randomStr = RandomUtil.randomString(12);
            String ramdomDataStr = originData +  randomStr;

            // 通过 Guava 进行 MurmurHash 计算
            long hash = Hashing.murmur3_32().hashString(ramdomDataStr, StandardCharsets.UTF_8).padToLong();
            // 转为 62 进制
            String hash62 = convertToBase62(hash);
            list.add(hash62);
        }

        RBloomFilter<Object> filter = redissonClient.getBloomFilter(bloomFilterKey);
        // 避免过滤器未初始化
        if (!filter.isExists()) {
            filter.tryInit(2048L, 0.03);
        }

        for (String val : list) {
            if (filter.contains(val)) {
                continue;
            } else {
                // 不存在，存入 redis
                filter.add(val);
                redissonClient.getBucket(reflectionKey + val).set(originData,DAY_TO_LIVE, TimeUnit.DAYS);
                return val;
            }
        }

        if (isRecursion) {
            return generateShortCode(bloomFilterKey, originData, isRecursion);
        }

        throw new BusinessException("generator error repeat 20 times");
    }

    public String accessData( String shortChain){
        return accessData(null, shortChain);
    }

    public String accessData(String bloomFilterKey , String shortChain){
        bloomFilterKey = Optional.ofNullable(bloomFilterKey).orElse("bloom:filter:key");
        String reflectionKey = bloomFilterKey+ ":reflection:";

        Object o = redissonClient.getBucket(reflectionKey + shortChain)
                .get();
        return String.valueOf(o);
    }
}
