package com.laien.aiCommand.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "任务创建请求", description = "任务创建请求")
public class AiTaskAddRequest {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "用户图片")
    private List<String> firebaseImgs;

    @ApiModelProperty(value = "traing:Unique token you want to represent your trained model. Ex: firstNameLastName.")
    private String token;

    @ApiModelProperty(value = "traing:Number of training steps to run,默认500,脚本配置最大支持800")
    private Integer max_training_steps;

    @ApiModelProperty(value = "generate:the prompt to rende")
    private String prompt;

    @ApiModelProperty(value = "generate:生成的图片数量,默认为8")
    private Integer n_iter;

    @ApiModelProperty(value = "generate:number of ddim sampling steps,默认50")
    private Integer ddim_steps;

//    @ApiModelProperty(value = "训练参数")
//    private Map<String, String> trainParams;
}
