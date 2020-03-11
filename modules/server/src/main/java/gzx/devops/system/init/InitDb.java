package gzx.devops.system.init;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.setting.Setting;
import cn.jiangzeyin.common.DefaultSystemLog;
import cn.jiangzeyin.common.PreLoadClass;
import cn.jiangzeyin.common.PreLoadMethod;
import gzx.devops.common.JpomManifest;
import gzx.devops.system.db.DbConfig;

import java.io.InputStream;


/**
 * 初始化数据库
 */
@PreLoadClass
public class InitDb {

    @PreLoadMethod
    private static void init() {
        Setting setting = new Setting();
        setting.set("url", DbConfig.getInstance().getDbUrl());
        setting.set("user", "gongzx");
        setting.set("pass", "gongzx");
        // 调试模式显示sql 信息
        if (JpomManifest.getInstance().isDebug()) {
            setting.set("showSql", "true");
            setting.set("sqlLevel", "INFO");
            setting.set("showParams", "true");
        }
        DefaultSystemLog.getLog().info("初始化数据中....");
        try {
            // 创建连接
            DSFactory dsFactory = DSFactory.create(setting);
            //todo esourceUtil.getStream("classpath:/bin/h2-db-v1.sql");
            InputStream inputStream = ResourceUtil.getStream("classpath:/bin/h2-db-v1.sql");
            String sql = IoUtil.read(inputStream, CharsetUtil.CHARSET_UTF_8);
            Db.use(dsFactory.getDataSource()).execute(sql);
            DSFactory.setCurrentDSFactory(dsFactory);
        } catch (Exception e) {
            DefaultSystemLog.getLog().error("初始化数据库失败", e);
            System.exit(0);
        }
    }
}
