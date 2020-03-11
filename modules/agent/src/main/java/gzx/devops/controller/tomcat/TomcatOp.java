package gzx.devops.controller.tomcat;

/**
 * tomcat操作
 *
 * @author gongzx
 * @date 2019/12/7
 **/
public enum TomcatOp {
    /**
     * 启动
     */
    start,
    stop,
    reload,
    /**
     * 删除发布
     */
    undeploy
}
