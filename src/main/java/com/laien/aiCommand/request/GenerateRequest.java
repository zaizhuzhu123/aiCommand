package com.laien.aiCommand.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "生成", description = "生成")
public class GenerateRequest {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "generate:the prompt to rende")
    private String prompt;

    @ApiModelProperty(value = "generate:生成的图片数量,默认为8")
    private Integer n_iter;

    @ApiModelProperty(value = "generate:number of ddim sampling steps,默认50")
    private Integer ddim_steps;

//    @ApiModelProperty(value = "训练参数")
//    private Map<String, String> trainParams;
}
