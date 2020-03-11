package gzx.devops.system;

/**
 * agent 客户端端异常
 *
 */
public class AgentException extends RuntimeException {

    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
