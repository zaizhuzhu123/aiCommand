package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
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
@Order(4)
@Slf4j
public class DownloadCkptStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        String cmd = "wget https://n1ckey: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@huggingface.co/CompVis/stable-diffusion-v-1-4-original/resolve/main/sd-v1-4-full-ema.ckpt -P " + dreamboothPath;
        String result = commandExecutor.execResult(300, TimeUnit.SECONDS, cmd);
        log.info(result);
        cmd = "mv " + dreamboothPath + "/sd-v1-4-full-ema.ckpt " + dreamboothPath + "/model.ckpt";
        result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
        log.info(result);
        currentStep.setRemainingFinishTime(currentStep.getRemainingFinishTime() - 300);
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_INITENO;
    }
}
