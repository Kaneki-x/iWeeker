
package com.bobo.iweeker.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.bobo.iweeker.Activity.CollectActivity;
import com.bobo.iweeker.Activity.FriendActivity;
import com.bobo.iweeker.Activity.MessageActivity;
import com.bobo.iweeker.Activity.ProfileActivity;
import com.bobo.iweeker.Activity.WbDetailActivity;
import com.bobo.iweeker.App.Preferences;
import com.bobo.iweeker.App.TaskID;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Model.Comment;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.Task;
import com.bobo.iweeker.Model.User;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.CommentsAPI;
import com.weibo.sdk.android.api.FavoritesAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

public class MainService extends Service implements Runnable{

	// 是否运行线程
	private boolean isRun = false;

	// 任务队列
	private static Queue<Task> tasks = new LinkedList<Task>();

	// UI队列
	private static ArrayList<Activity> appLists = new ArrayList<Activity>();

	// 当前微博列表
	public static List<Statuse> now_statuses;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressLint({
			"HandlerLeak", "HandlerLeak"
		})
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case TaskID.WEIBO_USER_DETAIL:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, TaskID.WEIBO_USER_DETAIL);
				}
				break;

			case TaskID.WEIBO_MENTION_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, bundle.get("last_statuses"),
								TaskID.WEIBO_MENTION_TIMELINE);
				}
				break;

			case TaskID.WEIBO_COMMENT_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, bundle.get("last_comments"),
								TaskID.WEIBO_COMMENT_TIMELINE);
				}
				break;

			case TaskID.WEIBO_COMMENT_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_COMMENT_TIMELINE_MORE);
				}
				break;

			case TaskID.WEIBO_MENTION_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_MENTION_TIMELINE_MORE);
				}
				break;

			case TaskID.WEIBO_HOME_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, bundle.get("last_statuses"),
								TaskID.WEIBO_HOME_TIMELINE);
				}
				break;
				
			case TaskID.WEIBO_COLLECT_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, bundle.get("last_statuses"),
								TaskID.WEIBO_COLLECT_TIMELINE);
				}
				break;
				
			case TaskID.WEIBO_COLLECT_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_COLLECT_TIMELINE_MORE);
				}
				break;

			case TaskID.WEIBO_HOME_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_HOME_TIMELINE_MORE);
				}
				break;

			case TaskID.WEIBO_DETAIL_COMMENT:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj);
				}
				break;

			case TaskID.WEIBO_DETAIL_COMMENT_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_HOME_TIMELINE_MORE);
				}
				break;
				
			case TaskID.WEIBO_PROFILE_USERINFO:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_PROFILE_USERINFO);
				}
				break;
				
			case TaskID.WEIBO_PROFILE_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_PROFILE_TIMELINE);
				}
				break;
				
			case TaskID.WEIBO_PROFILE_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_PROFILE_TIMELINE_MORE);
				}
				break;
				
			case TaskID.WEIBO_FRIEND_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_FRIEND_TIMELINE);
				}
				break;
				
			case TaskID.WEIBO_FRIEND_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_FRIEND_TIMELINE_MORE);
				}
				break;
				
			case TaskID.WEIBO_FOLLOW_TIMELINE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_FOLLOW_TIMELINE);
				}
				break;
				
			case TaskID.WEIBO_FOLLOW_TIMELINE_MORE:
				if (msg.obj != null) {
					Bundle bundle = msg.getData();
					IWeekerActivityInterface activity = (IWeekerActivityInterface) getActivityByName(bundle.getString("instance"));
					if (activity != null)
						activity.refreshUI(msg.obj, null, TaskID.WEIBO_FOLLOW_TIMELINE_MORE);
				}
				break;

			default:
				break;
			}
		}
	};

	public static void addActivity(Activity activity) {
		appLists.add(activity);
	}

	public static void removeActivity(Activity activity) {
		appLists.remove(activity);
	}

	/**
	 * 添加任务
	 */
	public static void addTask(Task task) {
		tasks.add(task);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isRun = true;
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			Task task = null;
			if (!tasks.isEmpty()) {
				task = tasks.poll();// 执行完任务后把该任务从任务队列中移除null
				if (null != task) {
					doTask(task);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据Activity的name获得相应Activity实例
	 * 
	 * @param name
	 * @return
	 */
	private Activity getActivityByName(String name) {

		if (!appLists.isEmpty()) {
			for (Activity activity : appLists) {
				if (null != activity) {
					if (activity.toString().equals(name)) {
						return activity;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 处理任务
	 */
	public void doTask(Task task) {

		Message msg = handler.obtainMessage();
		msg.what = task.getTaskId();
		Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken(
				WeiboApplication.userInfo.getAccess_token(),
				WeiboApplication.userInfo.getExpires_in());
		UsersAPI usersAPI = new UsersAPI(oauth2AccessToken);
		StatusesAPI statusesAPI = new StatusesAPI(oauth2AccessToken);
		CommentsAPI commentsAPI = new CommentsAPI(oauth2AccessToken);
		FavoritesAPI favoritesAPI = new FavoritesAPI(oauth2AccessToken);
		FriendshipsAPI friendshipsAPI = new FriendshipsAPI(oauth2AccessToken);

		switch (task.getTaskId()) {
		case TaskID.WEIBO_USER_DETAIL:
			usersAPI.show(Long.valueOf(WeiboApplication.userInfo.getUid()),
					new MyRequestListener(task));
			break;
			// 获取当前登录用户及其所关注用户的最新微博
		case TaskID.WEIBO_HOME_TIMELINE:
			statusesAPI.homeTimeline(0, 0, Preferences.PAGE_SIZE, Preferences.PAGE, false,
					WeiboAPI.FEATURE.ALL, false, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_MENTION_TIMELINE:
			statusesAPI.mentions(0, 0, Preferences.PAGE_SIZE, MessageActivity.statuses_page,  WeiboAPI.AUTHOR_FILTER.ATTENTIONS, WeiboAPI.SRC_FILTER.ALL, WeiboAPI.TYPE_FILTER.ALL, false, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_COMMENT_TIMELINE:
			commentsAPI.mentions(0, 0, Preferences.PAGE_SIZE, MessageActivity.comments_page, WeiboAPI.AUTHOR_FILTER.ATTENTIONS, WeiboAPI.SRC_FILTER.ALL, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_COMMENT_TIMELINE_MORE:
			commentsAPI.mentions(0, 0, Preferences.PAGE_SIZE, ++MessageActivity.comments_page, WeiboAPI.AUTHOR_FILTER.ATTENTIONS, WeiboAPI.SRC_FILTER.WEIBO, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_MENTION_TIMELINE_MORE:
			statusesAPI.mentions(0, 0, Preferences.PAGE_SIZE, ++MessageActivity.statuses_page,  WeiboAPI.AUTHOR_FILTER.ATTENTIONS, WeiboAPI.SRC_FILTER.ALL, WeiboAPI.TYPE_FILTER.ALL, false, new MyRequestListener(task));
			break;
			// 获取当前登录用户及其所关注用户的最新微博的下一页
		case TaskID.WEIBO_HOME_TIMELINE_MORE:
			statusesAPI.homeTimeline(0, 0, Preferences.PAGE_SIZE, ++Preferences.PAGE, false,
					WeiboAPI.FEATURE.ALL, false, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_DETAIL_COMMENT:
			commentsAPI.show(Long.valueOf(task.getTaskParams().get("id").toString()), 0, 0, 20,
					1, WeiboAPI.AUTHOR_FILTER.ALL, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_DETAIL_COMMENT_MORE:
			commentsAPI.show(Long.valueOf(task.getTaskParams().get("id").toString()), 0, 0, 20,
					++WbDetailActivity.comment_page, WeiboAPI.AUTHOR_FILTER.ALL, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_COLLECT_TIMELINE:
			favoritesAPI.favorites(Preferences.PAGE_SIZE, CollectActivity.statuses_page, new MyRequestListener(task));
			break;
		case TaskID.WEIBO_COLLECT_TIMELINE_MORE:
			favoritesAPI.favorites(Preferences.PAGE_SIZE, ++CollectActivity.statuses_page, new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_PROFILE_USERINFO:
			usersAPI.show(task.getTaskParams().get("name").toString(), new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_PROFILE_TIMELINE:
			statusesAPI.userTimeline(task.getTaskParams().get("name").toString(), 0, 0, Preferences.PAGE_SIZE, ProfileActivity.statuses_page, false, WeiboAPI.FEATURE.ALL, false, new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_PROFILE_TIMELINE_MORE:
			statusesAPI.userTimeline(task.getTaskParams().get("name").toString(), 0, 0, Preferences.PAGE_SIZE, ++ProfileActivity.statuses_page, false, WeiboAPI.FEATURE.ALL, false, new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_FRIEND_TIMELINE:
			friendshipsAPI.friends(task.getTaskParams().get("name").toString(), 50, FriendActivity.friend_page, true, new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_FRIEND_TIMELINE_MORE:
			friendshipsAPI.friends(task.getTaskParams().get("name").toString(), 50, FriendActivity.friend_page, true, new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_FOLLOW_TIMELINE:
			friendshipsAPI.followers(task.getTaskParams().get("name").toString(), 50, FriendActivity.follow_page, true, new MyRequestListener(task));
			break;	
		case TaskID.WEIBO_FOLLOW_TIMELINE_MORE:
			friendshipsAPI.followers(task.getTaskParams().get("name").toString(), 50, FriendActivity.follow_page, true, new MyRequestListener(task));
			break;	
		default:
			break;
		}
		handler.sendMessage(msg);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class MyRequestListener implements RequestListener {
		private Task task;

		public MyRequestListener(Task task) {
			super();
			this.task = task;
		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			switch (task.getTaskId()) {
			case TaskID.WEIBO_USER_DETAIL:
				try {
					JSONObject json = new JSONObject(arg0);
					User user = UserService.getUserFromJSON(json);
					Message msg = handler.obtainMessage();
					
					msg.what = TaskID.WEIBO_USER_DETAIL;
					msg.obj = user;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				// 获取当前登录用户及其所关注用户的最新微博
			case TaskID.WEIBO_HOME_TIMELINE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("statuses");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_HOME_TIMELINE;
					msg.obj = statuses;
					Bundle bundle = new Bundle();
					bundle.putString("last_statuses", jsonArray.toString());
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_COLLECT_TIMELINE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("favorites");
					List<Statuse> statuses = new ArrayList<Statuse>();
					for(int i = 0; i < jsonArray.length(); i++) {
						JSONObject status = jsonArray.getJSONObject(i).getJSONObject("status");
						if(StatuseService.getStatuseFromJSON(status) != null)
							statuses.add(StatuseService.getStatuseFromJSON(status));
					}			
					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_COLLECT_TIMELINE;
					msg.obj = statuses;
					Bundle bundle = new Bundle();
					bundle.putString("last_statuses", jsonArray.toString());
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_MENTION_TIMELINE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("statuses");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_MENTION_TIMELINE;
					msg.obj = statuses;
					Bundle bundle = new Bundle();
					bundle.putString("last_statuses", jsonArray.toString());
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_COMMENT_TIMELINE:
				try {
					JSONObject json_comments = new JSONObject(arg0);
					JSONArray jsonArray = json_comments.getJSONArray("comments");

					List<Comment> comments =
							CommentService.getCommentsFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_COMMENT_TIMELINE;
					msg.obj = comments;
					Bundle bundle = new Bundle();
					bundle.putString("last_comments", jsonArray.toString());
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_COLLECT_TIMELINE_MORE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("favorites");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_COLLECT_TIMELINE_MORE;
					msg.obj = statuses;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_COMMENT_TIMELINE_MORE:
				try {
					JSONObject json_comments = new JSONObject(arg0);
					JSONArray jsonArray = json_comments.getJSONArray("comments");

					List<Comment> comments =
							CommentService.getCommentsFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_COMMENT_TIMELINE_MORE;
					msg.obj = comments;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_MENTION_TIMELINE_MORE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("statuses");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_MENTION_TIMELINE_MORE;
					msg.obj = statuses;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
				// 获取当前登录用户及其所关注用户的最新微博的下一页
			case TaskID.WEIBO_HOME_TIMELINE_MORE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("statuses");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_HOME_TIMELINE_MORE;
					msg.obj = statuses;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
			case TaskID.WEIBO_DETAIL_COMMENT:
				try {
					JSONObject json_comments = new JSONObject(arg0);
					JSONArray jsonArray = json_comments.getJSONArray("comments");

					List<Comment> comments =
							CommentService.getCommentsFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_DETAIL_COMMENT;
					msg.obj = comments;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_DETAIL_COMMENT_MORE:
				try {
					JSONObject json_comments = new JSONObject(arg0);
					JSONArray jsonArray = json_comments.getJSONArray("comments");

					List<Comment> comments =
							CommentService.getCommentsFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_DETAIL_COMMENT_MORE;
					msg.obj = comments;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_PROFILE_USERINFO:
				try {
					JSONObject json_user = new JSONObject(arg0);
					User user = UserService.getUserFromJSON(json_user);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_PROFILE_USERINFO;
					msg.obj = user;
					
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_PROFILE_TIMELINE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("statuses");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_PROFILE_TIMELINE;
					msg.obj = statuses;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_PROFILE_TIMELINE_MORE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("statuses");
					List<Statuse> statuses = StatuseService
							.getStatusesFromJSON(jsonArray);

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_PROFILE_TIMELINE_MORE;
					msg.obj = statuses;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_FRIEND_TIMELINE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("users");
					List<User> users = UserService.getUsersFromJSON(jsonArray);
					FriendActivity.friend_page = all.getInt("next_cursor");

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_FRIEND_TIMELINE;
					msg.obj = users;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_FRIEND_TIMELINE_MORE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("users");
					List<User> users = UserService.getUsersFromJSON(jsonArray);
					FriendActivity.friend_page = all.getInt("next_cursor");

					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_FRIEND_TIMELINE_MORE;
					msg.obj = users;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_FOLLOW_TIMELINE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("users");
					List<User> users = UserService.getUsersFromJSON(jsonArray);
					FriendActivity.follow_page = all.getInt("next_cursor");
					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_FOLLOW_TIMELINE;
					msg.obj = users;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaskID.WEIBO_FOLLOW_TIMELINE_MORE:
				try {
					JSONObject all = new JSONObject(arg0);
					JSONArray jsonArray = all.getJSONArray("users");
					List<User> users = UserService.getUsersFromJSON(jsonArray);
					FriendActivity.follow_page = all.getInt("next_cursor");
					Message msg = handler.obtainMessage();
					msg.what = TaskID.WEIBO_FOLLOW_TIMELINE_MORE;
					msg.obj = users;
					Bundle bundle = new Bundle();
					bundle.putString("instance", task.getTaskParams().get("instance").toString());
					msg.setData(bundle);

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;

			default:
				break;
			}
		}

		@Override
		public void onError(WeiboException arg0) {
			// TODO Auto-generated method stub
			Log.d("bobo", arg0.toString());
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub
			Log.d("bobo", arg0.toString());
		}
	}
}
