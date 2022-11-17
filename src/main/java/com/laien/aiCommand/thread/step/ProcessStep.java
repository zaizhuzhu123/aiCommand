package com.laien.aiCommand.thread.step;

import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;

import java.io.IOException;

public interface ProcessStep {

    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException;

    public String type();
}
