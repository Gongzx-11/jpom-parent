package gzx.devops.model.data;

import gzx.devops.model.BaseModel;

/**
 * jdk 信息
 */
public class JdkInfoModel extends BaseModel {

    /**
     * jdk 路径
     */
    private String path;

    /**
     * jdk 版本
     */
    private String version;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
