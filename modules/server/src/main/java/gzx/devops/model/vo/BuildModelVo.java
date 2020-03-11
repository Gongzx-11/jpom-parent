package gzx.devops.model.vo;

import gzx.devops.build.BuildUtil;
import gzx.devops.model.data.BuildModel;

import java.io.File;

/**
 * vo
 */
public class BuildModelVo extends BuildModel {

    /**
     * 代码是否存在
     */
    private boolean sourceExist;

    public boolean isSourceExist() {
        File source = BuildUtil.getSource(this);
        return source.exists();
    }

    public void setSourceExist(boolean sourceExist) {
        this.sourceExist = sourceExist;
    }
}
