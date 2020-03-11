package gzx.devops.controller.user.role;

import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.common.spring.SpringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.BaseServerController;
import gzx.devops.common.interceptor.OptLog;
import gzx.devops.model.data.RoleModel;
import gzx.devops.model.log.UserOperateLogV1;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import gzx.devops.plugin.MethodFeature;
import gzx.devops.service.user.RoleService;
import gzx.devops.permission.BaseDynamicService;
import gzx.devops.permission.DynamicData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user/role")
@Feature(cls = ClassFeature.USER_ROLE)
public class UserRoleDynamicController extends BaseServerController {

    @Resource
    private RoleService roleService;

    @RequestMapping(value = "dynamicData.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public String list() {
        Map<ClassFeature, DynamicData> dynamicDataMap = DynamicData.getDynamicDataMap();
        setAttribute("dynamicDataMap", dynamicDataMap);
        return "user/role/dynamicData";
    }

    @RequestMapping(value = "getDynamic.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Feature(method = MethodFeature.EDIT)
    public String getDynamic(String id, String dynamic) {
        ClassFeature classFeature = ClassFeature.valueOf(dynamic);
        JSONArray jsonArray = roleService.listDynamic(id, classFeature, null);
        return JsonMessage.getString(200, "", jsonArray);
    }

    @RequestMapping(value = "saveDynamic.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Feature(method = MethodFeature.EDIT)
    @OptLog(value = UserOperateLogV1.OptType.EditRole)
    public String saveDynamic(String id, String dynamic) {
        RoleModel item = roleService.getItem(id);
        if (item == null) {
            return JsonMessage.getString(404, "角色信息错误");
        }
        //
        JSONObject jsonObject = JSONObject.parseObject(dynamic);
        Map<ClassFeature, List<RoleModel.TreeLevel>> dynamicData1 = new HashMap<>(jsonObject.keySet().size());
        //
        List<ClassFeature> root = DynamicData.getRoot();
        for (ClassFeature classFeature : root) {
            JSONArray value = jsonObject.getJSONArray(classFeature.name());
            if (value == null || value.isEmpty()) {
                continue;
            }
            DynamicData dynamicData = DynamicData.getDynamicData(classFeature);
            if (dynamicData == null) {
                return JsonMessage.getString(404, classFeature.getName() + "没有配置对应动态数据");
            }
            Class<? extends BaseDynamicService> baseOperService = dynamicData.getBaseOperService();
            BaseDynamicService bean = SpringUtil.getBean(baseOperService);
            List<RoleModel.TreeLevel> list = bean.parserValue(classFeature, value);
            dynamicData1.put(classFeature, list);
        }
        item.setDynamicData2(dynamicData1);
        roleService.updateItem(item);
        return JsonMessage.getString(200, "保存成功");
    }
}
