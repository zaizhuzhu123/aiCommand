package com.laien.aiCommand.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "任务创建请求", description = "任务创建请求")
public class AiTaskAddRequest {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "用户图片")
    private List<String> firebaseImgs;

    @ApiModelProperty(value = "训练参数")
    private Map<String, String> trainParams;
}
