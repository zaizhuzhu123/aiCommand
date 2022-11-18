package com.laien.aiCommand.service;

import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;

/**
 * <p>
 * 压缩任务表 服务类
 * </p>
 *
 * @author xsd
 * @since 2022-06-21
 */
public interface IAiTaskService {

    void addTask(AiTask aiTask);

    AiTask getTask(String taskId);

    AiTask getNextTask() throws InterruptedException;

    void setStepFinish(AiTaskStep taskStep);

    void setStepProcess(AiTaskStep taskStep);

    void setTaskFinish(AiTask aiTask);

}
