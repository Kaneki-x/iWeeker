
package com.bobo.iweeker.Activity;

import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bobo.iweeker.R;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Controller.UserInfoController;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Constants;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.Model.UserInfo;
import com.bobo.iweeker.Service.MainService;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.HttpManager;

@SuppressLint({
	"SetJavaScriptEnabled", "HandlerLeak", "NewApi"
})
public class AuthActivity extends Activity implements IWeekerActivityInterface {

	private RelativeLayout rl_back;
	private RelativeLayout rl_reload;
	private ImageView iv_reload;
	private WebView webView;
	private LayoutInflater mInflater;
	private Dialog dialog;

	private String auth_url = "https://api.weibo.com/oauth2/authorize" +
			"?client_id=1065511513&response_type=code" +
			"&redirect_uri=https://api.weibo.com/oauth2/default.html";
	private String code_access_url = "https://api.weibo.com/oauth2/access_token";

	public AuthActivity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		context = this;

		init();
		initListener();
	}

	private void initListener() {
		rl_back.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					rl_back.setBackgroundResource(R.drawable.abs__list_focused_holo);
				} else {
					rl_back.setBackground(null);
					finish();
				}
				return true;
			}
		});

		rl_reload.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					rl_reload.setBackgroundResource(R.drawable.abs__list_focused_holo);
				} else {
					rl_reload.setBackground(null);
					iv_reload.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate));
					webView.reload(); // 刷新
				}
				return true;
			}
		});

	}

	// Web视图
	private class MyWebViewClient extends WebViewClient {
		private String result = "";
		private String myurl = "";

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			iv_reload.clearAnimation();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			myurl = url;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// POST方法获取access_token

					WeiboParameters params = new WeiboParameters();
					params.add("client_id", Constants.APP_KEY);
					params.add("client_secret", "df428e88aae8bd31f20481d149c856ed");
					params.add("grant_type", "authorization_code");
					params.add("redirect_uri", Constants.REDIRECT_URL);
					params.add("code", myurl.split("=")[1]);
					try {
						result = HttpManager.openUrl(code_access_url, "POST",
								params, null);
						JSONObject json = new JSONObject(result);
						UserInfo userInfo = new UserInfo(json.getString("uid"), "",
								json.getString("access_token"),
								json.getString("expires_in"), "1", "");

						//clone一个对象而不是引用
						WeiboApplication.userInfo = (UserInfo) userInfo.clone();
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_USER_DETAIL, taskParams);
						MainService.addTask(task);
						// 通知activity跳转
						handler.sendEmptyMessage(1);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			return true;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		MainService.addActivity(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MainService.removeActivity(this);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.show();
		}
	};

	@Override
	public void init() {
		getActionBar().hide();
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_reload = (RelativeLayout) findViewById(R.id.rl_reload);
		iv_reload = (ImageView) findViewById(R.id.btn_reload);
		webView = (WebView) findViewById(R.id.wv_auth);
		mInflater = LayoutInflater.from(context);
		dialog = new Dialog(context, R.style.simple_dialog);

		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(mInflater.inflate(R.layout.dlg_load_user, null));

		iv_reload.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

		// 设置WebView属性，能够执行Javascript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new MyWebViewClient());
		webView.clearCache(true);
		webView.clearHistory();
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.loadUrl(auth_url);
	}

	@Override
	public void refreshUI(Object... objects) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		User user = (User) objects[0];
		WeiboApplication.userInfo.setScreen_name(user.getScreen_name());
		WeiboApplication.userInfo.setUser_icon(user.getAvatar_large());
		if(UserInfoController.getUserInfoByDefault() != null) {
			UserInfo userInfo = UserInfoController.getUserInfoByDefault();
			userInfo.setIsDefault("0");
			UserInfoController.updateUserInfo(userInfo);
		}
		UserInfoController.insertUserInfo(WeiboApplication.userInfo);
		context.startActivity(new Intent(context, MainActivity.class));
		WelcomeActivity.context.finish();
		finish();
	}

}
