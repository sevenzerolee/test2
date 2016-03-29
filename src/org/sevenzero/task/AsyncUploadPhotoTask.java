package org.sevenzero.task;

import java.io.FileNotFoundException;

import org.sevenzero.dialog.ProgressDialog;
import org.sevenzero.util.SendFileUtil;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 * @author linger
 *
 * @since 2015-9-23
 * 
 * 上传图片
 *
 */
public class AsyncUploadPhotoTask extends AsyncTask<String, String, String> {
	
	private Context ctx;
	private String url;
	private String filepath;
	
	private ProgressDialog dialog = null;
	
	public AsyncUploadPhotoTask(Context ctx, String url, String filepath) {
		this.ctx = ctx;
		this.url = url;
		this.filepath = filepath;
		
		dialog = new ProgressDialog(ctx);
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			SendFileUtil.send(url, filepath);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		dialog.dismiss();
	}

}
