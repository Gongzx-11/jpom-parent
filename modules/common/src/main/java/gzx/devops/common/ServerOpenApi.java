package gzx.devops.common;

/**
 * Server 开发接口api 列表
 *
 */
public class ServerOpenApi {

    public static final String HEAD = "DevOps-TOKEN";

    /**
     * 用户的token
     */
    public static final String USER_TOKEN_HEAD = "DevOps-USER-TOKEN";

    public static final String API = "/api/";

    public static final String UPDATE_NODE_INFO = API + "node/update";

    /**
     * 安装id
     */
    public static final String INSTALL_ID = API + "/installId";

    /**
     * 触发构建, 第一级构建id,第二级token
     */
    public static final String BUILD_TRIGGER_BUILD = API + "/build/{id}/{token}";
}
