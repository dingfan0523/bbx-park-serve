package com.cgnpc.bbxpark.common.enums;

/**
 * @description: 图片格式枚举
 */
public enum ImageEnum {
    /**
     * jpg
     */
    JPG("jpg"),
    /**
     * jpeg
     */
    JPEG("jpeg"),
    /**
     * png
     */
    PNG("png"),
    /**
     * gif
     */
    GIF("gif"),
    /**
     * bmp
     */
    BMP("bmp");

    private final String extension;

    ImageEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static boolean isSupported(String extension) {
        for (ImageEnum format : values()) {
            if (format.getExtension().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
