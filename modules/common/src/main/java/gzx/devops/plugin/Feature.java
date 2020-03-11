package gzx.devops.plugin;

import java.lang.annotation.*;

/**
 * 功能
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Feature {

    /**
     * 类
     *
     * @return ClassFeature
     */
    ClassFeature cls() default ClassFeature.NULL;

    /**
     * 方法
     *
     * @return MethodFeature
     */
    MethodFeature method() default MethodFeature.NULL;
}
