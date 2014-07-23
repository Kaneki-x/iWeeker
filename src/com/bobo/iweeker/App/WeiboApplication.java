
package com.bobo.iweeker.App;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.bobo.iweeker.R;
import com.bobo.iweeker.DB.DBHelper;
import com.bobo.iweeker.ImgCache.AsyncImageLoader;
import com.bobo.iweeker.Model.UserInfo;
import com.bobo.iweeker.Utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class WeiboApplication extends Application {
    private UnlimitedDiscCache unlimitedDiscCache;
    public static UserInfo userInfo;
    public static DBHelper dbHelper;
    public static Context context;
    public static FileUtils fileUtils;
    public static DisplayImageOptions defaultOptions;
    public static AsyncImageLoader asyncImageLoader;

    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();
        asyncImageLoader = new AsyncImageLoader();
        fileUtils = new FileUtils();

        try {
            fileUtils.createSDDir("iWeeker");
            unlimitedDiscCache = new UnlimitedDiscCache(fileUtils.createSDDir(FileUtils.ABSOLUTE_CACHE));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        defaultOptions = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.default_pic)
        .showImageForEmptyUri(R.drawable.default_pic)
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        .showImageOnFail(R.drawable.default_pic)
        .cacheInMemory()
        .cacheOnDisc()
        .bitmapConfig(Bitmap.Config.ARGB_8888).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions)
                .discCache(unlimitedDiscCache)
                .build();
        ImageLoader.getInstance().init(config);
        
    }

}
