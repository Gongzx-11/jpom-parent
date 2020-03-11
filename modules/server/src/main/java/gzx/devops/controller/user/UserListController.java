package gzx.devops.controller.user;

import cn.jiangzeyin.common.JsonMessage;
import gzx.devops.common.BaseServerController;
import gzx.devops.model.data.RoleModel;
import gzx.devops.model.data.UserModel;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import gzx.devops.plugin.MethodFeature;
import gzx.devops.service.user.RoleService;
import gzx.devops.service.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户列表
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/user")
@Feature(cls = ClassFeature.USER)
public class UserListController extends BaseServerController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    /**
     * 展示用户列表
     *
     * @return page
     */
    @RequestMapping(value = "list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Feature(method = MethodFeature.LIST)
    public String list() {
        List<RoleModel> roleModels = roleService.list();
        setAttribute("roleEmpty", roleModels == null || roleModels.isEmpty());
        return "user/list";
    }


    /**
     * 查询所有用户
     *
     * @return json
     */
    @RequestMapping(value = "getUserList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Feature(method = MethodFeature.LIST)
    public String getUserList() {
        UserModel userName = getUser();
        List<UserModel> userList = userService.list();
        if (userList != null) {
            userList = userList.stream().filter(userModel -> {
                // 不显示自己的信息
                return !userModel.getId().equals(userName.getId());
            }).collect(Collectors.toList());
        }
        return JsonMessage.getString(200, "", userList);
    }
}
