package org.sevenzero.dialog;

import org.sevenzero.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 
 * @author linger
 *
 * @since 2015-9-23
 *
 */
public class ProgressDialog extends Dialog {

	public ProgressDialog(Context context) {
		super(context, R.style.define_dialog_2);
		this.setTitle("上传");
		this.setCancelable(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress_layout);
	}

}
