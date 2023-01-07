package top.autuan.redis.anno;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.autuan.web.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Aspect
//@Component
@Slf4j
public class ProjectCacheAspect {
//    private final IDingTalkService dingTalkService;
    private final RedissonClient redissonClient;

    public ProjectCacheAspect(@Autowired RedissonClient redissonClient
//            ,@Autowired IDingTalkService dingTalkService
    ) {
//        this.dingTalkService = dingTalkService;
        this.redissonClient = redissonClient;
    }

    /**
     * 切入点配置
     *
     * @author Autuan.Yu
     */
    @Pointcut("@annotation(top.autuan.redis.anno.ProjectCache)")
    public void aspectLocation() {

    }

    @SneakyThrows
    @Around("aspectLocation()")
    // todo list 无法正常使用
    public Object around(ProceedingJoinPoint joinPoint) {
//        log.info("Project Cache Aspect -> around -> start");
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            ProjectCache projectCache = method.getAnnotation(ProjectCache.class);
            if (null == projectCache) {
                log.error("null == projectCache -> method -> {} declaringClass -> {}", method.getName(), method.getDeclaringClass());
                throw new BusinessException("服务器异常 warn projectCache");
            }
            String key = projectCache.key();
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
                } else if(obj instanceof Integer ){
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
            boolean keyExist = bucket.isExists();
            if (keyExist) {
                log.debug("ProjectCacheAspect -> cache -> check -> end -> method -> {} key -> {} class -> {}", method.getName(), key, method.getDeclaringClass());
                // 重复请求直接响应redis缓存值，不刷新过期时间
                String cacheVal = bucket.get();
                Class clazz = projectCache.clazz();
                Class returnType = clazz == Void.class ? method.getReturnType() : clazz;
                if (JSONUtil.isJsonObj(cacheVal)) {
                    if (returnType == Integer.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getInt("val");
                    } else if (returnType == Long.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getLong("val");
                    } else if (returnType == Short.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getShort("val");
                    } else if (returnType == Byte.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getByte("val");
                    } else if (returnType == BigDecimal.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getBigDecimal("val");
                    } else if (returnType == Boolean.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getBool("val");
                    } else if (returnType == Character.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getChar("val");
                    } else if (returnType == Float.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getFloat("val");
                    } else if (returnType == Double.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getDouble("val");
                    } else if (returnType == BigInteger.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getBigInteger("val");
                    } else if (returnType == LocalDateTime.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        return cacheObj.getLocalDateTime("val", null);
                    } else if (returnType == LocalDate.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        LocalDateTime dateTime = cacheObj.getLocalDateTime("val", null);
                        return dateTime.toLocalDate();
                    } else if (returnType == LocalTime.class) {
                        JSONObject cacheObj = JSONUtil.parseObj(cacheVal);
                        LocalDateTime dateTime = cacheObj.getLocalDateTime("val", null);
                        return dateTime.toLocalTime();
                    } else {

                        return JSONUtil.toBean(cacheVal, returnType);
                    }
                } else if (JSONUtil.isJsonArray(cacheVal)) {
                    return JSONUtil.toList(cacheVal, returnType);
                } else {
                    if (String.class.equals(returnType)) {
                        return cacheVal;
                    }
                    return BeanUtil.toBean(cacheVal, returnType);
                }
            } else {
                Object result = joinPoint.proceed();
                if (isBaseType(result)) {
                    JSONObject tempVal = new JSONObject();
                    tempVal.set("val", result);
                    bucket.set(tempVal.toString(), projectCache.expire(), projectCache.unit());
                } else {
                    bucket.set(JSONUtil.toJsonStr(result), projectCache.expire(), projectCache.unit());
                }
                return result;
            }
        } catch (BusinessException e) {
            Object result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            log.error("ProjectCacheAspect -> error -> ", e);
            // todo dingTalk ?
//            dingTalkService.sendError(e);
            return joinPoint.proceed();
        }
    }

    private boolean isBaseType(Object obj) {
        return obj instanceof Integer
                || obj instanceof Long
                || obj instanceof Short
                || obj instanceof Byte
                || obj instanceof BigDecimal
                || obj instanceof Boolean
                || obj instanceof Character
                || obj instanceof Float
                || obj instanceof Double
                || obj instanceof BigInteger
                || obj instanceof LocalDate
                || obj instanceof LocalDateTime
                || obj instanceof LocalTime
                ;
    }
}