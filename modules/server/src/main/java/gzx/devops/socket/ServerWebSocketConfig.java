package gzx.devops.socket;

import gzx.devops.socket.handler.ConsoleHandler;
import gzx.devops.socket.handler.ScriptHandler;
import gzx.devops.socket.handler.SshHandler;
import gzx.devops.socket.handler.TomcatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * socket 配置
 */
@Configuration
@EnableWebSocket
public class ServerWebSocketConfig implements WebSocketConfigurer {
    private final ServerWebSocketInterceptor serverWebSocketInterceptor = new ServerWebSocketInterceptor();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 控制台
        registry.addHandler(new ConsoleHandler(), "/console")
                .addInterceptors(serverWebSocketInterceptor).setAllowedOrigins("*");
        // 脚本模板
        registry.addHandler(new ScriptHandler(), "/script_run")
                .addInterceptors(serverWebSocketInterceptor).setAllowedOrigins("*");
        // tomcat
        registry.addHandler(new TomcatHandler(), "/tomcat_log")
                .addInterceptors(serverWebSocketInterceptor).setAllowedOrigins("*");
        // ssh
        registry.addHandler(new SshHandler(), "/ssh")
                .addInterceptors(serverWebSocketInterceptor).setAllowedOrigins("*");
    }
}
