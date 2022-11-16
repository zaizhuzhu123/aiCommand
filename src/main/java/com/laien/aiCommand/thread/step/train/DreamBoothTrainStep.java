package com.laien.aiCommand.thread.step.train;

import com.laien.aiCommand.request.AiTaskAddRequest;

import java.io.IOException;

public interface DreamBoothTrainStep {

    public void run(AiTaskAddRequest aiTaskAddRequest) throws IOException, InterruptedException;
}
