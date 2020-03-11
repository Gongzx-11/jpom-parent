package gzx.devops.monitor;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.task.Task;
import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.common.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.model.Cycle;
import gzx.devops.model.data.NodeModel;
import gzx.devops.model.log.SystemMonitorLog;
import gzx.devops.util.CronUtils;
import gzx.devops.service.dblog.DbSystemMonitorLogService;
import gzx.devops.service.node.NodeService;

import java.util.List;

/**
 * 节点监控
 *
 */
public class NodeMonitor implements Task {

    private static final String CRON_ID = "NodeMonitor";

    private static DbSystemMonitorLogService dbSystemMonitorLogService;

    /**
     * 开启调度
     */
    public static void start() {
        Task task = CronUtil.getScheduler().getTask(CRON_ID);
        if (task == null) {
            CronPattern cronPattern = Cycle.seconds30.getCronPattern();
            CronUtil.schedule(CRON_ID, cronPattern.toString(), new NodeMonitor());
            CronUtils.start();
        }
        dbSystemMonitorLogService = SpringUtil.getBean(DbSystemMonitorLogService.class);
    }

    public static void stop() {
        CronUtil.remove(CRON_ID);
    }

    @Override
    public void execute() {
        long time = System.currentTimeMillis();
        NodeService nodeService = SpringUtil.getBean(NodeService.class);
        //
        List<NodeModel> nodeModels = nodeService.listByCycle(Cycle.seconds30);
        //
        if (Cycle.one.getCronPattern().match(time, CronUtil.getScheduler().isMatchSecond())) {
            nodeModels.addAll(nodeService.listByCycle(Cycle.one));
        }
        //
        if (Cycle.five.getCronPattern().match(time, CronUtil.getScheduler().isMatchSecond())) {
            nodeModels.addAll(nodeService.listByCycle(Cycle.five));
        }
        //
        if (Cycle.ten.getCronPattern().match(time, CronUtil.getScheduler().isMatchSecond())) {
            nodeModels.addAll(nodeService.listByCycle(Cycle.ten));
        }
        //
        if (Cycle.thirty.getCronPattern().match(time, CronUtil.getScheduler().isMatchSecond())) {
            nodeModels.addAll(nodeService.listByCycle(Cycle.thirty));
        }
        //
        this.checkList(nodeModels);
    }

    private void checkList(List<NodeModel> nodeModels) {
        if (nodeModels == null || nodeModels.isEmpty()) {
            return;
        }
        nodeModels.forEach(nodeModel -> {
            ThreadUtil.execute(() -> getNodeInfo(nodeModel));
        });
    }

    private void getNodeInfo(NodeModel nodeModel) {
        JsonMessage message = NodeForward.request(nodeModel, null, NodeUrl.GetDirectTop);
        JSONObject jsonObject = (JSONObject) message.getData();
        if (jsonObject == null) {
            return;
        }
        double disk = jsonObject.getDoubleValue("disk");
        if (disk <= 0) {
            return;
        }
        //
        SystemMonitorLog log = new SystemMonitorLog();
        log.setId(IdUtil.fastSimpleUUID());
        log.setOccupyMemory(jsonObject.getDoubleValue("memory"));
        log.setOccupyDisk(disk);
        log.setOccupyCpu(jsonObject.getDoubleValue("cpu"));
        log.setMonitorTime(jsonObject.getLongValue("time"));
        log.setNodeId(nodeModel.getId());
        dbSystemMonitorLogService.insert(log);
    }
}
