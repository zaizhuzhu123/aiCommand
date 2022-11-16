//package com.laien.aiCommand.schedule.impl.process;
//
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.schedule.TaskCompressionActuator;
//import com.laien.aiCommand.service.IMp3Gain;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.File;
//
//import static com.laien.aiCommand.constant.MediaConstant.COMPRESSION_FILE_TYPE_SOUND;
//
//@Component
//@Slf4j
///**
// * sound 人声压缩 执行器
// */
//public class TaskCompressionActuatorBySoundMp3Gain implements TaskCompressionActuator {
//
//    @Resource
//    private IMp3Gain mp3Gain;
//
//    @Override
//    public String compression(Task task) throws Exception {
//        String sourceFilePath = task.getSourceFilePath();
//        File sourceFile = new File(sourceFilePath);
//        if (sourceFile != null && sourceFile.exists()) {
//            String absolutePath = sourceFile.getParentFile().getAbsolutePath();
//            mp3Gain.process(absolutePath);
//        }
//        return sourceFilePath;
//    }
//
//    @Override
//    public int type() {
//        return COMPRESSION_FILE_TYPE_SOUND;
//    }
//
//}
