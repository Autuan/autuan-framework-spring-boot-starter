package top.autuan.redis.anno;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import top.autuan.web.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Slf4j
public class ProjectCacheEvictAspect {
    private final RedissonClient redissonClient;

    public ProjectCacheEvictAspect(@Autowired RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(top.autuan.redis.anno.ProjectCacheEvict)")
    public void aspectLocation() {

    }

    @SneakyThrows
    @AfterReturning(pointcut = "aspectLocation()", returning = "result")
    public Object afterReturning(JoinPoint joinPoint, Object result) {
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            ProjectCacheEvict projectCacheEvict = method.getAnnotation(ProjectCacheEvict.class);
            if (null == projectCacheEvict) {
                log.error("null == projectCache -> method -> {} declaringClass -> {}", method.getName(), method.getDeclaringClass());
                throw new BusinessException("服务器异常 warn projectCache");
            }

            String[] keys = projectCacheEvict.key();
            for (String key : keys) {
                // 单引号 加号 先直接替换；未来再纳入静态字符串
                key = key.replaceAll("\\+", "");
                key = key.replaceAll("'", "");
                // 通用关键词替换
                key = key.replaceAll("#method", method.getName());

                Object[] args = joinPoint.getArgs();
                for (int i = 0; i < args.length; i++) {
                    Object obj = args[i];
                    Set<String> keySet = new HashSet<>();
                    Map<String, Object> paramMap = new HashMap<>();

                    List<String> baseTypeKeyList = new ArrayList<>();
                    String longKey = "$" + i + "Long";
                    String strKey = "$" + i + "String";
                    String integerKey = "$" + i + "Integer";

                    baseTypeKeyList.add(longKey);
                    baseTypeKeyList.add(strKey);
                    baseTypeKeyList.add(integerKey);

                    if (obj instanceof JSONObject) {
                        JSONObject jsonObj = JSONUtil.parseObj(obj);
                        keySet = jsonObj.keySet();
                        paramMap.putAll(jsonObj);
                    } else if (EnumUtil.isEnum(obj)) {
                        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) obj.getClass();
                        List<String> fieldNames = EnumUtil.getFieldNames(enumClass);
                        keySet.addAll(fieldNames);
                        for (String fieldName : fieldNames) {
                            Object fieldValue = ReflectUtil.getFieldValue(obj, fieldName);
                            paramMap.put(fieldName, fieldValue);
                        }
                    } else if (obj instanceof Long) {
                        Long val = (Long) obj;
                        paramMap.put(longKey, String.valueOf(val));
                        keySet.add(longKey);
                    } else if (obj instanceof String) {
                        paramMap.put(strKey, String.valueOf(obj));
                        keySet.add(strKey);
                    } else if (obj instanceof Integer) {
                        Integer val = (Integer) obj;
                        paramMap.put(integerKey, String.valueOf(val));
                        keySet.add(integerKey);
                    } else {
                        Map<String, Field> fieldMap = ReflectUtil.getFieldMap(obj.getClass());
                        for (Field itemField : fieldMap.values()) {
                            Object fieldValue = ReflectUtil.getFieldValue(obj, itemField);
                            paramMap.put(itemField.getName(), fieldValue);
                        }
                        keySet = fieldMap.keySet();
                    }
                    for (String name : keySet) {
                        if (key.contains("#" + name)) {
                            key = key.replaceAll("#" + name, String.valueOf(paramMap.get(name)));
                        }
                    }
                    for (String baseKey : baseTypeKeyList) {
                        if (key.contains(baseKey)) {
                            key = StrUtil.replace(key, baseKey, String.valueOf(paramMap.get(baseKey)));
                        }
                    }
                }

                RBucket<String> bucket = redissonClient.getBucket(key);
                bucket.delete();
            }

            return result;
        } catch (BusinessException e) {
            return result;
        } catch (Exception e) {
            log.error("ProjectCacheAspect -> error -> ", e);
            // todo dingTalk
//            dingTalkService.sendError(e);
            return result;
        }
    }

}
