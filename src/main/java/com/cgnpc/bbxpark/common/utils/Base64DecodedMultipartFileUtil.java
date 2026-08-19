package com.cgnpc.bbxpark.common.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Base64;

/**
 * @ClassName Base64DecodedMultipartFile
 * @Description TODO
 * @Author fengys5
 * @Date 2024/7/15 10:48
 */
public class Base64DecodedMultipartFileUtil implements MultipartFile {

    private final byte[] imgContent;
    private final String header;

    public Base64DecodedMultipartFileUtil(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }

    public static MultipartFile base64ToMultipartFile(String s) {
        MultipartFile image = null;
        StringBuilder base64 = new StringBuilder("");
        if (s != null && !"".equals(s)) {
            base64.append(s);
            String[] baseStrs = base64.toString().split(",");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            try {
                b = decoder.decodeBuffer(baseStrs[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < b.length; ++j) {
                if (b[j] < 0) {
                    b[j] += 256;
                }
            }
            image = new Base64DecodedMultipartFileUtil(b, baseStrs[0]);
        }
        return image;
    }

    /**
     * 根据文件路径专为MultipartFile
     * @param filePath
     * @return
     * @throws IOException
     */

//    public static MultipartFile fileToMultipartFile(String filePath) {
//        File file = new File(filePath);
//        FileInputStream input = null;
//        MultipartFile multipartFile = null;
//
//        try {
//            input = new FileInputStream(file);
//            multipartFile = new MockMultipartFile("file",
//                    file.getName(), "text/plain", input);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return multipartFile;
//    }

    /**
     * MultipartFile文件转为base64
     * @param file
     * @return
     * @throws IOException
     */
    public static String encodeFileToBase64(MultipartFile file) {
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(fileBytes);
    }

}