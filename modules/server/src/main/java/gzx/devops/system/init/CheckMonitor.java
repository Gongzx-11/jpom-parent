package gzx.devops.system.init;

import cn.jiangzeyin.common.DefaultSystemLog;
import cn.jiangzeyin.common.PreLoadClass;
import cn.jiangzeyin.common.PreLoadMethod;
import cn.jiangzeyin.common.spring.SpringUtil;
import gzx.devops.service.monitor.MonitorService;
import gzx.devops.service.node.NodeService;


@PreLoadClass
public class CheckMonitor {

    @PreLoadMethod
    private static void init() {
        MonitorService monitorService = SpringUtil.getBean(MonitorService.class);
        boolean status = monitorService.checkCronStatus();
        if (status) {
            DefaultSystemLog.getLog().info("已经开启监听调度：监控");
        }
        //
        NodeService nodeService = SpringUtil.getBean(NodeService.class);
        status = nodeService.checkCronStatus();
        if (status) {
            DefaultSystemLog.getLog().info("已经开启监听调度：节点信息采集");
        }
    }
}
