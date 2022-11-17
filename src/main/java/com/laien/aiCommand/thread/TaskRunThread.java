package com.laien.aiCommand.thread;

import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.service.IAiTaskService;
import com.laien.aiCommand.thread.step.ProcessStep;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.laien.aiCommand.config.AppliacationInfo.initEnvironment;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STATUS_FINISH;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STATUS_WAIT;

@Component
@Slf4j
public class TaskRunThread extends Thread {


    @Resource
    private IAiTaskService aiTaskService;


    @PostConstruct
    public void startThread() {
        this.start();
    }

    @Override
    public void run() {
        log.info("任务现成启动");
        while (true) {
            try {
                AiTask nextTask = aiTaskService.getNextTask();
                if (nextTask != null) {
                    log.info("得到任务" + nextTask.getTaskId());
                    //必须前一个任务完成 才执行下一个任务
                    while (true) {
                        try {
                            boolean isFinish = runSteps(nextTask);
                            if (isFinish) {
                                aiTaskService.setTaskFinish(nextTask);
                            } else {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Thread.sleep(30 * 1000);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean runSteps(AiTask aiTask) throws IOException, InterruptedException {
        List<AiTaskStep> steps = aiTask.getSteps();
        int lastStepsIndex = steps.size() - 1;
        for (int i = 0; i < steps.size(); i++) {
            AiTaskStep step = steps.get(i);
            String stepName = step.getStepName();
            log.info(stepName);
            if (step.getStatus().intValue() == TASK_STATUS_FINISH) {
                continue;
            }
            if (step.getStatus().intValue() == TASK_STATUS_WAIT) {
                List<ProcessStep> waitProcessSteps = step.getProcessSteps();
                if (CollectionUtils.isEmpty(waitProcessSteps) || step == initEnvironment) {
                    return false;
                }
                for (ProcessStep waitProcessStep : waitProcessSteps) {
                    log.info(stepName + "-" + waitProcessStep.getClass().getSimpleName());
                    waitProcessStep.run(aiTask, step);
                }
                aiTaskService.setStepFinish(step);
                if (i != lastStepsIndex) {
                    return runSteps(aiTask);
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
