package gzx.devops.controller.node.tomcat;

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

/**
 * @author gongzx
 * @date 2019/11/28
 */
@Controller
@RequestMapping(value = TomcatManageController.TOMCAT_URL)
@Feature(cls = ClassFeature.TOMCAT)
public class TomcatLogController extends BaseServerController {

    /**
     * tomcat 日志管理
     *
     * @param id tomcat id
     * @return 项目管理面
     */
    @RequestMapping(value = "console", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Feature(method = MethodFeature.LOG)
    public String console(String id) {
        setAttribute("id", id);
        return "node/tomcat/console";
    }

    @RequestMapping(value = "getLogList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Feature(method = MethodFeature.LOG)
    public String getLogList() {
        return NodeForward.request(getNode(), getRequest(), NodeUrl.Tomcat_LOG_List).toString();
    }
}
