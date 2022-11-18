package com.laien.aiCommand.service.impl;

import com.google.common.collect.Maps;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.service.IAiTaskService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static com.laien.aiCommand.constant.TaskConstant.TASK_STATUS_FINISH;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STATUS_PROCESS;

@Component
public class AiTaskServiceImpl implements IAiTaskService {

    private Map<String, AiTask> allAiTasks = Maps.newHashMap();


    private LinkedBlockingQueue<AiTask> waitProcessAiTasks = new LinkedBlockingQueue<>(50);

    @Override
    public void addTask(AiTask aiTask) {
        if (allAiTasks.containsKey(aiTask.getTaskId())) {
            throw new RuntimeException("Task id " + aiTask.getTaskId() + "already exists");
        }
        waitProcessAiTasks.offer(aiTask);
        allAiTasks.put(aiTask.getTaskId(), aiTask);
    }

    @Override
    public AiTask getTask(String taskId) {
        return allAiTasks.get(taskId);
    }

    @Override
    public AiTask getNextTask() throws InterruptedException {
        return waitProcessAiTasks.take();
    }

    @Override
    public void setStepFinish(AiTaskStep taskStep) {
        taskStep.setStatus(TASK_STATUS_FINISH);
        taskStep.setRemainingFinishTime(0L);
    }

    @Override
    public void setStepProcess(AiTaskStep taskStep) {
        taskStep.setStatus(TASK_STATUS_PROCESS);
    }

    @Override
    public void setTaskFinish(AiTask aiTask) {
        aiTask.setStatus(TASK_STATUS_FINISH);
        aiTask.setRealCompletionTime(new Date());

    }


}
