package com.bobo.iweeker.Activity;

import java.util.Calendar;
import java.util.Locale;

import main.java.me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;
import com.bobo.iweeker.Utils.FileUtils;
import com.bobo.iweeker.Utils.Utils;
import com.bobo.iweeker.Utils.emotions.EmotionsParse;

@SuppressLint("NewApi") public class WbSendActivity extends SwipeBackActivity implements IWeekerActivityInterface, 
OnTouchListener {
	private RelativeLayout rl_emotions;
	private RelativeLayout rl_back;
	private EditText et_statues;
	private ImageView iv_emotion;;
	private ImageView iv_trend;;
	private ImageView iv_at;;
	private ImageView iv_camera;;

	private LayoutInflater mInflater;
	private ViewPager viewPager;
	private EmotionsParse emotionsParse;
	private String[] emotion_array;
	private Bitmap bitmap;
	private boolean is_image = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		setEdgeFromLeft();
		init();
		initEmotionView();
		setListener();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		getActionBar().hide();

		rl_emotions = (RelativeLayout) findViewById(R.id.rl_send_emotions);
		rl_back = (RelativeLayout) findViewById(R.id.send_rl_back);
		et_statues = (EditText) findViewById(R.id.et_send); 
		iv_emotion = (ImageView) findViewById(R.id.iv_send_emotion); 
		iv_camera = (ImageView) findViewById(R.id.iv_send_camera); 
		iv_at = (ImageView) findViewById(R.id.iv_send_at); 
		iv_trend = (ImageView) findViewById(R.id.iv_send_trend); 

	}

	private void initEmotionView() {
		emotionsParse = new EmotionsParse();
		mInflater = LayoutInflater.from(this);
		viewPager = (ViewPager) findViewById(R.id.vp_send_emotions);
		emotion_array = getResources().getStringArray(R.array.default_emotions);

		viewPager.setAdapter(new SamplePagerAdapter());
	}

	private void setListener() {
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
				InputMethodManager imm = (InputMethodManager)getSystemService(WbSendActivity.INPUT_METHOD_SERVICE); 
				if(rl_emotions.getVisibility() == View.GONE) {
					iv_emotion.setImageBitmap(Utils.drawableToBitmap(WbSendActivity.this.getResources().getDrawable(R.drawable.weibo_detail_keyboard_button)));
					imm.hideSoftInputFromInputMethod(et_statues.getWindowToken(), 0);
					rl_emotions.setVisibility(View.VISIBLE);
				} else {
					rl_emotions.setVisibility(View.GONE);
					iv_emotion.setImageBitmap(Utils.drawableToBitmap(WbSendActivity.this.getResources().getDrawable(R.drawable.send_emotion_button)));
					imm.toggleSoftInput(0, 0);
				}
			}
		});

		iv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(is_image) {
					new AlertDialog.Builder(WbSendActivity.this)
					.setTitle("提醒")//对话框标题
					.setMessage("是否删除已经选择的图片？")
					.setPositiveButton("删除", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							iv_camera.setBackgroundResource(R.drawable.photo_btn_bg);
							is_image = false;
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					})
					.show();
					iv_emotion.setBackgroundResource(R.drawable.emotion_btn_bg);

				} else {
					final String[] items = {"拍照","图库"};//列表中显示的内容组成的数组
					new AlertDialog.Builder(WbSendActivity.this)
					.setTitle("选择")//对话框标题
					.setItems(items, new DialogInterface.OnClickListener() {//每一条的名称
						public void onClick(DialogInterface dialog, int which) {//响应点击事件
							switch (which) {
							case 0:
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
								startActivityForResult(intent, 1);  
								break;

							case 1:
								intent = new Intent(Intent.ACTION_GET_CONTENT);  
								intent.setType("image/*");  
								intent.putExtra("crop", true);  
								intent.putExtra("return-data", true);  
								startActivityForResult(intent, 2);  
								break;

							default:
								break;
							}
						}
					})
					.show();
				}
			}
		});

		iv_trend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int start = et_statues.getSelectionStart();
				et_statues.setText(et_statues.getText().toString().substring(0, start)+"##"+et_statues.getText().toString().substring(start, et_statues.getText().toString().length()));
				et_statues.setSelection(start+1);
			}
		});

		iv_at.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		
		rl_back.setOnTouchListener(this);
	}

	private void setIndicatorView(int position) {
		switch (position) {
		case 0:
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator);
			break;
		case 1:
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator);
			break;
		case 2:
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator_selected);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator);
			break;
		case 3:
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_1)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_2)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_3)).setBackgroundResource(R.drawable.emotion_indicator);
			((ImageView) findViewById(R.id.iv_send_emotion_indicator_4)).setBackgroundResource(R.drawable.emotion_indicator_selected);
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
			if (position == getCount() - 1) {
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.emotion_delete));
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int selectionStart = et_statues.getSelectionStart();// 获取光标的位置
						if (selectionStart > 0) {
							String body = et_statues.getText().toString();
							if (!TextUtils.isEmpty(body)) {
								String tempStr = body.substring(0, selectionStart);
								int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
								if (i != -1) {
									CharSequence cs = tempStr
											.subSequence(i, selectionStart);
									if (cs.charAt(cs.length()-1) == ']') {// 判断是否是一个表情
										et_statues.getEditableText().delete(i, selectionStart);
										return;
									}
								}
								et_statues.getEditableText().delete(tempStr.length() - 1,
										selectionStart);
							}
						}
					}
				});
			} else {
				imageView.setImageDrawable(emotionsParse.getEmotionByName(emotion_array[pager * 23 + position]));
				imageView.setOnClickListener(new MyImgClickListener(pager * 23 + position));
			}
			return view; 
		} 

		private class MyImgClickListener implements OnClickListener {
			private int position;

			public MyImgClickListener(int position) {
				this.position = position;
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Drawable drawable = emotionsParse.getEmotionByName(emotion_array[position]);
				drawable.setBounds(0, 0, 40, 40);
				ImageSpan is = new ImageSpan(drawable);//用ImageSpan指定图片替代文字
				SpannableString ss = new SpannableString(emotion_array[position]);// 其实写入EditView中的是这个字段“[fac”，表情图片会替代这个字段显示
				// 如果为了区分表情可以写一个集合每个表情对应一段文字
				ss.setSpan(is, 0, emotion_array[position].length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
				et_statues.append(ss);// 追加到EditView中 
			}
		}
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {  
			String sdStatus = Environment.getExternalStorageState();  
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
				Toast.makeText(this, "SD卡不存在，图片无法保存！", Toast.LENGTH_LONG).show(); 
				return;  
			}  			
			String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".png";     
			if(requestCode == 1) {			
				//Toast.makeText(this, name, Toast.LENGTH_LONG).show(); 
				Bundle bundle = data.getExtras();  
				bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
				try{ 
					FileUtils fileUtils = new FileUtils();
					fileUtils.createSDDir(FileUtils.ABSOLUTE_IMAGE);
					fileUtils.writetoSDFromInput(FileUtils.ABSOLUTE_IMAGE, name, FileUtils.bitmapToIS(bitmap));

					new AlertDialog.Builder(WbSendActivity.this)
					.setTitle("选择")//对话框标题
					.setMessage("是否渲染图片？")
					.setPositiveButton("是", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(WbSendActivity.this, ImageFilterActivity.class);
							Bundle b = new Bundle();
							b.putParcelable("bitmap", bitmap);
							intent.putExtra("image", b);
							startActivityForResult(intent, 3);
						}
					})
					.setNegativeButton("否", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							iv_camera.setBackground(new BitmapDrawable(bitmap));
							is_image = true;
						}
					})
					.show();	
				}catch (Exception e) {
					// TODO: handle exception
				}
			} 
			if(requestCode == 2) {
				Uri uri = data.getData();  
				//ContentResolver cr = this.getContentResolver();  
				try {  
					bitmap = decodeBitmap(getRealPathFromURI(uri));

					new AlertDialog.Builder(WbSendActivity.this)
					.setTitle("选择")//对话框标题
					.setMessage("是否渲染图片？")
					.setPositiveButton("是", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(WbSendActivity.this, ImageFilterActivity.class);
							Bundle b = new Bundle();
							b.putParcelable("bitmap", bitmap);
							intent.putExtra("image", b);
							startActivityForResult(intent, 3);
						}
					})
					.setNegativeButton("否", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							iv_camera.setBackground(new BitmapDrawable(bitmap));
							is_image = true;
						}
					})
					.show();
				} catch (Exception e) {  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				} 
			}
			if(requestCode == 3) {
				bitmap = data.getBundleExtra("image").getParcelable("bitmap");
				iv_camera.setBackground(new BitmapDrawable(bitmap));
				is_image = true;
			}
		} 
	}

	public Bitmap decodeBitmap(String path) {  
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;  
		// 通过这个bitmap获取图片的宽和高         
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);  

		float realWidth = options.outWidth;  
		float realHeight = options.outHeight;  

		// 计算缩放比         
		int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);  
		if (scale <= 0)  
		{  
			scale = 1;  
		}  
		options.inSampleSize = scale;  
		options.inJustDecodeBounds = false;  
		// 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。        
		bitmap = BitmapFactory.decodeFile(path, options);  
		return bitmap;  
	}  

	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

		if(cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	@Override
	public void refreshUI(Object... objects) {
		// TODO Auto-generated method stub

	}

}
