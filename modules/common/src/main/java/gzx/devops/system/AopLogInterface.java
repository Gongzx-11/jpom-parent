package gzx.devops.system;

import org.aspectj.lang.JoinPoint;

/**
 * 日志接口
 *
 */
public interface AopLogInterface {
    /**
     * 进入前
     *
     * @param joinPoint point
     */
    void before(JoinPoint joinPoint);

    /**
     * 执行后
     *
     * @param value 结果
     */
    void afterReturning(Object value);
}
