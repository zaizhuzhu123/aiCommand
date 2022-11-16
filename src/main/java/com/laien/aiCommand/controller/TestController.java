package com.laien.aiCommand.controller;

import com.laien.aiCommand.controller.base.ResponseController;
import com.laien.aiCommand.controller.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@Api(tags = "管理端：接口测试")
public class TestController extends ResponseController {

    @GetMapping("/test")
    @ApiOperation(value = "测试")
    public ResponseResult<String> test() {
        return succ("Hello World!");
    }
}
