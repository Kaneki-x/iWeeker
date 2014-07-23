
package com.bobo.iweeker.App;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.bobo.iweeker.Interface.WeiboParseCallback;

public class SimpleWeiboManager {

    private static WeiboCotentManager manager = new WeiboCotentManager();

    public static void display(final TextView textView, String weibo, Context context) {
        textView.setText(weibo);
        textView.setText(manager.parseWeibo(weibo, createCallback(textView, weibo), context));
    }

    public static WeiboParseCallback createCallback(final TextView textView, final String weibo) {

        return new WeiboParseCallback() {
            public void refresh(String weibo, SpannableStringBuilder spannable) {
                textView.setText(spannable);
            }
        };
    }
}
