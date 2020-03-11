package gzx.devops.model.data;

import gzx.devops.model.BaseJsonModel;

import java.util.List;

/**
 * 节点分发白名单
 */
public class ServerWhitelist extends BaseJsonModel {
    private List<String> outGiving;

    public List<String> getOutGiving() {
        return outGiving;
    }

    public void setOutGiving(List<String> outGiving) {
        this.outGiving = outGiving;
    }
}
