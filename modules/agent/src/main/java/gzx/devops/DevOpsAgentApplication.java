package gzx.devops;

import cn.jiangzeyin.common.EnableCommonBoot;
import gzx.devops.common.Type;
import gzx.devops.common.interceptor.AuthorizeInterceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 *启动类
 */
@SpringBootApplication
@ServletComponentScan
@EnableCommonBoot
public class DevOpsAgentApplication {

    /**
     * 启动执行
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        DevOpsApplication devOpsApplication = new DevOpsApplication(Type.Agent, DevOpsAgentApplication.class, args);
        devOpsApplication
                // 拦截器
                .addInterceptor(AuthorizeInterceptor.class)
                .run(args);
    }

}
