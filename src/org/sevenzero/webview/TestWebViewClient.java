package org.sevenzero.webview;

import java.util.Timer;

import org.sevenzero.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 
 * @author linger
 *
 */
public class TestWebViewClient extends WebViewClient {
	
	private static final String TAG = TestWebViewClient.class.getSimpleName();
	
	private Context context;
	private ProgressBar pb;
	// 超时时间 7 秒
	private long timeout = 7000L;
	private Timer timer;
	
	public static String sessionId = null;
	
	public Timer getTimer() {
		return timer;
	}

	public TestWebViewClient(Context context) {
		this.context = context;
	}
	
	public TestWebViewClient(ProgressBar pb) {
		this.pb = pb;
	}
	
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//		Util.printLog(TAG, "shouldInterceptRequest " + url);
		return super.shouldInterceptRequest(view, url);
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Util.printLog(TAG, "Loading Url ... " + url);
		
//		if(url.contains("/attch/detail?id=") || url.contains("/work/wdoc?workId=")) {
//			Util.printLog(TAG, "打开附件 ... ");
//			AsyncDownloadDocumentTask task = new AsyncDownloadDocumentTask(activity, url, pb);
//			task.execute();
//			return true;
//		}
		
		return super.shouldOverrideUrlLoading(view, url);
	}
	
	
	
	@Override
	public void onPageStarted(WebView view, final String url, Bitmap favicon) {
		Util.printLog(TAG, "pagestarted " + url);
//		pb.setVisibility(View.VISIBLE);
//		if (!"file:///android_asset/www/404.html".equals(url)) {
//			show();
//		}
//		show();
//		timer = new Timer();  
//        TimerTask tt = new TimerTask() {
//            @Override  
//            public void run() {
//            	Util.printLog(TAG, "执行任务 onPageStarted");
//
//            	 if (null != timer) {
//            		 Util.printLog(TAG, "取消任务 onPageStarted");
//	            	 timer.cancel();
//	                 timer.purge();
//            	 }
////                 hide();
//            	 activity.runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						activity.setUrl(url);
//						activity.getWeb().loadUrl("file:///android_asset/www/html/404.html");
////						hide();
//					}
//				});
            	 
//                 view.loadUrl("file:///android_asset/www/html/404.html");
//            }  
//        };  
//        timer.schedule(tt, timeout);
//        timer.schedule(tt, timeout, 1); 
//		try {
//			Thread.sleep(2500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public void show() {
//		Util.printLog(TAG, "显示进度条");
		pb.setVisibility(View.VISIBLE);
	}
	
	public void hide() {
//		Util.printLog(TAG, "隐藏进度条");
		pb.setVisibility(View.GONE);
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		Util.printLog(TAG, "pagefinished " + url);
		((WebviewActivity) context).setTitlLable(view.getTitle());
//		CookieManager cm = CookieManager.getInstance();
//		String cookie = cm.getCookie(url);
////		Util.printLog(TAG, cookie);
//		if (null != cookie && cookie.contains("connect.sid=")) {
//			sessionId = cookie.substring(cookie.indexOf("connect.sid="));
//		}
////		pb.setVisibility(View.GONE);
////		if (!"file:///android_asset/www/404.html".equals(url)) {
////			hide();
////		}
//		hide();
//		if (null != timer) {
////			Util.printLog(TAG, "取消任务 onPageFinished");
//			timer.cancel();
//	        timer.purge();
//		}
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
//		Util.printLog(TAG, "receivederror " + failingUrl);
////		pb.setVisibility(View.GONE);
//
//		if (null != timer) {
////			Util.printLog(TAG, "取消任务 onReceivedError");
//			timer.cancel();
//	        timer.purge();
//		}
//		activity.runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				hide();
//				activity.getWeb().loadUrl("file:///android_asset/www/html/404.html");
//			}
//		});
	}

}
