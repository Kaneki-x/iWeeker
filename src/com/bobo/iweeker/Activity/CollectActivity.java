package com.bobo.iweeker.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.bobo.iweeker.Adapter.WeiboListAdapter;
import com.bobo.iweeker.App.Preferences;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Service.MainService;
import com.bobo.iweeker.Service.StatuseService;
import com.bobo.iweeker.UI.XListView;
import com.bobo.iweeker.UI.XListView.IXListViewListener;
import com.bobo.iweeker.Utils.Utils;
import com.bobo.iweeker.Utils.WeiboSharedPreferencesUtils;

@SuppressLint("NewApi") public class CollectActivity extends SwipeBackActivity implements IWeekerActivityInterface, 
OnTouchListener {

	public static List<Statuse> statuses;
	public static int statuses_page;
	public CollectActivity context;

	private XListView xListView;
	private JSONArray last_statuses;
	private WeiboListAdapter weiBoListAdapter;

	private RelativeLayout rl_back;
	private ImageView iv_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
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

		rl_back = (RelativeLayout) findViewById(R.id.collect_rl_back);
		xListView = (XListView) findViewById(R.id.xlv_collect);
		iv_loading = (ImageView) findViewById(R.id.collect_pg_load_weibo);

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
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_COLLECT_TIMELINE, taskParams);
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
					Task task = new Task(TaskID.WEIBO_COLLECT_TIMELINE_MORE, taskParams);
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
		
		rl_back.setOnTouchListener(context);
	}

	private void initData() {
		// 从本地读取上次退出时加载的微博信息
		last_statuses = WeiboSharedPreferencesUtils.getCollectWeiboStatuses(context);

		if (last_statuses == null) {
			if (Utils.isNetworkConnected(this)) {
				iv_loading.setVisibility(View.VISIBLE);
				iv_loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
				statuses_page = 1;
				// 本地不存在则添加任务开始刷新微博信息
				HashMap<String, Object> taskParams = new HashMap<String, Object>();
				taskParams.put("instance", context.toString());
				Task task = new Task(TaskID.WEIBO_COLLECT_TIMELINE, taskParams);
				MainService.addTask(task);
			} else {
				Utils.showNetWorkErrorToast(context);
				xListView.stopRefresh();
			}
		} else {
			// 将本地微博信息加载到UI
			try {
				statuses = new ArrayList<Statuse>();
				for(int i = 0; i < last_statuses.length(); i++) {
					JSONObject status = last_statuses.getJSONObject(i).getJSONObject("status");
					if(StatuseService.getStatuseFromJSON(status) != null)
						statuses.add(StatuseService.getStatuseFromJSON(status));
				}	
				refreshUI(statuses, last_statuses, TaskID.WEIBO_COLLECT_TIMELINE);
				xListView.setPullLoadEnable(false);
			}catch (Exception e) {
				// TODO: handle exception
			}
			
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
		// TODO Auto-generated method stub
		iv_loading.clearAnimation();
		iv_loading.setVisibility(View.GONE);
		if (Integer.parseInt(objects[2].toString()) == TaskID.WEIBO_COLLECT_TIMELINE) {
			try {
				onLoad();

				statuses = (ArrayList<Statuse>) objects[0];
				last_statuses = new JSONArray(objects[1].toString());

				if(statuses.size() < Preferences.PAGE_SIZE)
					xListView.setPullLoadEnable(false);

				weiBoListAdapter = new WeiboListAdapter(context, statuses);
				xListView.setAdapter(weiBoListAdapter);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			onLoad();
			ArrayList<Statuse> more_statuses = (ArrayList<Statuse>) objects[0];
			statuses.addAll(more_statuses);

			weiBoListAdapter.setStatuses(statuses);
			weiBoListAdapter.notifyDataSetChanged();
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
		WeiboSharedPreferencesUtils.saveCollectWeiboStatuses(context, last_statuses);
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
