package com.laien.aiCommand.thread.step.train.impl;

import com.laien.aiCommand.request.AiTaskAddRequest;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Order(2)
public class TrainImg2CkptStep implements DreamBoothTrainStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTaskAddRequest aiTaskAddRequest) throws IOException, InterruptedException {

    }
}
