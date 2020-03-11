package gzx.devops.service.system;

import cn.jiangzeyin.common.DefaultSystemLog;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.BaseDataService;
import gzx.devops.model.data.ServerWhitelist;
import gzx.devops.system.ServerConfigBean;
import gzx.devops.util.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerWhitelistServer extends BaseDataService {

    public ServerWhitelist getWhitelist() {
        try {
            JSONObject jsonObject = getJSONObject(ServerConfigBean.OUTGIVING_WHITELIST);
            if (jsonObject == null) {
                return null;
            }
            return jsonObject.toJavaObject(ServerWhitelist.class);
        } catch (Exception e) {
            DefaultSystemLog.getLog().error(e.getMessage(), e);
        }
        return null;
    }

    public void saveWhitelistDirectory(ServerWhitelist serverWhitelist) {
        String path = getDataFilePath(ServerConfigBean.OUTGIVING_WHITELIST);
        JsonFileUtil.saveJson(path, serverWhitelist.toJson());
    }


    public List<String> getOutGiving() {
        ServerWhitelist serverWhitelist = getWhitelist();
        if (serverWhitelist == null) {
            return null;
        }
        return serverWhitelist.getOutGiving();
    }
}
