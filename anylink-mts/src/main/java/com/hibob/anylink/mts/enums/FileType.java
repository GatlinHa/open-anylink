package com.hibob.anylink.mts.enums;

import java.util.Arrays;

public enum FileType {
    IMAGE("jpg", "jpeg", "png", "gif", "bmp", "webp"),
    DOCUMENT("doc", "docx", "pdf", "txt"),

    AUDIO("mp3", "wav", "aac", "flac", "ogg", "webm"),
    // 可以继续添加其他类型

    VIDEO("mp4", "avi", "mkv", "mov", "flv", "webm", "wmv"),

    UNKNOWN(null);

    private final String[] extensions;

    FileType(String... extensions) {
        this.extensions = extensions;
    }

    public static FileType getFileTypeByExtension(String extension) {
        for (FileType fileType : values()) {
            if (fileType.extensions!= null && Arrays.asList(fileType.extensions).contains(extension.toLowerCase())) {
                return fileType;
            }
        }
        return UNKNOWN;
    }

    public static FileType determineFileType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            String extension = fileName.substring(dotIndex + 1);
            return getFileTypeByExtension(extension);
        }
        return UNKNOWN;
    }

    public static boolean isImageFile(String fileName) {
        FileType fileType = determineFileType(fileName);
        return fileType == FileType.IMAGE;
    }

    public static boolean isAudioFile(String fileName) {
        FileType fileType = determineFileType(fileName);
        return fileType == FileType.AUDIO;
    }

    public static boolean isVideoFile(String fileName) {
        FileType fileType = determineFileType(fileName);
        return fileType == FileType.VIDEO;
    }
}
