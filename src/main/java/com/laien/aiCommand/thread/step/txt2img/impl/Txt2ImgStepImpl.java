package com.laien.aiCommand.thread.step.txt2img.impl;

import com.laien.aiCommand.request.AiTaskAddRequest;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.txt2img.Txt2ImgStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Order(2)
public class Txt2ImgStepImpl implements Txt2ImgStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTaskAddRequest aiTaskAddRequest) throws IOException, InterruptedException {
        File ldmDir = new File("/workspace/Dreambooth-Stable-Diffusion/scripts/ldm");
        if (!ldmDir.exists()) {
            commandExecutor.execResult(3600, TimeUnit.SECONDS, "cp -r /workspace/Dreambooth-Stable-Diffusion/ldm /workspace/Dreambooth-Stable-Diffusion/scripts/");
        }

        StringBuffer cmd = new StringBuffer();
        cmd.append("python /workspace/Dreambooth-Stable-Diffusion/scripts/stable_txt2img.py ");
        cmd.append("--seed 10  ");
        cmd.append("--ddim_eta 0.0 ");
        cmd.append("--config /workspace/Dreambooth-Stable-Diffusion/configs/stable-diffusion/v1-inference.yaml ");
        cmd.append("--n_samples 1 ");
        cmd.append("--n_iter 8 ");
        cmd.append("--scale 10.0 ");
        cmd.append("--ddim_steps 50 ");
        cmd.append("--ckpt /workspace/logs/Marcos_Images2022-11-17T04-01-14_\\\"marcos\\\"/checkpoints/last.ckpt ");
        cmd.append("--prompt marcos,anime ");
        commandExecutor.execResult(3600, TimeUnit.SECONDS, cmd.toString(), new CommandExecutor.CommondListener() {
            @Override
            public void onStdout(String str) {
                log.info(str);
            }

            @Override
            public void onExit(int exitCode) {
                log.info("exitCode:" + exitCode);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
