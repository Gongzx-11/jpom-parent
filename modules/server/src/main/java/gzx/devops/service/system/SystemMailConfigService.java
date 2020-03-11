package gzx.devops.service.system;

import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.BaseDataService;
import gzx.devops.model.data.MailAccountModel;
import gzx.devops.system.ServerConfigBean;
import gzx.devops.util.JsonFileUtil;
import org.springframework.stereotype.Service;

/**
     * 监控管理Service
 */
@Service
public class SystemMailConfigService extends BaseDataService {

    /**
     * 获取配置
     *
     * @return config
     */
    public MailAccountModel getConfig() {
        JSONObject config = getJSONObject(ServerConfigBean.MAIL_CONFIG);
        if (config == null) {
            return null;
        }
        return config.toJavaObject(MailAccountModel.class);
    }

    /**
     * 报错配置
     *
     * @param mailAccountModel config
     */
    public void save(MailAccountModel mailAccountModel) {
        String path = getDataFilePath(ServerConfigBean.MAIL_CONFIG);
        JsonFileUtil.saveJson(path, mailAccountModel.toJson());
    }
}
