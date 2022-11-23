package com.laien.aiCommand.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.constant.TaskConstant;
import com.laien.aiCommand.request.GenerateRequest;
import com.laien.aiCommand.request.TrainingGenerateRequest;
import com.laien.aiCommand.service.IProcessStepService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.laien.aiCommand.config.AppliacationInfo.initEnvironment;
import static com.laien.aiCommand.constant.TaskConstant.*;

@Data
@ApiModel(value = "AiTask对象")
public class AiTask {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @JsonIgnore
//    @ApiModelProperty(value = "请求信息")
    private GenerateRequest requestData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "任务开始时间")
    private Date taskCreateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "计划完成时间")
    private Date planCompletionTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "实际完成时间")
    private Date realCompletionTime;

    @ApiModelProperty(value = "任务状态,0 准备中,1 进行中,2 已完成,3 失败")
    private Integer status;

    @ApiModelProperty(value = "任务流程")
    private List<AiTaskStep> steps = Lists.newArrayList();

    @ApiModelProperty(value = "生成的图片URl")
    private List<String> generateImgUrls = Lists.newArrayList();

    private AiTask(GenerateRequest requestData, List<AiTaskStep> steps) {
        this.taskId = requestData.getTaskId();
        this.requestData = requestData;
        this.steps = steps;
        this.taskCreateTime = new Date(System.currentTimeMillis());
        this.setStatus(TASK_STATUS_PROCESS);

        IProcessStepService processStepService = AppliacationInfo.applicationContext.getBean(IProcessStepService.class);
        for (AiTaskStep step : this.steps) {
            step.setProcessSteps(processStepService.getProcessSteps(step.getStepName()));
        }

        if (requestData.getDdim_steps() == null) {
            requestData.setDdim_steps(50);
        }
    }

    public static AiTask buildTrainingAndgenerate(TrainingGenerateRequest requestData) {

        List<AiTaskStep> steps = Lists.newArrayList();
        //初始化环境
        steps.add(initEnvironment);

        //训练模型
        AiTaskStep traingCkpt = new AiTaskStep();
        traingCkpt.setStepName(TASK_STEP_TYPE_TRAING);
        fillTrainCost(requestData, traingCkpt);
        traingCkpt.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(traingCkpt);

        //生成图片
        AiTaskStep txt2Img = new AiTaskStep();
        txt2Img.setStepName(TASK_STEP_TYPE_GENERATE);
        txt2Img.setRemainingFinishTime(300L);
        txt2Img.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(txt2Img);

        //上传图片
        AiTaskStep upFiles = new AiTaskStep();
        upFiles.setStepName(TASK_STEP_TYPE_UPLOADIMG);
        fillUploadTime(requestData, upFiles);
        upFiles.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(upFiles);

        AiTask aiTask = new AiTask(requestData, steps);
        return aiTask;
    }

    private static void fillTrainCost(TrainingGenerateRequest requestData, AiTaskStep traingCkpt) {
        Integer max_training_steps = requestData.getMax_training_steps();
        if (max_training_steps == null) {
            max_training_steps = 500;
            requestData.setMax_training_steps(max_training_steps);
        }
        long planCostSeconds = 1200;
        if (max_training_steps >= 100) {
            planCostSeconds = (long) (((max_training_steps / (100 * 1.0d)) * 154) + (60 * 3));
        }
        traingCkpt.setRemainingFinishTime(planCostSeconds);
    }

    public static AiTask buildGenerate(GenerateRequest requestData) {
        List<AiTaskStep> steps = Lists.newArrayList();
        //初始化环境
        steps.add(initEnvironment);

        //训练模型
        AiTaskStep traingCkpt = new AiTaskStep();
        traingCkpt.setStepName(TASK_STEP_TYPE_TRAING);
        traingCkpt.setRemainingFinishTime(0L);
        traingCkpt.setStatus(TASK_STATUS_FINISH);
        steps.add(traingCkpt);

        //生成图片
        AiTaskStep txt2Img = new AiTaskStep();
        txt2Img.setStepName(TASK_STEP_TYPE_GENERATE);
        txt2Img.setRemainingFinishTime(300L);
        txt2Img.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(txt2Img);

        //上传图片
        AiTaskStep upFiles = new AiTaskStep();
        upFiles.setStepName(TASK_STEP_TYPE_UPLOADIMG);
        fillUploadTime(requestData, upFiles);
        upFiles.setStatus(TaskConstant.TASK_STATUS_WAIT);
        steps.add(upFiles);

        AiTask aiTask = new AiTask(requestData, steps);

        return aiTask;
    }

    private static void fillUploadTime(GenerateRequest requestData, AiTaskStep upFiles) {
        Integer n_iter = requestData.getN_iter();
        if (n_iter == null) {
            n_iter = 8;
            requestData.setN_iter(n_iter);
        }
        upFiles.setRemainingFinishTime((long) (n_iter * 10) + 60L);
    }

    public Date getPlanCompletionTime() {
        long totalSeconds = 0L;
        Date oldTime = planCompletionTime;
        for (AiTaskStep step : steps) {
            totalSeconds += step.getRemainingFinishTime();
        }
        long newTime = System.currentTimeMillis() + (totalSeconds * 1000);
        //预留3分钟 提升体验
        newTime += (60 * 3);
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
