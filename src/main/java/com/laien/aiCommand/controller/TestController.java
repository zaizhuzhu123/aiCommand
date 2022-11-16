package com.laien.aiCommand.controller;

import com.laien.aiCommand.AiCommandJavaApplication;
import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.controller.base.ResponseController;
import com.laien.aiCommand.controller.base.ResponseResult;
import com.laien.aiCommand.request.AiTaskAddRequest;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
@RestController
@Api(tags = "AI接口")
@RequestMapping("/ai")
public class TestController extends ResponseController {

    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public ResponseResult<String> test() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        System.out.println("Local HostAddress: " + addr.getHostAddress());
        String hostname = addr.getHostName();
//        System.out.println("Local host name: " + hostname);
        try {
            System.out.println(MacUtils.getLocalHostMacAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return succ("Hello World!," + addr.getHostAddress() + "," + hostname + ",taskid=" + AppliacationInfo.currentTaskId);
    }

    @Resource
    private List<DreamBoothTrainStep> dreamBoothTrainSteps;

    @PostMapping("/add")
    @ApiOperation(value = "测试")
    public ResponseResult<String> add(AiTaskAddRequest aiTaskAddRequest) throws IOException, InterruptedException {
        if (CollectionUtils.isNotEmpty(dreamBoothTrainSteps)) {
            for (DreamBoothTrainStep dreamBoothTrainStep : dreamBoothTrainSteps) {
                dreamBoothTrainStep.run(aiTaskAddRequest);
            }
        }
        return succ("Hello World!");
    }
}
