package gzx.devops.socket.handler;

import cn.hutool.core.util.IdUtil;
import cn.jiangzeyin.common.DefaultSystemLog;
import com.alibaba.fastjson.JSONObject;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.model.data.UserModel;
import gzx.devops.model.log.UserOperateLogV1;
import gzx.devops.socket.BaseProxyHandler;
import gzx.devops.socket.ConsoleCommandOp;
import gzx.devops.socket.ProxySession;
import gzx.devops.system.init.OperateLogController;

import java.util.Map;

/**
 * 控制台消息处理器
 */
public class ConsoleHandler extends BaseProxyHandler {

    public ConsoleHandler() {
        super(NodeUrl.TopSocket, "projectId");
    }

    @Override
    protected void handleTextMessage(Map<String, Object> attributes, ProxySession proxySession, JSONObject json, ConsoleCommandOp consoleCommandOp) {
        UserOperateLogV1.OptType type = null;
        switch (consoleCommandOp) {
            case stop:
                type = UserOperateLogV1.OptType.Stop;
                break;
            case start:
                type = UserOperateLogV1.OptType.Start;
                break;
            case restart:
                type = UserOperateLogV1.OptType.Restart;
                break;
            default:
                break;
        }
        if (type != null) {
            // 记录操作日志
            UserModel userInfo = (UserModel) attributes.get("userInfo");
            String reqId = IdUtil.fastUUID();
            json.put("reqId", reqId);
            //
            String projectId = (String) attributes.get("projectId");
            OperateLogController.CacheInfo cacheInfo = cacheInfo(attributes, json, type, projectId);
            try {
                operateLogController.log(reqId, userInfo, "还没有响应", cacheInfo);
            } catch (Exception e) {
                DefaultSystemLog.getLog().error("记录操作日志异常", e);
            }
        }
        proxySession.send(json.toString());
    }
}
