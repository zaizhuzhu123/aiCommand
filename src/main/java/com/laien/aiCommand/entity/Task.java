package com.laien.aiCommand.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.laien.aiCommand.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 压缩任务表
 * </p>
 *
 * @author xsd
 * @since 2022-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("media_compression_task")
@ApiModel(value = "Task对象", description = "压缩任务表")
public class Task extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务名称 约定传 oog001、cms之类的 项目名称")
    private String serviceName;

    @ApiModelProperty(value = "调用方业务id")
    private String serviceId;

    @ApiModelProperty(value = "源文件路径")
    private String sourceFilePath;

    @ApiModelProperty(value = "媒体文件类型 1 video 2 music 3 sound")
    private Integer sourceFileType;

    @ApiModelProperty(value = "媒体文件 contentType")
    private String sourceFileContentType;

    @ApiModelProperty(value = "上传firebase所需参数")
    private String firebaseJson;

    @ApiModelProperty(value = "storage_bucket")
    private String storageBucket;

    @ApiModelProperty(value = "database_url")
    private String databaseUrl;

    @ApiModelProperty(value = "上传目录")
    private String dirName;

    @ApiModelProperty(value = "压缩后文件url")
    private String compressionFileUrl;

    @ApiModelProperty(value = "压缩状态 0等待压缩 1压缩中 2压缩成功 3压缩失败")
    private Integer compressionStatus;

    @ApiModelProperty(value = "上报状态 0未上报 1已上报")
    private Integer uploadResultStatus;

    @ApiModelProperty(value = "开始压缩时间")
    private LocalDateTime compressionStartTime;

    @ApiModelProperty(value = "压缩结束时间")
    private LocalDateTime compressionFinishTime;

    @ApiModelProperty(value = "压缩失败原因")
    private String compressionFailReason;

    @ApiModelProperty(value = "回调地址")
    private String callbackUrl;

    @ApiModelProperty(value = "远程ip")
    private String remoteIp;

    @ApiModelProperty(value = "文件大小 单位M")
    private Integer fileSize;


}
