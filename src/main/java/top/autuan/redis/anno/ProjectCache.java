package top.autuan.redis.anno;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
/**
 * @author Autuan.Yu
 */
public @interface ProjectCache {
    /**
     * redis 过期时间 ; 单位：分钟
     */
    long expire() default 5L;

    /**
     * <p>保存到 redis 中的key</p>
     * <p>支持下列关键词</p>
     * <ul>
     *     <li>#method  当前注解使用的方法名</li>
     * </ul>
     * <p>除了#method 之外，可以通过 #参数名  的形式使用入参属性（入参必须是一个封装对象）</p>
     * <p>例： #id </p>
     *
     * <p>完整示例 ：<strong>'member::login::'+#id </strong></p>
     * <p>对于非封装对象,使用 $下标类型 的方式使用入参变量 </p>
     * <p>例 $0Long</p>
     * <p>支持的有：</p>
     * <ul>
     *     <li>Long</li>
     *     <li>其余未实际测试</li>
     * </ul>
     */
    String key() default "#method";

    /**
     * 时间单位 默认分钟
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 因JVM 泛型擦除, List<Bean> 结果，需声明 clazz=Bean.class
     * 除外,响应基本数据类型及其封装对象以及BigDecimal 的，需要申明响应对象
     *
     *
     * <ul>
     *     <li>List响应时，声明List中的泛型对象</li>
     *     <li>Integer</li>
     *     <li>Long</li>
     *     <li>Short</li>
     *     <li>Byte</li>
     *     <li>BigDecimal</li>
     *     <li>Boolean</li>
     *     <li>Character</li>
     *     <li>Float</li>
     *     <li>Double</li>
     *     <li>BigInteger</li>
     *     <li>LocalDate</li>
     *     <li>LocalDateTime<br>精度会丢失毫秒后的信息: 15:15:40.625149800 -> 15:15:40.625</li>
     *     <li>LocalTime<br>精度会丢失毫秒后的信息: 15:15:40.625149800 -> 15:15:40.625</li>
     * </ul>
     */
    Class clazz() default Void.class;
}
