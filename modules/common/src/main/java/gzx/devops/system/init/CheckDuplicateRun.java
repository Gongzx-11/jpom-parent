package gzx.devops.system.init;

import cn.jiangzeyin.common.DefaultSystemLog;
import gzx.devops.DevOpsApplication;
import gzx.devops.common.JpomManifest;
import gzx.devops.util.JvmUtil;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.VmIdentifier;

import java.util.List;

class CheckDuplicateRun {

    static void check() {
        try {
            Class appClass = DevOpsApplication.getAppClass();
            String pid = String.valueOf(JpomManifest.getInstance().getPid());
            List<MonitoredVm> monitoredVms = JvmUtil.listMainClass(appClass.getName());
            monitoredVms.forEach(monitoredVm -> {
                VmIdentifier vmIdentifier = monitoredVm.getVmIdentifier();
                if (pid.equals(vmIdentifier.getUserInfo())) {
                    return;
                }
                DefaultSystemLog.getLog().info("建议一个机器上只运行一个对应的程序：" + DevOpsApplication.getAppType() + "  pid:" + vmIdentifier.getUserInfo());
            });
        } catch (Exception e) {
            DefaultSystemLog.getLog().error("检查异常", e);
        }
    }
}

