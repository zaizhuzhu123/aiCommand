package com.laien.aiCommand.thread.step;

import com.laien.aiCommand.entity.AiTask;

import java.io.IOException;

public interface ProcessStep {

    public void run(AiTask aiTask) throws IOException, InterruptedException;

    public String type();
}
