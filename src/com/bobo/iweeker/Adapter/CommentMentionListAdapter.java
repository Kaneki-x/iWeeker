
package com.bobo.iweeker.Adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Activity.ProfileActivity;
import com.bobo.iweeker.App.SimpleWeiboManager;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.ImgCache.ImageManager;
import com.bobo.iweeker.ImgCache.SimpleImageLoader;
import com.bobo.iweeker.Model.Comment;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.UI.RoundImageView;
import com.bobo.iweeker.Utils.WeiboDateFormat;

public class CommentMentionListAdapter extends BaseAdapter {

    private List<Comment> comments;
    private LayoutInflater mInflater;
    private Context context;

    public CommentMentionListAdapter(Context context, List<Comment> comments) {
        super();
        this.context = context;
        this.comments = comments;
        mInflater = LayoutInflater.from(context);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position < comments.size()) {
            return comments == null ? null : comments.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return comments.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        convertView = mInflater.inflate(R.layout.comment_list_item, null);
        convertView.setTag("Item");

        bindView(position, convertView);
        return convertView;
    }

    private void bindView(int position, View view) {

        WeiboHolder holder = new WeiboHolder();

        Comment comment = comments.get(position);

        holder.txt_wb_item_uname = (TextView) view.findViewById(R.id.comment_txt_wb_item_uname);
        holder.txt_wb_item_content = (TextView) view.findViewById(R.id.comment_txt_wb_item_content);
        holder.img_wb_item_head = (RoundImageView) view.findViewById(R.id.comment_img_wb_item_head);
        holder.img_wb_item_got_talent = (ImageView) view.findViewById(R.id.comment_img_wb_item_g);
        holder.img_wb_item_V = (ImageView) view.findViewById(R.id.comment_img_wb_item_V);
        holder.txt_wb_item_delete = (TextView) view.findViewById(R.id.comment_tv_message_delete);
        holder.img_wb_item_delete = (ImageView) view.findViewById(R.id.comment_iv_message_delete);
        holder.lyt_wb_item_sublayout = (LinearLayout) view.findViewById(R.id.comment_lyt_wb_item_sublayout);
        holder.txt_wb_item_subcontent = (TextView) view.findViewById(R.id.comment_txt_wb_item_subcontent);
        holder.txt_wb_item_time = (TextView) view.findViewById(R.id.comment_txt_wb_item_time);

        // 加粗字体
        TextPaint tp = holder.txt_wb_item_uname.getPaint();
        tp.setFakeBoldText(true);

        holder.txt_wb_item_uname.setText(comment.getUser().getScreen_name());
        holder.txt_wb_item_time.setText(WeiboDateFormat.getFormatDateFromCreate(comment
                .getCreated_at()));

        holder.img_wb_item_head.setRect_adius(90);
        // 异步加载头像
        SimpleImageLoader.showImg(holder.img_wb_item_head, comment.getUser().getProfile_image_url(), ImageManager.ROUND_DEFAULT);

        // 处理微博内容
        SimpleWeiboManager.display(holder.txt_wb_item_content, comment.getText(), context);

        // 设置认证图标
        if (comment.getUser().isVerified()) {
            if (comment.getUser().getVerified_type() == 0) {
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

        if (comment.getUser().getVerified_type() == 200
                || comment.getUser().getVerified_type() == 220) {
            holder.img_wb_item_got_talent.setVisibility(View.VISIBLE);
        } else {
            holder.img_wb_item_got_talent.setVisibility(View.GONE);
        }

        // 设置转发
        if (comment.getStatus() != null)
        {
            holder.lyt_wb_item_sublayout.setVisibility(View.VISIBLE);
            if(comment.getStatus().getUser().getScreen_name().equals(WeiboApplication.userInfo.getScreen_name())) {
	            SimpleWeiboManager.display(holder.txt_wb_item_subcontent, "评论我的微博："
	                    + comment.getStatus().getText(), context);
	            holder.txt_wb_item_delete.setVisibility(View.VISIBLE);
	            holder.img_wb_item_delete.setVisibility(View.VISIBLE);
            } else {
            	SimpleWeiboManager.display(holder.txt_wb_item_subcontent, comment.getStatus().getUser().getName()+"的微博："
	                    + comment.getStatus().getText(), context);
            	holder.txt_wb_item_delete.setVisibility(View.GONE);
	            holder.img_wb_item_delete.setVisibility(View.GONE);
            }
        } else
            holder.lyt_wb_item_sublayout.setVisibility(View.GONE);
        
        holder.img_wb_item_head.setOnClickListener(new MyOnClickListener(comment.getStatus().getUser()));
    }

    public static class WeiboHolder {

        RoundImageView img_wb_item_head;

        TextView txt_wb_item_uname;

        ImageView img_wb_item_V;

        ImageView img_wb_item_got_talent;

        TextView txt_wb_item_time;

        TextView txt_wb_item_content;

        LinearLayout lyt_wb_item_sublayout;

        TextView txt_wb_item_subcontent;
        
        TextView txt_wb_item_delete;
        
        ImageView img_wb_item_delete;

    }
    
    private class MyOnClickListener implements OnClickListener {

		private User user;

		public MyOnClickListener(User user) {
			super();
			// TODO Auto-generated constructor stub
			this.user = user;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("name", "");
			intent.putExtra("user", user);
			intent.setClass(context, ProfileActivity.class);
			context.startActivity(intent);
		}
	}
}
