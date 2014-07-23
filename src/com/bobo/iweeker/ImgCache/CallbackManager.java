package com.bobo.iweeker.ImgCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import android.graphics.Bitmap;

import com.bobo.iweeker.Interface.ImageLoaderCallback;

public class CallbackManager {
	private ConcurrentHashMap<String, List<ImageLoaderCallback>> callbackMap;


	public CallbackManager() {

		callbackMap = new ConcurrentHashMap<String, List<ImageLoaderCallback>>();
	}


	public void put(String url,ImageLoaderCallback callback) {

		if(!callbackMap.contains(url))
			callbackMap.put(url, new ArrayList<ImageLoaderCallback>());

		callbackMap.get(url).add(callback);

	}

	public void callback(String url,Bitmap bitmap) {

		List<ImageLoaderCallback> callbacks = callbackMap.get(url);

		if(null == callbacks)
			return;

		for (ImageLoaderCallback callback : callbacks) {
			if(null != callback)
				callback.refresh(url, bitmap);
		}

		callbacks.clear();
		callbackMap.remove(url);
	}
}
