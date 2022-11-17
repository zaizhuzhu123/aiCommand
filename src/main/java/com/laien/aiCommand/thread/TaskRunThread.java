package com.laien.aiCommand.thread;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.service.IAiTaskService;
import com.laien.aiCommand.thread.step.ProcessStep;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import com.laien.aiCommand.thread.step.txt2img.Txt2ImgStep;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.laien.aiCommand.constant.TaskConstant.*;

@Component
public class TaskRunThread extends Thread {

    @Resource
    private List<DreamBoothTrainStep> dreamBoothTrainStep;

    @Resource
    private List<Txt2ImgStep> txt2ImgStep;

    @Resource
    private IAiTaskService aiTaskService;

    private Map<String, List<ProcessStep>> processes = Maps.newHashMap();

    @PostConstruct
    public void startThread() {
        this.start();
    }

    @Override
    public void run() {
        List<ProcessStep> trains = Lists.newArrayList();
        trains.addAll(dreamBoothTrainStep);
        processes.put(TASK_STEP_TYPE_TRAING, trains);
        List<ProcessStep> generate = Lists.newArrayList();
        generate.addAll(txt2ImgStep);
        processes.put(TASK_STEP_TYPE_GENERATE, generate);
        while (true) {
            try {
                AiTask nextTask = aiTaskService.getNextTask();
                if (nextTask != null) {
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
            if (step.getStatus().intValue() == TASK_STATUS_WAIT) {
                List<ProcessStep> waitProcessSteps = processes.get(step.getStepName());
                if (CollectionUtils.isEmpty(waitProcessSteps)) {
                    return false;
                }
                for (ProcessStep waitProcessStep : waitProcessSteps) {
                    waitProcessStep.run(aiTask);
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
