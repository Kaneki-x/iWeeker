
package com.bobo.iweeker.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpDownloader {

    private URL url = null;

    // 下载任意文件，并写入到SD卡中
    /*
     * 该函数返回值整形-1：带表文件下载出错，0:代表下载成功；1：代表文件已存在
     */
    public int downFile(String urlStr, String path, String fileName) {
        InputStream inputStream = null;
        try {
            FileUtils fileUtils = new FileUtils();
            if (fileUtils.isFileExist(path + fileName)) {
                return 1;
            }
            else {
                inputStream = getInputStreamFromUrl(urlStr);
                File resultFile = fileUtils.writetoSDFromInput(path, fileName, inputStream);
                if (resultFile == null) {
                    return -1;
                }
            }
        } catch (Exception e) {
            Log.i("Lion", e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public InputStream getInputStreamFromUrl(String urlStr) throws IOException {
        url = new URL(urlStr);
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlconn.getInputStream();
        return inputStream;
    }
}
