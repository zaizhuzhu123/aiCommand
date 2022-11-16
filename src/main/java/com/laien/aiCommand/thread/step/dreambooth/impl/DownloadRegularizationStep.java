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
        String cmd, result;
//        cmd = "git clone https://github.com/djbielejeski/Stable-Diffusion-Regularization-Images-person_ddim.git /workspace/Dreambooth-Stable-Diffusion/Stable-Diffusion-Regularization-Images-person_ddim";
//        result = commandExecutor.execResult(180, TimeUnit.SECONDS, cmd);
//        log.info(result);
//        cmd = "mkdir -p /workspace/Dreambooth-Stable-Diffusion/regularization_images/person_ddim";
//        result = commandExecutor.execResult(10, TimeUnit.SECONDS, cmd);
//        log.info(result);
        cmd = "mv -v /workspace/Dreambooth-Stable-Diffusion/Stable-Diffusion-Regularization-Images-person_ddim/person_ddim/*.* /workspace/Dreambooth-Stable-Diffusion/regularization_images/person_ddim";
        result = commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
        log.info(result);
    }
}
