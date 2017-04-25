package com.yy.roundimageview;

import com.example.roundimageview.R;
import com.yy.utils.FastBulr;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private RoundImageView v;
		private boolean isFirst = true;
		private ObjectAnimator anim;
		private MyAnimatorUpdateListener listener;

		public PlaceholderFragment() {
		}

		@SuppressWarnings("deprecation")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			v = (RoundImageView) rootView.findViewById(R.id.iv);
			v.setOutsideColor(Color.BLUE);
			v.setInsideColor(Color.RED);
			v.setImageDrawable(getResources().getDrawable(R.drawable.p11));
			Bitmap bit = BitmapFactory.decodeResource(getResources(),
					R.drawable.p11);
			Matrix matrix = new Matrix();
			matrix.postScale(0.1f, 0.1f);
			Bitmap overlay = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
					bit.getHeight(), matrix, true);
			Bitmap bitmap = FastBulr.doBlur(overlay, 5, true);

			Drawable d = new BitmapDrawable(bitmap);
			rootView.setBackgroundDrawable(d);
			Button bt = (Button) rootView.findViewById(R.id.bt);
			LinearInterpolator lin = new LinearInterpolator();
			anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
			anim.setDuration(15000);
			anim.setInterpolator(lin);
			anim.setRepeatMode(Animation.RESTART);
			anim.setRepeatCount(-1);
			listener = new MyAnimatorUpdateListener(anim);
			anim.addUpdateListener(listener);
			bt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Button bt = (Button) arg0;

					if (isFirst) {
						anim.start();
						bt.setText("pause");
						isFirst = false;
					} else {

						if (listener.isPause()) {
							listener.play();
							bt.setText("pause");
						} else if (listener.isPlay()) {
							listener.pause();
							bt.setText("start");
						}
					}
				}
			});

			return rootView;
		}
	}

}
