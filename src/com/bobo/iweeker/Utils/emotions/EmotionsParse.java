
package com.bobo.iweeker.Utils.emotions;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bobo.iweeker.R;
import com.bobo.iweeker.App.WeiboApplication;

public class EmotionsParse {

    private HashMap<String, Integer> emotionsMap;
    private String[] emotionNames;
    private Context context;

    public EmotionsParse() {

        this.context = WeiboApplication.context;
        emotionNames = context.getResources().getStringArray(R.array.default_emotions);
        if (emotionNames.length != IDs.ids.length)
            throw new IllegalStateException("长度不等");

        int length = emotionNames.length;

        emotionsMap = new HashMap<String, Integer>(length);
        for (int i = 0; i < length; i++) {
            emotionsMap.put(emotionNames[i], IDs.ids[i]);
        }
    }

    public Drawable getEmotionByName(String name) {
        Integer id = emotionsMap.get(name);
        if (id != null)
            return context.getResources().getDrawable(id);

        return null;
    }

}
