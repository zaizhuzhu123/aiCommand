package com.laien.aiCommand.schedule.impl.process.util;

import lombok.Data;

@Data
public class SourceFileInfo {


    /**
     * 文件所在文件夹路径
     */
    private String parentDirPath;

    /**
     * 文件全名 包含后缀
     */
    private String name;

    /**
     * 文件名称，不包含后缀
     */
    private String pName;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小 单位KB
     */
    private Integer size;

    /**
     * 源文件路径
     */
    private String sourceFilePath;

    /**
     * 压缩后的文件路径（这里只是根据规则生成的文件路径 文件可能不存在）
     */
    private String compressionFilePath;
}
