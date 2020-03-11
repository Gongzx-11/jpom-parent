package gzx.devops.permission;

import java.lang.annotation.*;

/**
 * 系统管理的权限
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemPermission {
}
