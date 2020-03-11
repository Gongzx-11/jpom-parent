package gzx.devops.service.node.script;

import com.alibaba.fastjson.JSONArray;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.model.data.NodeModel;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.permission.BaseDynamicService;
import gzx.devops.service.node.NodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScriptServer implements BaseDynamicService {

    @Resource
    private NodeService nodeService;

    @Override
    public JSONArray listToArray(String dataId) {
        NodeModel item = nodeService.getItem(dataId);
        if (!item.isOpenStatus()) {
            return null;
        }
        return listToArray(item);
    }

    public JSONArray listToArray(NodeModel nodeModel) {
        JSONArray jsonArray = NodeForward.requestData(nodeModel, NodeUrl.Script_List, null, JSONArray.class);
        return filter(jsonArray, ClassFeature.SCRIPT);
    }
}
