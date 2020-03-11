package gzx.devops.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import cn.jiangzeyin.common.JsonMessage;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.BaseAgentController;
import gzx.devops.common.interceptor.NotAuthorize;
import gzx.devops.model.data.ProjectInfoModel;
import gzx.devops.service.WhitelistDirectoryService;
import gzx.devops.service.manage.ProjectInfoService;
import gzx.devops.util.JvmUtil;
import gzx.devops.common.JpomManifest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.List;

/**
 * 首页
 */
@RestController
public class IndexController extends BaseAgentController {
    @Resource
    private WhitelistDirectoryService whitelistDirectoryService;
    @Resource
    private ProjectInfoService projectInfoService;

    @RequestMapping(value = {"index", "", "index.html", "/"}, produces = MediaType.TEXT_PLAIN_VALUE)
    @NotAuthorize
    public String index() {
        return "DevOps-Agent";
    }

    @RequestMapping(value = "info", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String info() {
        int code;
        if (whitelistDirectoryService.isInstalled()) {
            code = 200;
        } else {
            code = 201;
        }
        return JsonMessage.getString(code, "", JpomManifest.getInstance());
    }

    /**
     * 返回节点项目状态信息
     *
     * @return array
     */
    @RequestMapping(value = "status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String status() {
        List<ProjectInfoModel> projectInfoModels = projectInfoService.list();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("javaVirtualCount", JvmUtil.getJavaVirtualCount());
        jsonObject.put("osName", SystemUtil.getOsInfo().getName());
        jsonObject.put("DevOpsVersion", JpomManifest.getInstance().getVersion());
        jsonObject.put("javaVersion", SystemUtil.getJavaRuntimeInfo().getVersion());
        //todo 操作系统级内存情况查询
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        //  获取JVM中内存总大小
        long totalMemory = SystemUtil.getTotalMemory();
        // 获取操作系统总物理内存
        long physicalTotal  = osmxb.getTotalPhysicalMemorySize();
        jsonObject.put("totalMemory", FileUtil.readableFileSize(totalMemory));
        //
        long freeMemory = SystemUtil.getFreeMemory();
        jsonObject.put("freeMemory", FileUtil.readableFileSize(freeMemory));
        int count = 0;
        if (projectInfoModels != null) {
            count = projectInfoModels.size();
        }
        jsonObject.put("count", count);
        // 运行时间
        jsonObject.put("runTime", JpomManifest.getInstance().getUpTime());
        return JsonMessage.getString(200, "", jsonObject);
    }
}
