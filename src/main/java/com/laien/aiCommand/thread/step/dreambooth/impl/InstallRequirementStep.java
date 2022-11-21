package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.laien.aiCommand.config.AppliacationInfo.dreamboothPath;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_INITENO;

@Component
@Order(2)
@Slf4j
public class InstallRequirementStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        //等待一分钟 防止出现pip not found
        Thread.sleep(60 * 1000);
        String cmd = "sh /workspace/aiCommand/target/soft/shell/installRequirement.sh";
        commandExecutor.execResult(3600, TimeUnit.SECONDS, cmd, new CommandExecutor.CommondListener() {
            @Override
            public void onStdout(String str) {
                log.info(str);
            }

            @Override
            public void onExit(int exitCode) {

            }

            @Override
            public void onError(Exception exception) {

            }
        }, new File(dreamboothPath));
        currentStep.setRemainingFinishTime(currentStep.getRemainingFinishTime() - 300);
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_INITENO;
    }
}
