package gzx.devops.service.manage;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import gzx.devops.common.BaseAgentController;
import gzx.devops.common.BaseOperService;
import gzx.devops.model.data.ProjectInfoModel;
import gzx.devops.model.data.ProjectRecoverModel;
import gzx.devops.system.AgentConfigBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * 项目管理
 */
@Service
public class ProjectInfoService extends BaseOperService<ProjectInfoModel> {
    @Resource
    private ProjectRecoverService projectRecoverService;

    public ProjectInfoService() {
        super(AgentConfigBean.PROJECT);
    }

    public HashSet<String> getAllGroup() {
        //获取所有分组
        List<ProjectInfoModel> projectInfoModels = list();
        HashSet<String> hashSet = new HashSet<>();
        if (projectInfoModels == null) {
            return hashSet;
        }
        for (ProjectInfoModel projectInfoModel : projectInfoModels) {
            hashSet.add(projectInfoModel.getGroup());
        }
        return hashSet;
    }


    /**
     * 删除项目
     *
     * @param id 项目
     */
    @Override
    public void deleteItem(String id) {
        ProjectInfoModel projectInfo = getItem(id);
        String userId = BaseAgentController.getNowUserName();
        super.deleteItem(id);
        // 添加回收记录
        ProjectRecoverModel projectRecoverModel = new ProjectRecoverModel(projectInfo);
        projectRecoverModel.setDelUser(userId);
        projectRecoverService.addItem(projectRecoverModel);
    }

    /**
     * 修改项目信息
     *
     * @param projectInfo 项目信息
     */
    @Override
    public void updateItem(ProjectInfoModel projectInfo) {
        projectInfo.setModifyTime(DateUtil.now());
        String userName = BaseAgentController.getNowUserName();
        if (!StrUtil.DASHED.equals(userName)) {
            projectInfo.setModifyUser(userName);
        }
        super.updateItem(projectInfo);
    }

    @Override
    public void addItem(ProjectInfoModel projectInfoModel) {
        projectInfoModel.setCreateTime(DateUtil.now());
        super.addItem(projectInfoModel);
    }

    public String getLogSize(String id) {
        ProjectInfoModel pim;
        pim = getItem(id);
        if (pim == null) {
            return null;
        }
        String logSize = null;
        File file = new File(pim.getLog());
        if (file.exists()) {
            long fileSize = file.length();
            if (fileSize <= 0) {
                return null;
            }
            logSize = FileUtil.readableFileSize(fileSize);
        }
        return logSize;
    }
}
