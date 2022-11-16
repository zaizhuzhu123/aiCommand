package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(6)
@Slf4j
public class KillWebuiStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run() throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        //杀死relauncher
        killProcess("relauncher");
        //杀死webui
        killProcess("webui");
    }

    private void killProcess(String machStr) throws IOException, InterruptedException {
        String cmd = "ps -aux | grep " + machStr;
        String result = commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
        String[] strings = result.split(" ");
        String pid = strings[1];
        cmd = "kill -9 " + pid;
        commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
    }
}
