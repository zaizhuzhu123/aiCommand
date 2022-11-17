package com.laien.aiCommand.constant;

public interface TaskConstant {


    public final static Integer TASK_STATUS_WAIT = 0;
    public final static Integer TASK_STATUS_PROCESS = 1;
    public final static Integer TASK_STATUS_FINISH = 2;
    public final static Integer TASK_STATUS_FAIL = 3;


    public final static String TASK_STEP_TYPE_INITENO = "INITENVIRONMENT";
    public final static String TASK_STEP_TYPE_TRAING = "TRAING";
    public final static String TASK_STEP_TYPE_GENERATE = "GENERATE";
    public final static String TASK_STEP_TYPE_UPLOADIMG = "UPFIREBASE";


//    /**
//     * 压缩文件类型
//     */
//    int COMPRESSION_FILE_TYPE_VIDEO = 1;
//    int COMPRESSION_FILE_TYPE_MUSIC = 2;
//    int COMPRESSION_FILE_TYPE_SOUND = 3;
//
//    List<String> COMPRESSION_FILE_TYPE_VIDEO_SUFFIX = Lists.newArrayList("MP4");
//    List<String> COMPRESSION_FILE_TYPE_MUSIC_SUFFIX = Lists.newArrayList("MP3");
//    List<String> COMPRESSION_FILE_TYPE_SOUND_SUFFIX = Lists.newArrayList("MP3");
//
//
//    List<Integer> COMPRESSION_FILE_TYPES = Lists.newArrayList(COMPRESSION_FILE_TYPE_VIDEO, COMPRESSION_FILE_TYPE_MUSIC, COMPRESSION_FILE_TYPE_SOUND);
//
//
//    Map<Integer, List<String>> MEDIA_TYPES = Maps.toMap(COMPRESSION_FILE_TYPES, new Function<Integer, List<String>>() {
//        @Nullable
//        @Override
//        public List<String> apply(@Nullable Integer input) {
//            if (input == COMPRESSION_FILE_TYPE_VIDEO) {
//                return COMPRESSION_FILE_TYPE_VIDEO_SUFFIX;
//            }
//            if (input == COMPRESSION_FILE_TYPE_MUSIC) {
//                return COMPRESSION_FILE_TYPE_MUSIC_SUFFIX;
//            }
//            if (input == COMPRESSION_FILE_TYPE_SOUND) {
//                return COMPRESSION_FILE_TYPE_SOUND_SUFFIX;
//            }
//            return null;
//        }
//    });
//
//    /**
//     * 压缩结果 1成功 0失败
//     */
//    int COMPRESSION_RESULT_SUCCESS = 1;
//
//    int COMPRESSION_RESULT_FAIL = 0;
//
//    /**
//     * 压缩状态 0等待压缩 1压缩中 2压缩成功 3压缩失败
//     */
//    int COMPRESSION_STATUS_WAIT = 0;
//    int COMPRESSION_STATUS_ING = 1;
//    int COMPRESSION_STATUS_SUCCESS = 2;
//    int COMPRESSION_STATUS_FAIL = 3;
//
//    /**
//     * 上报结果状态 0未上报 1已上报
//     */
//    int UPLOADRESULT_STATUS_NO = 0;
//    int UPLOADRESULT_STATUS_YES = 1;
}
