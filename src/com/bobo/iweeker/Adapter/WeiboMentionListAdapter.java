
package com.bobo.iweeker.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.bobo.iweeker.Activity.ViewPagerActivity;
import com.bobo.iweeker.App.SimpleWeiboManager;
import com.bobo.iweeker.ImgCache.ImageManager;
import com.bobo.iweeker.ImgCache.SimpleImageLoader;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.UI.RoundImageView;
import com.bobo.iweeker.Utils.StringUtils;
import com.bobo.iweeker.Utils.WeiboDateFormat;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WeiboMentionListAdapter extends BaseAdapter {

    private List<Statuse> statuses;
    private LayoutInflater mInflater;
    private Context context;

    public WeiboMentionListAdapter(Context context, List<Statuse> statuses) {
        super();
        this.context = context;
        this.statuses = statuses;
        mInflater = LayoutInflater.from(context);
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
        return statuses.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position < statuses.size()) {
            return statuses == null ? null : statuses.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return statuses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        convertView = mInflater.inflate(R.layout.mention_list_item, null);
        convertView.setTag("Item");

        bindView(position, convertView);
        return convertView;
    }

    private void bindView(int position, View view) {

        WeiboHolder holder = new WeiboHolder();

        Statuse statuse = statuses.get(position);

        holder.txt_wb_item_uname = (TextView) view.findViewById(R.id.mention_txt_wb_item_uname);
        holder.txt_wb_item_content = (TextView) view.findViewById(R.id.mention_txt_wb_item_content);
        holder.img_wb_item_head = (RoundImageView) view.findViewById(R.id.mention_img_wb_item_head);
        holder.img_wb_item_got_talent = (ImageView) view.findViewById(R.id.mention_img_wb_item_g);
        holder.img_wb_item_V = (ImageView) view.findViewById(R.id.mention_img_wb_item_V);
        holder.img_wb_item_content_pic = (ImageView) view
                .findViewById(R.id.mention_img_wb_item_content_pic);
        holder.content_pic_multi = (GridLayout) view
                .findViewById(R.id.mention_content_pic_multi);
        holder.sub_pic_multi =  (GridLayout) view
                .findViewById(R.id.mention_sub_pic_multi);
        holder.lyt_wb_item_sublayout = (LinearLayout) view.findViewById(R.id.mention_lyt_wb_item_sublayout);
        holder.txt_wb_item_subcontent = (TextView) view.findViewById(R.id.mention_txt_wb_item_subcontent);
        holder.img_wb_item_content_subpic = (ImageView) view
                .findViewById(R.id.mention_img_wb_item_content_subpic);
        holder.txt_wb_item_time = (TextView) view.findViewById(R.id.mention_txt_wb_item_time);

        // 加粗字体
        TextPaint tp = holder.txt_wb_item_uname.getPaint();
        tp.setFakeBoldText(true);

        holder.txt_wb_item_uname.setText(statuse.getUser().getScreen_name());
        holder.txt_wb_item_time.setText(WeiboDateFormat.getFormatDateFromCreate(statuse
                .getCreated_at()));

        holder.img_wb_item_head.setRect_adius(90);
        // 异步加载头像
        SimpleImageLoader.showImg(holder.img_wb_item_head, statuse.getUser().getProfile_image_url(), ImageManager.ROUND_DEFAULT);
        
        holder.img_wb_item_head.setOnClickListener(new MyOnClickListener(statuse));

        // 处理微博内容
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
    
    private class MyOnClickListener implements OnClickListener {

        private Statuse statuse;

        public MyOnClickListener(Statuse statuse) {
            super();
            // TODO Auto-generated constructor stub
            this.statuse = statuse;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        	Intent intent = new Intent();
        	intent.putExtra("name", "");
			intent.putExtra("user", statuse.getUser());
            intent.setClass(context, ProfileActivity.class);
            context.startActivity(intent);
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
