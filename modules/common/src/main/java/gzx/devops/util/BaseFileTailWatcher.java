package gzx.devops.util;

import cn.hutool.core.util.StrUtil;
import cn.jiangzeyin.common.DefaultSystemLog;
import gzx.devops.system.JpomRuntimeException;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 文件跟随器工具
 */
public abstract class BaseFileTailWatcher<T> {

    protected FileTailWatcherRun tailWatcherRun;
    protected File logFile;

    /**
     * 所有会话
     */
    protected final Set<T> socketSessions = new HashSet<>();

    public BaseFileTailWatcher(File logFile) throws IOException {
        this.logFile = logFile;
        this.tailWatcherRun = new FileTailWatcherRun(logFile, this::sendAll);
    }

    protected void send(T session, String msg) {
        try {
            if (session instanceof Session) {
                SocketSessionUtil.send((Session) session, msg);
            } else if (session instanceof WebSocketSession) {
                SocketSessionUtil.send((WebSocketSession) session, msg);
            } else {
                // todo JpomRuntimeException("没有对应类型");
                throw new JpomRuntimeException("没有对应类型");
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * 有新的日志
     *
     * @param msg 日志
     */
    private void sendAll(String msg) {
        Iterator<T> iterator = socketSessions.iterator();
        while (iterator.hasNext()) {
            T socketSession = iterator.next();
            try {
                this.send(socketSession, msg);
            } catch (Exception e) {
                DefaultSystemLog.getLog().error("发送消息失败", e);
                iterator.remove();
            }
        }
        if (this.socketSessions.isEmpty()) {
            this.close();
        }
    }

    /**
     * 添加监听会话
     *
     * @param session 会话
     */
    protected void add(T session, String name) {
        if (this.socketSessions.contains(session) || this.socketSessions.add(session)) {
            LimitQueue<String> limitQueue = this.tailWatcherRun.getLimitQueue();
            if (limitQueue.size() <= 0) {
                this.send(session, "日志文件为空");
                return;
            }
            this.send(session, StrUtil.format("******", name, this.socketSessions.size()));
            // 开发发送头信息
            for (String s : limitQueue) {
                this.send(session, s);
            }
        }
        //        else {
        //            this.send(session, "添加日志监听失败");
        //        }
    }

    /**
     * 关闭
     */
    protected void close() {
        this.tailWatcherRun.close();
    }
}
