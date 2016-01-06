package com.dplovers.sanjeevkumar.mediav1;

import java.io.File;

/**
 * Created by sanjeevkumar on 12/22/15.
 */
public class CommonFunctions {

    public static String getFileName(String file_path) {
        File userFile = new File(file_path);
        String filename = userFile.getName();
        return filename;
    }
}
