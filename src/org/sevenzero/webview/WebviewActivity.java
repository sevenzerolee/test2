package org.sevenzero.webview;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.sevenzero.R;
import org.sevenzero.dialog.SelectDialog;
import org.sevenzero.dialog.SelectDialog.SelectDialogListener;
import org.sevenzero.task.AsyncUploadFileTask;
import org.sevenzero.task.AsyncUploadPhotoTask;
import org.sevenzero.util.Constants;
import org.sevenzero.util.FilePathResolver;
import org.sevenzero.util.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
public class WebviewActivity extends Activity {
	
	static final String tag = WebviewActivity.class.getSimpleName(); 
	
	private WebView webview;
	
	private ImageView imgBack;
	private TextView titlLable;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 控制隐藏标题栏，注意必须在 layout 之前
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.webview_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);
		
		
		
		webview = (WebView) this.findViewById(R.id.webView);
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setAllowContentAccess(true);
		webview.setWebChromeClient(new TestWebChromeClient(this));
		webview.setWebViewClient(new TestWebViewClient(this));
		
		
		imgBack = (ImageView) this.findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				finish();
				if (null != webview && webview.canGoBack()) {
					webview.goBack();
				}
			}
		});
		
		titlLable = (TextView) this.findViewById(R.id.title_lable);
		titlLable.setText("Webview");
		
		// api 17 later
		webview.addJavascriptInterface(javascript(), "callnative");
//		webview.addJavascriptInterface(new Object() {
//			
//			public void java() {
//				Log.d(tag, "javascript call java");
//				webview.loadUrl("javascript:javaCallJs()");
//			}
//			
//		}, "call");
		
		String url = Constants.REQUEST_URL + "/html5/index.html";
		url = "http://www.baidu.com";
		url = "http://192.168.1.113:8087/html5/index5.html";
//		url = "http://192.168.6.12:9090/controller/jsp/user/patient/editContact.jsp";
//		web.loadUrl("file:///android_asset/www/html/404.html");
//		url = "file:///android_asset/q/登录.html";
//		Util.printLog(tag, "version: " + Build.VERSION_CODES.LOLLIPOP);
		webview.loadUrl(url);
		
	}
	
	public void setTitlLable(String titlLable) {
		this.titlLable.setText(titlLable);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// 拍照的返回值
	public static final int CAMERA_REQUEST_CODE = 400;
	
	// 从图库选择
	public static final int PTOTO_REQUEST_CODE = 401;
	
	// Android选择文件返回码 
	public static final int UPLOAD_FILE_SELECT_CODE = 402;
	
	private String mUploadUrl = null;
	
	private Object javascript() {
		Object obj = new Object() {
			
			@JavascriptInterface
			public void androidUploadFile(final String uploadUrl) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Util.printLog(tag, "### androidUploadFile " + uploadUrl);
						
						mUploadUrl = uploadUrl;
						// Android 上传分两步，先选择文件，然后才能上传文件
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("*/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
//						startActivity(Intent.createChooser(intent, "选择文件"));
						startActivityForResult(Intent.createChooser(intent, "选择文件"), 
								UPLOAD_FILE_SELECT_CODE);
					}
				});
			}
			
			@JavascriptInterface
			public void takePhotoUpdate(final String url) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Util.printLog(tag, "image " + url);
						String state = Environment.getExternalStorageState();
						if (state.equals(Environment.MEDIA_MOUNTED)) {
							SelectDialog dialog = new SelectDialog(WebviewActivity.this, listener);
							dialog.show();
						}
						else {
							Toast.makeText(WebviewActivity.this, "SDCard不可用", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
			
			@JavascriptInterface
			public void showTitleBar(final int flag) {
				Util.printLog(tag, "flag " + flag);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						switch (flag) {
						case 0:
							requestWindowFeature(Window.FEATURE_NO_TITLE);
							break;
						case 2:
							break;
						case 3:
							break;
						default:
							break;
						}
						
					}
				});
			}
			
		};
		
		return obj;
	}
	
	private SelectDialogListener listener = new DialogListener();
	
	class DialogListener implements SelectDialogListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_capture:
				Util.printLog(tag, "拍照");
//				Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				Intent i = new Intent(Intent.ACTION_CAMERA_BUTTON, null);
				startActivityForResult(i, CAMERA_REQUEST_CODE);
				
				break;
			case R.id.btn_take_from:
				Util.printLog(tag, "图库选择 " + Build.VERSION.SDK_INT);
				Intent i2 = new Intent();
				i2.setType("image/*");
			    i2.setAction(Intent.ACTION_GET_CONTENT);
			    
				i2.putExtra("crop", "true");
				i2.putExtra("aspectX", 2);
				i2.putExtra("aspectY", 1);
				i2.putExtra("outputX", 156);
				i2.putExtra("outputY", 156);
				i2.putExtra("scale", true);
				i2.putExtra("return-data", true);
				i2.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				i2.putExtra("noFaceDetection", true); // no face detection
			    
			    startActivityForResult(i2, PTOTO_REQUEST_CODE);
			    
				break;
				
			case R.id.btn_cancel:
				
				break;

			default:
				break;
			}
			
		}
		
	}
	
	
	String getRealPathFromUri(Uri uri) {
		Cursor cursor = null;
		if (null != uri) {
			try {
				String[] projection = {MediaStore.Images.Media.DATA};
				cursor = getContentResolver().query(uri, projection, null, null, null);
				int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				
				return cursor.getString(index);
			}
			finally {
				if (null != cursor) {
					cursor.close();
				}
			}
		}
		
		return null;
	}
	
	private Bitmap photo = null;
	private String saveImgPath = null;
	
	public static String getDocumentId(Uri uri) {

		String res = null;
		try {
			Class<?> c = Class.forName("android.provider.DocumentsContract");
			Method get = c.getMethod("getDocumentId", Uri.class);

			res = (String) get.invoke(c, uri);
		}
		catch (Exception ignored) {
			ignored.getMessage();
		}

		return res;
	}
	
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		}
		finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}  
	
	/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
    	if (null != uri) {
    		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    	}
    	return false;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
    	if (null != uri) {
    		return "com.android.providers.media.documents".equals(uri.getAuthority());
    	}
    	return false;
    }
	
	// 文件上传相关
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE_CODE = 200;
	public static final int FILE_CHOOSER_RESULT_CODE = 201;
	
	// 尽量用 @TargetApi 而不是 NewApi
//	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Util.printLog(tag, "onActivityResult " + requestCode + ", " + resultCode + ", " + intent);
		
		// webview upload file start
//		if (Build.VERSION.SDK_INT >= 21) {
//			Util.printLog(tag, "### upload ");
//			if (requestCode == REQUEST_SELECT_FILE_CODE) {
//				if (null == uploadMessage) {
//					return;
//				}
//				
//				uploadMessage.onReceiveValue(null);
//				uploadMessage = null;
//			}
//		}
//		else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
//			if (null == mUploadMessage) {
//				return;
//			}
//			
//			
//		}
		// end webview upload file
		
		if (requestCode == UPLOAD_FILE_SELECT_CODE) {
			Util.printLog(tag, "### UPLOAD_FILE_SELECT_CODE ");
			
			if (null == intent) {
				Util.printLog(tag, "intent null");
				return ;
			}
			
			Uri uri = intent.getData();
        	if (null != uri) {
        		String path = uri.getPath();
        		String authority = uri.getAuthority();
        		String scheme = uri.getScheme();
        		Util.printLog(tag, "##路径 " + path + ", " + authority + ", " + scheme);
        		
        		File file = new File(path);
        		// 直接判断是否文件 
//        		if (file.exists()) {
        		// file
        		if ("file".equalsIgnoreCase(scheme)) {
        			Util.printLog(tag, "文件类型 file");
        			
        			String url = "http://192.168.1.113:8087/webstudy/fileUpload2Svlt";
    				String filepath = Constants.PREFIX + File.separator + "2images/uploadtestfile.rar";
//    				filepath = Constants.PREFIX + File.separator + "2images/testfile11m.pdf";
    				filepath = uri.getPath();
    				try {
    					new AsyncUploadFileTask(this, url, filepath).execute();
    				}
    				catch (FileNotFoundException e) {
    					e.printStackTrace();
    				}
        		}
        		else if (isDownloadsDocument(uri)) {
        			String filepath = getDataColumn(this, uri, null, null);
        			Util.printLog(tag, "文件类型 Download " + filepath);
        			
        			final String docId = DocumentsContract.getDocumentId(uri);
        			Util.printLog(tag, "## " + docId);
        			
        			final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));

                    filepath = getDataColumn(this, contentUri, null, null);
                    Util.printLog(tag, "文件类型 Download " + filepath);
        			
        			String url = "http://192.168.1.113:8087/webstudy/fileUpload2Svlt";
//    				String filepath = Constants.PREFIX + File.separator + "2images/uploadtestfile.rar";
//    				filepath = Constants.PREFIX + File.separator + "2images/testfile11m.pdf";
//    				filepath = uri.getPath();
    				try {
    					new AsyncUploadFileTask(this, url, filepath).execute();
    				}
    				catch (FileNotFoundException e) {
    					e.printStackTrace();
    				}
        		}
        		else if (isMediaDocument(uri)) {
        			String filepath = getDataColumn(this, uri, null, null);
        			Util.printLog(tag, "文件类型 Media " + filepath);
        			
        			final String docId = DocumentsContract.getDocumentId(uri);
        			Util.printLog(tag, "## " + docId);
        			
        			String url = "http://192.168.1.113:8087/webstudy/fileUpload2Svlt";
//    				String filepath = Constants.PREFIX + File.separator + "2images/uploadtestfile.rar";
//    				filepath = Constants.PREFIX + File.separator + "2images/testfile11m.pdf";
//    				filepath = uri.getPath();
    				try {
    					new AsyncUploadFileTask(this, url, filepath).execute();
    				}
    				catch (FileNotFoundException e) {
    					e.printStackTrace();
    				}
        		}
        		// content 
        		else if ("content".equalsIgnoreCase(scheme)) {
        			String filepath = getDataColumn(this, uri, null, null);
        			Util.printLog(tag, "文件类型 content " + filepath);
        			
        			if (!Util.checkEmptyOrNull(filepath)) {
        				String url = "http://192.168.1.113:8087/webstudy/fileUpload2Svlt";
//        				String filepath = Constants.PREFIX + File.separator + "2images/uploadtestfile.rar";
//        				filepath = Constants.PREFIX + File.separator + "2images/testfile11m.pdf";
//        				filepath = uri.getPath();
        				try {
        					new AsyncUploadFileTask(this, url, filepath).execute();
        				}
        				catch (FileNotFoundException e) {
        					e.printStackTrace();
        				}
        			}
        		}
        		else {
        			String[] projection = {MediaStore.Images.Media.DATA};
        			CursorLoader cLoader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        			Cursor cursor = cLoader.loadInBackground();
        			try {
        				int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);  
        	        	cursor.moveToFirst();  
        	            path = cursor.getString(column_index);
        	            Util.printLog(tag, "### " + path);
        	            file = new File(path);
        	            if (file.exists()) {
        	            	Util.printLog(tag, "else " + path);
        	            	
        	            	String url = "http://192.168.1.113:8087/webstudy/fileUpload2Svlt";
        					String filepath = Constants.PREFIX + File.separator + "2images/uploadtestfile.rar";
//        					filepath = Constants.PREFIX + File.separator + "2images/testfile11m.pdf";
        					filepath = path;
        					try {
        						new AsyncUploadFileTask(this, url, filepath).execute();
        					}
        					catch (FileNotFoundException e) {
        						e.printStackTrace();
        					}
        	            }
        			}
        			catch (Exception ex) {
        				ex.printStackTrace();
        			}
        			finally {
        				if (null != cursor) {
        					cursor.close();
        				}
        			}
        		}
        		
        		
        	}
			
		}
		else if (requestCode == PTOTO_REQUEST_CODE) {
			if (null == intent) {
				return;
			}
			
			String url = Constants.REQUEST_URL + "/webstudy/fileUpload2Svlt";
//			url = "http://192.168.6.12:9090/controller/patient/image/uploadImage.do";
			Uri uri = intent.getData();
//			String path = getRealPathFromUri(uri);
			String path = FilePathResolver.getPath(this, uri);
			Util.printLog(tag, "图库路径 " + path);
			
//			Uri cropUri = Uri.parse(path);
//			Intent i = new Intent("com.android.camera.action.CROP");
			
			
			
//			AsyncUploadPhotoTask task = new AsyncUploadPhotoTask(this, url, path);
//			task.execute();
		}
		else if (requestCode == CAMERA_REQUEST_CODE) {
			if (null == intent) {
				return;
			}
//        	Toast.makeText(WebviewActivity.this, Environment.getExternalStorageDirectory() + "", Toast.LENGTH_LONG).show();
        	if (null != photo && !photo.isRecycled()) {
        		Util.printLog(tag, "回收图片资源");
        		photo.recycle();
        		photo = null;
        	}
        	
        	Uri uri = intent.getData();
        	if (null != uri) {
        		Util.printLog(tag, uri.getPath());
        		photo = BitmapFactory.decodeFile(uri.getPath());
        	}
        	if (null == photo) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					photo = (Bitmap) bundle.get("data");
				}
				else {
					Toast.makeText(WebviewActivity.this, "获取图像失败", Toast.LENGTH_LONG).show();
					return;
				}
        	}
        	
        	saveImgPath = this.saveBitmap();
        	if (null == saveImgPath) {
        		Util.printLog(tag, "保存失败");
        		Toast.makeText(WebviewActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	else {
        		Util.printLog(tag, "已保存到手机");
        		Toast.makeText(WebviewActivity.this, "已保存到手机", Toast.LENGTH_SHORT).show();
        		
        		
        		
        		String url = Constants.REQUEST_URL + "/webstudy/fileUpload2Svlt";
//        		url = "http://192.168.6.12:9090/controller/patient/image/uploadImage.do";
        		AsyncUploadPhotoTask task = new AsyncUploadPhotoTask(this, url, saveImgPath);
    			task.execute();
        	}
        	
        }
	}
	
	
	String saveBitmap() {
		if (null != photo) {
//			DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//			String filename = df.format(new Date());
//			Util.printLog(TAG, filename);
			
//			String saveDir = Environment.getExternalStorageDirectory() + "/2lkoa";
			String saveDir = Constants.TMP_PATH;
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			String name = Util.getUuid() + ".jpg";
			String imgPath = saveDir + File.separator + name;
			Util.printLog(tag, imgPath);
			File file = new File(imgPath);
			if (file.exists()) {
				file.delete();
			}
			
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			try {
				baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 80, baos);
				byte[] byteArray = baos.toByteArray();
				
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(byteArray);
				bos.flush();
				
				Util.printLog(tag, file.getPath());
				return file.getPath();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
			finally {
				if (null != baos) {
					try {
						baos.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != bos) {
					try {
						bos.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != fos) {
					try {
						fos.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
