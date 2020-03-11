package gzx.devops.controller.node.manage;

import cn.hutool.core.util.StrUtil;
import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.common.validator.ValidatorItem;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.BaseServerController;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.common.interceptor.OptLog;
import gzx.devops.model.RunMode;
import gzx.devops.model.log.UserOperateLogV1;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import gzx.devops.plugin.MethodFeature;
import gzx.devops.service.node.manage.ProjectInfoService;
import gzx.devops.service.system.WhitelistDirectoryService;
import gzx.devops.system.ConfigBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目管理
 */
@Controller
@RequestMapping(value = "/node/manage/")
@Feature(cls = ClassFeature.PROJECT)
public class EditProjectController extends BaseServerController {
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private WhitelistDirectoryService whitelistDirectoryService;

    @RequestMapping(value = "getProjectData.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getProjectData(@ValidatorItem String id) {
        JSONObject projectInfo = projectInfoService.getItem(getNode(), id);
        return JsonMessage.getString(200, "", projectInfo);
    }

    /**
     * 修改项目页面
     *
     * @param id 项目Id
     * @return json
     */
    @RequestMapping(value = "editProject", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public String editProject(String id) {
        JSONObject projectInfo = projectInfoService.getItem(getNode(), id);

        // 白名单
        List<String> jsonArray = whitelistDirectoryService.getProjectDirectory(getNode());
        setAttribute("whitelistDirectory", jsonArray);

        setAttribute("item", projectInfo);
        // 运行模式
        JSONArray runModes = (JSONArray) JSONArray.toJSON(RunMode.values());
        setAttribute("runModes", runModes);
        //
        List<String> hashSet = projectInfoService.getAllGroup(getNode());
        if (hashSet.isEmpty()) {
            hashSet.add("默认");
        }
        setAttribute("groups", hashSet);
        //jdk
        JSONArray array = projectInfoService.getJdkList(getNode(), getRequest());
        setAttribute("jdkArray", array);
        return "node/manage/editProject";
    }


    /**
     * 保存项目
     *
     * @param id id
     * @return json
     */
    @RequestMapping(value = "saveProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @OptLog(UserOperateLogV1.OptType.SaveProject)
    @Feature(method = MethodFeature.EDIT)
    public String saveProject(String id) {
        // 防止和系统冲突
        if (StrUtil.isNotEmpty(ConfigBean.getInstance().applicationTag) && ConfigBean.getInstance().applicationTag.equalsIgnoreCase(id)) {
            return JsonMessage.getString(401, "当前项目id已经被系统占用");
        }
        return NodeForward.request(getNode(), getRequest(), NodeUrl.Manage_SaveProject).toString();

    }
    /**
     * 验证lib 暂时用情况
     *
     * @return json
     */
    @RequestMapping(value = "judge_lib.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveProject() {
        return NodeForward.request(getNode(), getRequest(), NodeUrl.Manage_Jude_Lib).toString();
    }
}
