
package com.bobo.iweeker.Adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.ImgCache.ImageManager;
import com.bobo.iweeker.ImgCache.SimpleImageLoader;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.UI.RoundImageView;

public class FriendListAdapter extends BaseAdapter {

	private List<User> users;
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context context;

	public FriendListAdapter(Context context, List<User> users) {
		super();
		this.context = context;
		this.users = users;
		mInflater = LayoutInflater.from(context);
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position < users.size()) {
			return users == null ? null : users.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return users.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		convertView = mInflater.inflate(R.layout.followers_item, null);
		convertView.setTag("Item");

		bindView(position, convertView);
		return convertView;
	}

	private void bindView(int position, View view) {

		WeiboHolder holder = new WeiboHolder();

		User user = users.get(position);

		holder.mention_item_iv_avatar = (RoundImageView) view.findViewById(R.id.mention_item_iv_avatar);
		holder.mention_item_iv_new = (ImageView) view.findViewById(R.id.mention_item_iv_new);
		holder.mention_item_tv_like = (TextView) view.findViewById(R.id.mention_item_tv_like);
		holder.mention_item_tv_name = (TextView) view.findViewById(R.id.mention_item_tv_name);
		holder.mention_item_iv_v = (ImageView) view.findViewById(R.id.mention_item_iv_v);
		holder.mention_item_tv_summery = (TextView) view.findViewById(R.id.mention_item_tv_summery);

		// 异步加载头像
		SimpleImageLoader.showImg(holder.mention_item_iv_avatar, user.getProfile_image_url(), ImageManager.ROUND_DEFAULT);

		// 设置认证图标
		if (user.isVerified()) {
			if (user.getVerified_type() == 0) {
				holder.mention_item_iv_v.setBackgroundResource(R.drawable.avatar_vip);
				holder.mention_item_iv_v.setVisibility(View.VISIBLE);
			} else {
				// 企业认证
				holder.mention_item_iv_v.setBackgroundResource(R.drawable.avatar_enterprise_vip);
				holder.mention_item_iv_v.setVisibility(View.VISIBLE);
			}
			
			if (user.getVerified_type() == 200
					|| user.getVerified_type() == 220) {
				holder.mention_item_iv_v.setBackgroundResource(R.drawable.avatar_grassroot);
				holder.mention_item_iv_v.setVisibility(View.VISIBLE);
			} else {
				holder.mention_item_iv_v.setVisibility(View.GONE);
			}
		} 
		
		holder.mention_item_tv_name.setText(user.getScreen_name());
		
		holder.mention_item_tv_summery.setText(user.getDescription());
		
		if(position < 4)
			holder.mention_item_iv_new.setVisibility(View.VISIBLE);
		else
			holder.mention_item_iv_new.setVisibility(View.GONE);
		
		if(user.isFollow_me()) {
			holder.mention_item_tv_like.setText("相互关注");
		} else {
			holder.mention_item_tv_like.setText("关注");
		}
	}

	public static class WeiboHolder {

		RoundImageView mention_item_iv_avatar;
		ImageView mention_item_iv_new;
		TextView mention_item_tv_like;
		TextView mention_item_tv_name;
		ImageView mention_item_iv_v;
		TextView mention_item_tv_summery;

	}
}
