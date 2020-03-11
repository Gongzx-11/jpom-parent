package gzx.devops.service.manage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.JavaInfo;
import cn.hutool.system.JavaRuntimeInfo;
import gzx.devops.model.data.JdkInfoModel;
import gzx.devops.common.BaseOperService;
import gzx.devops.system.AgentConfigBean;
import org.springframework.stereotype.Service;

/**
 * jdk 管理
 */
@Service
public class JdkInfoService extends BaseOperService<JdkInfoModel> {

    public JdkInfoService() {
        super(AgentConfigBean.JDK_CONF);
    }

    /**
     * 使用中的jdk
     *
     * @return JdkInfoModel
     */
    private JdkInfoModel getDefaultJdk() {
        JavaRuntimeInfo info = new JavaRuntimeInfo();
        String homeDir = info.getHomeDir();
        String version = new JavaInfo().getVersion();
        if (StrUtil.isEmpty(homeDir) || StrUtil.isEmpty(version)) {
            return null;
        }
        String path = FileUtil.normalize(homeDir.replace("jre", ""));
        JdkInfoModel jdkInfoModel = new JdkInfoModel();
        jdkInfoModel.setId(IdUtil.fastUUID());
        jdkInfoModel.setVersion(version);
        jdkInfoModel.setPath(path);
        return jdkInfoModel;
    }

}
