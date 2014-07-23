
package com.bobo.iweeker.Activity;

import java.util.ArrayList;

import com.bobo.iweeker.R;
import com.bobo.iweeker.UI.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;

	private LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().hide();
		mInflater = LayoutInflater.from(this);
		mViewPager = new HackyViewPager(this);
		setContentView(mViewPager);

		mViewPager.setAdapter(new SamplePagerAdapter(getIntent().getStringArrayListExtra("pic_urls")));
		mViewPager.setCurrentItem(getIntent().getIntExtra("index", 0));
	}

	private class SamplePagerAdapter extends PagerAdapter {

		private ArrayList<String> pic_urls;

		public SamplePagerAdapter(ArrayList<String> pic_urls) {
			super();
			this.pic_urls = pic_urls;
		}

		@Override
		public int getCount() {
			return pic_urls.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {

			View view = mInflater.inflate(R.layout.activity_gallery, null);
			PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_browse);
			TextView textView = (TextView) view.findViewById(R.id.tv_index);
			RelativeLayout rl_gallery_action_bar = (RelativeLayout) view.findViewById(R.id.gallery_action_bar);
			RelativeLayout rl_gallery_tool_bar = (RelativeLayout) view.findViewById(R.id.gallery_tool_bar);
			if(pic_urls.size() > 1) {
				textView.setVisibility(View.VISIBLE);
				textView.setText((position + 1) + "/" + pic_urls.size());
			}
			
			photoView.setOnClickListener(new MyOnClickListener(rl_gallery_action_bar, rl_gallery_tool_bar));
			
			ImageLoader.getInstance().displayImage(pic_urls.get(position), photoView);

			// Now just add PhotoView to ViewPager and return it
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
	
	private class MyOnClickListener implements OnClickListener {
		RelativeLayout rl_gallery_action_bar;
		RelativeLayout rl_gallery_tool_bar;
		
		public MyOnClickListener(RelativeLayout rl_gallery_action_bar, RelativeLayout rl_gallery_tool_bar) {
			this.rl_gallery_action_bar = rl_gallery_action_bar;
			this.rl_gallery_tool_bar = rl_gallery_tool_bar;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (rl_gallery_action_bar.getVisibility() == View.GONE) {
				rl_gallery_action_bar.setVisibility(View.VISIBLE);
				rl_gallery_tool_bar.setVisibility(View.VISIBLE);
			} else {
				rl_gallery_action_bar.setVisibility(View.GONE);
				rl_gallery_tool_bar.setVisibility(View.GONE);
			}
		}
	}
}
