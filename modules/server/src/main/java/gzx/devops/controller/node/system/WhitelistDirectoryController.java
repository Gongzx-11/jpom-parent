package gzx.devops.controller.node.system;

import gzx.devops.common.BaseServerController;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.common.interceptor.OptLog;
import gzx.devops.model.data.AgentWhitelist;
import gzx.devops.model.log.UserOperateLogV1;
import gzx.devops.service.system.WhitelistDirectoryService;
import gzx.devops.permission.SystemPermission;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 白名单目录
 */
@Controller
@RequestMapping(value = "/node/system")
public class WhitelistDirectoryController extends BaseServerController {
    @Resource
    private WhitelistDirectoryService whitelistDirectoryService;

    /**
     * 页面
     */
    @RequestMapping(value = "whitelistDirectory", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @SystemPermission
    public String whitelistDirectory() {
        AgentWhitelist agentWhitelist = whitelistDirectoryService.getData(getNode());
        if (agentWhitelist != null) {
            setAttribute("project", AgentWhitelist.convertToLine(agentWhitelist.getProject()));
            //
            setAttribute("certificate", AgentWhitelist.convertToLine(agentWhitelist.getCertificate()));
            //
            setAttribute("nginx", AgentWhitelist.convertToLine(agentWhitelist.getNginx()));
        }
        return "node/system/whitelistDirectory";
    }

    /**
     * 保存接口
     *
     * @return json
     */
    @RequestMapping(value = "whitelistDirectory_submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @OptLog(UserOperateLogV1.OptType.EditWhitelist)
    @SystemPermission
    public String whitelistDirectorySubmit() {
        return NodeForward.request(getNode(), getRequest(), NodeUrl.WhitelistDirectory_Submit).toString();
    }
}
