package org.sevenzero;

import org.sevenzero.menu.IndexMenu;
import org.sevenzero.util.Util;
import org.sevenzero.webview.WebviewActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author linger
 *
 * @since 2015-9-12
 *
 */
public class TestActivity extends Activity {
	
	private Button btnWebview, btnAli;
	private ImageView imgBack, imgDianhua;
	private TextView titlLable;
	private TextView menu;
	private Button btnMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.test_activity);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.test_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);
		
		imgBack = (ImageView) this.findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TestActivity.this, "back", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		imgDianhua = (ImageView) this.findViewById(R.id.dianhua);
		imgDianhua.setOnClickListener(listener);
		
		titlLable = (TextView) this.findViewById(R.id.title_lable);
		titlLable.setText("Test");
		
		menu = (TextView) this.findViewById(R.id.menu);
		this.registerForContextMenu(menu);
		
		btnMenu = (Button) this.findViewById(R.id.btnMenu);
		this.registerForContextMenu(btnMenu);
		
		btnWebview = (Button) this.findViewById(R.id.btnWebview);
		btnWebview.setOnClickListener(listener);
		
		btnAli = (Button) this.findViewById(R.id.btnAli);
		btnAli.setOnClickListener(listener);
		
		Intent intent = new Intent();
		intent.setClass(TestActivity.this, WebviewActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	private static final String tag = TestActivity.class.getSimpleName();
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Util.printLog(tag, "context menu");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		Util.printLog(tag, "menu opened");
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Util.printLog(tag, "menu");
//		getMenuInflater().inflate(R.menu.splash, menu);
		menu.add("Menu");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Util.printLog(tag, "item");
		return super.onOptionsItemSelected(item);
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnWebview:
				Intent intent = new Intent();
				intent.setClass(TestActivity.this, WebviewActivity.class);
				startActivity(intent);
				
				break;
				
			case R.id.btnAli:
//				Intent intent2 = new Intent();
//				intent2.setClass(TestActivity.this, null);
//				startActivity(intent2);
				
				
				break;
			case R.id.dianhua:
				Util.printLog(tag, "电话");
				
				IndexMenu menu = new IndexMenu(TestActivity.this);
				menu.showAtLocation(TestActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
				break;
				
			default:
				break;
			}
			
		}
	};

}
