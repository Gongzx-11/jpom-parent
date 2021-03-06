package gzx.devops.service.node.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.model.data.NodeModel;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.permission.BaseDynamicService;
import gzx.devops.service.node.NodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 项目的简单操作
 */
@Service
public class ProjectInfoService implements BaseDynamicService {

    @Resource
    private NodeService nodeService;

    @Override
    public JSONArray listToArray(String dataId) {
        NodeModel item = nodeService.getItem(dataId);
        return listAll(item, null);
    }


    public JSONArray listAll(NodeModel nodeModel, HttpServletRequest request) {
        if (!nodeModel.isOpenStatus()) {
            return null;
        }
        JSONArray jsonArray = NodeForward.requestData(nodeModel, NodeUrl.Manage_GetProjectInfo, request, JSONArray.class);
        return filter(jsonArray, ClassFeature.PROJECT);
    }

    public List<String> getAllGroup(NodeModel nodeModel) {
        return NodeForward.requestData(nodeModel, NodeUrl.Manage_GetProjectGroup, null, List.class);
    }

    public JSONObject getItem(NodeModel nodeModel, String id) {
        return NodeForward.requestData(nodeModel, NodeUrl.Manage_GetProjectItem, JSONObject.class, "id", id);
    }

    public JSONObject getLogSize(NodeModel nodeModel, String id) {
        return NodeForward.requestData(nodeModel, NodeUrl.Manage_Log_LogSize, JSONObject.class, "id", id);
    }

    public JSONObject getItem(String nodeId, String id) {
        NodeModel item = nodeService.getItem(id);
        return NodeForward.requestData(item, NodeUrl.Manage_GetProjectItem, JSONObject.class, "id", id);
    }

    public JSONArray getJdkList(NodeModel nodeModel, HttpServletRequest request) {
        return NodeForward.requestData(nodeModel, NodeUrl.Manage_jdk_list, request, JSONArray.class);
    }
}
