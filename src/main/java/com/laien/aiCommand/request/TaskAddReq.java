//package com.laien.media_compression.request;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//@Data
//@ApiModel(value = "任务创建请求", description = "任务创建请求")
//public class TaskAddReq {
//
//    @ApiModelProperty(value = "调用方业务id", required = true)
//    private String serviceId;
//
//    @ApiModelProperty(value = "媒体文件类型,1 video,2 music", required = true)
//    private Integer fileType;
//
//    @ApiModelProperty(value = "上传firebase所需参数,主要指.json文件中的内容", required = true)
//    private String firebaseJson;
//
//    @ApiModelProperty(value = "上传结果回调通知地址", required = true)
//    private String callbackUrl;
//
//    @ApiModelProperty(value = "storage_bucket", required = true)
//    private String storageBucket;
//
//    @ApiModelProperty(value = "database_url", required = true)
//    private String databaseUrl;
//
//    @ApiModelProperty(value = "上传目录", required = true)
//    private String dirName;
//
//}
