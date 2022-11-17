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

import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_INITENO;

@Component
@Order(5)
@Slf4j
public class DownloadRegularizationStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTask aiTask) throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        String cmd = "sh /workspace/aiCommand/target/soft/shell/downloadRegularization.sh";
        commandExecutor.execResult(300, TimeUnit.SECONDS, cmd);
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_INITENO;
    }
}
