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
@Order(4)
@Slf4j
public class DownloadCkptStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run() throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        String cmd = "cd /workspace/Dreambooth-Stable-Diffusion\n";
        String result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
        log.info(result);
        cmd = "wget https://n1ckey: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@huggingface.co/CompVis/stable-diffusion-v-1-4-original/resolve/main/sd-v1-4.ckpt\n";
        result = commandExecutor.execResult(300, TimeUnit.SECONDS, cmd);
        log.info(result);
        cmd = "mv sd-v1-4.ckpt model.ckpt\n";
        result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
        log.info(result);
    }
}
