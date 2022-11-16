//package com.laien.aiCommand.schedule.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Lists;
//import com.laien.aiCommand.callback.CallBackReq;
//import com.laien.aiCommand.controller.base.ResponseCode;
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.schedule.TaskResultUploader;
//import com.laien.aiCommand.service.ITaskService;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.laien.aiCommand.constant.MediaConstant.*;
//
//@Component
//@Slf4j
//public class TaskResultUploaderImpl implements TaskResultUploader {
//
//    @Autowired
//    private ITaskService taskService;
//
//    @Autowired
//    private OkHttpClient okHttpClient;
//
//    @Override
//    @Scheduled(fixedDelay = 5000)
//    public void upload() {
//        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
//        //成功和失败的都需要上报结果
//        queryWrapper.in(Task::getCompressionStatus, Lists.newArrayList(COMPRESSION_STATUS_SUCCESS, COMPRESSION_STATUS_FAIL));
//        //未上报结果的
//        queryWrapper.eq(Task::getUploadResultStatus, UPLOADRESULT_STATUS_NO);
//        //24小时内的结果如果上报失败会重复上报
//        queryWrapper.gt(Task::getCompressionFinishTime, LocalDateTime.now().minusDays(1));
//        List<Task> uploadTaskList = taskService.list(queryWrapper);
//        if (CollectionUtils.isNotEmpty(uploadTaskList)) {
//            for (Task task : uploadTaskList) {
//                try {
//                    //回调地址
//                    String callbackUrl = task.getCallbackUrl();
//                    //回调结果内容
//                    CallBackReq callBackReq = new CallBackReq();
//                    callBackReq.setCompressionFileUrl(task.getCompressionFileUrl());
//                    callBackReq.setFailReason(task.getCompressionFailReason());
//                    callBackReq.setServiceId(task.getServiceId());
//                    callBackReq.setResult(task.getCompressionStatus() == COMPRESSION_STATUS_SUCCESS ? COMPRESSION_RESULT_SUCCESS : COMPRESSION_RESULT_FAIL);
//                    Duration duration = Duration.between(task.getCompressionStartTime(), task.getCompressionFinishTime());
//                    callBackReq.setCost((int) duration.toMinutes());
//                    Response response = callback(callbackUrl, callBackReq);
//                    try {
//                        if (response.code() == ResponseCode.SUCCESS.getCode()) {
//                            String bodyStr = response.body().string();
//                            JsonNode jsonNode = new ObjectMapper().readTree(bodyStr);
//                            if (jsonNode != null && jsonNode.get("code").asInt() == ResponseCode.SUCCESS.getCode()) {
//                                task.setUploadResultStatus(UPLOADRESULT_STATUS_YES);
//                                taskService.updateById(task);
//                            }
//                        } else {
//                            log.error("upload result error,address = " + task.getCallbackUrl() + " , taskId = " + task.getId() + ",responseCode = " + response.code());
//                        }
//                    } finally {
//                        if (response != null) {
//                            response.close();
//                        }
//                    }
//                } catch (Throwable e) {
//                    log.error("upload result error,address = " + task.getCallbackUrl() + " , taskId = " + task.getId(), e.getMessage());
//                }
//            }
//        }
//    }
//
//    private Response callback(String url, CallBackReq callBackReq) throws IOException {
//        MediaType parse = MediaType.parse("application/json; charset=utf-8");
//        String jsonStr = null;
//        jsonStr = new ObjectMapper().writeValueAsString(callBackReq);
//        RequestBody requestBody = RequestBody.create(parse, jsonStr);
//        Request request = new Request.Builder()
//                .addHeader("content-type", "application/json")
//                .url(url)
//                .post(requestBody).build();
//        return okHttpClient.newCall(request).execute();
//    }
//}
