package com.laien.aiCommand.thread.step.train.impl;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.request.TrainingGenerateRequest;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_TRAING;

@Component
@Order(1)
public class DownloadFirebaseImgStep implements DreamBoothTrainStep {

    @Resource
    private OkHttpClient okHttpClient;

    @Override
    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException {
        TrainingGenerateRequest trainingGenerateRequest = (TrainingGenerateRequest) aiTask.getRequestData();
        List<String> firebaseImgs = trainingGenerateRequest.getFirebaseImgs();
        if (CollectionUtils.isNotEmpty(firebaseImgs)) {
            String userUploadImgs = AppliacationInfo.userUploadImgPath.replace("{TASKID}", aiTask.getTaskId());
            File taskDir = new File(userUploadImgs);
            if (!taskDir.exists()) {
                taskDir.mkdirs();
            }
            FileUtils.cleanDirectory(taskDir);
            for (String firebaseImg : firebaseImgs) {
                String prefix = StringUtils.substringAfterLast(StringUtils.substringBefore(firebaseImg, "?"), ".");
                File file = new File(taskDir.getAbsolutePath() + "/" + UUID.randomUUID() + "." + prefix);
                if (file.exists()) {
                    file.createNewFile();
                }
                byte[] fileBytes = getFileBytes(firebaseImg);
                IOUtils.write(fileBytes, new FileOutputStream(file));
            }
        }
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_TRAING;
    }

    private byte[] getFileBytes(String imgUrl) throws IOException {
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)")
                .url(imgUrl)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            return bytes;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
