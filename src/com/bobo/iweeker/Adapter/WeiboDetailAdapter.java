
package com.bobo.iweeker.Adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Activity.ProfileActivity;
import com.bobo.iweeker.Activity.WbDetailActivity;
import com.bobo.iweeker.App.SimpleWeiboManager;
import com.bobo.iweeker.ImgCache.ImageManager;
import com.bobo.iweeker.ImgCache.SimpleImageLoader;
import com.bobo.iweeker.Model.Comment;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.User;
import com.bobo.iweeker.UI.RoundImageView;
import com.bobo.iweeker.Utils.StringUtils;
import com.bobo.iweeker.Utils.WeiboDateFormat;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WeiboDetailAdapter extends BaseAdapter {

    private Statuse statuse;
    private List<Comment> comments;
    private LayoutInflater mInflater;

    private Context context;

    public WeiboDetailAdapter(Context context, List<Comment> comments, Statuse statuse) {
        super();
        this.context = context;
        this.comments = comments;
        this.statuse = statuse;
        mInflater = LayoutInflater.from(context);
    }

    public Statuse getStatuse() {
        return statuse;
    }

    public void setStatuse(Statuse statuse) {
        this.statuse = statuse;
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
        return comments.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position == 0) {
            return statuse;
        } else {
            return comments.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        if (position == 0) {
            return statuse.getId();
        } else {
            return comments.get(position - 1).getId();             
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (position == 0) {
            convertView = mInflater.inflate(R.layout.wb_detail_item_head, null);
            convertView.setTag("Head");
        } else {
            convertView = mInflater.inflate(R.layout.detail_list_item, null);
            convertView.setTag("Item");
        }

        bindView(position, convertView);
        return convertView;
    }

    private void bindView(int position, View view) {

        if (view.getTag().equals("Head") && position == 0) {
            DetailHeadHolder holder = new DetailHeadHolder();

            holder.txt_wb_detail_uname = (TextView) view.findViewById(R.id.txt_wb_detail_uname);
            holder.txt_wb_detail_content = (TextView) view.findViewById(R.id.txt_wb_detail_content);
            holder.txt_wb_detail_from = (TextView) view.findViewById(R.id.txt_wb_detail_from);
            holder.img_wb_detail_head = (RoundImageView) view.findViewById(R.id.img_wb_detail_head);
            holder.img_wb_detail_got_talent = (ImageView) view.findViewById(R.id.img_wb_detail_g);
            holder.img_wb_detail_V = (ImageView) view.findViewById(R.id.img_wb_detail_V);
            holder.img_wb_detail_content_pic = (ImageView) view
                    .findViewById(R.id.img_wb_detail_content_pic);
            holder.detail_content_pic_multi = (GridLayout) view.findViewById(R.id.detail_content_pic_multi);
            holder.detail_sub_pic_multi = (GridLayout) view.findViewById(R.id.detail_sub_pic_multi);
            holder.lyt_wb_detail_sublayout = (LinearLayout) view
                    .findViewById(R.id.lyt_wb_detail_sublayout);
            holder.txt_wb_detail_subcontent = (TextView) view
                    .findViewById(R.id.txt_wb_detail_subcontent);
            holder.img_wb_detail_content_subpic = (ImageView) view
                    .findViewById(R.id.img_wb_detail_content_subpic);
            holder.txt_wb_detail_time = (TextView) view.findViewById(R.id.txt_wb_detail_time);
            holder.txt_wb_detail_redirect = (TextView) view
                    .findViewById(R.id.txt_wb_detail_redirect);
            holder.txt_wb_detail_comment = (TextView) view.findViewById(R.id.txt_wb_detail_comment);
            holder.img_wb_detail_redirect = (ImageView) view
                    .findViewById(R.id.img_wb_detail_redirect);
            holder.img_wb_detail_comment = (ImageView) view
                    .findViewById(R.id.img_wb_detail_comment);

            // 加粗字体
            TextPaint tp = holder.txt_wb_detail_uname.getPaint();
            tp.setFakeBoldText(true);

            holder.txt_wb_detail_uname.setText(statuse.getUser().getScreen_name());
            holder.txt_wb_detail_from.setText("" + Html.fromHtml(statuse.getSource()));
            holder.txt_wb_detail_time.setText(WeiboDateFormat.getFormatDateFromCreate(statuse
                    .getCreated_at()));

            if (WbDetailActivity.comment) {
                holder.txt_wb_detail_comment.setTextColor(Color.RED);
                holder.txt_wb_detail_redirect.setTextColor(0xff8C8C8C);
                holder.img_wb_detail_comment.setVisibility(View.VISIBLE);
                holder.img_wb_detail_redirect.setVisibility(View.GONE);
            } else {
                holder.txt_wb_detail_comment.setTextColor(0xff8C8C8C);
                holder.txt_wb_detail_redirect.setTextColor(Color.RED);
                holder.img_wb_detail_comment.setVisibility(View.GONE);
                holder.img_wb_detail_redirect.setVisibility(View.VISIBLE);
            }

            holder.txt_wb_detail_redirect.setText("转发(" + statuse.getReposts_count() + ")");  
            holder.txt_wb_detail_comment.setText("评论(" + statuse.getComments_count() + ")");

            holder.txt_wb_detail_comment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    WbDetailActivity.comment = true;
                    WbDetailActivity.handler.sendEmptyMessage(0);
                }
            });

            holder.txt_wb_detail_redirect.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    WbDetailActivity.comment = false;
                    WbDetailActivity.handler.sendEmptyMessage(1);
                }
            });

            holder.img_wb_detail_head.setRect_adius(90);
            // 异步加载头像
            SimpleImageLoader.showImg(holder.img_wb_detail_head, statuse.getUser().getProfile_image_url(), ImageManager.ROUND_DEFAULT);
            // 处理微博内容
            SimpleWeiboManager.display(holder.txt_wb_detail_content, statuse.getText(), context);

            // 设置认证图标
            if (statuse.getUser().isVerified()) {
                if (statuse.getUser().getVerified_type() == 0) {
                    holder.img_wb_detail_V.setBackgroundResource(R.drawable.avatar_vip);
                    holder.img_wb_detail_V.setVisibility(View.VISIBLE);
                } else {
                    // 企业认证
                    holder.img_wb_detail_V.setBackgroundResource(R.drawable.avatar_enterprise_vip);
                    holder.img_wb_detail_V.setVisibility(View.VISIBLE);
                }
            } else {
                holder.img_wb_detail_V.setVisibility(View.GONE);
            }

            if (statuse.getUser().getVerified_type() == 200
                    || statuse.getUser().getVerified_type() == 220) {
                holder.img_wb_detail_got_talent.setVisibility(View.VISIBLE);
            } else {
                holder.img_wb_detail_got_talent.setVisibility(View.GONE);
            }

            // 设置微博图片
            if (!StringUtils.isEmpty(statuse.getThumbnail_pic())) {
                if(statuse.getPic_urls().length == 1) {
                    holder.detail_content_pic_multi.setVisibility(View.GONE);
                    holder.img_wb_detail_content_pic.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(statuse.getBmiddle_pic(), holder.img_wb_detail_content_pic);
                } else {
                    holder.detail_content_pic_multi.setVisibility(View.VISIBLE);
                    holder.img_wb_detail_content_pic.setVisibility(View.GONE);
                    bindMutiLayout(holder.detail_content_pic_multi, statuse.getPic_urls());
                }
            } else {
                holder.detail_content_pic_multi.setVisibility(View.GONE);
                holder.img_wb_detail_content_pic.setVisibility(View.GONE);
            }

            // 设置转发
            if (statuse.getRetweeted_status() != null) {
                holder.lyt_wb_detail_sublayout.setVisibility(View.VISIBLE);
                SimpleWeiboManager.display(holder.txt_wb_detail_subcontent, "@"
                        + statuse.getRetweeted_status().getUser().getScreen_name() + ":"
                        + statuse.getRetweeted_status().getText(), context);
                // 设置转发图片
                if (!StringUtils.isEmpty(statuse.getRetweeted_status().getThumbnail_pic())) {
                    if (statuse.getRetweeted_status().getPic_urls().length == 1) {
                        holder.detail_sub_pic_multi.setVisibility(View.GONE);
                        holder.img_wb_detail_content_subpic.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(statuse
                                .getRetweeted_status().getBmiddle_pic(), holder.img_wb_detail_content_subpic);
                    } else {
                        holder.detail_sub_pic_multi.setVisibility(View.VISIBLE);
                        holder.img_wb_detail_content_subpic.setVisibility(View.GONE);
                        bindMutiLayout(holder.detail_sub_pic_multi, statuse.getRetweeted_status().getPic_urls());
                    }
                } else {
                    holder.detail_sub_pic_multi.setVisibility(View.GONE);
                    holder.img_wb_detail_content_subpic.setVisibility(View.GONE);
                }
            } else
                holder.lyt_wb_detail_sublayout.setVisibility(View.GONE);
            
            holder.img_wb_detail_head.setOnClickListener(new MyOnClickListener(statuse.getUser()));
        } else {
            CommentHolder holder = new CommentHolder();

            holder.img_wb_comment_head = (RoundImageView) view.findViewById(R.id.img_wb_comment_head);
            holder.txt_wb_comment_uname = (TextView) view.findViewById(R.id.txt_wb_comment_uname);
            holder.img_wb_comment_V = (ImageView) view.findViewById(R.id.img_wb_comment_V);
            holder.img_wb_comment_got_talent = (ImageView) view.findViewById(R.id.img_wb_comment_g);
            holder.txt_wb_comment_time = (TextView) view.findViewById(R.id.txt_wb_comment_time);
            holder.txt_wb_comment_content = (TextView) view
                    .findViewById(R.id.txt_wb_comment_content);

            holder.txt_wb_comment_uname.setText(comments.get(position - 1).getUser()
                    .getScreen_name());
            holder.txt_wb_comment_time.setText(WeiboDateFormat.getFormatDateFromCreate(comments.get(position - 1).getCreated_at()));

            // 加粗字体
            TextPaint tp = holder.txt_wb_comment_uname.getPaint();
            tp.setFakeBoldText(true);
            holder.img_wb_comment_head.setRect_adius(90);
            // 异步加载头像
            SimpleImageLoader.showImg(holder.img_wb_comment_head, comments.get(position - 1).getUser().getProfile_image_url(), ImageManager.ROUND_DEFAULT);

            // 处理评论内容
            SimpleWeiboManager.display(holder.txt_wb_comment_content, comments.get(position - 1).getText(), context);

            // 设置认证图标
            if (comments.get(position - 1).getUser().isVerified()) {
                if (comments.get(position - 1).getUser().getVerified_type() == 0) {
                    holder.img_wb_comment_V.setBackgroundResource(R.drawable.avatar_vip);
                    holder.img_wb_comment_V.setVisibility(View.VISIBLE);
                } else {
                    // 企业认证
                    holder.img_wb_comment_V.setBackgroundResource(R.drawable.avatar_enterprise_vip);
                    holder.img_wb_comment_V.setVisibility(View.VISIBLE);
                }
            } else {
                holder.img_wb_comment_V.setVisibility(View.GONE);
            }

            if (comments.get(position - 1).getUser().getVerified_type() == 200
                    || comments.get(position - 1).getUser().getVerified_type() == 220) {
                holder.img_wb_comment_got_talent.setVisibility(View.VISIBLE);
            } else {
                holder.img_wb_comment_got_talent.setVisibility(View.GONE);
            }
            
            holder.img_wb_comment_head.setOnClickListener(new MyOnClickListener(comments.get(position - 1).getUser()));
        }
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

    public static class DetailHeadHolder {

        RoundImageView img_wb_detail_head;

        TextView txt_wb_detail_uname;

        ImageView img_wb_detail_V;

        ImageView img_wb_detail_got_talent;

        TextView txt_wb_detail_time;

        TextView txt_wb_detail_content;

        GridLayout detail_content_pic_multi;

        ImageView img_wb_detail_content_pic;

        LinearLayout lyt_wb_detail_sublayout;

        TextView txt_wb_detail_subcontent;

        GridLayout detail_sub_pic_multi;

        ImageView img_wb_detail_content_subpic;

        TextView txt_wb_detail_from;

        TextView txt_wb_detail_redirect;

        TextView txt_wb_detail_comment;

        ImageView img_wb_detail_redirect;

        ImageView img_wb_detail_comment;
    }

    public static class CommentHolder {

        RoundImageView img_wb_comment_head;

        TextView txt_wb_comment_uname;

        ImageView img_wb_comment_V;

        ImageView img_wb_comment_got_talent;

        TextView txt_wb_comment_time;

        TextView txt_wb_comment_content;
    }

    private void bindMutiLayout(GridLayout gridLayout, String[] pic_urls) {
        for (int i = 0; i < pic_urls.length; i++) {
            final ImageView pic = (ImageView) gridLayout.getChildAt(i);
            ImageLoader.getInstance().displayImage(pic_urls[i], pic);
            pic.setVisibility(View.VISIBLE);
        }
    }

}
