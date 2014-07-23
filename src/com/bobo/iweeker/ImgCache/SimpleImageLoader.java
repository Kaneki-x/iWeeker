package com.bobo.iweeker.ImgCache;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Interface.ImageLoaderCallback;
import com.bobo.iweeker.Utils.Utils;

public class SimpleImageLoader {
	
	public static void showImg(ImageView view,String url, int type) {
		
		view.setTag(R.id.tag_first, url);
		view.setTag(R.id.tag_second, type);
		
		if(ImageManager.ROUND_DEFAULT == type)
			view.setImageBitmap(Utils.getRCB(WeiboApplication.asyncImageLoader.get(url, getCallback(url,view), type), 90));
		else
			view.setImageBitmap(WeiboApplication.asyncImageLoader.get(url, getCallback(url,view), type));
	}
	
	private static ImageLoaderCallback getCallback(final String url,final ImageView view) {
		
		return new ImageLoaderCallback() {
			
			@Override
			public void refresh(String url, Bitmap bitmap) {
				if(url.equals(view.getTag(R.id.tag_first).toString())) {
					if(ImageManager.ROUND_DEFAULT == Integer.parseInt(view.getTag(R.id.tag_second).toString())) {
						if(bitmap != null)
							view.setImageBitmap(Utils.getRCB(bitmap, 90));
						else
							view.setImageBitmap(Utils.getRCB(ImageManager.userDefualtHead, 90));
					}
					else {
						if(bitmap != null)
							view.setImageBitmap(bitmap);
						else
							view.setImageBitmap(Utils.getRCB(ImageManager.userDefualtHead, 90));
					}
				}
			}
		};
	}
}
