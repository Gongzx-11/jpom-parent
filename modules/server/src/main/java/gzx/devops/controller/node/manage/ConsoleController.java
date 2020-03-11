package gzx.devops.controller.node.manage;

import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.BaseServerController;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import gzx.devops.plugin.MethodFeature;
import gzx.devops.service.node.manage.ProjectInfoService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * 控制台
 */
@Controller
@RequestMapping(value = "/node/manage/")
@Feature(cls = ClassFeature.PROJECT)
public class ConsoleController extends BaseServerController {

    @Resource
    private ProjectInfoService projectInfoService;

    /**
     * 管理项目
     *
     * @param id id
     * @return page
     */
    @RequestMapping(value = "console", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public String console(String id) {
        JSONObject projectInfoModel = projectInfoService.getItem(getNode(), id);
        if (projectInfoModel != null) {
            setAttribute("projectInfo", projectInfoModel);
            JSONObject logSize = projectInfoService.getLogSize(getNode(), id);
            setAttribute("logSize", logSize);
            setAttribute("manager", true);
        }
        return "node/manage/console";
    }
}
