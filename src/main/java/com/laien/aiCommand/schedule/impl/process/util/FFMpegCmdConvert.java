//package com.laien.aiCommand.schedule.impl.process.util;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * ffmpeg
// */
//@Component
//public class FFMpegCmdConvert {
//
//    @Value("${ffmpeg-path}")
//    private String ffmpegPath;
//
//    /**
//     * 将命令模版转化为真实执行的命令
//     *
//     * @param commandTemp
//     * @param replaceSearchList
//     * @param replacementList
//     * @return
//     */
//    public String getCommond(String commandTemp, String[] replaceSearchList, String[] replacementList) {
//        String newCommandTemp = ffmpegPath + commandTemp;
//        return StringUtils.replaceEach(newCommandTemp, replaceSearchList, replacementList);
//    }
//
//
//}
