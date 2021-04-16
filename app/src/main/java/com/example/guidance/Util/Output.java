package com.example.guidance.Util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Conor K on 12/04/2021.
 */
public class Output {

    public static String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException fileNotFound) {
            return null;
        }
    }

    public static boolean create(Context context, String fileName, String jsonString){
//        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (IOException fileNotFound) {
            return false;
        }

    }

    public static boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        String TAG = "Output";
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        Log.d(TAG, "file_size: " + file_size);
        return file.exists();
    }

    public static File getFile(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        String TAG = "Output";
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        Log.d(TAG, "file_size: " + file_size);
        return file;
    }

}
