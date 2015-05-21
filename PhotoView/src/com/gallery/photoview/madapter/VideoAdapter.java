package com.gallery.photoview.madapter;

import java.util.List;

import com.gallery.photoview.R;
import com.gallery.photoview.data.ImageBean;
import com.gallery.photoview.method.inflater;

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


public class VideoAdapter extends BaseAdapter{  
    
    private Context context;  
    private List<ImageBean> videoGroup;
	private Bitmap bmp;
	private Point mPoint=new Point(240,240);
	private ViewGroup mGroupGridView;  
	
    public VideoAdapter(Context context, List<ImageBean> data,GridView mGridView){  
        this.context = context;  
        this.videoGroup = data;  
        this.mGroupGridView=mGridView;
        Resources res = context.getResources();  
	    bmp= BitmapFactory.decodeResource(res, R.drawable.friends_sends_pictures_no); 
	    
    }  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return videoGroup.size();  
    }  
    @Override  
    public Object getItem(int p) {  
        // TODO Auto-generated method stub  
        return videoGroup.get(p);  
    }  
    @Override  
    public long getItemId(int p) {  
        // TODO Auto-generated method stub  
        return p;  
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        ViewHolder holder = null;
        ImageBean mImageBean = videoGroup.get(position);
		String path = mImageBean.getTopImagePath();
		
        if(convertView == null){  
            holder = new ViewHolder();  
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_group_item, null);  
            holder.thumbImage = (ImageView)convertView.findViewById(R.id.group_image);  
            holder.titleText = (TextView)convertView.findViewById(R.id.group_title);
            holder.count = (TextView)convertView.findViewById(R.id.group_count);
            convertView.setTag(holder);  
        }else{  
            holder = (ViewHolder)convertView.getTag();
        }
            
        holder.titleText.setText(mImageBean.getFolderName());
        holder.count.setText(Integer.toString(mImageBean.getImageCounts()));
     
		inflater.loadBit(context,holder.thumbImage,bmp,path,mPoint,1,mGroupGridView);
    	 
        return convertView;  
    }  
    
    class ViewHolder{  
        ImageView thumbImage;  
        TextView titleText;  
        TextView count;
    }  
    
}