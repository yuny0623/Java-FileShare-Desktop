package org.imageghost.FileController;

import java.io.*;
import java.util.Base64;

public class FileTranslator {
    private File file;

    /*
        파일 객체 업로더
     */
    public File uploadFile(String filePath){
        this.file = new File(filePath);
        return this.file;
    }

    /*
        파일 -> 암호텍스트 변환기
     */
    public static String transferFile2Text(File file) throws Exception{
        String out = "";
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        fis = new FileInputStream(file);

        int len = 0;
        byte[] buf = new byte[1024];
        while((len = fis.read(buf)) != -1){
            baos.write(buf, 0, len);
        }
        byte[] fileArray = baos.toByteArray();
        out = new String(base64Enc(fileArray));
        fis.close();
        baos.close();
        return out;
    }

    public static byte[] base64Enc(byte[] buffer){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(buffer);
    }

    /*
        암호텍스트 -> 파일 변환기
     */
    public static File transferText2File(String binaryFile, String filePath, String fileName) throws Exception{
        FileOutputStream fos = null ;

        File fileDir = new File(filePath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        File destFile = new File(filePath + fileName);
        byte[] buff = binaryFile.getBytes();
        String toStr = new String(buff);
        byte[] b64dec = base64Dec(toStr);

        fos = new FileOutputStream(destFile);
        fos.write(b64dec);
        fos.close();
        return destFile;
    }

    public static byte[] base64Dec(String toStr){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(toStr);
    }
}
