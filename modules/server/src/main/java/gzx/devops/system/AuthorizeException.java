package gzx.devops.system;

import cn.jiangzeyin.common.JsonMessage;

/**
 * 授权错误
 */
public class AuthorizeException extends RuntimeException {
    private JsonMessage jsonMessage;

    public AuthorizeException(JsonMessage jsonMessage, String msg) {
        super(msg);
        this.jsonMessage = jsonMessage;
    }

    public JsonMessage getJsonMessage() {
        return jsonMessage;
    }
}
