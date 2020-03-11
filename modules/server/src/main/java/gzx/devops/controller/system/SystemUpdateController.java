package gzx.devops.controller.system;

import cn.hutool.http.HttpStatus;
import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.controller.multipart.MultipartFileBuilder;
import gzx.devops.DevOpsApplication;
import gzx.devops.DevOpsServerApplication;
import gzx.devops.common.BaseServerController;
import gzx.devops.common.JpomManifest;
import gzx.devops.common.forward.NodeForward;
import gzx.devops.common.forward.NodeUrl;
import gzx.devops.common.interceptor.OptLog;
import gzx.devops.model.data.NodeModel;
import gzx.devops.model.log.UserOperateLogV1;
import gzx.devops.system.ServerConfigBean;
import gzx.devops.permission.SystemPermission;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Objects;

/**
 * 在线升级
 *
 */
@Controller
@RequestMapping(value = "system")
public class SystemUpdateController extends BaseServerController {

    @RequestMapping(value = "update.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @SystemPermission
    public String update() {
        return "system/update";
    }

    @RequestMapping(value = "info", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @SystemPermission
    public String info() {
        NodeModel nodeModel = tryGetNode();
        if (nodeModel != null) {
            return NodeForward.request(getNode(), getRequest(), NodeUrl.Info).toString();
        }
        return JsonMessage.getString(200, "", JpomManifest.getInstance());
    }

    @RequestMapping(value = "uploadJar.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @OptLog(UserOperateLogV1.OptType.UpdateSys)
    @SystemPermission
    public String uploadJar() throws IOException {
        NodeModel nodeModel = tryGetNode();
        if (nodeModel != null) {
            return NodeForward.requestMultipart(getNode(), getMultiRequest(), NodeUrl.SystemUploadJar).toString();
        }
        //
        Objects.requireNonNull(JpomManifest.getScriptFile());
        MultipartFileBuilder multipartFileBuilder = createMultipart();
        multipartFileBuilder
                .setFileExt("jar")
                .addFieldName("file")
                .setUseOriginalFilename(true)
                .setSavePath(ServerConfigBean.getInstance().getUserTempPath().getAbsolutePath());
        String path = multipartFileBuilder.save();
        // 基础检查
        JsonMessage error = JpomManifest.checkJpomJar(path, DevOpsServerApplication.class);
        if (error.getCode() != HttpStatus.HTTP_OK) {
            return error.toString();
        }
        String version = error.getMsg();
        JpomManifest.releaseJar(path, version);
        //
        DevOpsApplication.restart();
        return JsonMessage.getString(200, "升级中大约需要30秒");
    }
}
