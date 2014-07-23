package com.bobo.iweeker.ImgCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Utils.MD5Utils;

public class ImageManager {

	public static final int USER_DEFAULT = 1;
	public static final int ROUND_DEFAULT = 2;

	Map<String, SoftReference<Bitmap>> imgCache ;

	private Context context;

	public static Bitmap userDefualtHead;

	public ImageManager(Context context) {
		this.context = context;
		imgCache = new HashMap<String, SoftReference<Bitmap>>();

		userDefualtHead = drawableToBitmap(context.getResources().getDrawable(R.drawable.default_avatar));
	}

	public boolean contains(String url) {
		return imgCache.containsKey(url);
	}

	public Bitmap getFromCache(String url) {
		Bitmap bitmap = null;

		bitmap = this.getFromMapCache(url);

		if(null == bitmap) {

			bitmap = getFromFile(url);
		}

		return bitmap;
	}

	/**
	 * 从文件中获取Bitmap
	 * @param url
	 * @return
	 */
	public Bitmap getFromFile(String url) {

		String fileName = this.getMd5(url);

		FileInputStream is = null;

		try {
			is = context.openFileInput(fileName);

			return BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			return null;
		} finally {
			if(null != is) {
				try {
					is.close();
				}catch(Exception ex) {
					
				}
			}
		}


	}

	/**
	 * 从Map缓存中获取BitMap
	 * @param url
	 * @return
	 */
	public Bitmap getFromMapCache(String url) {
		Bitmap bitmap = null;

		SoftReference<Bitmap> ref = null;

		synchronized (this) {
			ref = imgCache.get(url);
		}
		if(null != ref) {
			bitmap = ref.get();
		}
		return bitmap;
	}

	public Bitmap safeGet(String url) throws HttpException {

		Bitmap bitmap = this.getFromFile(url);

		if(null != bitmap) {
			synchronized (this) {
				imgCache.put(url, new SoftReference<Bitmap>(bitmap));
			}
			return bitmap;
		}

		return downloadImg(url);

	}

	/**
	 * 下载图片并保持文件到系统缓存
	 * @param urlStr
	 * @return
	 */

	public Bitmap downloadImg(String urlStr) throws HttpException {

		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			String fileName = writerToFile(getMd5(urlStr),connection.getInputStream());

			return BitmapFactory.decodeFile(fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String  writerToFile(String fileName, InputStream is) {

		BufferedInputStream bis = null;

		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));

			byte[] buffer = new byte[1024];
			int length;

			while((length = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}

		} catch (Exception e) {

		} finally {
			try {
				if(null != bis) {
					bis.close();
				}
				if(null != bos) {
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return context.getFilesDir() + "/" + fileName;
	}

	private String getMd5(String src) {
		return MD5Utils.getMD5String(src);
	}

	private Bitmap drawableToBitmap(Drawable drawable) {

		BitmapDrawable tempDrawable = (BitmapDrawable)drawable;
		return tempDrawable.getBitmap();

	}
}
