package gzx.devops.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.jiangzeyin.controller.base.AbstractController;

/**
 * controller
 */
public abstract class BaseJpomController extends AbstractController {
    /**
     * 路径安全格式化
     *
     * @param path 路径
     * @return 去掉 提权字符串
     */
    public static String pathSafe(String path) {
        if (path == null) {
            return null;
        }
        String newPath = path.replace("../", StrUtil.EMPTY);
        newPath = newPath.replace("..\\", StrUtil.EMPTY);
        newPath = newPath.replace("+", StrUtil.EMPTY);
        return FileUtil.normalize(newPath);
    }

    protected boolean checkPathSafe(String path) {
        if (path == null) {
            return false;
        }
        String newPath = path.replace("../", StrUtil.EMPTY);
        newPath = newPath.replace("..\\", StrUtil.EMPTY);
        newPath = newPath.replace("+", StrUtil.EMPTY);
        return newPath.equals(path);
    }
}
