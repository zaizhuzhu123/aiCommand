//package com.laien.aiCommand.schedule.impl.process;
//
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.schedule.TaskCompressionActuator;
//import com.laien.aiCommand.schedule.impl.process.util.FFMpegCmdConvert;
//import com.laien.aiCommand.schedule.impl.process.util.SourceFileInfo;
//import com.laien.aiCommand.schedule.impl.process.util.SourceFileParseUtils;
//import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//import static com.laien.aiCommand.constant.MediaConstant.COMPRESSION_FILE_TYPE_MUSIC;
//
//@Component
//@Slf4j
///**
// * music 背景音乐 压缩执行器
// */
//public class TaskCompressionActuatorByMusic implements TaskCompressionActuator {
//
//    @Autowired
//    private CommandExecutor commandExecutor;
//
//    @Autowired
//    private FFMpegCmdConvert ffMpegCmdConvert;
//
//    private static String comondPeakTemp = "ffmpeg -i #{SOURCE_FILE_PATH} -filter:a volumedetect -f null /dev/null";
//
//    private static String commondCompressionTemp = "ffmpeg -y -i #{SOURCE_FILE_PATH} -filter:a volume=#{VOL}dB #{COMPRESSION_FILE_PATH}";
//
//    @Override
//    public String compression(Task task) throws Exception {
//        SourceFileInfo sourceFileInfo = SourceFileParseUtils.parseFile(task.getSourceFilePath());
//        //获取maxVolume
//        String commondPeak = ffMpegCmdConvert.getCommond(comondPeakTemp,
//                new String[]{"#{SOURCE_FILE_PATH}"},
//                new String[]{sourceFileInfo.getSourceFilePath()});
//        String result = commandExecutor.execResult(10, TimeUnit.SECONDS, commondPeak);
//        float maxVolume = getMaxVolume(result);
//        //修改volume
//        float volume = -4 - maxVolume;
//        //执行压缩
//        String commondCompression = ffMpegCmdConvert.getCommond(commondCompressionTemp,
//                new String[]{"#{SOURCE_FILE_PATH}", "#{VOL}", "#{COMPRESSION_FILE_PATH}"},
//                new String[]{sourceFileInfo.getSourceFilePath(), volume + "", sourceFileInfo.getCompressionFilePath()});
//        commandExecutor.exec(1L, TimeUnit.HOURS, commondCompression, "muxing overhead");
//        return sourceFileInfo.getCompressionFilePath();
//    }
//
//    private float getMaxVolume(String result) {
//        float maxVolume = 0f;
//        if (result.contains("max_volume:")) {
//            maxVolume = Float.parseFloat(StringUtils.substringBefore(StringUtils.substringAfterLast(result, "max_volume:"), " dB").trim());
//        }
//        return maxVolume;
//    }
//
//    @Override
//    public int type() {
//        return COMPRESSION_FILE_TYPE_MUSIC;
//    }
//}
