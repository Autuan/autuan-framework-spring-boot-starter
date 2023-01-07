package top.autuan.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.autuan.web.enums.BaseEnum;
import top.autuan.web.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ProjectInterceptor implements HandlerInterceptor {
    @Autowired
    private RedissonClient redissonClient;
    @Value("${web.interceptor.prefix.token}")
    private String prefixToken;
    @Value("${web.interceptor.prefix.limit}")
    private String prefixLimit;
    @Value("${web.interceptor.prefix.record}")
    private String prefixRecord;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // todo 枚举
        String token = request.getHeader("token");

        // todo map 位置
        // todo key
//        RMap<String, Object> map = redissonClient.getMap("sso:login:" + token);
        RMap<String, Object> map = redissonClient.getMap(prefixToken + token);

        if (CollUtil.isEmpty(map)) {
            // todo 枚举     TOKEN("501008","token失效,请重新登录"),
            throw new BusinessException(BaseEnum.UN_ROOT_ERROR);
        }

        // IP
        String clientIP = ServletUtil.getClientIP(request);
        RRateLimiter limiter = getLimiter(token);
        if (limiter.tryAcquire()) {
            return true;
        } else {
            // 已被限流
            // 记录IP
//            RMap<String, String> recordMap = redissonClient.getMap("limit:record:" + token);
            RMap<String, String> recordMap = redissonClient.getMap(prefixRecord + token);
            recordMap.put("ip", clientIP);
            recordMap.put("time", LocalDateTime.now().toString());
            if (recordMap.isExists()) {
                Integer frequency = Optional.ofNullable(recordMap.get("frequency"))
                        .map(Integer::valueOf)
                        .orElse(0);
                frequency++;
                recordMap.put("frequency", String.valueOf(frequency));
            } else {
                recordMap.put("frequency", "1");
            }
            recordMap.expire(7, TimeUnit.DAYS);

            // 提示异常
            throw new BusinessException(BaseEnum.FREQUENCY);
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    private RRateLimiter getLimiter(String token) {
        // todo prefix
//        RRateLimiter limiter = redissonClient.getRateLimiter("limiter:sso:" + token);
        RRateLimiter limiter = redissonClient.getRateLimiter(prefixLimit + token);
        if (limiter.isExists()) {
            // 刷新过期时间
            limiter.expire(5, TimeUnit.MINUTES);
            return limiter;
        } else {
            // 60 秒  600个 令牌
            limiter.trySetRate(RateType.OVERALL, 600, 60, RateIntervalUnit.SECONDS);
            // 5分钟未请求 过期此限流器
            limiter.expire(5, TimeUnit.MINUTES);
            return limiter;
        }
    }
}
