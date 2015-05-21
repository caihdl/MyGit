package com.gallery.photoview.mfragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gallery.photoview.MyApp;
import com.gallery.photoview.R;
import com.gallery.photoview.data.Myintent;
import com.gallery.photoview.data.reflasher;
import com.gallery.photoview.madapter.PhotolistAdapter;
import com.gallery.photoview.method.sort;

public class PhotoFragment extends Fragment{
      
	 private ArrayList<Object> photolist;
	 private ListView listview;
	 private View mview;
	 private Context mcontext;
	 private PhotolistAdapter madapter;
	 private Bitmap bmp;
	 //private int screenWidth;
	 private Point mPoint = new Point(0, 0);
	 String path;
	 private int ImgWidth;
	private ArrayList<String> alist;
	private Myintent mintent;
	private int type;
	private reflasher mreflash;
	 
	 public PhotoFragment()
	 {
		super();
	 }
	 public PhotoFragment(ArrayList<String> a,Context context,Myintent mintent,int type,reflasher mreflash)
	 {
		super();
	    photolist = sort.sortphoto(a,3);
	    this.mreflash=mreflash;
	    this.alist=a;
	    this.type=type;
	    this.mintent=mintent;
	    mcontext=context;
	   
	 }
	 
	 @Override  
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  
	 {  	    
	        mview =inflater.inflate(R.layout.photolayout, container, false);  
	        listview =(ListView) mview.findViewById(R.id.photo_list);
	        madapter = new PhotolistAdapter(type,mcontext,photolist,alist,listview,mintent,mreflash);
	        listview.setAdapter(madapter);
	        return mview;
	 }  
	 
	 public void delete()
	 {
		  madapter.delete();
	 
	 
	   
	 }
}
