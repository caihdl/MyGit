package com.gallery.photoview.mfragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.gallery.photoview.madapter.VideolistAdapter;
import com.gallery.photoview.method.inflater;
import com.gallery.photoview.method.sort;

public class VideoFragment extends Fragment{
      
	 private ArrayList<Object> photolist;
	 private ListView listview;
	 private View mview;
	 private Context mcontext;

	 private Bitmap bmp;
	 //private int screenWidth;
	 private Point mPoint = new Point(0, 0);
	 String path;
	 private int ImgWidth;
	private VideolistAdapter mAdapter;
	 
	 public VideoFragment()
	 {
		super();
	 }
	 
	 public VideoFragment(ArrayList<String> a,Context context)
	 {
		super();
	    photolist = sort.sortphoto(a,3);
	    mcontext=context;
	   
	 }
	 
	 @Override  
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  
	 {  	    
	        mview =inflater.inflate(R.layout.photolayout, container, false);  
	        listview =(ListView) mview.findViewById(R.id.photo_list);
	        mAdapter = new VideolistAdapter(mcontext,photolist,listview);
	        listview.setAdapter(mAdapter);
	        return mview;
	 }  
	 
	
}
