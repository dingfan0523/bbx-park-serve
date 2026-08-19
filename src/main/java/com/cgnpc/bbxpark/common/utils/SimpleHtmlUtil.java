package com.cgnpc.bbxpark.common.utils;



/**
 *
 *
 */
public class SimpleHtmlUtil {
    /**
     * html的小于
     */
    public static final String HTML_LT = "＜";

    /**
     * html的小于
     */
    public static final String HTML_LT_NEW = "<";

    /**
     * html的大于
     */
    public static final String HTML_GT = "＞";

    /**
     * html的大于
     */
    public static final String HTML_GT_NEW = ">";

    /**
     * HTML编码
     */
    public static String transform(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        return content.replaceAll(HTML_LT, HTML_LT_NEW).replaceAll(HTML_GT,HTML_GT_NEW);
    }


    public static void main(String[] args) {
        String a= "＜p＞777777＜/p＞";
        String b= "<p>777777</p>";
        System.out.println(a.replaceAll("＜", "<").replaceAll("＞",">"));
    }
}
