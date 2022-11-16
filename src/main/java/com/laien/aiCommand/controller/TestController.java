package com.laien.aiCommand.controller;

import com.laien.aiCommand.AiCommandJavaApplication;
import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.controller.base.ResponseController;
import com.laien.aiCommand.controller.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@RestController
@Api(tags = "管理端：接口测试")
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
}
