package org.sevenzero.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sevenzero.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 
 * @author linger
 *
 * @since 2015-10-20
 *
 */
public class IndexMenu extends PopupWindow {
	
	private View view;
	private ListView listview;
	private Button cancel;

	public IndexMenu(Context context) {
		super(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.menu_layout, null);
		
		listview = (ListView) view.findViewById(R.id.menu_item);
		cancel = (Button) view.findViewById(R.id.menu_cancel);
		
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		
		List<String> list = new ArrayList<String>();
		for (int i=0; i<3; i++) {
			list.add("MenuItem " + i);
		}
		
		MyAdapter adapter = new MyAdapter(context, list);
		listview.setAdapter(adapter);
		
		//设置SelectPicPopupWindow的View  
        this.setContentView(view);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        view.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = view.findViewById(R.id.menu_list).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
	}


}

class MyAdapter extends BaseAdapter {
	
	private Logger log = Logger.getLogger(MyAdapter.class.getSimpleName());
	
	private Context context;
	private List<String> list;
	private LayoutInflater inflater;
	
	MyAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView view = null;
		if (null == convertView) {
			view = new HoldView();
			convertView = inflater.inflate(R.layout.menu_item_layout, null);
			view.tv = (TextView) convertView.findViewById(R.id.menu_item);
			
			convertView.setTag(view);
		}
		else {
			view = (HoldView) convertView.getTag();
		}
		
		view.tv.setText(list.get(position));
		
//		convertView.setOnClickListener(new ViewClick());
//		convertView.setOnTouchListener(new ViewTouch(convertView));
		
		return convertView;
	}
	
	class ViewClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			log.info("Click");
		}
		
	}
	
	class ViewTouch implements OnTouchListener {
		
		private View view;
		
		ViewTouch(View view) {
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			if (MotionEvent.ACTION_DOWN == action) {
				v.setBackgroundResource(R.color.selected);
				log.info("Down");
			}
			else if (MotionEvent.ACTION_UP == action) {
				v.setBackgroundResource(R.color.unselected);
				log.info("Up");
			}
			
			return false;
		}
		
	}
	
	class HoldView {
		
		TextView tv;
		
		HoldView() {}
		
	}
	
}
