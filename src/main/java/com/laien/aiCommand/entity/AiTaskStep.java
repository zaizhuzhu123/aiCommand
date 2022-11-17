package com.laien.aiCommand.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AiTaskStep对象，任务步骤")
public class AiTaskStep {

    @ApiModelProperty(value = "步骤名称")
    private String stepName;

    @ApiModelProperty(value = "步骤状态,1 进行中,2 已完成,3 失败")
    private Integer status;

    @ApiModelProperty(value = "剩余完成时间，单位秒")
    private Long remainingFinishTime;

}
