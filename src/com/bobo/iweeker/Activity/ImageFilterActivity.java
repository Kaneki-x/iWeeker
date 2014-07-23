package com.bobo.iweeker.Activity;

import java.util.ArrayList;
import java.util.List;

import HaoRan.ImageFilter.AutoAdjustFilter;
import HaoRan.ImageFilter.BannerFilter;
import HaoRan.ImageFilter.BigBrotherFilter;
import HaoRan.ImageFilter.BlackWhiteFilter;
import HaoRan.ImageFilter.BlindFilter;
import HaoRan.ImageFilter.BlockPrintFilter;
import HaoRan.ImageFilter.BrickFilter;
import HaoRan.ImageFilter.BrightContrastFilter;
import HaoRan.ImageFilter.CleanGlassFilter;
import HaoRan.ImageFilter.ColorQuantizeFilter;
import HaoRan.ImageFilter.ColorToneFilter;
import HaoRan.ImageFilter.ComicFilter;
import HaoRan.ImageFilter.EdgeFilter;
import HaoRan.ImageFilter.FeatherFilter;
import HaoRan.ImageFilter.FillPatternFilter;
import HaoRan.ImageFilter.FilmFilter;
import HaoRan.ImageFilter.FocusFilter;
import HaoRan.ImageFilter.GammaFilter;
import HaoRan.ImageFilter.GaussianBlurFilter;
import HaoRan.ImageFilter.Gradient;
import HaoRan.ImageFilter.HslModifyFilter;
import HaoRan.ImageFilter.IImageFilter;
import HaoRan.ImageFilter.IllusionFilter;
import HaoRan.ImageFilter.Image;
import HaoRan.ImageFilter.InvertFilter;
import HaoRan.ImageFilter.LensFlareFilter;
import HaoRan.ImageFilter.LightFilter;
import HaoRan.ImageFilter.LomoFilter;
import HaoRan.ImageFilter.MirrorFilter;
import HaoRan.ImageFilter.MistFilter;
import HaoRan.ImageFilter.MonitorFilter;
import HaoRan.ImageFilter.MosaicFilter;
import HaoRan.ImageFilter.NeonFilter;
import HaoRan.ImageFilter.NightVisionFilter;
import HaoRan.ImageFilter.NoiseFilter;
import HaoRan.ImageFilter.OilPaintFilter;
import HaoRan.ImageFilter.OldPhotoFilter;
import HaoRan.ImageFilter.PaintBorderFilter;
import HaoRan.ImageFilter.PixelateFilter;
import HaoRan.ImageFilter.PosterizeFilter;
import HaoRan.ImageFilter.RadialDistortionFilter;
import HaoRan.ImageFilter.RainBowFilter;
import HaoRan.ImageFilter.RaiseFrameFilter;
import HaoRan.ImageFilter.RectMatrixFilter;
import HaoRan.ImageFilter.ReflectionFilter;
import HaoRan.ImageFilter.ReliefFilter;
import HaoRan.ImageFilter.SaturationModifyFilter;
import HaoRan.ImageFilter.SceneFilter;
import HaoRan.ImageFilter.SepiaFilter;
import HaoRan.ImageFilter.SharpFilter;
import HaoRan.ImageFilter.ShiftFilter;
import HaoRan.ImageFilter.SmashColorFilter;
import HaoRan.ImageFilter.SoftGlowFilter;
import HaoRan.ImageFilter.SupernovaFilter;
import HaoRan.ImageFilter.ThreeDGridFilter;
import HaoRan.ImageFilter.ThresholdFilter;
import HaoRan.ImageFilter.TileReflectionFilter;
import HaoRan.ImageFilter.TintFilter;
import HaoRan.ImageFilter.VideoFilter;
import HaoRan.ImageFilter.VignetteFilter;
import HaoRan.ImageFilter.VintageFilter;
import HaoRan.ImageFilter.WaterWaveFilter;
import HaoRan.ImageFilter.XRadiationFilter;
import HaoRan.ImageFilter.YCBCrLinearFilter;
import HaoRan.ImageFilter.ZoomBlurFilter;
import HaoRan.ImageFilter.Distort.BulgeFilter;
import HaoRan.ImageFilter.Distort.RippleFilter;
import HaoRan.ImageFilter.Distort.TwistFilter;
import HaoRan.ImageFilter.Distort.WaveFilter;
import HaoRan.ImageFilter.Textures.CloudsTexture;
import HaoRan.ImageFilter.Textures.LabyrinthTexture;
import HaoRan.ImageFilter.Textures.MarbleTexture;
import HaoRan.ImageFilter.Textures.TextileTexture;
import HaoRan.ImageFilter.Textures.TexturerFilter;
import HaoRan.ImageFilter.Textures.WoodTexture;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bobo.iweeker.R;
import com.bobo.iweeker.Interface.IWeekerActivityInterface;

@SuppressLint("NewApi") @SuppressWarnings("deprecation")
public class ImageFilterActivity extends Activity implements OnTouchListener,IWeekerActivityInterface{

	private ImageView imageView;
	private Bitmap origin_bitmap;
	private Bitmap execute_bitmap;
	private RelativeLayout rl_save;
	private RelativeLayout rl_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		getActionBar().hide();

		init();
		setListener();
		LoadImageFilter();
	}
	
	private void setListener() {
		rl_back.setOnTouchListener(this);
		rl_save.setOnTouchListener(this);
	}

	private void LoadImageFilter() {
		Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
		final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(
				ImageFilterActivity.this);
		gallery.setAdapter(new ImageFilterAdapter(ImageFilterActivity.this));
		gallery.setSelection(2);
		gallery.setAnimationDuration(3000);
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
				new processImageTask(filter).execute();
			}
		});
	}

	public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
		private IImageFilter filter;
		public processImageTask(IImageFilter imageFilter) {
			this.filter = imageFilter;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(ImageFilterActivity.this, "正在渲染图片…", Toast.LENGTH_SHORT).show();
		}

		public Bitmap doInBackground(Void... params) {
			Image img = null;
			try
			{
				img = new Image(origin_bitmap);
				if (filter != null) {
					img = filter.process(img);
					img.copyPixelsFromBuffer();
				}
				return img.getImage();
			}
			catch(Exception e){
				if (img != null && img.destImage.isRecycled()) {
					img.destImage.recycle();
					img.destImage = null;
					System.gc(); 
				}
			}
			finally{
				if (img != null && img.image.isRecycled()) {
					img.image.recycle();
					img.image = null;
					System.gc(); 
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if(result != null){
				super.onPostExecute(result);
				execute_bitmap = result;
				imageView.setImageBitmap(execute_bitmap);	
			}
		}
	}

	public class ImageFilterAdapter extends BaseAdapter {
		private class FilterInfo {
			public int filterID;
			public IImageFilter filter;

			public FilterInfo(int filterID, IImageFilter filter) {
				this.filterID = filterID;
				this.filter = filter;
			}
		}

		private Context mContext;
		private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

		public ImageFilterAdapter(Context c) {
			mContext = c;
			//v0.4 
			filterArray.add(new FilterInfo(R.drawable.video_filter1, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter2, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
			filterArray.add(new FilterInfo(R.drawable.video_filter3, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
			filterArray.add(new FilterInfo(R.drawable.video_filter4, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1, new TileReflectionFilter(20, 8, 45, (byte)1)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2, new TileReflectionFilter(20, 8, 45, (byte)2)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter, new FillPatternFilter(ImageFilterActivity.this, R.drawable.texture1)));
			filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1, new FillPatternFilter(ImageFilterActivity.this, R.drawable.texture2)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter1, new MirrorFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.mirror_filter2, new MirrorFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f, 0.3f))));
			filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter2, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.276f, 0.163f), new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter, new TexturerFilter(new CloudsTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter1, new TexturerFilter(new LabyrinthTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter2, new TexturerFilter(new MarbleTexture(), 1.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter3, new TexturerFilter(new WoodTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.texturer_filter4, new TexturerFilter(new TextileTexture(), 0.8f, 0.8f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter, new HslModifyFilter(20f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0, new HslModifyFilter(40f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1, new HslModifyFilter(60f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2, new HslModifyFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3, new HslModifyFilter(100f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4, new HslModifyFilter(150f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5, new HslModifyFilter(200f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6, new HslModifyFilter(250f)));
			filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7, new HslModifyFilter(300f)));

			//v0.3  
			filterArray.add(new FilterInfo(R.drawable.zoomblur_filter, new ZoomBlurFilter(30)));
			filterArray.add(new FilterInfo(R.drawable.threedgrid_filter, new ThreeDGridFilter(16, 100)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
			filterArray.add(new FilterInfo(R.drawable.colortone_filter2, new ColorToneFilter(0x00FF00, 192)));//green
			filterArray.add(new FilterInfo(R.drawable.colortone_filter3, new ColorToneFilter(0xFF0000, 192)));//blue
			filterArray.add(new FilterInfo(R.drawable.colortone_filter4, new ColorToneFilter(0x00FFFF, 192)));//yellow
			filterArray.add(new FilterInfo(R.drawable.softglow_filter, new SoftGlowFilter(10, 0.1f, 0.1f)));
			filterArray.add(new FilterInfo(R.drawable.tilereflection_filter, new TileReflectionFilter(20, 8)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter1, new BlindFilter(true, 96, 100, 0xffffff)));
			filterArray.add(new FilterInfo(R.drawable.blind_filter2, new BlindFilter(false, 96, 100, 0x000000)));
			filterArray.add(new FilterInfo(R.drawable.raiseframe_filter, new RaiseFrameFilter(20)));
			filterArray.add(new FilterInfo(R.drawable.shift_filter, new ShiftFilter(10)));
			filterArray.add(new FilterInfo(R.drawable.wave_filter, new WaveFilter(25, 10)));
			filterArray.add(new FilterInfo(R.drawable.bulge_filter, new BulgeFilter(-97)));
			filterArray.add(new FilterInfo(R.drawable.twist_filter, new TwistFilter(27, 106)));
			filterArray.add(new FilterInfo(R.drawable.ripple_filter, new RippleFilter(38, 15, true)));
			filterArray.add(new FilterInfo(R.drawable.illusion_filter, new IllusionFilter(3)));
			filterArray.add(new FilterInfo(R.drawable.supernova_filter, new SupernovaFilter(0x00FFFF,20,100)));
			filterArray.add(new FilterInfo(R.drawable.lensflare_filter, new LensFlareFilter()));
			filterArray.add(new FilterInfo(R.drawable.posterize_filter, new PosterizeFilter(2)));
			filterArray.add(new FilterInfo(R.drawable.gamma_filter, new GammaFilter(50)));
			filterArray.add(new FilterInfo(R.drawable.sharp_filter, new SharpFilter()));

			//v0.2
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new ComicFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene())));//green
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene1())));//purple
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene2())));//blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene3())));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new FilmFilter(80f)));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new FocusFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new LomoFilter()));

			//v0.1
			filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
			filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
			filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
			filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
			filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
			filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
			filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
			filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
			filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,new BrightContrastFilter()));
			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,	new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.threshold_filter,	new ThresholdFilter()));
			filterArray.add(new FilterInfo(R.drawable.noisefilter,	new NoiseFilter()));
			filterArray.add(new FilterInfo(R.drawable.banner_filter1, new BannerFilter(10, true)));
			filterArray.add(new FilterInfo(R.drawable.banner_filter2, new BannerFilter(10, false)));
			filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter, new RectMatrixFilter()));
			filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
			filterArray.add(new FilterInfo(R.drawable.brick_filter,	new BrickFilter()));
			filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter,	new GaussianBlurFilter()));
			filterArray.add(new FilterInfo(R.drawable.light_filter,	new LightFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MistFilter()));
			filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MosaicFilter()));
			filterArray.add(new FilterInfo(R.drawable.oilpaint_filter,	new OilPaintFilter()));
			filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter,new RadialDistortionFilter()));
			filterArray.add(new FilterInfo(R.drawable.reflection1_filter,new ReflectionFilter(true)));
			filterArray.add(new FilterInfo(R.drawable.reflection2_filter,new ReflectionFilter(false)));
			filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter,	new SaturationModifyFilter()));
			filterArray.add(new FilterInfo(R.drawable.smashcolor_filter,new SmashColorFilter()));
			filterArray.add(new FilterInfo(R.drawable.tint_filter,	new TintFilter()));
			filterArray.add(new FilterInfo(R.drawable.vignette_filter,	new VignetteFilter()));
			filterArray.add(new FilterInfo(R.drawable.autoadjust_filter,new AutoAdjustFilter()));
			filterArray.add(new FilterInfo(R.drawable.colorquantize_filter,	new ColorQuantizeFilter()));
			filterArray.add(new FilterInfo(R.drawable.waterwave_filter,	new WaterWaveFilter()));
			filterArray.add(new FilterInfo(R.drawable.vintage_filter,new VintageFilter()));
			filterArray.add(new FilterInfo(R.drawable.oldphoto_filter,new OldPhotoFilter()));
			filterArray.add(new FilterInfo(R.drawable.sepia_filter,	new SepiaFilter()));
			filterArray.add(new FilterInfo(R.drawable.rainbow_filter,new RainBowFilter()));
			filterArray.add(new FilterInfo(R.drawable.feather_filter,new FeatherFilter()));
			filterArray.add(new FilterInfo(R.drawable.xradiation_filter,new XRadiationFilter()));
			filterArray.add(new FilterInfo(R.drawable.nightvision_filter,new NightVisionFilter()));

			filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,null/* �˴������ԭͼЧ�� */));
		}

		public int getCount() {
			return filterArray.size();
		}

		public Object getItem(int position) {
			return position < filterArray.size() ? filterArray.get(position).filter
					: null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Bitmap bmImg = BitmapFactory
					.decodeResource(mContext.getResources(),
							filterArray.get(position).filterID);
			int width = 200;// bmImg.getWidth();
			int height = 200;// bmImg.getHeight();
			bmImg.recycle();
			ImageView imageview = new ImageView(mContext);
			imageview.setImageResource(filterArray.get(position).filterID);
			imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
			imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
			return imageview;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_rl_back:
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_back.setBackgroundResource(R.drawable.abs__list_focused_holo);
			} else {
				rl_back.setBackground(null);
				Intent intent = new Intent(ImageFilterActivity.this, WbSendActivity.class);
				Bundle b = new Bundle();
				b.putParcelable("bitmap", origin_bitmap);
				intent.putExtra("image", b);
				/* 将数据打包到aintent Bundle 的过程略 */
				setResult(RESULT_OK, intent); //这理有2个参数(int resultCode, Intent intent)
				finish();
			}
			break;

		case R.id.image_send_rl_wb_send:
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				rl_save.setBackgroundResource(R.drawable.abs__list_focused_holo);
			} else {
				rl_save.setBackground(null);
				Intent intent = new Intent(ImageFilterActivity.this, WbSendActivity.class);
				Bundle b = new Bundle();
				b.putParcelable("bitmap", execute_bitmap);
				intent.putExtra("image", b);
				/* 将数据打包到intent Bundle 的过程略 */
				setResult(RESULT_OK, intent); //这理有2个参数(int resultCode, Intent intent)
				finish();
			}
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		imageView = (ImageView) findViewById(R.id.imgfilter);
		rl_back = (RelativeLayout) findViewById(R.id.image_rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.image_send_rl_wb_send);

		origin_bitmap = getIntent().getBundleExtra("image").getParcelable("bitmap");
		imageView.setImageBitmap(origin_bitmap);
	}

	@Override
	public void refreshUI(Object... objects) {
		// TODO Auto-generated method stub
		
	};

}
