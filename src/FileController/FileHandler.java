package FileController;

import java.io.File;

public class FileHandler {
    private File file;

    public void uploadFile(String filePath){
        this.file = new File(filePath);
    }

    public String transferFile2String(File imageHandler){
        return new String();
    }

    public File transferString2File(String text){
        return new File();
    }
}
