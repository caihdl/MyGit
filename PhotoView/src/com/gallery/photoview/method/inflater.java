package com.gallery.photoview.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gallery.photoview.MyApp;
import com.gallery.photoview.R;
import com.gallery.photoview.R.id;
import com.gallery.photoview.R.layout;
import com.gallery.photoview.data.ListChild.ListChildUri;
import com.gallery.photoview.data.ListChild.ViewHolderImg;
import com.gallery.photoview.data.ListChild.ViewHolderText;
import com.gallery.photoview.data.Myintent;
import com.gallery.photoview.method.NativeImageLoader.NativeImageCallBack;
import com.gallery.photoview.ShowImageActivity;

public class inflater {

	public inflater() {
		// TODO Auto-generated constructor stub
	}

	public static View inflate(LayoutInflater mInflater,View convertView,ViewHolderImg holderImg)
	{
		 convertView = mInflater.inflate(R.layout.photolist,null);
    	 holderImg.lin=(LinearLayout) convertView.findViewById(R.id.linear);
    	 holderImg.img[0]=(ImageView) convertView.findViewById(R.id.img1);
    	 holderImg.text[0]=(TextView) convertView.findViewById(R.id.grey);
    	 holderImg.box[0]=(CheckBox) convertView.findViewById(R.id.child_checkbox);
    	 
    	 holderImg.img[1]=(ImageView) convertView.findViewById(R.id.img2);
    	 holderImg.text[1]=(TextView) convertView.findViewById(R.id.grey1);
    	 holderImg.box[1]=(CheckBox) convertView.findViewById(R.id.child_checkbox1);
    	 
    	 holderImg.img[2]=(ImageView) convertView.findViewById(R.id.img3);
    	 holderImg.text[2]=(TextView) convertView.findViewById(R.id.grey2);
    	 holderImg.box[2]=(CheckBox) convertView.findViewById(R.id.child_checkbox2);
    	 
    	 holderImg.img[3]=(ImageView) convertView.findViewById(R.id.img4);
    	 holderImg.text[3]=(TextView) convertView.findViewById(R.id.grey3);
    	 holderImg.box[3]=(CheckBox) convertView.findViewById(R.id.child_checkbox3);
    	 
    	 convertView.setTag(holderImg);
    	 return convertView;
	}
	
	public static View inflate(LayoutInflater mInflater,View convertView,ViewHolderText holderText)
	{
		 convertView = mInflater.inflate(R.layout.title,null);
    	 holderText.lin=(LinearLayout) convertView.findViewById(R.id.linear);
    	 holderText.title=(TextView) convertView.findViewById(R.id.title);
    	 convertView.setTag(holderText);
    	 return convertView;
	}
	
	
	//异步加载指定viewgroup中的一个ImageView
	public static void loadBit(Context mcontext,ImageView mbit,Bitmap bmp,String path,final Point mPoint,int type,final ViewGroup mgroupView)
	{
		 mbit.setTag(path);
  	     mbit.setImageBitmap(bmp);
		 Bitmap bitmap=NativeImageLoader.getInstance().loadNativeImage(mcontext,path, mPoint, type,new NativeImageCallBack() {
				
	         @Override
	         public void onImageLoader(Bitmap bitmap, String path) {
					ImageView mImageView = (ImageView) mgroupView.findViewWithTag(path);
					if(bitmap != null && mImageView != null){
						//bitmap=ThumbnailUtils.extractThumbnail(bitmap, mPoint.x, mPoint.y,
						    	 //ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
						mImageView.setImageBitmap(bitmap);
					}
		      }
			});
	    if(bitmap != null){
	    	mbit.setImageBitmap(bitmap);
	    }
	}
	
		//用作选中item的接口
		public static void setcheck(final int position,final Myintent mintent,final ViewGroup mgroup,final ImageView mImageView,final TextView mTextView,final CheckBox mCheckBox,final Notice notice,final MyApp myapp)
		{
			
		/*长按图片事件*/
		   	final HashMap<Integer, Boolean> mSelectMap = myapp.getmap();
			mImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(myapp.getState())
						{
							  mCheckBox.setChecked(mSelectMap.containsKey(position) ? !mSelectMap.get(position) : true);
						}
						else
						{
							mintent.startintent(position);
						}
					}
			    	});
			 
			 
		    mImageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(!myapp.getState())
				{
					//mgroup.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
					myapp.newhash();
					myapp.setState(true);
				}
				else
				{
					//mgroup.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
				    myapp.newhash();
					myapp.setState(false);
				}
				notice.notce();
				return true;
			}
	    	});
		
		   /*根据isShow变量确定勾选框是否可见*/
		   if (!myapp.getState()) 
		   {
			      mTextView.setVisibility(View.GONE);
			      mCheckBox.setChecked(false);
			      mCheckBox.setVisibility(View.GONE);
		   }
		   else
		   {
			  mCheckBox.setVisibility(View.VISIBLE);
		      mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果是未选中的CheckBox,则添加动画
				//if(!mSelectMap.containsKey(position) || !mSelectMap.get(position)){
				if (isChecked){
					if (!MyApp.getinstance().check(position)) MyApp.getinstance().addcount();
					addAnimation(mCheckBox);
				}
				else if (MyApp.getinstance().check(position)) MyApp.getinstance().subcount();
					
				mTextView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				mSelectMap.put(position, isChecked);
				notice.notce();
			}
		    });
		 
		    mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);
		    }
		//mTextView.setVisibility(mCheckBox.isChecked() ? View.VISIBLE : View.GONE);
		}
	
		
		private static void addAnimation(View view) {
			float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f,
			1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
			
			AnimatorSet set = new AnimatorSet();
			set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), 
					ObjectAnimator.ofFloat(view, "scaleY", vaules));
					set.setDuration(150);
			
			set.start();
		}	
	
	
	public static interface Notice
	{
	   public void notce();
	}
	
	
}
