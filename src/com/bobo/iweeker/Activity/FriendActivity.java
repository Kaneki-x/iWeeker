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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Adapter.FriendListAdapter;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.Service.MainService;
import com.bobo.iweeker.UI.XListView;
import com.bobo.iweeker.UI.XListView.IXListViewListener;
import com.bobo.iweeker.Utils.Utils;

@SuppressLint("NewApi") public class FriendActivity extends SwipeBackActivity implements IWeekerActivityInterface, 
OnTouchListener {

	public static List<User> users;
	public static int friend_page;
	public static int follow_page;
	public FriendActivity context;

	private XListView xListView;
	private FriendListAdapter friendListAdapter;

	private RelativeLayout rl_back;
	private ImageView iv_loading;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
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

		rl_back = (RelativeLayout) findViewById(R.id.friend_rl_back);
		xListView = (XListView) findViewById(R.id.xlv_friend);
		iv_loading = (ImageView) findViewById(R.id.friend_pg_load_weibo);
		tv_title = (TextView) findViewById(R.id.friend_tv_title);

		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
	}

	private void setListener() {
		xListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(getIntent().getIntExtra("type", 0) == 0) {
					if (Utils.isNetworkConnected(context)) {
						friend_page = 0;
						xListView.setPullLoadEnable(true);
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("name", getIntent().getStringExtra("name"));
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_FRIEND_TIMELINE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopRefresh();
					}
				} else {
					if (Utils.isNetworkConnected(context)) {
						follow_page = 0;
						xListView.setPullLoadEnable(true);
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("name", getIntent().getStringExtra("name"));
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_FOLLOW_TIMELINE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopRefresh();
					}
				}
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if(getIntent().getIntExtra("type", 0) == 0) {
					if (Utils.isNetworkConnected(context)) {
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("name", getIntent().getStringExtra("name"));
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_FRIEND_TIMELINE_MORE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopLoadMore();
					}
				} else {
					if (Utils.isNetworkConnected(context)) {
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("name", getIntent().getStringExtra("name"));
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_FOLLOW_TIMELINE_MORE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopLoadMore();
					}
				}
			}
		});
		
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("name", "");
				intent.putExtra("user", users.get(arg2 -1));
				intent.setClass(context, ProfileActivity.class);
				context.startActivity(intent);
			}
		});

		rl_back.setOnTouchListener(context);
	}

	private void initData() {
		if(getIntent().getIntExtra("type", 0) == 0) {
			tv_title.setText("关注列表");
			HashMap<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put("name", getIntent().getStringExtra("name"));
			taskParams.put("instance", context.toString());
			Task task = new Task(TaskID.WEIBO_FRIEND_TIMELINE, taskParams);
			MainService.addTask(task);
		} else {
			tv_title.setText("粉丝列表");
			HashMap<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put("name", getIntent().getStringExtra("name"));
			taskParams.put("instance", context.toString());
			Task task = new Task(TaskID.WEIBO_FOLLOW_TIMELINE, taskParams);
			MainService.addTask(task);
		}
		iv_loading.setVisibility(View.VISIBLE);
		iv_loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime(Utils.getRefreshTime(xListView.getRefreshTime()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refreshUI(Object... objects) {
		// TODO Auto-generated method stub
		iv_loading.clearAnimation();
		iv_loading.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		if(getIntent().getIntExtra("type", 0) == 0) {
			if (Integer.parseInt(objects[2].toString()) == TaskID.WEIBO_FRIEND_TIMELINE) {
				onLoad();

				users = (ArrayList<User>) objects[0];

				if(users.size() < 50)
					xListView.setPullLoadEnable(false);

				friendListAdapter = new FriendListAdapter(context, users);
				xListView.setAdapter(friendListAdapter);

			} else {
				onLoad();
				ArrayList<User> more_users = (ArrayList<User>) objects[0];
				users.addAll(more_users);

				friendListAdapter.setUsers(users);
				friendListAdapter.notifyDataSetChanged();
			}
		} else {
			if (Integer.parseInt(objects[2].toString()) == TaskID.WEIBO_FOLLOW_TIMELINE) {
				onLoad();

				users = (ArrayList<User>) objects[0];

				if(users.size() < 50)
					xListView.setPullLoadEnable(false);

				friendListAdapter = new FriendListAdapter(context, users);
				xListView.setAdapter(friendListAdapter);
			} else {
				onLoad();
				ArrayList<User> more_users = (ArrayList<User>) objects[0];
				users.addAll(more_users);

				friendListAdapter.setUsers(users);
				friendListAdapter.notifyDataSetChanged();
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
