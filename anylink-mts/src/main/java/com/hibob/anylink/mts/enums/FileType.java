package com.hibob.anylink.mts.enums;

import java.util.Arrays;

public enum FileType {
    IMAGE("image/", "jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "ico", "jfif", "svg"),
    AUDIO("audio/", "mp3", "wav", "aac", "flac", "ogg", "webm", "m4a", "wma", "amr"),
    VIDEO("video/", "mp4", "avi", "mkv", "mov", "flv", "webm", "wmv", "mpeg", "3gp", "m4v"),
    DOCUMENT("application/"),
    TEXT("text/"),
    UNKNOWN("");

    private final String contentType;
    private final String[] extensions;

    FileType(String contentType, String... extensions) {
        this.contentType = contentType;
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

    public static FileType determineFileType(String contentType) {
        if (contentType.startsWith(IMAGE.contentType)) {
            return IMAGE;
        } else if (contentType.startsWith(AUDIO.contentType)) {
            return AUDIO;
        } else if (contentType.startsWith(VIDEO.contentType)) {
            return VIDEO;
        } else if (contentType.startsWith(DOCUMENT.contentType)) {
            return DOCUMENT;
        } else if (contentType.startsWith(TEXT.contentType)) {
            return TEXT;
        } else  {
            return UNKNOWN;
        }
    }

    public static boolean checkExtension(FileType fileType, String fileName) {
        String extension = getExtension(fileName);
        return Arrays.stream(fileType.extensions).anyMatch(extension::equals);
    }

    private static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
}
