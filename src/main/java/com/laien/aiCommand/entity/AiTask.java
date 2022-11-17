package com.laien.aiCommand.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.laien.aiCommand.constant.TaskConstant;
import com.laien.aiCommand.request.AiTaskAddRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.laien.aiCommand.config.AppliacationInfo.initEnvironment;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_GENERATE;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_TRAING;

@Data
@ApiModel(value = "AiTask对象")
public class AiTask {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "请求信息")
    private AiTaskAddRequest requestData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "计划完成时间")
    private Date planCompletionTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "任务开始时间")
    private Date taskCreateTime;

    @ApiModelProperty(value = "任务状态,0 准备中,1 进行中,2 已完成,3 失败")
    private Integer status;

    @ApiModelProperty(value = "任务流程")
    private List<AiTaskStep> steps = Lists.newArrayList();

    private AiTask(AiTaskAddRequest requestData, List<AiTaskStep> steps) {
        this.taskId = requestData.getTaskId();
        this.requestData = requestData;
        this.steps = steps;
        taskCreateTime = new Date(System.currentTimeMillis());
    }


    public static AiTask buildTrainingAndgenerate(AiTaskAddRequest requestData) {
        List<AiTaskStep> steps = Lists.newArrayList();
        //初始化环境
        steps.add(initEnvironment);

        //训练模型
        AiTaskStep traingCkpt = new AiTaskStep();
        traingCkpt.setStepName(TASK_STEP_TYPE_TRAING);
        traingCkpt.setRemainingFinishTime(3600L);
        traingCkpt.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(traingCkpt);

        //生成图片
        AiTaskStep txt2Img = new AiTaskStep();
        txt2Img.setStepName(TASK_STEP_TYPE_GENERATE);
        txt2Img.setRemainingFinishTime(3600L);
        txt2Img.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(txt2Img);

        AiTask aiTask = new AiTask(requestData, steps);
        return aiTask;
    }

    public static AiTask buildGenerate(AiTaskAddRequest requestData) {
        List<AiTaskStep> steps = Lists.newArrayList();
        //初始化环境
        steps.add(initEnvironment);

        //训练模型
        AiTaskStep traingCkpt = new AiTaskStep();
        traingCkpt.setStepName(TASK_STEP_TYPE_TRAING);
        traingCkpt.setRemainingFinishTime(0L);
        traingCkpt.setStatus(TaskConstant.TASK_STATUS_FINISH);
        steps.add(traingCkpt);

        //生成图片
        AiTaskStep txt2Img = new AiTaskStep();
        txt2Img.setStepName(TASK_STEP_TYPE_GENERATE);
        txt2Img.setRemainingFinishTime(3600L);
        txt2Img.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(txt2Img);

        AiTask aiTask = new AiTask(requestData, steps);

        return aiTask;
    }

    public Date getPlanCompletionTime() {
        long totalSeconds = 0L;
        Date oldTime = planCompletionTime;
        for (AiTaskStep step : steps) {
            totalSeconds += step.getRemainingFinishTime();
        }
        long newTime = System.currentTimeMillis() + (totalSeconds * 1000);
        if (oldTime != null) {
            long oldTimeStamp = oldTime.getTime();
            if (newTime < oldTimeStamp) {
                planCompletionTime = new Date(newTime);
            } else {
                planCompletionTime = new Date(oldTimeStamp);
            }
        } else {
            planCompletionTime = new Date(newTime);
        }
        return planCompletionTime;
    }
}
