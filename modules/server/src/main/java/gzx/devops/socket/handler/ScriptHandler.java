package gzx.devops.socket.handler;

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
 * 脚本模板消息控制器
 */
public class ScriptHandler extends BaseProxyHandler {

    public ScriptHandler() {
        super(NodeUrl.Script_Run, "scriptId");
    }

    @Override
    protected void handleTextMessage(Map<String, Object> attributes, ProxySession proxySession, JSONObject json, ConsoleCommandOp consoleCommandOp) {
        UserOperateLogV1.OptType type = null;
        switch (consoleCommandOp) {
            case stop:
                type = UserOperateLogV1.OptType.Script_Stop;
                break;
            case start:
                type = UserOperateLogV1.OptType.Script_Start;
                break;
            default:
                break;
        }
        if (type != null) {
            // 记录操作日志
            UserModel userInfo = (UserModel) attributes.get("userInfo");
            //
            String scriptId = (String) attributes.get("scriptId");
            OperateLogController.CacheInfo cacheInfo = cacheInfo(attributes, json, type, scriptId);
            try {
                operateLogController.log(userInfo, "脚本模板执行...", cacheInfo);
            } catch (Exception e) {
                DefaultSystemLog.getLog().error("记录操作日志异常", e);
            }
        }
        proxySession.send(json.toString());
    }
}
