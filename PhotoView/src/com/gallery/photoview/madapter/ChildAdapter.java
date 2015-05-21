package com.gallery.photoview.madapter;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;








import android.widget.TextView;
import android.widget.Toast;

import com.gallery.photoview.MainActivity;
import com.gallery.photoview.MyApp;
import com.gallery.photoview.R;
import com.gallery.photoview.R.drawable;
import com.gallery.photoview.R.id;
import com.gallery.photoview.R.layout;
import com.gallery.photoview.ShowImageActivity;
import com.gallery.photoview.data.MyImageView;
import com.gallery.photoview.data.MyImageView.OnMeasureListener;
import com.gallery.photoview.data.Myintent;
import com.gallery.photoview.data.reflasher;
import com.gallery.photoview.madapter.ChildAdapter.ViewHolder;
import com.gallery.photoview.method.NativeImageLoader;
import com.gallery.photoview.method.NativeImageLoader.NativeImageCallBack;
import com.gallery.photoview.method.inflater;
import com.gallery.photoview.method.inflater.Notice;

public class ChildAdapter extends BaseAdapter {
	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	/**
	 * 用来存储图片的选中情况
	 */
	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;
	private Bitmap bmp;
	private MyApp appState;
	private Notice notice;
    private Myintent mintent;
	private Context mcontext;
	private int ImgWidth;
	private reflasher mreflash;
    
	public ChildAdapter(Context context, List<String> list, GridView mGridView,Myintent mintent,reflasher mreflash) {
		this.list = list;
		this.mGridView = mGridView;
		this.mintent=mintent;
		this.mcontext=context;
		this.mreflash=mreflash;
		mInflater = LayoutInflater.from(context);
		WindowManager wm = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE); 
		DisplayMetrics  dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);     
		float scale = mcontext.getResources().getDisplayMetrics().density;
	    ImgWidth = dm.widthPixels/4-(int)(5 * scale + 0.5f);
	    mPoint=new Point(ImgWidth,ImgWidth);
	    
		Resources res = context.getResources();  
	    bmp= BitmapFactory.decodeResource(res, R.drawable.friends_sends_pictures_no);
	    bmp = ThumbnailUtils.extractThumbnail(bmp, ImgWidth, ImgWidth,
		    	 ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	    
	    appState = MyApp.getinstance(); 
	    notice=new Notice(){

			@Override
			public void notce() {
				notifyDataSetChanged();
			}
	      
	    };
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			//viewHolder.mFrameLayout=(FrameLayout) convertView.findViewById(R.id.child_frame);
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.grey);
			LayoutParams lp=viewHolder.mImageView.getLayoutParams();
			lp.width=ImgWidth;
			lp.height=ImgWidth;
			viewHolder.mImageView.setLayoutParams(lp);
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		inflater.setcheck(position,mintent,mGridView,(ImageView)viewHolder.mImageView,viewHolder.mTextView,viewHolder.mCheckBox,notice,appState);
		inflater.loadBit(mcontext,viewHolder.mImageView, bmp, path, mPoint, 0, mGridView);
		return convertView;
	}
	
    
	
	public static class ViewHolder{
		public MyImageView mImageView;
		public CheckBox mCheckBox;
		public TextView mTextView;
	}
	
	public void deleteOperation() {
		final List<Integer> selectPos =  MyApp.getinstance().getSelectItems();
		
		if(!selectPos.isEmpty()) {
			AlertDialog.Builder ad = new AlertDialog.Builder (mInflater.getContext());
	        ad.setTitle("删除");
		    ad.setMessage("确认删除?");
		        
		    ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
		    	@Override
				public void onClick(DialogInterface dialog, int i) {
		    		doDelete(selectPos);
				}
		    });
		        
		    ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int i) {
				}
			});
				
			ad.show();
	    }
		else
			Toast.makeText(mcontext, "未选择任意一项", Toast.LENGTH_SHORT).show();
	}
	
	public void doDelete(List<Integer> selectPos) {
		
		while(!selectPos.isEmpty()) {
			File f= new File(list.get(selectPos.get(0)));
		    if(f.exists()) {
		    	f.delete();
		    	MyApp.getinstance().addlist(list.get(selectPos.get(0)));
		    	list.set(selectPos.get(0), null);
		            
				mInflater.getContext().sendBroadcast(new Intent
						(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
			}
		    
		    selectPos.remove(0);
	    }
		 for (int i=list.size()-1; i>=0; i--){  
	            if (list.get(i)==null) list.remove(i);
	        }  
		Toast.makeText(mInflater.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
		mreflash.reflash();
	}
}
