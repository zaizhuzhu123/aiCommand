package com.laien.aiCommand.config;

import com.laien.aiCommand.entity.AiTaskStep;

import static com.laien.aiCommand.constant.TaskConstant.TASK_STATUS_WAIT;
import static com.laien.aiCommand.constant.TaskConstant.TASK_STEP_TYPE_INITENO;

public class AppliacationInfo {

    public static String machineId = "";
    public static Boolean isSendHeartbeatSuccess = false;
    public static Boolean isInitStableDiffusionSuccess = false;
    //    public static String userImgSavePath = "/workspace/Dreambooth-Stable-Diffusion/"+ currentTaskId;
    public static String userImgSavePath = "/Users/apple/Documents/test/" + machineId;
    public static String basePath = "/workspace";
    public static String dreamboothPath = basePath + "/Dreambooth-Stable-Diffusion";
    public static String taskPath = basePath + "/Task" + "/{TASKID}";
    public static String userUploadImgPath = taskPath + "/uploadImgs";
    public static String userTraingCkptPath = taskPath + "/traingCkpt";
    public static String userGeneratePath = taskPath + "/generate";
    public static AiTaskStep initEnvironment = new AiTaskStep();

    static {
        initEnvironment.setStepName(TASK_STEP_TYPE_INITENO);
        initEnvironment.setRemainingFinishTime(3600L);
        initEnvironment.setStatus(TASK_STATUS_WAIT);
    }

}
