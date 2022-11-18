package com.laien.aiCommand.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "训练生成", description = "训练生成")
public class TrainingGenerateRequest extends GenerateRequest {


    @ApiModelProperty(value = "用户图片")
    private List<String> firebaseImgs;

    @ApiModelProperty(value = "traing:Unique token you want to represent your trained model. Ex: firstNameLastName.")
    private String token;

    @ApiModelProperty(value = "traing:Number of training steps to run,默认500,脚本配置最大支持800")
    private Integer max_training_steps;


//    @ApiModelProperty(value = "训练参数")
//    private Map<String, String> trainParams;
}
