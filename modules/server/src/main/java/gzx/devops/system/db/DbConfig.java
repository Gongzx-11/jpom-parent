package gzx.devops.system.db;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import cn.hutool.db.sql.Direction;
import cn.hutool.db.sql.Order;
import cn.jiangzeyin.common.DefaultSystemLog;
import gzx.devops.DevOpsApplication;
import gzx.devops.system.ExtConfigBean;
import gzx.devops.system.ServerExtConfigBean;

import java.io.File;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * 数据库配置
 */
public class DbConfig {

    private static final String DB = "db";

    private static DbConfig dbConfig;

    /**
     * 单利模式
     *
     * @return config
     */
    public static DbConfig getInstance() {
        if (dbConfig == null) {
            dbConfig = new DbConfig();
        }
        return dbConfig;
    }

    /**
     * 获取数据库的jdbc 连接
     *
     * @return jdbc
     */
    public String getDbUrl() {
        File file = FileUtil.file(ExtConfigBean.getInstance().getAbsolutePath(), DB, DevOpsApplication.getAppType().name());
        String path = FileUtil.getAbsolutePath(file);
        return StrUtil.format("jdbc:h2:{}", path);
    }

    /**
     * 清除超限制数量的数据
     *
     * @param tableName 表名
     * @param timeClo   时间字段名
     */
    public static void autoClear(String tableName, String timeClo) {
        autoClear(tableName, timeClo, ServerExtConfigBean.getInstance().getH2DbLogStorageCount(), time -> {
            Entity entity = Entity.create(tableName);
            entity.set(timeClo, "< " + time);
            int count = 0;
            try {
                count = Db.use().setWrapper((Character) null).del(entity);
            } catch (SQLException e) {
                DefaultSystemLog.getLog().error("清理数据异常", e);
            }
            DefaultSystemLog.getLog().info("{} 清理了 {}条数据", tableName, count);
        });
    }

    /**
     * 自动清理数据接口
     *
     * @param tableName 表名
     * @param timeClo   时间字段
     * @param maxCount  最大数量
     * @param consumer  查询出超过范围的时间回调
     */
    public static void autoClear(String tableName, String timeClo, int maxCount, Consumer<Long> consumer) {
        if (ServerExtConfigBean.getInstance().getH2DbLogStorageCount() <= 0) {
            return;
        }
        ThreadUtil.execute(() -> {
            Entity entity = Entity.create(tableName);
            Page page = new Page(maxCount, 1);
            page.addOrder(new Order(timeClo, Direction.DESC));
            PageResult<Entity> pageResult;
            try {
                pageResult = Db.use().setWrapper((Character) null).page(entity, page);
                if (pageResult.isEmpty()) {
                    return;
                }
                Entity entity1 = pageResult.get(0);
                long time = Convert.toLong(entity1.get(timeClo.toUpperCase()), 0L);
                if (time <= 0) {
                    return;
                }
                consumer.accept(time);
            } catch (SQLException e) {
                DefaultSystemLog.getLog().error("数据库查询异常", e);
            }
        });
    }
}
