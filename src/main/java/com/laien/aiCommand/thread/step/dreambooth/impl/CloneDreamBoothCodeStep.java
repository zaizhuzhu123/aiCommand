package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.laien.aiCommand.config.AppliacationInfo.dreamboothPath;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_INITENO;

@Component
@Order(1)
@Slf4j
public class CloneDreamBoothCodeStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTask aiTask) throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        String cmd = "rm -rf " + dreamboothPath;
        commandExecutor.execResult(60, TimeUnit.SECONDS, cmd);
        cmd = "git clone https://github.com/zaizhuzhu123/Dreambooth-Stable-Diffusion.git " + dreamboothPath;
        commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_INITENO;
    }
}
