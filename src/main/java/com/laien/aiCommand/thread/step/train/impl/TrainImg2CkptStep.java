package com.laien.aiCommand.thread.step.train.impl;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.request.AiTaskAddRequest;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.train.DreamBoothTrainStep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.laien.aiCommand.config.AppliacationInfo.dreamboothPath;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_TRAING;

@Component
@Order(2)
@Slf4j
public class TrainImg2CkptStep implements DreamBoothTrainStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException {
        int training_step = 10;
        StringBuffer cmd = new StringBuffer();
        String ckptPath = AppliacationInfo.userTraingCkptPath.replace("{TASKID}", aiTask.getTaskId());
        String userUploadImgs = AppliacationInfo.userUploadImgPath.replace("{TASKID}", aiTask.getTaskId());
//        String projectName = StringUtils.substringAfterLast(ckptPath, "/");
//        String logDir = StringUtils.substringBeforeLast(ckptPath, "/");
        cmd.append("python " + dreamboothPath + "/main.py ");
        cmd.append("--base " + dreamboothPath + "/configs/stable-diffusion/v1-finetune_unfrozen.yaml ");
        cmd.append("--logdir " + ckptPath + " ");
        cmd.append("--datadir_in_name false ");
//        cmd.append("--project " + projectName + " ");
        cmd.append("-t ");
        cmd.append("--actual_resume " + dreamboothPath + "/model.ckpt ");
        cmd.append("--reg_data_root " + dreamboothPath + "/regularization_images/person_ddim ");
//        cmd.append("-n " + projectName + " ");
        cmd.append("--gpus 0, ");
        cmd.append("--data_root " + userUploadImgs + " ");
        cmd.append("--max_training_steps " + training_step + " ");
        cmd.append("--class_word \"person\" ");
        cmd.append("--token marcos ");
        cmd.append("--no-test");
        commandExecutor.execResult(3600, TimeUnit.SECONDS, cmd.toString(), new CommandExecutor.CommondListener() {
            @Override
            public void onStdout(String str) {
                if (str.contains("Epoch 0:") && str.contains("[")) {
                    try {
                        String[] split = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(str, "["), "|").trim().split("/");
                        int finishStep = Integer.parseInt(split[0]);
                        int totalStep = Integer.parseInt(split[1]);
                        log.info("finishStep:" + finishStep);
                        String totalTime = StringUtils.substringBefore(StringUtils.substringAfterLast(str, "["), ",").split("<")[1];
                        log.info("totalTime:" + totalTime);
                        String[] timeStrs = totalTime.split(":");
                        int hour = 0;
                        int minutes = 0;
                        int seconds = 0;
                        if (timeStrs.length == 3) {
                            hour = Integer.parseInt(timeStrs[0]);
                            minutes = Integer.parseInt(timeStrs[1]);
                            seconds = Integer.parseInt(timeStrs[2]);
                        } else {
                            minutes = Integer.parseInt(timeStrs[0]);
                            seconds = Integer.parseInt(timeStrs[1]);
                        }
                        log.info("hour:" + hour);
                        log.info("minutes:" + minutes);
                        log.info("seconds:" + seconds);
                        double finishRate = (training_step - finishStep) / (training_step * 1.0d);
                        long totalSeconds = hour * 3600 + minutes * 60 + seconds;
                        long traingingStepTotalSeconds = (long) (totalSeconds * (training_step / (totalStep * 1.0d)));
                        log.info("totalSeconds:" + totalSeconds);
                        //预留三分钟保险
//                        long finishRemainingSeconds = (long) (finishRate * traingingStepTotalSeconds) + 60 * 3;
                        long finishRemainingSeconds = (long) (finishRate * traingingStepTotalSeconds);
                        log.info("finishRemainingSeconds:" + finishRemainingSeconds);
                        currentStep.setRemainingFinishTime(finishRemainingSeconds);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                log.info(str);
            }

            @Override
            public void onExit(int exitCode) {
                log.info("exitCode:" + exitCode);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });

    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_TRAING;
    }

    public static void main(String[] args) {
        System.out.println((long) (((500 - 100) / (500 * 1.0d)) * 2000));
    }
}
