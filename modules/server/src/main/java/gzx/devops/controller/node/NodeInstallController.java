package gzx.devops.controller.node;

import cn.jiangzeyin.common.JsonMessage;
import gzx.devops.common.BaseServerController;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.common.interceptor.OptLog;
import gzx.devops.model.log.UserOperateLogV1;
import gzx.devops.service.system.WhitelistDirectoryService;
import gzx.devops.permission.SystemPermission;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务器初始化
 */
@RestController
@RequestMapping(value = "/node")
public class NodeInstallController extends BaseServerController {

    @Resource
    private WhitelistDirectoryService whitelistDirectoryService;

    @RequestMapping(value = "install_node.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @OptLog(UserOperateLogV1.OptType.InstallNode)
    @SystemPermission
    public String installNode() {
        List<String> list = whitelistDirectoryService.getProjectDirectory(getNode());
        if (list != null && !list.isEmpty()) {
            return JsonMessage.getString(402, "服务器节点已经初始化过啦");
        }
        return NodeForward.request(getNode(), getRequest(), NodeUrl.WhitelistDirectory_Submit).toString();
    }
}
