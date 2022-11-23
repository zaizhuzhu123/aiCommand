package com.laien.aiCommand.thread.step.uploadfirebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.thread.step.ProcessStep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_UPLOADIMG;

@Component
@Slf4j
public class UploadFirebaseStep implements ProcessStep {
    @Override
    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException {
        String userGeneratePath = AppliacationInfo.userGeneratePath.replace("{TASKID}", aiTask.getTaskId()) + "/samples";
        log.info("upload dir " + userGeneratePath + " to firebase");
        File file1 = new File(userGeneratePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        Collection<File> files = FileUtils.listFiles(new File(userGeneratePath), null, false);
        for (File file : files) {
            String fireBaseUrl = uploadToFireBase(file.getAbsolutePath(), aiTask);
            aiTask.getGenerateImgUrls().add(fireBaseUrl);
        }
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_UPLOADIMG;
    }

    /**
     * 上传压缩后的文件至firebase 返回文件在firebase的地址
     *
     * @param compressionFilePath
     * @return
     * @throws IOException
     */
    private String uploadToFireBase(String compressionFilePath, AiTask aiTask) throws IOException {
        FirebaseApp instance = FirebaseApp.getInstance();
        Bucket bucket = StorageClient.getInstance(instance).bucket();
        String fileServerName = StringUtils.substringAfterLast(compressionFilePath, File.separator);
        Map<String, String> map = new HashMap<>(1);
        String token = UUID.randomUUID().toString();
        map.put("firebaseStorageDownloadTokens", token);
        String directory = "/aiCommand/" + aiTask.getTaskId();
        String blobName = directory + fileServerName;
        BlobId blobId = BlobId.of(bucket.getName(), blobName);
        File file = new File(compressionFilePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map)
                // 在浏览器显示
                .setContentDisposition("inline; filename*=utf-8''" + URLEncoder.encode(fileServerName, "UTF-8").replaceAll("\\+", "%20"))
                .build();
        Blob blob = bucket.getStorage().create(blobInfo, FileCopyUtils.copyToByteArray(file));
        String fileParams = "?alt=media&token=" + token;
        String bucketDirectoryConvert = directory.replaceAll("/", "%2F");
        String link = blob.getSelfLink();
        // 从url 截取出相对路径，为什么？因为firebase会处理url中的非英文字符
        String fileRelativeUrl = link + fileParams;
        return fileRelativeUrl;
    }
}
