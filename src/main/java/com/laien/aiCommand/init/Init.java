package com.laien.aiCommand.init;

import com.laien.aiCommand.thread.InstallDreamEnvThread;
import com.laien.aiCommand.thread.SendHeartBeatThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
@Slf4j
/**
 * spring容器实例初始化完成后需要执行的操作
 */
public class Init {

    @Resource
    private SendHeartBeatThread sendHeartBeatThread;

    @Resource
    private InstallDreamEnvThread installStableDiffusionEnvThread;

    @PostConstruct
    public void run() {
        //调用心跳接口 将任务id汇报给程序1 代表程序2启动成功
        sendHeartBeatThread.start();
        //初始化计算环境
        installStableDiffusionEnvThread.start();
    }

//    @Override
//    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
//        //调用心跳接口 将任务id汇报给程序1 代表程序2启动成功
//        sendHeartBeatThread.start();
//        //初始化计算环境
//        installStableDiffusionEnvThread.start();
//    }
}
