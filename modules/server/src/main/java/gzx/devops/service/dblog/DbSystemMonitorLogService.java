package gzx.devops.service.dblog;

import cn.hutool.db.Entity;
import cn.hutool.db.PageResult;
import gzx.devops.model.log.SystemMonitorLog;
import org.springframework.stereotype.Service;

@Service
public class DbSystemMonitorLogService extends BaseDbLogService<SystemMonitorLog> {

    public DbSystemMonitorLogService() {
        super(SystemMonitorLog.TABLE_NAME, SystemMonitorLog.class);
        setKey("id");
    }

    public PageResult<SystemMonitorLog> getMonitorData(long startTime, long endTime) {
        Entity entity = new Entity(SystemMonitorLog.TABLE_NAME);
        entity.set(" MONITORTIME", ">= " + startTime);
        entity.set("MONITORTIME", "<= " + endTime);
        return listPage(entity, null);
    }
}
