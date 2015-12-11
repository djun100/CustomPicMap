package com.diegocarloslima.byakugallery.sample;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.R;
import com.diegocarloslima.byakugallery.lib.TileBitmapDrawable;
import com.diegocarloslima.byakugallery.lib.TouchImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class TouchImageViewSampleActivity extends Activity implements TouchImageView.IOnScroll{
	FrameLayout mFl;
	ArrayList<Point> picPoints=new ArrayList<>();
	ArrayList<TextView> textViews=new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.touch_image_view_sample);
		mFl=(FrameLayout)findViewById(R.id.mFl);
		final TouchImageView image = (TouchImageView) findViewById(R.id.touch_image_view_sample_image);
		final InputStream is = getResources().openRawResource(R.raw.android1);
		final Drawable placeHolder = getResources().getDrawable(R.drawable.android_placeholder);
		TileBitmapDrawable.attachTileBitmapDrawable(image, is, placeHolder, new TileBitmapDrawable.OnInitializeListener() {
			@Override
			public void onStartInitialization() {
			}

			@Override
			public void onEndInitialization() {
				Point point1=new Point(1182,1722);
				Point point2=new Point(550,550);
				picPoints.add(point1);
				picPoints.add(point2);

				for (Point point:picPoints){
					TextView textView=new TextView(TouchImageViewSampleActivity.this);
					textView.setText(point.x+" "+point.y);
					textView.setTag(point.x+" "+point.y);
					textView.setBackgroundColor(Color.WHITE);
					textViews.add(textView);
					mFl.addView(textView);
					FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams)textView.getLayoutParams();
					layoutParams.height= FrameLayout.LayoutParams.WRAP_CONTENT;
					layoutParams.width=FrameLayout.LayoutParams.WRAP_CONTENT;
				}
			}

			@Override
			public void onError(Exception ex) {
			}
		});

		image.setOnscrollListener(this);
	}

	private final float[] mMatrixValues = new float[9];
	/**根据图片当前的matrix求图片指定像素点的坐标location
	 * point(x,y)=(picX+scale*pixelX,picY+scale*pixelY)*/
	private Point calculateLocation(TextView textView,Matrix matrix){
		//get textview`s x y
		String location[]=textView.getTag().toString().split(" ");
		matrix.getValues(mMatrixValues);
		return new Point((int) (mMatrixValues[Matrix.MTRANS_X]+mMatrixValues[Matrix.MSCALE_X]*Integer.parseInt(location[0])),
				(int) (mMatrixValues[Matrix.MTRANS_Y]+mMatrixValues[Matrix.MSCALE_Y]*Integer.parseInt(location[1])));
	}

	@Override
	public void onScroll(Matrix matrix) {
		for (TextView textView :textViews){
			FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams)textView.getLayoutParams();
			int x=calculateLocation(textView,matrix).x;
			int y=calculateLocation(textView,matrix).y;
			textView.setX(x);
			textView.setY(y);
		}
	}
}
