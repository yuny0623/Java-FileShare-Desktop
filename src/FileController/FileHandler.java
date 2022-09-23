package FileController;

import java.io.*;
import java.util.Base64;

public class FileHandler {
    private File file;

    public File uploadFile(String filePath){
        this.file = new File(filePath);
        return this.file;
    }

    public String transferFile2String(File file){
        String out = new String();
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try{
            fis = new FileInputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        int len = 0;
        byte[] buf = new byte[1024];
        try{
            while((len = fis.read(buf)) != -1){
                baos.write(buf, 0, len);
            }
            byte[] fileArray = baos.toByteArray();
            out = new String(base64Enc(fileArray));

            fis.close();
            baos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return out;
    }
    public byte[] base64Enc(byte[] buffer){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(buffer);
    }

    public File transferString2File(String binaryFile, String filePath, String fileName){
        FileOutputStream fos = null ;

        File fileDir = new File(filePath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        File destFile = new File(filePath + fileName);
        byte[] buff = binaryFile.getBytes();
        String toStr = new String(buff);
        byte[] b64dec = base64Dec(toStr);

        try{
            fos = new FileOutputStream(destFile);
            fos.write(b64dec);
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return destFile;
    }

    public byte[] base64Dec(String toStr){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(toStr);
    }
}
