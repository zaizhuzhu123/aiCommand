//package com.laien.aiCommand.schedule.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Bucket;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.cloud.StorageClient;
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.schedule.TaskCompressionActuator;
//import com.laien.aiCommand.schedule.TaskDispatcher;
//import com.laien.aiCommand.service.ITaskService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.FileCopyUtils;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.nio.charset.Charset;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import static com.laien.aiCommand.constant.MediaConstant.*;
//
//@Component
//@Slf4j
///**
// * 任务分发起 压缩任务执行入口
// */
//public class TaskDispatcherImpl implements TaskDispatcher {
//
//    @Autowired
//    private ITaskService taskService;
//
//    @Autowired
//    private List<TaskCompressionActuator> processList;
//
//
//    @Override
//    @Scheduled(fixedDelay = 5000)
//    public void dispatch() {
//        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Task::getCompressionStatus, COMPRESSION_STATUS_WAIT);
//        List<Task> tasks = taskService.list(queryWrapper);
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            for (Task task : tasks) {
//                TaskCompressionActuator process = null;
//                //根据不同的文件类型 找到对应的压缩执行器
//                for (TaskCompressionActuator taskCompressionActuator : processList) {
//                    if (taskCompressionActuator.type() == task.getSourceFileType().intValue()) {
//                        process = taskCompressionActuator;
//                        break;
//                    }
//                }
//                if (process != null) {
//                    //执行任务
//                    try {
//                        log.debug("============================================================");
//                        log.debug("============================================================");
//                        log.debug("======================START=================================");
//                        log.debug("======================" + task.getId() + "," + task.getServiceName() + ":" + task.getServiceId());
//                        //把任务状态置为执行中
//                        log.debug("======================update compression status to ing");
//                        task.setCompressionStatus(COMPRESSION_STATUS_ING);
//                        task.setCompressionStartTime(LocalDateTime.now());
//                        taskService.updateById(task);
//                        //任务有可能是之前中断过的任务 中间可能产生了一些任务文件 需要把这些中间产物删除 防止影响本次任务
//                        deleteLastTimeFiles(task);
//                        //进行视频 音频压缩，得到压缩后的文件本地路径
//                        long start = System.currentTimeMillis();
//                        log.debug("======================compressioning start");
//                        String compressionFilePath = process.compression(task);
//                        long end = System.currentTimeMillis();
//                        log.debug("======================compressioning finish . cost: " + (end - start) + " ms");
//                        //将压缩后的文件 进行firebase上传
//                        if (StringUtils.isNotBlank(compressionFilePath)) {
//                            log.debug("======================compression file : " + compressionFilePath);
//                            log.debug("======================upload firebase  start ");
//                            start = System.currentTimeMillis();
//                            String compressionFileUrl = uploadToFireBase(compressionFilePath, task);
//                            end = System.currentTimeMillis();
//                            log.debug("======================upload firebase  finish . cost: " + (end - start) + " ms");
//                            log.debug("======================" + compressionFileUrl);
//                            task.setCompressionFileUrl(compressionFileUrl);
//                            task.setCompressionStatus(COMPRESSION_STATUS_SUCCESS);
//                            FileUtils.deleteQuietly(new File(task.getSourceFilePath()).getParentFile());
//                            log.debug("======================success=============================");
//                        }
//                    } catch (Exception e) {
//                        log.debug("======================FAIL==============================");
//                        log.debug("======================" + e.getMessage());
//                        task.setCompressionStatus(COMPRESSION_STATUS_FAIL);
//                        task.setCompressionFailReason(e.getMessage());
//                    } finally {
//                        task.setCompressionFinishTime(LocalDateTime.now());
//                        taskService.updateById(task);
//                        log.debug("======================END===================================");
//                        log.debug("============================================================");
//                        log.debug("============================================================");
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 某些任务 可能会经历几次压缩 因此需要把那些上次中断任务中间产生的文件删除
//     *
//     * @param task
//     */
//    private void deleteLastTimeFiles(Task task) {
//        File file = new File(task.getSourceFilePath());
//        if (file.exists()) {
//            File[] files = file.getParentFile().listFiles();
//            for (File file1 : files) {
//                if (!file1.getName().equals(file.getName())) {
//                    file1.delete();
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 上传压缩后的文件至firebase 返回文件在firebase的地址
//     *
//     * @param compressionFilePath
//     * @param task
//     * @return
//     * @throws IOException
//     */
//    private String uploadToFireBase(String compressionFilePath, Task task) throws IOException {
//        FirebaseApp instance = null;
//        try {
//            instance = FirebaseApp.getInstance(task.getServiceName());
//        } catch (Throwable e) {
//            InputStream serviceAccount = new ByteArrayInputStream(task.getFirebaseJson().getBytes
//                    (Charset.forName("UTF-8")));
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setStorageBucket(task.getStorageBucket())
//                    .setDatabaseUrl(task.getDatabaseUrl())
//                    .build();
//            serviceAccount.close();
//            instance = FirebaseApp.initializeApp(options, task.getServiceName());
//        }
//        Bucket bucket = StorageClient.getInstance(instance).bucket();
//        String fileServerName = StringUtils.substringAfterLast(compressionFilePath, File.separator);
//        Map<String, String> map = new HashMap<>(1);
//        String token = UUID.randomUUID().toString();
//        map.put("firebaseStorageDownloadTokens", token);
//        String directory = task.getServiceName() + task.getDirName();
//        String blobName = directory + fileServerName;
//        BlobId blobId = BlobId.of(bucket.getName(), blobName);
//        File file = new File(compressionFilePath);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType(task.getSourceFileContentType())
//                // 在浏览器显示
//                .setContentDisposition("inline; filename*=utf-8''" + URLEncoder.encode(fileServerName, "UTF-8").replaceAll("\\+", "%20"))
//                .build();
//        Blob blob = bucket.getStorage().create(blobInfo, FileCopyUtils.copyToByteArray(file));
//        String fileParams = "?alt=media&token=" + token;
//        String bucketDirectoryConvert = directory.replaceAll("/", "%2F");
//        String link = blob.getSelfLink();
//        // 从url 截取出相对路径，为什么？因为firebase会处理url中的非英文字符
//        String fileRelativeUrl = link.substring(link.indexOf(bucketDirectoryConvert)) + fileParams;
//        return fileRelativeUrl;
//    }
//
//}
