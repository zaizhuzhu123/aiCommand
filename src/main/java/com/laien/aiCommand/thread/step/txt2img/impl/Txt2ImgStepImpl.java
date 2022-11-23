package com.laien.aiCommand.thread.step.txt2img.impl;

import com.laien.aiCommand.config.AppliacationInfo;
import com.laien.aiCommand.entity.AiTask;
import com.laien.aiCommand.entity.AiTaskStep;
import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.txt2img.Txt2ImgStep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.laien.aiCommand.config.AppliacationInfo.dreamboothPath;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_GENERATE;

@Slf4j
@Component
@Order(1)
public class Txt2ImgStepImpl implements Txt2ImgStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run(AiTask aiTask, AiTaskStep currentStep) throws IOException, InterruptedException {
        int ddim_steps = aiTask.getRequestData().getDdim_steps();
        if (aiTask.getRequestData().getDdim_steps() != null && aiTask.getRequestData().getDdim_steps() > 10) {
            ddim_steps = aiTask.getRequestData().getDdim_steps();
        }
        File ldmDir = new File(dreamboothPath + "/scripts/ldm");
        if (!ldmDir.exists()) {
            commandExecutor.execResult(3600, TimeUnit.SECONDS, "cp -r " + dreamboothPath + "/ldm " + dreamboothPath + "/scripts/");
        }
        String ckptDirPath = AppliacationInfo.userTraingCkptPath.replace("{TASKID}", aiTask.getTaskId());
        String userGeneratePath = AppliacationInfo.userGeneratePath.replace("{TASKID}", aiTask.getTaskId());
        File userGenerateDir = new File(userGeneratePath);
        if (userGenerateDir.exists()) {
            FileUtils.cleanDirectory(userGenerateDir);
        }
        File ckptPathDir = new File(ckptDirPath);
        if (!ckptPathDir.exists()) {
            ckptDirPath = AppliacationInfo.userTraingCkptPath.replace("{TASKID}", AppliacationInfo.lastTraingCkptTaskId);
            ckptPathDir = new File(ckptDirPath);
            if (!ckptPathDir.exists()) {
                ckptPathDir = scanLastestCkptDir(ckptPathDir);
            }
        }

        File[] files = ckptPathDir.listFiles();
        String ckptPath = "";
        if (files.length > 0) {
            ckptPath = files[files.length - 1].getAbsolutePath() + "/checkpoints/last.ckpt";
        }
        StringBuffer cmd = new StringBuffer();
        cmd.append("/venv/bin/python " + dreamboothPath + "/scripts/stable_txt2img.py ");
        cmd.append("--seed 10  ");
        cmd.append("--ddim_eta 0.0 ");
        cmd.append("--config " + dreamboothPath + "/configs/stable-diffusion/v1-inference.yaml ");
        cmd.append("--n_samples 1 ");
        cmd.append("--n_iter " + aiTask.getRequestData().getN_iter() + " ");
        cmd.append("--scale 10.0 ");
        cmd.append("--ddim_steps " + ddim_steps + " ");
        cmd.append("--ckpt " + ckptPath + " ");
        cmd.append("--outdir " + userGeneratePath + " ");
        cmd.append("--prompt " + aiTask.getRequestData().getPrompt() + "");
        commandExecutor.execResult(3600, TimeUnit.SECONDS, cmd.toString(), new CommandExecutor.CommondListener() {
            @Override
            public void onStdout(String str) {
                if (str.contains("Sampling:")) {
                    String s1 = StringUtils.substringAfterLast(str, "Sampling:");
                    if (s1.contains("]")) {
                        String totalTime = StringUtils.substringAfterLast(StringUtils.substringBefore(StringUtils.substringBefore(s1, "]"), ","), "<");
                        if (totalTime.contains(":")) {
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
                            long totalSeconds = hour * 3600 + minutes * 60 + seconds;
                            log.info("finishRemainingSeconds:" + totalSeconds);
                            currentStep.setRemainingFinishTime(totalSeconds);
                        }
                    }
                }
                log.info(str);
            }

            @Override
            public void onExit(int exitCode) {
                log.info("exitCode:" + exitCode);
                throw new RuntimeException("exitCode:" + exitCode);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private File scanLastestCkptDir(File ckptPathDir) {
        File taskDir = new File(AppliacationInfo.basePath + "/Task");
        File[] files = taskDir.listFiles();
        if (files != null && files.length > 0) {
            long currentLastModified = 0L;
            File ckptDir = null;
            for (File file : files) {
                File ckptFile = new File(file.getAbsoluteFile() + "/checkpoints/last.ckpt");
                if (ckptFile.exists()) {
                    long l = file.lastModified();
                    if (l > currentLastModified) {
                        currentLastModified = l;
                        ckptDir = file;
                    }
                }
            }
            if (ckptDir != null) {
                ckptPathDir = ckptDir;
            }
        }
        return ckptPathDir;
    }

    @Override
    public String type() {
        return TASK_STEP_TYPE_GENERATE;
    }
}
