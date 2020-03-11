package gzx.devops.controller.openapi.build;

import cn.hutool.core.util.StrUtil;
import cn.jiangzeyin.common.JsonMessage;
import gzx.devops.common.ServerOpenApi;
import gzx.devops.common.interceptor.NotLogin;
import gzx.devops.model.data.BuildModel;
import gzx.devops.model.data.UserModel;
import gzx.devops.service.build.BuildService;
import gzx.devops.service.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@NotLogin
public class BuildTriggerApiController {

    @Resource
    private BuildService buildService;

    @Resource
    private UserService userService;

    @RequestMapping(value = ServerOpenApi.BUILD_TRIGGER_BUILD, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String install(@PathVariable String id, @PathVariable String token) {
        BuildModel item = buildService.getItem(id);
        if (item == null) {
            return JsonMessage.getString(404, "没有对应数据");
        }
        List<UserModel> list = userService.list(false);
        Optional<UserModel> first = list.stream().filter(UserModel::isSystemUser).findFirst();
        if (!first.isPresent()) {
            return JsonMessage.getString(404, "没有对应数据");
        }
        UserModel userModel = first.get();
        if (!StrUtil.equals(token, item.getTriggerToken())) {
            return JsonMessage.getString(404, "触发token错误");
        }
        return buildService.start(userModel, id);
    }
}
