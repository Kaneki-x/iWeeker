
package com.bobo.iweeker.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Activity.FriendActivity;
import com.bobo.iweeker.Activity.ViewPagerActivity;
import com.bobo.iweeker.App.SimpleWeiboManager;
import com.bobo.iweeker.ImgCache.ImageManager;
import com.bobo.iweeker.ImgCache.SimpleImageLoader;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.UI.RoundImageView;
import com.bobo.iweeker.Utils.StringUtils;
import com.bobo.iweeker.Utils.Utils;
import com.bobo.iweeker.Utils.WeiboDateFormat;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WeiboProfileListAdapter extends BaseAdapter {

	private User user;
	private List<Statuse> statuses;
	private LayoutInflater mInflater;
	private int backround;

	private Context context;

	public WeiboProfileListAdapter(Context context, List<Statuse> statuses, User user) {
		super();
		this.context = context;
		this.user = user;
		this.statuses = statuses;
		this.backround = new Random().nextInt(5);
		mInflater = LayoutInflater.from(context);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Statuse> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<Statuse> statuses) {
		this.statuses = statuses;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return statuses.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return user;
		} else {
			return statuses.get(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return user.getId();
		} else {
			return statuses.get(position - 1).getId();             
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (position == 0) {
			convertView = mInflater.inflate(R.layout.wb_profile_item_head, null);
			convertView.setTag("Head");
		} else {
			convertView = mInflater.inflate(R.layout.home_list_item, null);
			convertView.setTag("Item");
		}

		bindView(position, convertView);
		return convertView;
	}

	private void bindView(int position, View view) {
		if (view.getTag().equals("Head") && position == 0) {
			ProfileHeadHolder holder = new ProfileHeadHolder();

			holder.ll_background = (LinearLayout) view.findViewById(R.id.profile_ll_background);
			holder.ll_favourite = (LinearLayout) view.findViewById(R.id.profile_ll_favourite);
			holder.tv_name = (TextView) view.findViewById(R.id.profile_tv_name);
			holder.tv_location = (TextView) view.findViewById(R.id.profile_tv_location);
			holder.tv_summary = (TextView) view.findViewById(R.id.profile_tv_summary);
			holder.tv_verify = (TextView) view.findViewById(R.id.profile_tv_verify);
			holder.tv_mention_count = (TextView) view.findViewById(R.id.profile_tv_mention_count);
			holder.tv_follow_count = (TextView) view.findViewById(R.id.profile_tv_follow_count);
			holder.tv_favourite_count = (TextView) view.findViewById(R.id.profile_tv_favourite_count);
			holder.tv_status_count = (TextView) view.findViewById(R.id.profile_tv_status_count);
			holder.iv_avatar = (RoundImageView) view.findViewById(R.id.profile_iv_avatar);
			holder.iv_V = (ImageView) view.findViewById(R.id.profile_iv_V);
			holder.btn_mention = (Button) view.findViewById(R.id.profile_btn_mention);
			holder.ll_background.setBackgroundResource(R.drawable.profile_bgpic_1+ backround);
			if(user != null) {

				holder.iv_avatar.setRect_adius(90);
				// 异步加载头像
				SimpleImageLoader.showImg(holder.iv_avatar, user.getProfile_image_url(), ImageManager.ROUND_DEFAULT);

				holder.tv_name.setText(user.getScreen_name());
				holder.tv_location.setText(user.getLocation());

				// 设置认证图标
				if (user.isVerified()) {
					if (user.getVerified_type() == 0) {
						holder.iv_V.setBackgroundResource(R.drawable.avatar_vip);
						holder.iv_V.setVisibility(View.VISIBLE);
					} else {
						// 企业认证
						holder.iv_V.setBackgroundResource(R.drawable.avatar_enterprise_vip);
						holder.iv_V.setVisibility(View.VISIBLE);
					}
					holder.tv_verify.setText("新浪认证："+user.getVerified_reason());
					holder.tv_verify.setVisibility(View.VISIBLE);
				} else {
					holder.iv_V.setVisibility(View.GONE);
					holder.tv_verify.setVisibility(View.INVISIBLE);
				}

				if(!user.getDescription().equals(""))
					holder.tv_summary.setText(user.getDescription());
				else
					holder.tv_summary.setText("暂无简介");

				if(user.getFavourites_count() == 0) {
					holder.ll_favourite.setVisibility(View.GONE);
				} else {
					holder.ll_favourite.setVisibility(View.VISIBLE);
					holder.tv_favourite_count.setText(user.getFavourites_count()+"");
				}

				holder.tv_mention_count.setText(user.getFriends_count()+"");
				holder.tv_follow_count.setText(Utils.getFormatCount(user.getFollowers_count()));
				holder.tv_status_count.setText(user.getStatuses_count()+"");
				holder.tv_favourite_count.setText(user.getFavourites_count()+"");

				holder.tv_mention_count.setOnClickListener(new TextViewOnClickListener(user.getScreen_name(), 0));
				holder.tv_follow_count.setOnClickListener(new TextViewOnClickListener(user.getScreen_name(), 1));
			} else {
				holder.iv_avatar.setRect_adius(90);

				holder.iv_avatar.setImageBitmap(Utils.getRCB(Utils.drawableToBitmap(context.getResources().getDrawable(R.drawable.default_avatar)), 90));

				holder.tv_name.setText("");
				holder.tv_location.setText("");
				holder.tv_summary.setText("");
				holder.tv_mention_count.setText("");
				holder.tv_follow_count.setText("");
				holder.tv_status_count.setText("");
				holder.tv_favourite_count.setText("");
			}
		} else {
			WeiboHolder holder = new WeiboHolder();

			Statuse statuse = statuses.get(position - 1);

			holder.txt_wb_item_uname = (TextView) view.findViewById(R.id.txt_wb_item_uname);
			holder.txt_wb_item_content = (TextView) view.findViewById(R.id.txt_wb_item_content);
			holder.txt_wb_item_from = (TextView) view.findViewById(R.id.txt_wb_item_from);
			holder.img_wb_item_head = (RoundImageView) view.findViewById(R.id.img_wb_item_head);
			holder.img_wb_item_got_talent = (ImageView) view.findViewById(R.id.img_wb_item_g);
			holder.img_wb_item_V = (ImageView) view.findViewById(R.id.img_wb_item_V);
			holder.img_wb_item_content_pic = (ImageView) view
					.findViewById(R.id.img_wb_item_content_pic);
			holder.content_pic_multi = (GridLayout) view
					.findViewById(R.id.content_pic_multi);
			holder.sub_pic_multi =  (GridLayout) view
					.findViewById(R.id.sub_pic_multi);
			holder.lyt_wb_item_sublayout = (LinearLayout) view.findViewById(R.id.lyt_wb_item_sublayout);
			holder.txt_wb_item_subcontent = (TextView) view.findViewById(R.id.txt_wb_item_subcontent);
			holder.img_wb_item_content_subpic = (ImageView) view
					.findViewById(R.id.img_wb_item_content_subpic);
			holder.txt_wb_item_time = (TextView) view.findViewById(R.id.txt_wb_item_time);
			holder.txt_wb_item_redirect = (TextView) view.findViewById(R.id.txt_wb_item_redirect);
			holder.txt_wb_item_comment = (TextView) view.findViewById(R.id.txt_wb_item_comment);

			// 加粗字体
			TextPaint tp = holder.txt_wb_item_uname.getPaint();
			tp.setFakeBoldText(true);

			holder.txt_wb_item_uname.setText(statuse.getUser().getScreen_name());
			holder.txt_wb_item_from.setText("" + Html.fromHtml(statuse.getSource()));
			holder.txt_wb_item_time.setText(WeiboDateFormat.getFormatDateFromCreate(statuse
					.getCreated_at()));
			holder.txt_wb_item_redirect.setText("转发(" + statuse.getReposts_count() + ")");
			holder.txt_wb_item_comment.setText("评论(" + statuse.getComments_count() + ")");

			holder.img_wb_item_head.setRect_adius(90);
			// 异步加载头像
			SimpleImageLoader.showImg(holder.img_wb_item_head, statuse.getUser().getProfile_image_url(), ImageManager.ROUND_DEFAULT);

			// 处理微博内容
			//holder.txt_wb_item_content.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			SimpleWeiboManager.display(holder.txt_wb_item_content, statuse.getText(), context);

			// 设置认证图标
			if (statuse.getUser().isVerified()) {
				if (statuse.getUser().getVerified_type() == 0) {
					holder.img_wb_item_V.setBackgroundResource(R.drawable.avatar_vip);
					holder.img_wb_item_V.setVisibility(View.VISIBLE);
				} else {
					// 企业认证
					holder.img_wb_item_V.setBackgroundResource(R.drawable.avatar_enterprise_vip);
					holder.img_wb_item_V.setVisibility(View.VISIBLE);
				}
			} else {
				holder.img_wb_item_V.setVisibility(View.GONE);
			}

			if (statuse.getUser().getVerified_type() == 200
					|| statuse.getUser().getVerified_type() == 220) {
				holder.img_wb_item_got_talent.setVisibility(View.VISIBLE);
			} else {
				holder.img_wb_item_got_talent.setVisibility(View.GONE);
			}

			// 设置微博图片
			if (!StringUtils.isEmpty(statuse.getThumbnail_pic())) {
				if (statuse.getPic_urls().length == 1) {
					holder.content_pic_multi.setVisibility(View.GONE);
					holder.img_wb_item_content_pic.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(statuse.getThumbnail_pic(), holder.img_wb_item_content_pic);
					if (!statuse.getThumbnail_pic().contains("gif"))
						holder.img_wb_item_content_pic.setOnClickListener(
								new MyImageViewOnClickListener(new String[]{statuse.getBmiddle_pic()}, 0));
				} else {
					holder.content_pic_multi.setVisibility(View.VISIBLE);
					holder.img_wb_item_content_pic.setVisibility(View.GONE);
					bindMutiLayout(holder.content_pic_multi, statuse.getPic_urls());
				}
			} else {
				holder.content_pic_multi.setVisibility(View.GONE);
				holder.img_wb_item_content_pic.setVisibility(View.GONE);
			}

			// 设置转发
			if (statuse.getRetweeted_status() != null)
			{
				holder.lyt_wb_item_sublayout.setVisibility(View.VISIBLE);
				//holder.txt_wb_item_subcontent.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
				SimpleWeiboManager.display(holder.txt_wb_item_subcontent, "@"
						+ statuse.getRetweeted_status().getUser().getScreen_name() + ":"
						+ statuse.getRetweeted_status().getText(), context);
				// 设置转发图片
				if (!StringUtils.isEmpty(statuse.getRetweeted_status().getThumbnail_pic()))
				{
					if (statuse.getRetweeted_status().getPic_urls().length == 1) {
						holder.sub_pic_multi.setVisibility(View.GONE);
						holder.img_wb_item_content_subpic.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(statuse.getRetweeted_status().getThumbnail_pic(), holder.img_wb_item_content_subpic);
						if (!statuse.getRetweeted_status().getThumbnail_pic().contains("gif"))
							holder.img_wb_item_content_subpic.setOnClickListener(
									new MyImageViewOnClickListener(new String[]{statuse.getRetweeted_status().getBmiddle_pic()}, 0));
					} else {
						holder.sub_pic_multi.setVisibility(View.VISIBLE);
						holder.img_wb_item_content_subpic.setVisibility(View.GONE);
						bindMutiLayout(holder.sub_pic_multi, statuse.getRetweeted_status().getPic_urls());
					}

				} else {
					holder.sub_pic_multi.setVisibility(View.GONE);
					holder.img_wb_item_content_subpic.setVisibility(View.GONE);
				}
			} else
				holder.lyt_wb_item_sublayout.setVisibility(View.GONE);
		}
	}

	private class TextViewOnClickListener implements OnClickListener {

		private String name;
		private int type;

		public TextViewOnClickListener(String name, int type) {
			super();
			// TODO Auto-generated constructor stub
			this.name = name;
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("name", name);
			intent.putExtra("type", type);
			intent.setClass(context, FriendActivity.class);
			context.startActivity(intent);
		}

	}

	public static class ProfileHeadHolder {

		LinearLayout ll_background;

		LinearLayout ll_favourite;

		RoundImageView iv_avatar;

		ImageView iv_V;

		Button btn_mention;

		TextView tv_name;

		TextView tv_location;

		TextView tv_summary;

		TextView tv_verify;

		TextView tv_favourite_count;

		TextView tv_mention_count;

		TextView tv_status_count;

		TextView tv_follow_count;
	}

	public static class WeiboHolder {

		RoundImageView img_wb_item_head;

		TextView txt_wb_item_uname;

		ImageView img_wb_item_V;

		ImageView img_wb_item_got_talent;

		TextView txt_wb_item_time;

		TextView txt_wb_item_content;

		GridLayout content_pic_multi;

		ImageView img_wb_item_content_pic;

		LinearLayout lyt_wb_item_sublayout;

		TextView txt_wb_item_subcontent;

		GridLayout sub_pic_multi;

		ImageView img_wb_item_content_subpic;

		TextView txt_wb_item_from;

		TextView txt_wb_item_redirect;

		TextView txt_wb_item_comment;
	}

	private void bindMutiLayout(GridLayout gridLayout, String[] pic_urls) {
		for (int i = 0; i < pic_urls.length; i++) {
			final ImageView pic = (ImageView) gridLayout.getChildAt(i);
			if(!pic_urls[i].contains("gif"))
				pic.setOnClickListener(new MyImageViewOnClickListener(pic_urls, i));
			ImageLoader.getInstance().displayImage(pic_urls[i], pic);
			pic.setVisibility(View.VISIBLE);
		}
	}

	private class MyImageViewOnClickListener implements OnClickListener {

		private String[] pic_urls;
		private int index;

		public MyImageViewOnClickListener(String[] pic_urls, int index) {
			super();
			// TODO Auto-generated constructor stub
			this.pic_urls = pic_urls;
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			ArrayList<String> bmiddle_pic_urls = new ArrayList<String>(); 
			for (int i = 0; i < pic_urls.length; i++)
				bmiddle_pic_urls.add(pic_urls[i]);

			intent.putStringArrayListExtra("pic_urls", bmiddle_pic_urls);
			intent.putExtra("index", index);
			intent.setClass(context, ViewPagerActivity.class);
			context.startActivity(intent);
		}
	}
}
