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
@Order(5)
@Slf4j
public class DownloadRegularizationStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run() throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        String cmd = "cd /workspace/Dreambooth-Stable-Diffusion\n";
        String result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
        log.info(result);
        cmd = "git clone https://github.com/djbielejeski/Stable-Diffusion-Regularization-Images-person_ddim.git\n";
        result = commandExecutor.execResult(180, TimeUnit.SECONDS, cmd);
        log.info(result);
        cmd = "mkdir -p regularization_images/person_ddim\n";
        result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
        log.info(result);
        cmd = "mv -v Stable-Diffusion-Regularization-Images-person_ddim/person_ddim/*.* regularization_images/person_ddim\n";
        result = commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
        log.info(result);
    }
}
