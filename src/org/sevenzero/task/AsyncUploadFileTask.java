package org.sevenzero.task;

import java.io.File;
import java.io.FileNotFoundException;

import org.sevenzero.dialog.RealProgressDialog;
import org.sevenzero.util.SendFileUtil;
import org.sevenzero.util.Util;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 * @author linger
 *
 * @since 2015-9-23
 * 
 * 上传文件
 *
 */
public class AsyncUploadFileTask extends AsyncTask<String, String, String> {
	
	private static final String tag = AsyncUploadFileTask.class.getSimpleName();
	
	private Context ctx;
	private String url;
	private String filepath;
	
	private RealProgressDialog dialog = null;
	private int sum;
	
	public AsyncUploadFileTask(Context ctx, String url, String filepath) throws FileNotFoundException {
		this.ctx = ctx;
		this.url = url;
		this.filepath = filepath;
		
		File file = new File(filepath);
		if (!file.exists()) {
			throw new FileNotFoundException("文件不存在");
		}
		
		long len = file.length();
		Util.printLog(tag, "文件长度 " + len);
		int iLen = (int) len;
		Util.printLog(tag, "文件长度 " + iLen);
		sum = iLen;
		dialog = new RealProgressDialog(ctx, iLen, 0);
		dialog.setTask(this);
		
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		
		try {
//			SendFileUtil.send(url, filepath);
			SendFileUtil.sendForProgress(url, filepath, this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
//		int count = 0;
//		while(true) {
//			try {
//				Thread.sleep(5 * 1000);
//				count++;
//				publishProgress();
//			}
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			if (count > 10) {
//				break;
//			}
//		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		dialog.dismiss();
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
//		Util.printLog(tag, "##@ " + values + ", " + sendLen + ", " + sum + ", " + (float)sendLen/sum + ", " + (float)(sendLen/sum));
		float pro = (float)sendLen/sum;
		dialog.getNumberProgress().setText(String.format("%.2f", pro * 100) + "%");
		dialog.getRealProgressBar().setProgress(sendLen);
	}
	
	private int sendLen;
	
	public void updateProgress(int sendLen) {
		this.sendLen = sendLen;
		
		publishProgress();
	}

}
