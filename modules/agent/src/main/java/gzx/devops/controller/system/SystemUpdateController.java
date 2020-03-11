package gzx.devops.controller.system;

import cn.hutool.http.HttpStatus;
import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.controller.multipart.MultipartFileBuilder;
import gzx.devops.DevOpsAgentApplication;
import gzx.devops.DevOpsApplication;
import gzx.devops.common.BaseAgentController;
import gzx.devops.system.AgentConfigBean;
import gzx.devops.common.JpomManifest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

/**
 * 在线升级
 */
@RestController
@RequestMapping(value = "system")
public class SystemUpdateController extends BaseAgentController {

    @RequestMapping(value = "uploadJar.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String uploadJar() throws IOException {
        //
        Objects.requireNonNull(JpomManifest.getScriptFile());
        MultipartFileBuilder multipartFileBuilder = createMultipart();
        multipartFileBuilder
                .setFileExt("jar")
                .addFieldName("file")
                .setUseOriginalFilename(true)
                .setSavePath(AgentConfigBean.getInstance().getTempPath().getAbsolutePath());
        String path = multipartFileBuilder.save();
        // 基础检查

        JsonMessage error = JpomManifest.checkJpomJar(path, DevOpsAgentApplication.class);
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
