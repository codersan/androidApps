package com.dplovers.sanjeevkumar.mediav1;

import android.media.MediaMetadataRetriever;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjeevkumar on 12/12/15.
 * Called from CreateAlbumsList Class
 * Fetches all albums from /storage directory in sorted order on Album title
 */

public class ExtractMediaFilesPathFromDeviceStorage {

    private ArrayList<String> media_file_extension_list = new ArrayList<>();
    private List<String> media_file_path_list = new ArrayList<>();
    MediaMetadataRetriever mmr;
    public ExtractMediaFilesPathFromDeviceStorage() {
        //empty
    }
    int c=0;

    //main Function: Will extract all files and store it in albumMetaDataList
    public List<String> extractFilesFromDeviceStorage() {

        // Will store all albums
        media_file_extension_list.add("mp3");

        String rootDirectory = "/storage/";

        try {
            File home = new File(rootDirectory);
            File[] list_files = home.listFiles();
            if (list_files != null && list_files.length > 0) {
                for (File file : list_files) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory() && !file.getAbsolutePath().contains("/storage/emulated")) {
                        scanDirectoryRecursively(file);
                    } else {
                        checkForMediaFileTypeAndAdd(file);
                    }
                }
            }

        }
        catch (Exception ex) {

        }
        return media_file_path_list;

    }

    // scan Directory Recursively
    private void scanDirectoryRecursively(File directory) {

        if (directory != null && !directory.getAbsolutePath().contains("/storage/emulated")) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectoryRecursively(file);
                    } else {
                        checkForMediaFileTypeAndAdd(file);
                    }
                }
            }
        }
    }

    //add album to file path list
    private void checkForMediaFileTypeAndAdd(File file) {

        String file_name = file.getName();
        String this_extension = getFileExtension(file_name);

        if (media_file_extension_list.contains(this_extension)) {
            media_file_path_list.add(file.getPath());
            if(file.getPath().contains("emulated")) {
                c++;
            }
        }
    }

    private String getFileExtension(String file_name) {
        String extension = "";
        int i = file_name.lastIndexOf('.');
        if (i > 0) {
            extension = file_name.substring(i+1);
        }
        return extension;
    }
}
