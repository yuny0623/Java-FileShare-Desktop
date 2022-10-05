package org.imageghost.FileController;

import java.util.ArrayList;
import java.util.List;

public class FileHolder {
    private static List<String> encFileList = new ArrayList<>();

    public static List<String> getMyEncFileList(){
        return encFileList;
    }

    public static void deleteMyEncFileList(){
        encFileList.clear();
    }
}
