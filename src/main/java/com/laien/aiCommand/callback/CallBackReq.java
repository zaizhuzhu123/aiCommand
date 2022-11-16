package com.laien.aiCommand.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "通知回调请求", description = "通知回调请求")
public class CallBackReq {
    @ApiModelProperty(value = "调用方业务id", required = true)
    private String serviceId;

    @ApiModelProperty(value = "压缩结果 1成功 0 失败", required = true)
    private Integer result;

    @ApiModelProperty(value = "失败原因")
    private String failReason;

    @ApiModelProperty(value = "压缩后文件url")
    private String compressionFileUrl;

    @ApiModelProperty(value = "总耗时，单位分钟")
    private int cost;
}
