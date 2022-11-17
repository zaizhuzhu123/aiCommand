package com.laien.aiCommand.thread.step.train.impl;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.request.AiTaskAddRequest;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(2)
@Slf4j
public class TrainImg2CkptStep implements DreamBoothTrainStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTaskAddRequest aiTaskAddRequest) throws IOException, InterruptedException {
        File userImgDir = new File(AppliacationInfo.userImgSavePath);
        StringBuffer cmd = new StringBuffer();
        cmd.append("python \"/workspace/Dreambooth-Stable-Diffusion/main.py\" ");
        cmd.append("--base /workspace/Dreambooth-Stable-Diffusion/configs/stable-diffusion/v1-finetune_unfrozen.yaml ");
        cmd.append("-t ");
        cmd.append("--actual_resume /workspace/Dreambooth-Stable-Diffusion/model.ckpt ");
        cmd.append("--reg_data_root \"/workspace/Dreambooth-Stable-Diffusion/regularization_images/person_ddim\" ");
        cmd.append("-n \"marcos\" ");
        cmd.append("--gpus 0, ");
        cmd.append("--data_root \"/workspace/Marcos_Images\" ");
        cmd.append("--max_training_steps 500 ");
        cmd.append("--class_word \"person\" ");
        cmd.append("--token \"marcos\" ");
        cmd.append("--no-test");
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

    }
}
