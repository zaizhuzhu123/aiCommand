package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.google.common.collect.Lists;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Order(2)
@Slf4j
public class InstallRequirementStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run() throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        List<String> commandList = Lists.newArrayList();
        commandList.add("pip install omegaconf\n");
        commandList.add("pip install einops\n");
        commandList.add("pip install pytorch-lightning==1.6.5\n");
        commandList.add("pip install test-tube\n");
        commandList.add("pip install transformers\n");
        commandList.add("pip install kornia\n");
        commandList.add("pip install -e git+https://github.com/CompVis/taming-transformers.git@master#egg=taming-transformers\n");
        commandList.add("pip install -e git+https://github.com/openai/CLIP.git@main#egg=clip\n");
        commandList.add("pip install setuptools==59.5.0\n");
        commandList.add("pip install pillow==9.0.1\n");
        commandList.add("pip install torchmetrics==0.6.0\n");
        commandList.add("pip install -e .\n");
        commandList.add("pip install protobuf==3.20.1\n");
        commandList.add("pip install gdown\n");
        commandList.add("pip install -qq diffusers[“training”]==0.3.0 transformers ftfy\n");
        commandList.add("pip install -qq \"ipywidgets>=7,<8\"\n");
        commandList.add("pip install huggingface_hub\n");
        commandList.add("pip install ipywidgets==7.7.1\n");
        commandList.add("pip install captionizer==1.0.1\n");
        for (String command : commandList) {
            String result = commandExecutor.execResult(30, TimeUnit.SECONDS, command);
            log.info(result);
        }
    }
}
