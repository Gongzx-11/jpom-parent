package gzx.devops.system.init;

import cn.hutool.core.util.StrUtil;
import cn.jiangzeyin.common.DefaultSystemLog;
import cn.jiangzeyin.common.PreLoadClass;
import cn.jiangzeyin.common.PreLoadMethod;
import gzx.devops.system.JpomRuntimeException;
import gzx.devops.system.ServerExtConfigBean;
import gzx.devops.util.CheckPassword;

/**
 * 验证token 合法性
 */
@PreLoadClass
public class CheckAuthorizeToken {

    @PreLoadMethod
    private static void check() {
        String authorizeToken = ServerExtConfigBean.getInstance().getAuthorizeToken();
        if (StrUtil.isEmpty(authorizeToken)) {
            return;
        }
        if (authorizeToken.length() < 6) {
            DefaultSystemLog.getLog().error("", new JpomRuntimeException("配置的授权token长度小于六位不生效"));
            System.exit(-1);
        }
        int password = CheckPassword.checkPassword(authorizeToken);
        if (password != 2) {
            DefaultSystemLog.getLog().error("", new JpomRuntimeException("配置的授权token 需要包含数字，字母，符号的组合"));
            System.exit(-1);
        }
    }
}
