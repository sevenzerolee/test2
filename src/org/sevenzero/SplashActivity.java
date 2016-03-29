package org.sevenzero;

import org.sevenzero.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

public class SplashActivity extends Activity {
	
	private static final String tag = SplashActivity.class.getSimpleName();
	
	private VideoView vv;
	private Button test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 取消标题
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		vv = (VideoView) this.findViewById(R.id.videoView);
//		MediaController mc = new MediaController(this);
//		vv.setMediaController(mc);
		// videoView.setVideoURI(Uri.parse(""));
//		final String path = Constants.PREFIX + "/2images/gujian.flv";
//		final String uri = "android.resource://" + getPackageName() + "/" + R.raw.gujian;
		final String uri = "android.resource://" + getPackageName() + "/" + R.raw.welcome_video_scene_dark;
		Util.printLog(tag, "uri = " + uri);
		Util.printLog(tag, "uri = " + Uri.parse(uri));
//		vv.setVideoPath(path);
		vv.setVideoURI(Uri.parse(uri));
		vv.requestFocus();
		vv.seekTo(9000);
		vv.start();
		
		vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  
			  
            @Override  
            public void onPrepared(MediaPlayer mp) {  
            	Util.printLog(tag, "执行循环 ### ");
                mp.start();  
                mp.setLooping(true);  
            }  
        });  
		
		vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
			  
            @Override  
            public void onCompletion(MediaPlayer mp) {  
            	Util.printLog(tag, "执行循环 *** ");
//                vv.setVideoPath(path);
                vv.setVideoURI(Uri.parse(uri));
                vv.start();  

            }  
        });  
		
		test = (Button) this.findViewById(R.id.test);
		test.setOnClickListener(listener);
		
		
		// 直接跳转
		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, TestActivity.class);
		startActivity(intent);
		finish();
		
//		Intent intent = new Intent();
//		intent.setClass(SplashActivity.this, TestActivity.class);
//		startActivity(intent);
//		finish();
		
//		new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				Intent intent = new Intent();
//				intent.setClass(SplashActivity.this, TestActivity.class);
//				startActivity(intent);
//				finish();
//				
//			}
//		}, 1000L);

//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_splash,
					container, false);
			return rootView;
		}
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.test:
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, TestActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			
		}
	};

}
