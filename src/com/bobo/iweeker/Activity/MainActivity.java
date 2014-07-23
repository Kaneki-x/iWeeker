
package com.bobo.iweeker.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Adapter.WeiboListAdapter;
import com.bobo.iweeker.App.Preferences;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Controller.UserInfoController;
import com.bobo.iweeker.ImgCache.ImageManager;
import com.bobo.iweeker.ImgCache.SimpleImageLoader;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Model.UserInfo;
import com.bobo.iweeker.Service.MainService;
import com.bobo.iweeker.Service.StatuseService;
import com.bobo.iweeker.UI.XListView;
import com.bobo.iweeker.UI.XListView.IXListViewListener;
import com.bobo.iweeker.Utils.Utils;
import com.bobo.iweeker.Utils.WeiboSharedPreferencesUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

/**
 * @author echo
 */
@SuppressLint("NewApi") public class MainActivity extends SlidingActivity implements IWeekerActivityInterface,
OnTouchListener {

	public static List<Statuse> statuses;
	public static List<Statuse> more_statuses;
	public LayoutInflater mInflater;
	public MainActivity context;

	private XListView xListView;
	private JSONArray last_statuses;
	private WeiboListAdapter weiBoListAdapter;

	private TextView tv_main;
	private ImageView iv_loading;
	private ImageView iv_send;
	private RelativeLayout rl_menu;
	private View view;
	private TextView menu_user_name;
	private PopupWindow popupWindow;

	private Button btn_message;
	private Button btn_favourite;
	private Button btn_main;
	private Button btn_profile;
	private Button btn_logout;
	private Button btn_setting;
	private Spinner spinner_avatar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		initSlideMenu();
		setListener();
		initTimeLine();
		initPopupGroup();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		context = this;
		getActionBar().hide();

		mInflater = LayoutInflater.from(context);
		view = mInflater.inflate(R.layout.slidingdrawer_contents, null);

		menu_user_name = (TextView) view.findViewById(R.id.nickname);
		xListView = (XListView) findViewById(R.id.xlv_home);
		iv_loading = (ImageView) findViewById(R.id.pg_load_weibo);
		iv_send = (ImageView) findViewById(R.id.title_tv_main_write);
		tv_main = (TextView) findViewById(R.id.title_tv_main);
		rl_menu = (RelativeLayout) findViewById(R.id.rl_menu);

		btn_message = (Button) view.findViewById(R.id.btn_message);
		btn_favourite = (Button) view.findViewById(R.id.btn_favourite);
		btn_profile = (Button) view.findViewById(R.id.btn_profile);
		btn_main = (Button) view.findViewById(R.id.btn_main);
		btn_setting = (Button) view.findViewById(R.id.btn_setting);
		btn_logout = (Button) view.findViewById(R.id.btn_logout);
		spinner_avatar = (Spinner) view.findViewById(R.id.spinner_avatar);

		spinner_avatar.setAdapter(new AvatarAdapter(spinner_avatar));

		menu_user_name.setText(WeiboApplication.userInfo.getScreen_name());

		tv_main.setText(WeiboApplication.userInfo.getScreen_name());
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
	}

	private void initSlideMenu() {
		setBehindContentView(view);
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(20);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(Utils.getDisplayWidthAndHeight(this)[0] / 3);
		sm.setFadeDegree(0.35f);
		// 设置slding menu的几种手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
		// TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding
		// menu
		// TOUCHMODE_NONE 自然是不能通过手势打开啦
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);		
	}

	private void setListener() {
		xListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (Utils.isNetworkConnected(context)) {
					Preferences.PAGE = 1;
					xListView.setPullLoadEnable(true);
					HashMap<String, Object> taskParams = new HashMap<String, Object>();
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_HOME_TIMELINE, taskParams);
					MainService.addTask(task);
				} else {
					Utils.showNetWorkErrorToast(context);
					xListView.stopRefresh();
				}
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (Utils.isNetworkConnected(context)) {
					HashMap<String, Object> taskParams = new HashMap<String, Object>();
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_HOME_TIMELINE_MORE, taskParams);
					MainService.addTask(task);
				} else {
					Utils.showNetWorkErrorToast(context);
					xListView.stopLoadMore();
				}
			}
		});

		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, WbDetailActivity.class);
				intent.putExtra("statuse", statuses.get(position - 1));
				startActivity(intent);
			}
		});

		iv_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context, WbSendActivity.class));
			}
		});

		tv_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 显示窗口 
				popupWindow.showAsDropDown(v); 
			}
		});

		btn_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, MessageActivity.class);
				startActivity(intent);
			}
		});

		btn_favourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, CollectActivity.class);
				startActivity(intent);
			}
		});

		btn_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggle();
			}
		});

		btn_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("name", WeiboApplication.userInfo.getScreen_name());
				intent.setClass(context, ProfileActivity.class);
				startActivity(intent);
			}
		});

		btn_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, SetActivity.class);
				startActivity(intent);
			}
		});

		btn_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserInfo userInfo = WeiboApplication.userInfo;
				userInfo.setIsDefault("0");
				UserInfoController.updateUserInfo(userInfo);

				finish();
				Intent intent = new Intent();
				intent.setClass(context, WelcomeActivity.class);
				startActivity(intent);
			}
		});

		rl_menu.setOnTouchListener(this);
	}

	private void initTimeLine() {
		// 从本地读取上次退出时加载的微博信息
		last_statuses = WeiboSharedPreferencesUtils.getWeiboStatuses(this);

		if (last_statuses == null) {
			if (Utils.isNetworkConnected(this)) {
				iv_loading.setVisibility(View.VISIBLE);
				iv_loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
				// 本地不存在则添加任务开始刷新微博信息
				HashMap<String, Object> taskParams = new HashMap<String, Object>();
				taskParams.put("instance", context.toString());
				Task task = new Task(TaskID.WEIBO_HOME_TIMELINE, taskParams);
				MainService.addTask(task);
			} else {
				Utils.showNetWorkErrorToast(context);
				xListView.stopRefresh();
			}
		} else {
			// 将本地微博信息加载到UI
			statuses = StatuseService.getStatusesFromJSON(last_statuses);
			refreshUI(statuses, last_statuses, TaskID.WEIBO_HOME_TIMELINE);
			xListView.setPullLoadEnable(false);
		}
	}

	@SuppressWarnings("deprecation")
	private void initPopupGroup() {
		// 引入窗口配置文件 
		View view = mInflater.inflate(R.layout.popup_group_list, null);

		((ListView) view.findViewById(R.id.list_group)).setAdapter(new MyGroupAdapter());
		// 创建PopupWindow对象 
		popupWindow = new PopupWindow(view, 180, LayoutParams.WRAP_CONTENT, false); 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true); 
		// 设置此参数获得焦点，否则无法点击 
		popupWindow.setFocusable(true);
	}

	private class AvatarAdapter extends BaseAdapter {

		ArrayList<UserInfo> data = new ArrayList<UserInfo>();

		int count = 0;

		public AvatarAdapter(Spinner spinner) {
			data.addAll(UserInfoController.getAllUserInfo());
			if (data.size() == 1) {
				count = 1;
			} else {
				count = data.size() - 1;
			}
			Iterator<UserInfo> iterator = data.iterator();
			while (iterator.hasNext()) {
				UserInfo accountBean = iterator.next();
				if (accountBean.getUid()
						.equals(WeiboApplication.userInfo.getUid())) {
					iterator.remove();
					break;
				}
			}

		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = mInflater.inflate(R.layout.slidingdrawer_avatar, parent, false);
			ImageView iv = (ImageView) view.findViewById(R.id.avatar);
			
			if(null != WeiboApplication.userInfo)
				SimpleImageLoader.showImg(iv, WeiboApplication.userInfo.getUser_icon(), ImageManager.USER_DEFAULT);
		
			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View view = mInflater
					.inflate(R.layout.slidingdrawer_avatar_dropdown, parent, false);
			TextView nickname = (TextView) view.findViewById(R.id.nickname);
			ImageView avatar = (ImageView) view.findViewById(R.id.avatar);

			if (data.size() > 0) {
				final UserInfo accountBean = data.get(position);
				
				SimpleImageLoader.showImg(avatar, accountBean.getUser_icon(), ImageManager.USER_DEFAULT);
				
				nickname.setText(accountBean.getScreen_name());

				view.setOnClickListener(new MyAccountOnClickListener(accountBean));
			} else {
				avatar.setVisibility(View.GONE);
				nickname.setTextColor(getResources().getColor(R.color.gray));
				nickname.setText("不存在其他授权用户");
			}
			return view;
		}
		
		private class MyAccountOnClickListener implements OnClickListener {
			private UserInfo account;
			
			public MyAccountOnClickListener(UserInfo account) {
				this.account = account;
			}
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserInfo userInfo = UserInfoController.getUserInfoByDefault();
				userInfo.setIsDefault("0");
				UserInfoController.updateUserInfo(userInfo);
				account.setIsDefault("1");
				UserInfoController.updateUserInfo(account);
				
				Intent intent = new Intent();
				intent.setClass(context, WelcomeActivity.class);
				context.startActivity(intent);
			}
			
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MainService.addActivity(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 将当前微博信息保存到本地方便下次快速加载
		WeiboSharedPreferencesUtils.saveWeiboStatuses(this, last_statuses);
		MainService.removeActivity(this);
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_menu) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_menu.setBackgroundResource(R.drawable.abs__list_focused_holo);
			} else {
				rl_menu.setBackground(null);
				toggle();
			}
		}
		return true;
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime(Utils.getRefreshTime(xListView.getRefreshTime()));
	}

	private int backClickTime;

	/** 连续按back键2次(间隔小于3秒 ) */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (backClickTime == 1) {
				finish();
				backClickTime = 0;
			} else {
				Toast.makeText(this, "再次点击返回退出", Toast.LENGTH_SHORT)
				.show();
				backClickTime++;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						backClickTime = 0;
					}
				}, 3000);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refreshUI(Object... objects) {
		// TODO Auto-generated method stub
		iv_loading.clearAnimation();
		iv_loading.setVisibility(View.GONE);
		if (Integer.parseInt(objects[2].toString()) == TaskID.WEIBO_HOME_TIMELINE) {
			try {
				onLoad();

				statuses = (ArrayList<Statuse>) objects[0];
				last_statuses = new JSONArray(objects[1].toString());

				weiBoListAdapter = new WeiboListAdapter(this, statuses);
				xListView.setAdapter(weiBoListAdapter);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			onLoad();

			more_statuses = (ArrayList<Statuse>) objects[0];
			statuses.addAll(more_statuses);

			weiBoListAdapter.setStatuses(statuses);
			weiBoListAdapter.notifyDataSetChanged();

		}
	}

	private class MyGroupAdapter extends BaseAdapter {
		String[] str = {"所有人","同事","同学","企业","朋友","dota"};

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mInflater.inflate(R.layout.group_list_item, null);
			TextView textView = (TextView) convertView.findViewById(R.id.tv_group_item);
			textView.setText(str[position]);
			return convertView;
		}
	}
}
