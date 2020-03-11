package gzx.devops.system;

/**
 * 运行错误
 *
 */
public class JpomRuntimeException extends RuntimeException {

    public JpomRuntimeException(String message) {
        super(message);
    }

    public JpomRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
