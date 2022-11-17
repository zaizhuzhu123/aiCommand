package com.laien.aiCommand.thread;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.constant.TaskConstant;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.laien.aiCommand.config.AppliacationInfo.initEnvironment;

@Component
@Slf4j
public class InstallDreamEnvThread extends Thread {

    @Resource
    private List<InstallDreamBoothStep> steps;


    @Override
    public void run() {
        while (!AppliacationInfo.isInitStableDiffusionSuccess) {
            try {
                initEnvironment.setStatus(TaskConstant.TASK_STATUS_PROCESS);
                for (InstallDreamBoothStep step : steps) {
                    step.run(null);
                }
                AppliacationInfo.isInitStableDiffusionSuccess = true;
                initEnvironment.setStatus(TaskConstant.TASK_STATUS_FINISH);
                initEnvironment.setRemainingFinishTime(0L);
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
