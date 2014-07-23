package com.bobo.iweeker.ImgCache;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Interface.ImageLoaderCallback;

public class AsyncImageLoader {
	private static final int MESSAGE_ID = 1;
	public static final String EXTRA_IMG_URL = "extra_img_url";
	public static final String EXTRA_IMG = "extra_img";

	private ImageManager imgManger = new ImageManager(WeiboApplication.context);

	private BlockingQueue<String> urlQueue = new ArrayBlockingQueue<String>(50);

	private DownloadImageThread downloadImgThread = new DownloadImageThread();

	private CallbackManager callbackManager = new CallbackManager();


	public Bitmap get(String url, ImageLoaderCallback callback, int type) {
		Bitmap bitmap = null;

		bitmap = ImageManager.userDefualtHead;

		if(imgManger.contains(url)) {

			bitmap = imgManger.getFromCache(url);
			return bitmap;
		} else {
			callbackManager.put(url, callback);
			startDownLoadThread(url);
		}

		return bitmap;
	}

	private void startDownLoadThread(String url) {

		putUrlToUrlQueue(url);

		State state = downloadImgThread.getState();

		if(state == State.NEW) {
			downloadImgThread.start();
		} else if(state == State.TERMINATED) {
			downloadImgThread = new DownloadImageThread();
			downloadImgThread.start();
		}
	}

	private void putUrlToUrlQueue(String url) {

		if(!urlQueue.contains(url)) {
			try {
				urlQueue.put(url);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch(msg.what) {
			case MESSAGE_ID :

				Bundle bundle = msg.getData();

				String url = bundle.getString(EXTRA_IMG_URL);
				Bitmap bitmap = bundle.getParcelable(EXTRA_IMG);

				callbackManager.callback(url, bitmap);

				break;
			default:
				break;
			}

		};
	};

	private  class DownloadImageThread extends Thread {

		private boolean isRun = true;

		public void shutDown() {
			isRun = false;
		}

		public void run() {
			try {
				while(isRun) {
					String url = urlQueue.poll();

					if(null == url) break;

					Bitmap bitmap = imgManger.safeGet(url);

					Message msg = handler.obtainMessage(MESSAGE_ID);
					Bundle bundle = msg.getData();
					bundle.putSerializable(EXTRA_IMG_URL, url);
					bundle.putParcelable(EXTRA_IMG, bitmap);

					handler.sendMessage(msg);
				}
			} catch (Exception e) {

			} finally {
				shutDown();
			}
		}
	}
}
