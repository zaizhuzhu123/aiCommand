package com.laien.aiCommand.thread;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class InstallDreamEnvThread extends Thread {

    @Resource
    private List<InstallDreamBoothStep> steps;


    @Override
    public void run() {
        while (!AppliacationInfo.isInitStableDiffusionSuccess) {
            try {
                for (InstallDreamBoothStep step : steps) {
                    step.run();
                }
                AppliacationInfo.isInitStableDiffusionSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
