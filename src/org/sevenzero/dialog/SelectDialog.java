package org.sevenzero.dialog;

import org.sevenzero.R;
import org.sevenzero.util.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author linger
 *
 * @since 2015-9-15
 * 
 * 自定义 Dialog
 *
 */
public class SelectDialog extends Dialog implements android.view.View.OnClickListener {
	
	private static final String tag = SelectDialog.class.getSimpleName();
	
	private Button btnCapture, btnTakeFrom, btnCancel;

	private SelectDialogListener listener;
	
	public SelectDialog(Context context, SelectDialogListener listener) {
		super(context, R.style.define_dialog);
		this.setTitle("选择");
		this.setCancelable(false);
		this.listener = listener;
		
//		LayoutParams params = getWindow().getAttributes();
//		float density = context.getResources().getDisplayMetrics().density;
//		Util.printLog(tag, "... " + density);
		
//		params.width = (int) (100 * density);
//		params.height = (int) (100 * density);
//		params.x = 20;
//		params.y = 40;
//		params.width = 140;
//		params.height = 80;
//		params.gravity = Gravity.CENTER;
//		getWindow().setAttributes(params);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_layout);
		
		btnCapture  = (Button) this.findViewById(R.id.btn_capture);
		btnTakeFrom = (Button) this.findViewById(R.id.btn_take_from);
		btnCancel   = (Button) this.findViewById(R.id.btn_cancel);
		btnCapture .setOnClickListener(this);
		btnTakeFrom.setOnClickListener(this);
		btnCancel  .setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Util.printLog(tag, "call");
		listener.onClick(v);
		dismiss();
	}
	
	public interface SelectDialogListener {
		
		public void onClick(View v);
		
	}

}
