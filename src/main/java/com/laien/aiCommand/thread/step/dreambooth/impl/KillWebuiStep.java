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
        String cmd = "ps -aux\n";
        String result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
        log.info(result);
        String[] split = result.split("\n");
        for (String s : split) {
            log.info(s);
        }
    }
}
