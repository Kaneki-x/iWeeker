package com.bobo.iweeker.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Adapter.WeiboProfileListAdapter;
import com.bobo.iweeker.App.Preferences;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.Service.MainService;
import com.bobo.iweeker.UI.XListView;
import com.bobo.iweeker.UI.XListView.IXListViewListener;
import com.bobo.iweeker.Utils.Utils;

@SuppressLint("NewApi") public class ProfileActivity extends SwipeBackActivity implements IWeekerActivityInterface, 
OnTouchListener {

	public List<Statuse> statuses;
	public User user;
	public static int statuses_page;
	public ProfileActivity context;

	private XListView xListView;
	private WeiboProfileListAdapter weiBoListAdapter;

	private RelativeLayout rl_back;
	private ImageView iv_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setEdgeFromLeft();
		init();
		setListener();
		initData();
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		context = this;
		getActionBar().hide();

		rl_back = (RelativeLayout) findViewById(R.id.profile_rl_back);
		xListView = (XListView) findViewById(R.id.xlv_profile);
		iv_loading = (ImageView) findViewById(R.id.profile_pg_load_weibo);

		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
	}

	private void setListener() {
		xListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (Utils.isNetworkConnected(context)) {
					statuses_page = 1;
					xListView.setPullLoadEnable(true);
					HashMap<String, Object> taskParams = new HashMap<String, Object>();
					taskParams.put("name", user.getScreen_name());
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_PROFILE_TIMELINE, taskParams);
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
					taskParams.put("name", user.getScreen_name());
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_PROFILE_TIMELINE_MORE, taskParams);
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
				intent.putExtra("statuse", statuses.get(position - 2));
				startActivity(intent);
			}
		});
		
		rl_back.setOnTouchListener(context);
	}

	private void initData() {
		statuses_page = 1;
		Intent intent = getIntent();
		iv_loading.setVisibility(View.VISIBLE);
		iv_loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
		xListView.setVisibility(View.GONE);
		if(intent.getStringExtra("name").equals("")) {
			
			user = (User) intent.getSerializableExtra("user");

			HashMap<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put("name", user.getScreen_name());
			taskParams.put("instance", context.toString());
			Task task = new Task(TaskID.WEIBO_PROFILE_TIMELINE, taskParams);
			MainService.addTask(task);
		} else {
			String name = intent.getStringExtra("name");
			HashMap<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put("name", name);
			taskParams.put("instance", context.toString());
			Task task = new Task(TaskID.WEIBO_PROFILE_USERINFO, taskParams);
			MainService.addTask(task);
			task = new Task(TaskID.WEIBO_PROFILE_TIMELINE, taskParams);
			MainService.addTask(task);
		}
	}
	
	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime(Utils.getRefreshTime(xListView.getRefreshTime()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refreshUI(Object... objects) {
		iv_loading.clearAnimation();
		iv_loading.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		switch (Integer.parseInt(objects[2].toString())) {
		case TaskID.WEIBO_PROFILE_USERINFO:
			user = (User) objects[0];

			if(weiBoListAdapter == null ) {
				weiBoListAdapter = new WeiboProfileListAdapter(context, new ArrayList<Statuse>(), user);
				xListView.setAdapter(weiBoListAdapter);
				xListView.setPullLoadEnable(false);
				xListView.setPullRefreshEnable(false);
			} else {
				weiBoListAdapter.setUser(user);
				weiBoListAdapter.notifyDataSetChanged();
			}
			break;

		case TaskID.WEIBO_PROFILE_TIMELINE:
			onLoad();
			xListView.setPullLoadEnable(true);
			xListView.setPullRefreshEnable(true);
			statuses = new ArrayList<Statuse>();
			statuses.addAll((ArrayList<Statuse>) objects[0]);
			if(statuses.size() < Preferences.PAGE_SIZE)
				xListView.setPullLoadEnable(false);
			
			if(weiBoListAdapter == null ) {
				weiBoListAdapter = new WeiboProfileListAdapter(context, statuses, user);
				xListView.setAdapter(weiBoListAdapter);
			} else {
				weiBoListAdapter.setStatuses(statuses);
				weiBoListAdapter.notifyDataSetChanged();
			}
			break;

		case TaskID.WEIBO_PROFILE_TIMELINE_MORE:
			onLoad();
			ArrayList<Statuse> more_statuses = (ArrayList<Statuse>) objects[0];
			statuses.addAll(more_statuses);

			weiBoListAdapter.setStatuses(statuses);
			weiBoListAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		iv_loading.clearAnimation();
		iv_loading.setVisibility(View.GONE);
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
		MainService.removeActivity(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			rl_back.setBackgroundResource(R.drawable.abs__list_focused_holo);
		} else {
			rl_back.setBackground(null);
			finish();
		}
		return true;
	}

}
