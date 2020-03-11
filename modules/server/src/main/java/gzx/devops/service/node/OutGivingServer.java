package gzx.devops.service.node;

import com.alibaba.fastjson.JSONArray;
import gzx.devops.common.BaseOperService;
import gzx.devops.model.data.OutGivingModel;
import gzx.devops.model.data.OutGivingNodeProject;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.system.ServerConfigBean;
import gzx.devops.permission.BaseDynamicService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分发管理
 */
@Service
public class OutGivingServer extends BaseOperService<OutGivingModel> implements BaseDynamicService {

    public OutGivingServer() {
        super(ServerConfigBean.OUTGIVING);
    }

    public boolean checkNode(String nodeId) {
        List<OutGivingModel> list = list();
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (OutGivingModel outGivingModel : list) {
            List<OutGivingNodeProject> outGivingNodeProjectList = outGivingModel.getOutGivingNodeProjectList();
            if (outGivingNodeProjectList != null) {
                for (OutGivingNodeProject outGivingNodeProject : outGivingNodeProjectList) {
                    if (outGivingNodeProject.getNodeId().equals(nodeId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public JSONArray listToArray(String dataId) {
        return (JSONArray) JSONArray.toJSON(this.list());
    }

//    @Override
//    public List<OutGivingModel> list() {
//        return (List<OutGivingModel>) filter(super.list(), ClassFeature.OUTGIVING);
//    }
}
