package gzx.devops.permission;

import gzx.devops.common.BaseServerController;
import gzx.devops.plugin.ClassFeature;
import gzx.devops.plugin.MethodFeature;
import gzx.devops.service.build.BuildService;
import gzx.devops.service.node.NodeService;
import gzx.devops.service.node.OutGivingServer;
import gzx.devops.service.node.manage.ProjectInfoService;
import gzx.devops.service.node.script.ScriptServer;
import gzx.devops.service.node.ssh.SshService;
import gzx.devops.service.node.tomcat.TomcatService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态数据权限
 */
public class DynamicData {

    private static final Map<ClassFeature, DynamicData> DYNAMIC_DATA_MAP = new HashMap<>();

    /**
     * 二级数据
     */
    private static final Map<ClassFeature, Set<ClassFeature>> PARENT = new HashMap<>();

    static {
        //
        put(ClassFeature.NODE, new DynamicData(NodeService.class, "id", BaseServerController.NODE_ID, MethodFeature.LIST));
        //
//        put(ClassFeature.OUTGIVING, new DynamicData(OutGivingServer.class, MethodFeature.LIST));
        //
        put(ClassFeature.PROJECT, new DynamicData(ProjectInfoService.class, MethodFeature.LIST));
        //
        put(ClassFeature.SCRIPT, new DynamicData(ScriptServer.class, MethodFeature.LIST));
        //
        put(ClassFeature.SSH, new DynamicData(SshService.class, MethodFeature.LIST));
        //
        put(ClassFeature.TOMCAT, new DynamicData(TomcatService.class, MethodFeature.LIST));
        //
//        put(ClassFeature.BUILD, new DynamicData(BuildService.class, MethodFeature.LIST));
    }

    private static void put(ClassFeature feature, DynamicData dynamicData) {
        DYNAMIC_DATA_MAP.put(feature, dynamicData);
        if (feature.getParent() != null) {
            Set<ClassFeature> classFeatures = PARENT.computeIfAbsent(feature.getParent(), classFeature -> new HashSet<>());
            classFeatures.add(feature);
        }
    }

    /**
     * 获取一级功能
     *
     * @return 子级
     */
    public static List<ClassFeature> getRoot() {
        return DYNAMIC_DATA_MAP.keySet().stream().filter(dynamicData -> dynamicData.getParent() == null).collect(Collectors.toList());
    }

    /**
     * 获取子级功能
     *
     * @param classFeature 功能
     * @return 子级
     */
    public static Set<ClassFeature> getChildren(ClassFeature classFeature) {
        return PARENT.get(classFeature);
    }

    public static Map<ClassFeature, DynamicData> getDynamicDataMap() {
        return DYNAMIC_DATA_MAP;
    }

    public static DynamicData getDynamicData(ClassFeature classFeature) {
        return DYNAMIC_DATA_MAP.get(classFeature);
    }

    /**
     * id 请求参数
     */
    private String parameterName;

    /**
     * 数据操作的server类
     */
    private Class<? extends BaseDynamicService> baseOperService;

    /**
     * 排除验证方法
     */
    private MethodFeature[] excludeMethod;

    private String childrenParameterName;

    private DynamicData(Class<? extends BaseDynamicService> baseOperService, MethodFeature... excludeMethod) {
        this(baseOperService, "id", excludeMethod);
    }

    private DynamicData(Class<? extends BaseDynamicService> baseOperService, String parameterName, MethodFeature... excludeMethod) {
        this(baseOperService, parameterName, null, excludeMethod);
    }

    private DynamicData(Class<? extends BaseDynamicService> baseOperService, String parameterName, String childrenParameterName, MethodFeature... excludeMethod) {
        this.parameterName = parameterName;
        this.baseOperService = baseOperService;
        this.excludeMethod = excludeMethod;
        this.childrenParameterName = childrenParameterName;
    }

    public String getChildrenParameterName() {
        return childrenParameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<? extends BaseDynamicService> getBaseOperService() {
        return baseOperService;
    }

    public MethodFeature[] getExcludeMethod() {
        return excludeMethod;
    }
}
