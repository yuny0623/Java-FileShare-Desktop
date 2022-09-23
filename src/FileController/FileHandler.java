package FileController;

import java.io.*;
import java.util.Base64;

public class FileHandler {
    private File file;

    public void uploadFile(String filePath){
        this.file = new File(filePath);
    }

    public String transferFile2String(){
        String out = new String();
        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try{
            fis = new FileInputStream(this.file);
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
    public static byte[] base64Enc(byte[] buffer){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(buffer);
    }
}
