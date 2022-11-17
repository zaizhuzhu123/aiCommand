package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
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
        StringBuffer cmd = new StringBuffer();
        cmd.append("python /workspace/Dreambooth-Stable-Diffusion/scripts/stable_txt2img.py ");
        cmd.append("--seed 10 ");
        cmd.append("--ddim_eta 0.0 ");
        cmd.append("--n_samples 1 ");
        cmd.append("--n_iter 1500 ");
        cmd.append("--scale 10.0 ");
        cmd.append("--ddim_steps 50 ");
        cmd.append("--ckpt /workspace/Dreambooth-Stable-Diffusion/model.ckpt ");
        cmd.append("--prompt person ");
        commandExecutor.execResult(10, TimeUnit.SECONDS, cmd.toString(), new CommandExecutor.CommondListener() {
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
        });
        File regularization_images_dir = new File("/workspace/Dreambooth-Stable-Diffusion/regularization_images/person");
        if(!regularization_images_dir.exists()){
            regularization_images_dir.mkdirs();
        }
        commandExecutor.execResult(10,TimeUnit.SECONDS,"mv /workspace/Dreambooth-Stable-Diffusion/outputs/txt2img-samples/*.png /workspace/Dreambooth-Stable-Diffusion/regularization_images/person");
    }
}
