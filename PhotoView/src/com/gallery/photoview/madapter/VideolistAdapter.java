package com.gallery.photoview.madapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gallery.photoview.R;
import com.gallery.photoview.data.ListChild.ListChildTitle;
import com.gallery.photoview.data.ListChild.ListChildUri;
import com.gallery.photoview.data.ListChild.ViewHolderImg;
import com.gallery.photoview.data.ListChild.ViewHolderText;
import com.gallery.photoview.method.inflater;

public class VideolistAdapter extends BaseAdapter{
	private LayoutInflater mInflater; 
	private Context mcontext;
	private ArrayList<Object> photolist;
	private ListView listview;
	private Bitmap bmp;
	private Point mPoint;
	
	public VideolistAdapter(Context context, ArrayList<Object> photolist,ListView listview)
	{
		mcontext=context;
		mInflater=LayoutInflater.from(context);
		this.photolist=photolist;
		this.listview=listview;
		DisplayMetrics  dm = new DisplayMetrics();     
	    WindowManager wm = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE); 
		wm.getDefaultDisplay().getMetrics(dm);     
		float scale = mcontext.getResources().getDisplayMetrics().density;
	    int ImgWidth = dm.widthPixels/4-(int)(5 * scale + 0.5f);
	    mPoint=new Point(ImgWidth,ImgWidth);
	    Resources res = mcontext.getResources();  
		bmp= BitmapFactory.decodeResource(res, R.drawable.friends_sends_pictures_no);  
	    bmp = ThumbnailUtils.extractThumbnail(bmp, ImgWidth, ImgWidth,
						    	 ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = 0;
		if (photolist!=null) count = photolist.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object x=null;
		if (photolist!=null) x=photolist.get(position);
		return x;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Object current=getItem(position);
		ViewHolderText holderText;
		ViewHolderImg  holderImg;
		ListChildTitle x=(ListChildTitle) current;  
		if (x.isUri){
			if (convertView == null) {
                 
            	 holderImg = new ViewHolderImg();
            	 convertView=inflater.inflate(mInflater,convertView,holderImg);
			}
			else
			{
				try
				{
				 holderImg=(ViewHolderImg)convertView.getTag();
				}
				catch(Exception e){
                	 holderImg = new ViewHolderImg();
                	 convertView=inflater.inflate(mInflater,convertView,holderImg);
				}
			}
            ListChildUri p=(ListChildUri) getItem(position);
            String path;
    		for (int i=0;i<=3;i++) {
    		     if (p.uri[i]!=null)
    		        {
    			    	  path=p.uri[i];
    			    	  inflater.loadBit(mcontext,holderImg.img[i],bmp,path,mPoint,1,listview);
    		        }
    		    	else 
    		    	{
    		    		     holderImg.img[i].setVisibility(0);
    		    		     holderImg.img[i].setClickable(false);
    		    	}
    		     }
		 }
		else
		{
			if (convertView == null) {
           	 holderText = new ViewHolderText();
           	 convertView=inflater.inflate(mInflater,convertView,holderText);
			}
			else
			{
				try
				{
				 holderText=(ViewHolderText)convertView.getTag();
				}
				catch(Exception e){
                	 holderText = new ViewHolderText();
                	 convertView=inflater.inflate(mInflater,convertView,holderText);
				}
			}
			holderText.title.setText(x.title);
		}
		 return convertView;
     }
}
