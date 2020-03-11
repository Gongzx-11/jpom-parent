package gzx.devops;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.sun.tools.attach.VirtualMachine;
import gzx.devops.util.JvmUtil;
import gzx.devops.util.StringUtil;
import gzx.devops.util.CommandUtil;

import java.io.IOException;

/**
 * 命令行关闭
 *
 */
public class DevOpsClose {
    private static DevOpsClose Manager;

    public void main(String[] args) throws Exception {
//        todo StringUtil.getArgsValue(args, "jpom.applicationTag");
        String tag = StringUtil.getArgsValue(args, "jpom.applicationTag");
        if (StrUtil.isEmpty(tag)) {
            return;
        }
        // 事件
        String event = StringUtil.getArgsValue(args, "event");
        if ("stop".equalsIgnoreCase(event)) {
            String status = DevOpsClose.getInstance().status(tag);
            if (!status.contains(StrUtil.COLON)) {
                Console.error("系统并没有运行");
            } else {
                String msg = DevOpsClose.getInstance().stop(tag);
                Console.log(msg);
            }
            System.exit(0);
        } else if ("status".equalsIgnoreCase(event)) {
            String status = DevOpsClose.getInstance().status(tag);
            Console.log(status);
            System.exit(0);
        }
    }

    /**
     * 单利模式
     */
    public static DevOpsClose getInstance() {
        if (Manager != null) {
            return Manager;
        }
        if (SystemUtil.getOsInfo().isLinux()) {
            Manager = new Linux();
        } else {
            Manager = new Windows();
        }
        return Manager;
    }


    public String stop(String tag) throws IOException {
        VirtualMachine virtualMachine = JvmUtil.getVirtualMachine(tag);
        if (virtualMachine == null) {
            return null;
        }
        try {
            return virtualMachine.id();
        } finally {
            virtualMachine.detach();
        }
    }

    public String status(String tag) throws IOException {
        VirtualMachine virtualMachine = JvmUtil.getVirtualMachine(tag);
        if (virtualMachine == null) {
            return "系统并没有运行";
        }
        try {
            return "系统运行中:" + virtualMachine.id();
        } finally {
            virtualMachine.detach();
        }
    }


    private static class Windows extends DevOpsClose {

        @Override
        public String stop(String tag) throws IOException {
            String pid = super.stop(tag);
            if (pid == null) {
                return "stop";
            }
            String cmd = String.format("taskkill /F /PID %s", pid);
            return CommandUtil.execSystemCommand(cmd);
        }
    }

    private static class Linux extends DevOpsClose {

        @Override
        public String stop(String tag) throws IOException {
            String pid = super.stop(tag);
            if (pid == null) {
                return "stop";
            }
            String cmd = String.format("kill  %s", pid);
            return CommandUtil.execSystemCommand(cmd);
        }
    }
}
