package gzx.devops.service.system;

import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.model.data.AgentWhitelist;
import gzx.devops.model.data.NodeModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 白名单
 */
@Service
public class WhitelistDirectoryService {

    public AgentWhitelist getData(NodeModel model) {
        return NodeForward.requestData(model, NodeUrl.WhitelistDirectory_data, null, AgentWhitelist.class);
    }

    /**
     * 获取项目路径白名单
     *
     * @param model 实体
     * @return project
     */
    public List<String> getProjectDirectory(NodeModel model) {
        AgentWhitelist agentWhitelist = getData(model);
        if (agentWhitelist == null) {
            return null;
        }
        return agentWhitelist.getProject();
    }

    public List<String> getNgxDirectory(NodeModel model) {
        AgentWhitelist agentWhitelist = getData(model);
        if (agentWhitelist == null) {
            return null;
        }
        return agentWhitelist.getNginx();
    }

    public List<String> getCertificateDirectory(NodeModel model) {
        AgentWhitelist agentWhitelist = getData(model);
        if (agentWhitelist == null) {
            return null;
        }
        return agentWhitelist.getCertificate();
    }


}
