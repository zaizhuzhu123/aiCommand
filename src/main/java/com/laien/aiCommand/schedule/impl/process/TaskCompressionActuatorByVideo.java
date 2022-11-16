//package com.laien.aiCommand.schedule.impl.process;
//
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.schedule.TaskCompressionActuator;
//import com.laien.aiCommand.schedule.impl.process.util.FFMpegCmdConvert;
//import com.laien.aiCommand.schedule.impl.process.util.SourceFileInfo;
//import com.laien.aiCommand.schedule.impl.process.util.SourceFileParseUtils;
//import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//import static com.laien.aiCommand.constant.MediaConstant.COMPRESSION_FILE_TYPE_VIDEO;
//
//@Component
//@Slf4j
///**
// * video 视频压缩 执行器
// */
//public class TaskCompressionActuatorByVideo implements TaskCompressionActuator {
//
//    @Autowired
//    private CommandExecutor commandExecutor;
//
//    @Autowired
//    private FFMpegCmdConvert ffMpegCmdConvert;
//
//    private static String comondTemp = "ffmpeg -i #{SOURCE_FILE_PATH} -vcodec libx264 -crf 28 -tune fastdecode -tune zerolatency -movflags +faststart #{COMPRESSION_FILE_PATH}";
//
//    @Override
//    public String compression(Task task) throws Exception {
//        SourceFileInfo sourceFileInfo = SourceFileParseUtils.parseFile(task.getSourceFilePath());
//        String commond = ffMpegCmdConvert.getCommond(comondTemp,
//                new String[]{"#{SOURCE_FILE_PATH}", "#{COMPRESSION_FILE_PATH}"},
//                new String[]{sourceFileInfo.getSourceFilePath(), sourceFileInfo.getCompressionFilePath()});
//        commandExecutor.exec(1L, TimeUnit.HOURS, commond, "kb/s");
//        return sourceFileInfo.getCompressionFilePath();
//    }
//
//    @Override
//    public int type() {
//        return COMPRESSION_FILE_TYPE_VIDEO;
//    }
//}
