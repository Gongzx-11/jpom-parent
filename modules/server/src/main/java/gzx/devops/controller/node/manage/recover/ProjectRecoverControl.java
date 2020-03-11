package gzx.devops.controller.node.manage.recover;

import gzx.devops.common.BaseServerController;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import gzx.devops.plugin.MethodFeature;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * 项目管理
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/node/manage/recover")
@Feature(cls = ClassFeature.PROJECT_RECOVER)
public class ProjectRecoverControl extends BaseServerController {

    /**
     * 展示项目页面
     *
     * @return page
     */
    @RequestMapping(value = "list.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Feature(method = MethodFeature.LIST)
    public String projectInfo() {
        List list = NodeForward.requestData(getNode(), NodeUrl.Manage_Recover_List_Data, getRequest(), List.class);
        setAttribute("array", list);
        return "node/manage/project_recover";
    }

    @RequestMapping(value = "data.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Feature(method = MethodFeature.LIST)
    public String project() throws IOException {
        return NodeForward.request(getNode(), getRequest(), NodeUrl.Manage_Recover_Item_Data).toString();
    }

}
