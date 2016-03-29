package org.sevenzero.webview;

import org.sevenzero.util.Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 
 * @author linger
 * 
 * @since 2015-2-5
 * 
 */
public class TestWebChromeClient extends WebChromeClient {
	
	private static final String tag = TestWebChromeClient.class.getSimpleName();
	
	private Context context;
	
	public TestWebChromeClient(Context context) {
		this.context = context;
	}	
	
	public ValueCallback<Uri> getmUploadMessage() {
		return mUploadMessage;
	}

	public void setmUploadMessage(ValueCallback<Uri> mUploadMessage) {
		this.mUploadMessage = mUploadMessage;
	}

	// 自动扩充缓存容量
	@Override
	public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
			QuotaUpdater quotaUpdater) {
		quotaUpdater.updateQuota(requiredStorage * 2);
	}
	
	@Override
	public void onReceivedTitle(WebView view, String title) {
		Util.printLog(tag, "onReceivedTitle " + title);
		if (null != context) {
			((WebviewActivity) context).setTitlLable(title);
		}
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			final JsResult result) {
//		Util.printLog(TAG, message + ", " + url);
		
		AlertDialog.Builder b2 = new AlertDialog.Builder(context)
				.setTitle("Alert").setMessage(message)
				.setPositiveButton("ok", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
						// MyWebView.this.finish();
					}
				});

		b2.setCancelable(false);
		b2.create();
		b2.show();
		return true;
	}
	
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
//		Util.printLog(TAG, message + ", " + defaultValue + ", " + url);
		
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}
	
	
	// ------------------------------------------------------------------------
	// ------------------------文件上传相关--------------------------------------
	// ------------------------------------------------------------------------
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> uploadMessage;
	
	// For Android 3.0+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		Util.printLog(tag, "### 3.0 openFileChooser in");
		
		if (null != mUploadMessage) {
			mUploadMessage.onReceiveValue(null);
			mUploadMessage = null;
		}
		
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("image/*");
		
		WebviewActivity wa = (WebviewActivity) context;
		wa.startActivityForResult(Intent.createChooser(i, "FileBrowser"), WebviewActivity.FILE_CHOOSER_RESULT_CODE);
	}

	// For Android < 3.0
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		openFileChooser(uploadMsg, "");
	}

	// For Android > 4.1.1
	public void openFileChooser(ValueCallback<Uri> uploadMsg,
			String acceptType, String capture) {
		Util.printLog(tag, "### 4.1 openFileChooser in");
		
		if (null != mUploadMessage) {
			mUploadMessage.onReceiveValue(null);
			mUploadMessage = null;
		}
		
		WebviewActivity wa = (WebviewActivity) context;
		
		mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        wa.startActivityForResult(Intent.createChooser(intent, "File Browser"), WebviewActivity.FILE_CHOOSER_RESULT_CODE);
    
	}
	
	// For Lollipop 5.0+ Devices
	@SuppressLint("NewApi")
//	public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback,
//			WebChromeClient.FileChooserParams fileChooserParams) {
//		Util.printLog(tag, "### 5.0 onShowFileChooser in");
//		
//		if (uploadMessage != null) {
//			uploadMessage.onReceiveValue(null);
//			uploadMessage = null;
//		}
//		
//		WebviewActivity wa = (WebviewActivity) context;
//
//		uploadMessage = filePathCallback;
//
//		Intent intent = fileChooserParams.createIntent();
//		try {
//			wa.startActivityForResult(intent, WebviewActivity.REQUEST_SELECT_FILE_CODE);
//		}
//		catch (ActivityNotFoundException e) {
//			uploadMessage = null;
//			
//			Toast.makeText(wa.getApplicationContext(),
//					"Cannot Open File Chooser", Toast.LENGTH_LONG).show();
//			return false;
//		}
//		return true;
//	}

	public void onShowFileChooser(ValueCallback<Uri> uploadMsg,
			String acceptType, String capture) {

	}
	
	
	
	/*
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
		Util.printLog(TAG, "openFileChooser, " + acceptType + ", " + capture);
		mUploadMessage = uploadMsg;  
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
        i.addCategory(Intent.CATEGORY_OPENABLE);  
        i.setType("image/*");
//        i.setType("file/**");
        
        MainActivity activity = (MainActivity) context;
        activity.startActivityForResult(Intent.createChooser( i, "File Chooser" ), 
        		MainActivity.FILECHOOSER_RESULTCODE);

    }
    */

}
