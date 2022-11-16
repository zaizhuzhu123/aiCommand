//package com.laien.media_compression.schedule.impl.process;
//
//import com.laien.media_compression.entity.Task;
//import com.laien.media_compression.schedule.TaskCompressionActuator;
//import com.laien.media_compression.schedule.impl.process.util.FFMpegCmdConvert;
//import com.laien.media_compression.schedule.impl.process.util.CommandExecutor;
//import com.laien.media_compression.schedule.impl.process.util.SourceFileInfo;
//import com.laien.media_compression.schedule.impl.process.util.SourceFileParseUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//import static com.laien.media_compression.constant.MediaConstant.COMPRESSION_FILE_TYPE_SOUND;
//
//@Component
//@Slf4j
///**
// * sound 人声压缩 执行器
// */
//public class TaskCompressionActuatorBySound implements TaskCompressionActuator {
//
//    @Autowired
//    private CommandExecutor commandExecutor;
//
//
//    @Autowired
//    private FFMpegCmdConvert ffMpegCmdConvert;
//
//    //基准音量
//    private static float baseVolume = -0.2F;
//
//    //音量标准化
//    private static String loudnormTp = "loudnorm=I=-18:TP=-1";
//
//    //动态音频规范化器
//    private static String dynaudnorm = "dynaudnorm";
//
//    //#低通滤波器
//    private static String lowpass = "lowpass=frequency=8000";
//    //#高通滤波器
//    private static String highpass = "highpass=frequency=100";
//    //#EQ均衡
//    private static String eq = "equalizer=f=120:g=3,equalizer=f=315:g=5,equalizer=f=400:g=5,equalizer=f=7000:g=5";
//    //#压缩器
//    private static String compressor = "acompressor=threshold=0.25118864315:attack=200:release=1000";
//    private static String compand = ".3|.3:1|1:-90/-60|-60/-40|-40/-30|-20/-20:6:0:-90:0.2";
//
//    private static String compressionFileFullName2 = "#{SOURCE_FILE_DIR}" + File.separator + "#{SOURCE_FILE_NAME}_compression2.#{SOURCE_FILE_SUFFIX}";
//
//    private static String commandLoudnormTpTemp = "ffmpeg -i #{SOURCE_FILE_PATH} -af " + loudnormTp + ":print_format=json -f null -";
//
//    private static String commandDurationTemp = "ffprobe -i #{SOURCE_FILE_PATH} -v quiet -show_entries format=duration -hide_banner -of default=noprint_wrappers=1:nokey=1";
//
//    private static String comondPeakSourceTemp = "ffmpeg -i #{SOURCE_FILE_PATH} -filter:a volumedetect -f null /dev/null";
//
//    private static String comondPeakTargetTemp = "ffmpeg -i #{COMPRESSION2_FILE_PATH} -filter:a volumedetect -f null /dev/null";
//
//    //volumn压缩
//    private static String commondHideBannerVolumnTemp = "ffmpeg -hide_banner -y -i #{SOURCE_FILE_PATH}  -filter:a volume=#{VOL}dB -c:a libmp3lame -b:a 320k #{COMPRESSION_FILE_PATH}";
//
//
//    //loudness压缩
//    private static String commondHideBannerLoudnessTemp = "ffmpeg -hide_banner -y -i #{SOURCE_FILE_PATH} -af " + lowpass + "," + highpass + "," + eq + "," + compressor + " -c:a libmp3lame -b:a 320k #{COMPRESSION2_FILE_PATH}";
//
//    //peakout
//    private static String commondPeakOutTemp = "ffmpeg -y -i #{COMPRESSION2_FILE_PATH} -filter:a volume=#{VOL}dB #{COMPRESSION_FILE_PATH}";
//
//    private static String commondHideBannerNormalTemp = "ffmpeg -hide_banner -y -i #{SOURCE_FILE_PATH} -af " + lowpass + "," + highpass + "," + eq + "," + compressor + " #{COMPRESSION2_FILE_PATH}";
//
//    @Override
//    public String compression(Task task) throws Exception {
//        SourceFileInfo sourceFileInfo = SourceFileParseUtils.parseFile(task.getSourceFilePath());
//        //获取 input_i、target_offset
//        String commandLoudnormTp = ffMpegCmdConvert.getCommond(commandLoudnormTpTemp,
//                new String[]{"#{SOURCE_FILE_PATH}"},
//                new String[]{sourceFileInfo.getSourceFilePath()});
//        String result = commandExecutor.execResult(1, TimeUnit.MINUTES, commandLoudnormTp);
//        Float inputI = getInputI(result);
//        Float targetOffset = getTargetOffset(result);
//        if ((inputI == null) || (targetOffset == null)) {
//            //获取maxVolume
//            float maxVolume = getMaxVolume(sourceFileInfo);
//            float volume = -1 - maxVolume;
//            //执行压缩
//            String commondHideBannerVolumn = ffMpegCmdConvert.getCommond(commondHideBannerVolumnTemp,
//                    new String[]{"#{SOURCE_FILE_PATH}", "#{VOL}", "#{COMPRESSION_FILE_PATH}"},
//                    new String[]{sourceFileInfo.getSourceFilePath(), volume + "", sourceFileInfo.getCompressionFilePath()});
//            commandExecutor.exec(1L, TimeUnit.HOURS, commondHideBannerVolumn, "muxing overhead");
//        } else {
//            //获取duration
//            float duration = getDuration(sourceFileInfo);
//            //超过3秒
//            if (duration > 3F) {
//                //loudness压缩
//                String compress2 = compressionFileFullName2.replace("#{SOURCE_FILE_DIR}", sourceFileInfo.getParentDirPath()).replace("#{SOURCE_FILE_NAME}", sourceFileInfo.getPName()).replace("#{SOURCE_FILE_SUFFIX}", sourceFileInfo.getSuffix());
//                String commondHideBannerLoudness = ffMpegCmdConvert.getCommond(commondHideBannerLoudnessTemp,
//                        new String[]{"#{SOURCE_FILE_PATH}", "#{COMPRESSION2_FILE_PATH}"},
//                        new String[]{sourceFileInfo.getSourceFilePath(), compress2});
//
//                commandExecutor.exec(1L, TimeUnit.HOURS, commondHideBannerLoudness, "muxing overhead");
//                //peak
//                peakAndOut(sourceFileInfo, compress2);
//            } else {
//                String compress2 = compressionFileFullName2.replace("#{SOURCE_FILE_DIR}", sourceFileInfo.getParentDirPath()).replace("#{SOURCE_FILE_NAME}", sourceFileInfo.getPName()).replace("#{SOURCE_FILE_SUFFIX}", sourceFileInfo.getSuffix());
//                String commondHideBannerNormal = ffMpegCmdConvert.getCommond(commondHideBannerNormalTemp,
//                        new String[]{"#{SOURCE_FILE_PATH}", "#{COMPRESSION2_FILE_PATH}"},
//                        new String[]{sourceFileInfo.getSourceFilePath(), compress2});
//                commandExecutor.exec(1L, TimeUnit.HOURS, commondHideBannerNormal, "muxing overhead");
//                //peak
//                peakAndOut(sourceFileInfo, compress2);
//            }
//        }
//        return sourceFileInfo.getCompressionFilePath();
//    }
//
//    private void peakAndOut(SourceFileInfo sourceFileInfo, String compress2) throws IOException, InterruptedException {
//        String result;
//        String comondPeakTarget = ffMpegCmdConvert.getCommond(comondPeakTargetTemp,
//                new String[]{"#{COMPRESSION2_FILE_PATH}"},
//                new String[]{compress2});
//        result = commandExecutor.execResult(1, TimeUnit.MINUTES, comondPeakTarget);
//        float maxVolume = getMaxVolume(result);
//        //peak out
//        float volumn = baseVolume - maxVolume;
//        String commondPeakOut =
//                ffMpegCmdConvert.getCommond(commondPeakOutTemp,
//                        new String[]{"#{COMPRESSION_FILE_PATH}", "#{COMPRESSION2_FILE_PATH}"},
//                        new String[]{sourceFileInfo.getCompressionFilePath(), compress2})
//                        .replace("#{VOL}", volumn + "");
//        commandExecutor.exec(1L, TimeUnit.HOURS, commondPeakOut, "muxing overhead");
//    }
//
//    private float getDuration(SourceFileInfo sourceFileInfo) throws IOException, InterruptedException {
//        String result;
//        String commandDuration = ffMpegCmdConvert.getCommond(commandDurationTemp,
//                new String[]{"#{SOURCE_FILE_PATH}"},
//                new String[]{sourceFileInfo.getSourceFilePath()});
//        result = commandExecutor.execResult(1, TimeUnit.MINUTES, commandDuration);
//        return StringUtils.isNotBlank(result) ? Float.parseFloat(result.trim()) : 0F;
//    }
//
//    private float getMaxVolume(SourceFileInfo sourceFileInfo) throws IOException, InterruptedException {
//        String result;
//        String commondPeak = ffMpegCmdConvert.getCommond(comondPeakSourceTemp,
//                new String[]{"#{SOURCE_FILE_PATH}"},
//                new String[]{sourceFileInfo.getSourceFilePath()});
//        result = commandExecutor.execResult(10, TimeUnit.SECONDS, commondPeak);
//        return getMaxVolume(result);
//    }
//
//    @Override
//    public int type() {
//        return COMPRESSION_FILE_TYPE_SOUND;
//    }
//
//    private Float getInputI(String result) {
//        Float maxVolume = null;
//        String cutStr = "\"input_i\" : \"";
//        if (result.contains(cutStr)) {
//            try {
//                maxVolume = Float.parseFloat(StringUtils.substringBefore(StringUtils.substringAfterLast(result, cutStr), "\"").trim());
//            } catch (Throwable e) {
//
//            }
//        }
//        return maxVolume;
//    }
//
//    private Float getTargetOffset(String result) {
//        Float maxVolume = null;
//        String cutStr = "\"target_offset\" : \"";
//        if (result.contains(cutStr)) {
//            try {
//                maxVolume = Float.parseFloat(StringUtils.substringBefore(StringUtils.substringAfterLast(result, cutStr), "\"").trim());
//            } catch (Throwable e) {
//
//            }
//        }
//        return maxVolume;
//    }
//
//    private float getMaxVolume(String result) {
//        float maxVolume = 0f;
//        if (result.contains("max_volume:")) {
//            maxVolume = Float.parseFloat(StringUtils.substringBefore(StringUtils.substringAfterLast(result, "max_volume:"), " dB").trim());
//        }
//        return maxVolume;
//    }
//}
