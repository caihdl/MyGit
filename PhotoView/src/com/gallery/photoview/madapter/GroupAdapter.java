package com.gallery.photoview.madapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallery.photoview.R;
import com.gallery.photoview.data.ImageBean;
import com.gallery.photoview.data.MyImageView;
import com.gallery.photoview.data.MyImageView.OnMeasureListener;
import com.gallery.photoview.method.inflater;

public class GroupAdapter extends BaseAdapter {
	private List<ImageBean> list;
	private GridView mGridView;
	private Bitmap bmp;
	protected LayoutInflater mInflater;
	private Point mPoint = new Point(180, 180);//用来封装ImageView的宽和高的对象
	private String path;
	private Context mcontext;
	
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
	
	public GroupAdapter(Context context, List<ImageBean> list, GridView mGridView){
		this.list = list;
		this.mGridView = mGridView;
		this.bmp=bmp;
		this.mcontext=context;
		mInflater = LayoutInflater.from(context);
		Resources res = context.getResources();  
	    bmp= BitmapFactory.decodeResource(res, R.drawable.friends_sends_pictures_no);  
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ImageBean mImageBean = list.get(position);
		path = mImageBean.getTopImagePath();
		
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_group_item, null);
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.group_count);
	
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean.getImageCounts()));
		inflater.loadBit(mcontext,viewHolder.mImageView,bmp,path,mPoint,0,mGridView);
		return convertView;
	}
	
	public static class ViewHolder{
		public ImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}
}
