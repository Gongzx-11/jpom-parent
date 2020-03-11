package gzx.devops.controller.node.tomcat;

import gzx.devops.common.BaseServerController;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.Feature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gongzx
 * @date 2019/11/28
 */
@Controller
@RequestMapping(value = TomcatManageController.TOMCAT_URL)
@Feature(cls = ClassFeature.TOMCAT)
public class TomcatProjectController extends BaseServerController {
}
