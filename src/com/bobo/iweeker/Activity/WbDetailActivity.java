
package com.bobo.iweeker.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Adapter.WeiboDetailAdapter;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Comment;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Service.CommentService;
import com.bobo.iweeker.Service.MainService;
import com.bobo.iweeker.UI.XListView;
import com.bobo.iweeker.UI.XListView.IXListViewListener;
import com.bobo.iweeker.Utils.Utils;
import com.bobo.iweeker.Utils.emotions.EmotionsParse;

@SuppressLint("NewApi") public class WbDetailActivity extends SwipeBackActivity implements IWeekerActivityInterface,
OnTouchListener {

	public static int comment_page = 1;
	public static boolean comment;
	private static Statuse statuse;
	private static List<Comment> comments;
	private static List<Comment> requests;
	private WbDetailActivity context;
	private EmotionsParse emotionsParse;
	private String[] emotion_array;

	private RelativeLayout rl_detail_back;
	private RelativeLayout rl_emotions;
	private EditText et_comment;
	private ImageView iv_loading;
	private ImageView iv_emotion;
	private LayoutInflater mInflater;
	private ViewPager viewPager;

	private static XListView xListView;
	private static WeiboDetailAdapter weiboDetailAdapter;

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			List<Comment> temp_comments = new ArrayList<Comment>();
			if (msg.what == 0) {                
				temp_comments.addAll(comments);
			} else {
				temp_comments.addAll(requests);
			}
			weiboDetailAdapter.setComments(temp_comments);
			weiboDetailAdapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		setEdgeFromLeft();

		init();

		initEmotionView();

		setListener();

		loadCommentsByTask();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		context = this;
		getActionBar().hide();

		rl_detail_back = (RelativeLayout) findViewById(R.id.rl_detail_back);
		rl_emotions = (RelativeLayout) findViewById(R.id.rl_emotions);
		et_comment = (EditText) findViewById(R.id.et_comment);
		iv_loading = (ImageView) findViewById(R.id.pg_load_detail);
		iv_emotion = (ImageView) findViewById(R.id.iv_emotion);
		xListView = (XListView) findViewById(R.id.xlv_detail);

		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);

		iv_loading.setVisibility(View.VISIBLE);
		iv_loading.setAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
		statuse = (Statuse) getIntent().getSerializableExtra("statuse");
	}

	private void initEmotionView() {
		emotionsParse = new EmotionsParse();
		mInflater = LayoutInflater.from(this);
		viewPager = (ViewPager) findViewById(R.id.vp_emotions);
		emotion_array = getResources().getStringArray(R.array.default_emotions);

		viewPager.setAdapter(new SamplePagerAdapter());
	}

	private void setIndicatorView(int position) {
		switch (position) {
		case 0:
			((ImageView) findViewById(R.id.iv_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			((ImageView) findViewById(R.id.iv_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator);
			break;
		case 1:
			((ImageView) findViewById(R.id.iv_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			((ImageView) findViewById(R.id.iv_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator);
			break;
		case 2:
			((ImageView) findViewById(R.id.iv_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			((ImageView) findViewById(R.id.iv_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator);
			break;
		case 3:
			((ImageView) findViewById(R.id.iv_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			break;

		default:
			break;
		}
	}

	private class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = mInflater.inflate(R.layout.emotion_gridview, null);
			GridView gridView = (GridView) view.findViewById(R.id.emotion_view);
			gridView.setAdapter(new SampleGridViewAdapter(position));

			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	class SampleGridViewAdapter extends BaseAdapter{ 

		int pager;

		public SampleGridViewAdapter(int pager) {
			super();
			this.pager = pager;
		}

		public int getCount() { 
			if (pager != 3)
				return 24;
			else
				return emotion_array.length - pager * 23;
		} 

		public Object getItem(int item) { 
			return item; 
		} 

		public long getItemId(int id) { 
			return id; 
		} 

		//创建View方法 
		public View getView(int position, View convertView, ViewGroup parent) { 
			View view = mInflater.inflate(R.layout.emotion_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.send_emotion);
			if (position == getCount() - 1)
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.emotion_delete));
			else
				imageView.setImageDrawable(emotionsParse.getEmotionByName(emotion_array[pager * 23 + position]));
			return view; 
		} 
	} 

	private void setListener() {
		rl_detail_back.setOnTouchListener(context);

		et_comment.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					rl_emotions.setVisibility(View.GONE);
			}
		});

		et_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rl_emotions.setVisibility(View.GONE);
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setIndicatorView(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		iv_emotion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rl_emotions.getVisibility() == View.VISIBLE) {
					rl_emotions.setVisibility(View.GONE);
				} else {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
					imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
					rl_emotions.setVisibility(View.VISIBLE);
				}
			}
		});

		xListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (Utils.isNetworkConnected(context)) {
					xListView.setPullLoadEnable(true);
					comment_page = 1;
					loadCommentsByTask();
				} else {
					Utils.showNetWorkErrorToast(context);
					xListView.stopLoadMore();
				}
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (Utils.isNetworkConnected(context)) {
					HashMap<String, Object> taskParams = new HashMap<String, Object>();
					taskParams.put("id", statuse.getIdstr());
					taskParams.put("instance", context.toString());
					Task task = new Task(TaskID.WEIBO_DETAIL_COMMENT_MORE, taskParams);
					MainService.addTask(task);
				} else {
					Utils.showNetWorkErrorToast(context);
					xListView.stopLoadMore();
				}
			}
		});
	}

	private void loadCommentsByTask() {
		// 查询评论
		HashMap<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put("id", statuse.getIdstr());
		taskParams.put("instance", context.toString());
		Task task = new Task(TaskID.WEIBO_DETAIL_COMMENT, taskParams);
		MainService.addTask(task);
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
		comment = true;
		MainService.removeActivity(this);
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
		if (objects.length == 1) {
			onLoad();
			comments = CommentService.getCommentFromComments((List<Comment>) objects[0], statuse);
			requests = CommentService.getRequestFromComments((List<Comment>) objects[0], statuse);

			if (comment) {
				if (comments.size() == 0)
				{
					comments = new ArrayList<Comment>();
					xListView.setPullLoadEnable(false);
				}

				weiboDetailAdapter = new WeiboDetailAdapter(context, comments, statuse);
				xListView.setAdapter(weiboDetailAdapter);
			} else {
				if (requests.size() == 0)
				{
					requests = new ArrayList<Comment>();
					xListView.setPullLoadEnable(false);
				}

				weiboDetailAdapter = new WeiboDetailAdapter(context, requests, statuse);
				xListView.setAdapter(weiboDetailAdapter);
			}

		} else {
			onLoad();
			if (comment) {
				List<Comment> more_comments = CommentService.getCommentFromComments((List<Comment>) objects[0], statuse);
				comments.addAll(more_comments);

				weiboDetailAdapter.setComments(comments);
				weiboDetailAdapter.notifyDataSetChanged();

			} else {
				List<Comment> more_requests = CommentService.getRequestFromComments((List<Comment>) objects[0], statuse);
				requests.addAll(more_requests);
				weiboDetailAdapter.setComments(requests);
				weiboDetailAdapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rl_detail_back) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_detail_back.setBackgroundResource(R.drawable.abs__list_focused_holo);
			} else {
				rl_detail_back.setBackground(null);
				scrollToFinishActivity();
			}
		}
		return true;
	}

}
