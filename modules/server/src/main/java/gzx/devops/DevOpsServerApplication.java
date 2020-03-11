package gzx.devops;

import cn.jiangzeyin.common.EnableCommonBoot;
import cn.jiangzeyin.common.spring.event.ApplicationEventLoad;
import gzx.devops.common.Type;
import gzx.devops.common.interceptor.LoginInterceptor;
import gzx.devops.common.interceptor.OpenApiInterceptor;
import gzx.devops.common.interceptor.PermissionInterceptor;
import gzx.devops.permission.CacheControllerFeature;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 *  启动类
 *
 */
@SpringBootApplication
@ServletComponentScan
@EnableCommonBoot
public class DevOpsServerApplication implements ApplicationEventLoad {


    /**
     * 启动执行
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        DevOpsApplication devOpsApplication = new DevOpsApplication(Type.Server, DevOpsServerApplication.class, args);
        devOpsApplication
                // 拦截器
                .addInterceptor(LoginInterceptor.class)
                .addInterceptor(OpenApiInterceptor.class)
                .addInterceptor(PermissionInterceptor.class)
                .run(args);
    }


    @Override
    public void applicationLoad() {
        CacheControllerFeature.init();
    }
}
