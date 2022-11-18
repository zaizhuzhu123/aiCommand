package com.laien.aiCommand.controller;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.controller.base.ResponseController;
import com.laien.aiCommand.controller.base.ResponseResult;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.request.GenerateRequest;
import com.laien.aiCommand.request.TrainingGenerateRequest;
import com.laien.aiCommand.service.IAiTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@RestController
@Api(tags = "AI接口")
@RequestMapping("/ai")
@Slf4j
public class TestController extends ResponseController {

    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public ResponseResult<String> test() {
        return succ("Hello World!,machineID=" + AppliacationInfo.machineId);
    }

    @Resource
    private IAiTaskService aiTaskService;

    @PostMapping("/trainingAndgenerate")
    @ApiOperation(value = "训练加生成")
    public ResponseResult<AiTask> trainingAndgenerate(TrainingGenerateRequest trainingGenerateRequest) throws IOException, InterruptedException {
        AiTask aiTask = AiTask.buildTrainingAndgenerate(trainingGenerateRequest);
        try {
            aiTaskService.addTask(aiTask);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        return succ(aiTask);
    }

    @PostMapping("/generateBylastTraing")
    @ApiOperation(value = "基于上一次的训练结果重新生成")
    public ResponseResult<AiTask> generateBylastTraing(GenerateRequest generateRequest) throws IOException, InterruptedException {
        AiTask aiTask = AiTask.buildGenerate(generateRequest);
        try {
            aiTaskService.addTask(aiTask);
        } catch (Exception e) {
            return fail(e.getMessage());
        }
        return succ(aiTask);
    }

    @GetMapping("/progress")
    @ApiOperation(value = "获取进度")
    public ResponseResult<AiTask> progress(String taskId) throws IOException, InterruptedException {
        return succ(aiTaskService.getTask(taskId));
    }
}
