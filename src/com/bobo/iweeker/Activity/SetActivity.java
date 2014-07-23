package com.bobo.iweeker.Activity;

import main.java.me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;

@SuppressLint("NewApi") public class SetActivity extends SwipeBackActivity implements IWeekerActivityInterface, 
OnTouchListener {

	public SetActivity context;

	private RelativeLayout rl_back;
	private RelativeLayout rl_refresh;
	private RelativeLayout rl_font;
	private RelativeLayout rl_cache;
	private RelativeLayout rl_image;
	private RelativeLayout rl_down;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		setEdgeFromLeft();
		init();
		setListener();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		context = this;
		getActionBar().hide();

		rl_back = (RelativeLayout) findViewById(R.id.set_rl_back);
		rl_refresh = (RelativeLayout) findViewById(R.id.set_rl_refresh);
		rl_font = (RelativeLayout) findViewById(R.id.set_rl_font);
		rl_cache = (RelativeLayout) findViewById(R.id.set_rl_cache);
		rl_image = (RelativeLayout) findViewById(R.id.set_rl_image);
		rl_down = (RelativeLayout) findViewById(R.id.set_rl_down);
	}

	private void setListener() {

		rl_back.setOnTouchListener(context);
		rl_refresh.setOnTouchListener(context);
		rl_font.setOnTouchListener(context);
		rl_cache.setOnTouchListener(context);
		rl_image.setOnTouchListener(context);
		rl_down.setOnTouchListener(context);
	}

	@Override
	public void refreshUI(Object... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.set_rl_back:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_back.setBackgroundResource(R.drawable.abs__list_focused_holo);
			} else {
				rl_back.setBackground(null);
				finish();
			}
			break;
		case R.id.set_rl_refresh:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_refresh.setBackgroundResource(R.color.light_gray);
			} else {
				rl_refresh.setBackground(null);
			}
			break;
		case R.id.set_rl_font:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_font.setBackgroundResource(R.color.light_gray);
			} else {
				rl_font.setBackground(null);
				String[] items = {"较大","普通","较小"};//列表中显示的内容组成的数组
				Builder dialog = new AlertDialog.Builder(SetActivity.this);
				dialog.setTitle("字体大小设置");//对话框标题
				dialog.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {//每一条的名称
					public void onClick(DialogInterface dialog, int which) {//响应点击事件
						switch (which) {
						case 0:
							dialog.dismiss();
							break;

						case 1:
							dialog.dismiss();
							break;

						case 2:
							dialog.dismiss();
							break;

						default:
							break;
						}
					}
				});
				dialog.show();
			}
			break;
		case R.id.set_rl_cache:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_cache.setBackgroundResource(R.color.light_gray);
			} else {
				rl_cache.setBackground(null);
				Builder dialog = new AlertDialog.Builder(SetActivity.this);
				dialog.setTitle("清空缓存");//对话框标题
				dialog.setMessage("是否清空缓存？");
				dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				dialog.show();	
			}
			break;
		case R.id.set_rl_image:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_image.setBackgroundResource(R.color.light_gray);
			} else {
				rl_image.setBackground(null);
				String[] items = {"中图","小图","无图"};//列表中显示的内容组成的数组
				Builder dialog = new AlertDialog.Builder(SetActivity.this);
				dialog.setTitle("微博图片显示模式");//对话框标题
				dialog.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {//每一条的名称
					public void onClick(DialogInterface dialog, int which) {//响应点击事件
						switch (which) {
						case 0:
							dialog.dismiss();
							break;

						case 1:
							dialog.dismiss();
							break;

						case 2:
							dialog.dismiss();
							break;

						default:
							break;
						}
					}
				});
				dialog.show();
			} 
			break;
		case R.id.set_rl_down:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_down.setBackgroundResource(R.color.light_gray);
			} else {
				rl_down.setBackground(null);
				String[] items = {"中图","小图","无图"};//列表中显示的内容组成的数组
				Builder dialog = new AlertDialog.Builder(SetActivity.this);
				dialog.setTitle("下载图片质量");//对话框标题
				dialog.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {//每一条的名称
					public void onClick(DialogInterface dialog, int which) {//响应点击事件
						switch (which) {
						case 0:
							dialog.dismiss();
							break;

						case 1:
							dialog.dismiss();
							break;

						case 2:
							dialog.dismiss();
							break;

						default:
							break;
						}
					}
				});
				dialog.show();
			}
			break;

		default:
			break;
		}

		return true;
	}

}
