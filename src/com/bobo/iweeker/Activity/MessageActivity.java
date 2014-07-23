package com.bobo.iweeker.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Adapter.CommentMentionListAdapter;
import com.bobo.iweeker.Adapter.WeiboMentionListAdapter;
import com.bobo.iweeker.App.Preferences;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Comment;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Service.CommentService;
import com.bobo.iweeker.Service.MainService;
import com.bobo.iweeker.Service.StatuseService;
import com.bobo.iweeker.UI.XListView;
import com.bobo.iweeker.UI.XListView.IXListViewListener;
import com.bobo.iweeker.Utils.Utils;
import com.bobo.iweeker.Utils.WeiboSharedPreferencesUtils;

@SuppressLint("NewApi") public class MessageActivity extends SwipeBackActivity implements IWeekerActivityInterface, 
OnTouchListener {

	public static List<Statuse> statuses;
	public static List<Comment> comments;
	public static int statuses_page;
	public static int comments_page;
	public MessageActivity context;

	private XListView xListView;
	private JSONArray last_statuses;
	private JSONArray last_comments;
	private WeiboMentionListAdapter weiBoListAdapter;
	private CommentMentionListAdapter commentMentionListAdapter;

	private RelativeLayout rl_message_commment;
	private RelativeLayout rl_message_statuse;
	private RelativeLayout rl_back;
	private ImageView iv_loading;
	private TextView txt_message_comment_tab;
	private TextView txt_message_mention_tab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
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

		rl_message_commment = (RelativeLayout) findViewById(R.id.message_comment_tab);
		rl_message_statuse = (RelativeLayout) findViewById(R.id.message_mention_tab);
		rl_back = (RelativeLayout) findViewById(R.id.message_rl_back);
		xListView = (XListView) findViewById(R.id.xlv_message);
		iv_loading = (ImageView) findViewById(R.id.message_pg_load_weibo);
		txt_message_comment_tab = (TextView) findViewById(R.id.message_comment_tab_text);
		txt_message_mention_tab = (TextView) findViewById(R.id.message_mention_tab_text);
		
		
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		rl_message_commment.setSelected(true);
	}

	private void setListener() {
		xListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(rl_message_commment.isSelected()) {
					if (Utils.isNetworkConnected(context)) {
						comments_page = 1;
						xListView.setPullLoadEnable(true);
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_COMMENT_TIMELINE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopRefresh();
					}
				} else {
					if (Utils.isNetworkConnected(context)) {
						statuses_page = 1;
						xListView.setPullLoadEnable(true);
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_MENTION_TIMELINE, taskParams);
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
				if(rl_message_commment.isSelected()) {
					if (Utils.isNetworkConnected(context)) {
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_COMMENT_TIMELINE_MORE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopLoadMore();
					}
				} else {
					if (Utils.isNetworkConnected(context)) {
						HashMap<String, Object> taskParams = new HashMap<String, Object>();
						taskParams.put("instance", context.toString());
						Task task = new Task(TaskID.WEIBO_MENTION_TIMELINE_MORE, taskParams);
						MainService.addTask(task);
					} else {
						Utils.showNetWorkErrorToast(context);
						xListView.stopLoadMore();
					}
				}
			}
		});

		rl_message_commment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt_message_comment_tab.setTextColor(0xffdd3737);
				txt_message_mention_tab.setTextColor(0xff838181);
				rl_message_commment.setSelected(true);
				rl_message_statuse.setSelected(false);
				initData();
			}
		});

		rl_message_statuse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt_message_mention_tab.setTextColor(0xffdd3737);
				txt_message_comment_tab.setTextColor(0xff838181);
				rl_message_statuse.setSelected(true);
				rl_message_commment.setSelected(false);
				initData();
			}
		});
		
		rl_back.setOnTouchListener(context);
	}

	private void initData() {
		// 从本地读取上次退出时加载的微博信息
		last_comments = WeiboSharedPreferencesUtils.getMentionComment(context);
		last_statuses = WeiboSharedPreferencesUtils.getMentionWeiboStatuses(context);
		if(rl_message_commment.isSelected()) {
			if (last_comments == null) {
				if (Utils.isNetworkConnected(this)) {
					iv_loading.setVisibility(View.VISIBLE);
					xListView.setVisibility(View.GONE);
					iv_loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
					comments_page = 1;
					// 本地不存在则添加任务开始刷新微博信息
					HashMap<String, Object> taskParams = new HashMap<String, Object>();
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_COMMENT_TIMELINE, taskParams);
					MainService.addTask(task);
				} else {
					Utils.showNetWorkErrorToast(context);
					xListView.stopRefresh();
				}
			} else {
				// 将本地微博信息加载到UI
				comments = CommentService.getCommentsFromJSON(last_comments);
				refreshUI(comments, last_comments, TaskID.WEIBO_COMMENT_TIMELINE);
				xListView.setPullLoadEnable(false);
			}
		} else {
			if (last_statuses == null) {
				if (Utils.isNetworkConnected(this)) {
					iv_loading.setVisibility(View.VISIBLE);
					xListView.setVisibility(View.GONE);
					iv_loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
					statuses_page = 1;
					// 本地不存在则添加任务开始刷新微博信息
					HashMap<String, Object> taskParams = new HashMap<String, Object>();
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_MENTION_TIMELINE, taskParams);
					MainService.addTask(task);
				} else {
					Utils.showNetWorkErrorToast(context);
					xListView.stopRefresh();
				}
			} else {
				// 将本地微博信息加载到UI
				statuses = StatuseService.getStatusesFromJSON(last_statuses);
				refreshUI(statuses, last_statuses, TaskID.WEIBO_MENTION_TIMELINE);
				xListView.setPullLoadEnable(false);
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
		xListView.setVisibility(View.VISIBLE);
		if(rl_message_commment.isSelected()) {
			if (Integer.parseInt(objects[2].toString()) == TaskID.WEIBO_COMMENT_TIMELINE) {
				try {
					onLoad();

					comments = (ArrayList<Comment>) objects[0];
					last_comments = new JSONArray(objects[1].toString());

					if(comments.size() < Preferences.PAGE_SIZE)
						xListView.setPullLoadEnable(false);

					commentMentionListAdapter = new CommentMentionListAdapter(context, comments);
					xListView.setAdapter(commentMentionListAdapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				onLoad();
				ArrayList<Comment> more_comments = (ArrayList<Comment>) objects[0];
				comments.addAll(more_comments);

				commentMentionListAdapter.setComments(comments);
				commentMentionListAdapter.notifyDataSetChanged();
			}
		} else {
			if (Integer.parseInt(objects[2].toString()) == TaskID.WEIBO_MENTION_TIMELINE) {
				try {
					onLoad();

					statuses = (ArrayList<Statuse>) objects[0];
					last_statuses = new JSONArray(objects[1].toString());

					if(statuses.size() < Preferences.PAGE_SIZE)
						xListView.setPullLoadEnable(false);

					weiBoListAdapter = new WeiboMentionListAdapter(this, statuses);
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
		if(last_statuses != null)
			WeiboSharedPreferencesUtils.saveMentionWeiboStatuses(context, last_statuses);
		if(last_comments != null)
			WeiboSharedPreferencesUtils.saveCommentMentionWeiboStatuses(context, last_comments);
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
