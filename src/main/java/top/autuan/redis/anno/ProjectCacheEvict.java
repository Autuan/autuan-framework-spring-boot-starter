package top.autuan.redis.anno;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
/**
 * @author Autuan.Yu
 */
public @interface ProjectCacheEvict {

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
    String[] key() default {"#method"};
//    String key() default "#method";

}
