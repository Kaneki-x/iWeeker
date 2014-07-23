
package com.bobo.iweeker.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bobo.iweeker.R;

public class Utils {

	/**
	 * 获得手机的分辨率
	 */
	public static int[] getDisplayWidthAndHeight(Activity context) {
		int display[] = new int[2];

		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		display[0] = mDisplayMetrics.widthPixels;
		display[1] = mDisplayMetrics.heightPixels;

		return display;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static boolean isInstalled(Context context, String appName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager()
					.getPackageInfo(appName, 0);

		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
			return false;
		}
		if (packageInfo != null)
			return true;
		return false;
	}

	/*
	 * 检测网络连接
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 获得圆角bitmap
	// Rounded Corner
	// Bitmap
	public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
		Bitmap dstbmp = Bitmap
				.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstbmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return dstbmp;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) { 

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), 
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565); 
		Canvas canvas = new Canvas(bitmap); 

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); 
		drawable.draw(canvas); 
		return bitmap; 
	} 

	public static Bitmap compressImage(Bitmap image) {  

		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		image.compress(Bitmap.CompressFormat.PNG, 80, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
		int options = 80;  
		while (baos.toByteArray().length/1024 > 200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
			baos.reset();//重置baos即清空baos  
			image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
			options -= 10;//每次都减少10  
		}  
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
		return bitmap;  
	}  

	public static void showNetWorkErrorToast(Context context) {
		Toast.makeText(context, context.getResources().getString(R.string.net_work),
				Toast.LENGTH_LONG).show();
	}

	public static String getFormatCount(int count) {
		int num = String.valueOf(count).length();

		switch (num) {
		case 5:
			return ""+count/10000+"万";
		case 6:
			return ""+count/10000+"万";
		case 7:
			return ""+count/10000+"万";
		case 8:
			return ""+count/10000000+"千万";
		default:
			return ""+count;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getRefreshTime(String last_time) {
		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");//设置日期格式
		String now_time = df.format(new Date());
		if (last_time.equals("刚刚")) {
			return now_time;
		} else { 
			if (getTimeSub(last_time, now_time) == 0) {
				return now_time;
			} else {
				return "刚刚";
			}
		}
	}

	public static int getTimeSub(String last_time, String now_time) {
		if (last_time.split(" ")[0].equals(now_time.split(" ")[0])) {
			if (last_time.split(" ")[1].split(":")[0].equals(now_time.split(" ")[1].split(":")[0])) {
				if (Integer.parseInt(now_time.split(" ")[1].split(":")[1]) - Integer.parseInt(
						last_time.split(" ")[1].split(":")[1]) < 5){
					return 1;      
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		} else {
			return 0;
		}   
	}
}
