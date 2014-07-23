
package com.bobo.iweeker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Controller.UserInfoController;
import com.bobo.iweeker.DB.DBHelper;
import com.bobo.iweeker.Service.MainService;

public class WelcomeActivity extends Activity {

	private ImageView img_logo;
	private Button btn_login;
	private AlphaAnimation animation;

	public static WelcomeActivity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_login);

		context.startService(new Intent(context,
				MainService.class));
		
		WeiboApplication.dbHelper = new DBHelper(this);

		WeiboApplication.userInfo = UserInfoController.getUserInfoByDefault();
		initView();
	}

	private void initView() {
		img_logo = (ImageView) findViewById(R.id.logo_bg);
		btn_login = (Button) findViewById(R.id.btn_login);

		animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(2000);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (btn_login.getVisibility() == View.GONE) {
					context.startActivity(new Intent(context,
							MainActivity.class));
					finish();
				}
			}
		});

		// 不存在用户则进行授权
		if (WeiboApplication.userInfo == null)
			btn_login.setVisibility(View.VISIBLE);

		img_logo.setAnimation(animation);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WelcomeActivity.this, AuthActivity.class));
			}
		});

	}
}
