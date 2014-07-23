
package com.bobo.iweeker.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

    public static final String MAIN_PATH = "iWeeker/";
    public static final String USER_LOGO = "userlogo/";
    public static final String CACHE = "temp/";
    public static final String IMAGE = "image/";
    private static final String TAG = "FileUtils";

    private String SDPATH;

    public static final String ABSOLUTE_USERLOGO = MAIN_PATH + USER_LOGO;
    public static final String ABSOLUTE_CACHE =  MAIN_PATH + CACHE;
    public static final String ABSOLUTE_IMAGE =  MAIN_PATH + IMAGE;

    public String getSDPATH() {
        return SDPATH;
    }

    public static Drawable getDrawableFromFile(File file) {
        try {
            if (!file.exists()) {
                return null;
            }

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] bt = new byte[512];

            // 获得文件输入流
            InputStream in = new FileInputStream(file);

            int readLength = in.read(bt);
            while (readLength != -1) {
                outStream.write(bt, 0, readLength);
                readLength = in.read(bt);
            }
            byte[] data = outStream.toByteArray();
            in.close();
            outStream.close();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            @SuppressWarnings("deprecation")
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            return bitmapDrawable;
        } catch (Exception e) {
            // TODO: handle exception
            Log.i(TAG, e.getMessage());
        }
        return null;
    }
    
    public static InputStream bitmapToIS(Bitmap bm){  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
            InputStream sbs = new ByteArrayInputStream(baos.toByteArray());    
            return sbs;  
        }

    public FileUtils() {
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public File createSDFlie(String filename) throws IOException {
        File file = new File(SDPATH + filename);
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dirname) throws IOException {
        File dir = new File(SDPATH + dirname);
        if(!dir.exists())
            dir.mkdirs();
        return dir;
    }

    public Boolean isFileExist(String filename) throws IOException {
        File file = new File(SDPATH + filename);
        return file.exists();
    }

    public File writetoSDFromInput(String path, String filename, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createSDFlie(path + filename);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[512];
            int temp;
            while ((temp = input.read(buffer)) > 0) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return file;
    }
}
