package com.laien.aiCommand.thread.step.txt2img;

import com.laien.aiCommand.request.AiTaskAddRequest;

import java.io.IOException;

public interface Txt2ImgStep {

    public void run(AiTaskAddRequest aiTaskAddRequest) throws IOException, InterruptedException;

}
