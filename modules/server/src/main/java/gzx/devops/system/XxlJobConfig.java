//package gzx.devops.system;
//
//import  com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
//import  org.slf4j.Logger;
//import  org.slf4j.LoggerFactory;
//import  org.springframework.beans.factory.annotation.Value;
//import  org.springframework.context.annotation.Bean;
//import  org.springframework.context.annotation.ComponentScan;
//import  org.springframework.context.annotation.Configuration;
//
///**
// *  加载核心配置
// *
// *  @author  gongzx
// */
//@Configuration
////注意本次更改新增如下注解，扫描指定包类，地址为各自项目job地址包路径，请归纳在一个包里面，此处配合修复改造项目有几率不触发。
//@ComponentScan(basePackages  =  "gzx.devops.util")
//
//public  class  XxlJobConfig  {
//    private  Logger  logger  =  LoggerFactory.getLogger(XxlJobConfig.class);
//
//    @Value("${xxl.job.admin.addresses}")
//    private  String  adminAddresses;
//
//    @Value("${xxl.job.executor.appname}")
//    private  String  appName;
//
//    @Value("${xxl.job.executor.ip}")
//    private  String  ip;
//
//    @Value("${xxl.job.executor.port}")
//    private  int  port;
//
//    @Value("${xxl.job.accessToken}")
//    private  String  accessToken;
//
//    @Value("${xxl.job.executor.logpath}")
//    private  String  logPath;
//
//    @Value("${xxl.job.executor.logretentiondays}")
//    private  int  logRetentionDays;
//
//
//    @Bean(initMethod  =  "start",  destroyMethod  =  "destroy")
//    public XxlJobSpringExecutor xxlJobExecutor() {
//        logger.info(">>>>>>>>>>> xxl-job config init.");
//        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
//        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
//        xxlJobSpringExecutor.setAppName(appName);
//        xxlJobSpringExecutor.setIp(ip);
//        xxlJobSpringExecutor.setPort(port);
//        xxlJobSpringExecutor.setAccessToken(accessToken);
//        xxlJobSpringExecutor.setLogPath(logPath);
//        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
//
//        return xxlJobSpringExecutor;
//    }
//
//}
