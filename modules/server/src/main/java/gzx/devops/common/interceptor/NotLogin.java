package gzx.devops.common.interceptor;

import java.lang.annotation.*;

/**
 * 游客可以访问的Controller 标记
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NotLogin {

}
