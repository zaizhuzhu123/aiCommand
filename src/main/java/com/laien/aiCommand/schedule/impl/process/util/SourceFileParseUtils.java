package com.laien.aiCommand.schedule.impl.process.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 源文件解析工具
 */
public class SourceFileParseUtils {


    private static String compressionFileFullName = "#{SOURCE_FILE_DIR}" + File.separator + "#{SOURCE_FILE_NAME}_compression.#{SOURCE_FILE_SUFFIX}";

    /**
     * 根据源文件路径 返回源文件各项参数
     *
     * @param sourceFilePath
     * @return
     */
    public static SourceFileInfo parseFile(String sourceFilePath) {
        File file = new File(sourceFilePath);
        if (!file.exists()) {
            throw new RuntimeException("file - " + sourceFilePath + " does not exist");
        }
        //截取文件夹路径
        String parentDirPath = file.getParent();
        //文件名称
        String name = file.getName();
        //文件名称 不包含后缀
        String pName = StringUtils.substringBeforeLast(name, ".");
        //文件后缀名
        String suffix = StringUtils.substringAfterLast(name, ".");
        //文件大小
        int size = (int) (file.length() / (1024));
        //压缩后的文件路径
        String compressionFilePath = compressionFileFullName
                .replace("#{SOURCE_FILE_DIR}", parentDirPath)
                .replace("#{SOURCE_FILE_NAME}", pName)
                .replace("#{SOURCE_FILE_SUFFIX}", suffix);

        SourceFileInfo sourceFileInfo = new SourceFileInfo();
        sourceFileInfo.setParentDirPath(parentDirPath);
        sourceFileInfo.setName(name);
        sourceFileInfo.setPName(pName);
        sourceFileInfo.setSuffix(suffix);
        sourceFileInfo.setSize(size);
        sourceFileInfo.setCompressionFilePath(compressionFilePath);
        sourceFileInfo.setSourceFilePath(sourceFilePath);
        return sourceFileInfo;
    }

}
