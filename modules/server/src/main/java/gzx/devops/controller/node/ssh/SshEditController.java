package gzx.devops.controller.node.ssh;

import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.common.validator.ValidatorItem;
import cn.jiangzeyin.common.validator.ValidatorRule;
import gzx.devops.common.BaseServerController;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import gzx.devops.plugin.MethodFeature;
import gzx.devops.service.node.ssh.SshService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "node/ssh")
@Feature(cls = ClassFeature.SSH)
public class SshEditController extends BaseServerController {

    @Resource
    private SshService sshService;

    @RequestMapping(value = "del.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Feature(method = MethodFeature.DEL)
    public String del(@ValidatorItem(value = ValidatorRule.NOT_BLANK) String id) {
        sshService.deleteItem(id);
        return JsonMessage.getString(200, "操作成功");
    }
}
