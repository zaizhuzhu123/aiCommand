package com.laien.aiCommand.thread.step;

import java.io.IOException;

public interface ProcessStep {

    public void run() throws IOException, InterruptedException;
}
