package com.laien.aiCommand.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "任务创建请求", description = "任务创建请求")
public class AiTaskAddRequest {

    private List<String> firebaseImgs;

    private Map<String, String> trainParams;
}
