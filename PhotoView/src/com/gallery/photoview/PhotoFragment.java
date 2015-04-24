package com.gallery.photoview;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhotoFragment extends Fragment{
      
	 private ArrayList<Object> photolist;
	 private ListView listview;
	 private View mview;
	 private Context mcontext;
	 private MyAdapter madapter;
	 
	 public PhotoFragment(ArrayList<URI> a,Context context,int sortType)
	 {
		super();
	    photolist = sort.sortphoto(a,sortType);
	    mcontext=context;
	 }
	 
	 @Override  
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  
	 {  
		    
	        mview =inflater.inflate(R.layout.photolayout, container, false);  
	        listview =(ListView) mview.findViewById(R.id.photo_list);
	        MyAdapter mAdapter = new MyAdapter(mcontext);
	        listview.setAdapter(mAdapter);
	        return mview;
	 }  
	 
	 public class MyAdapter extends BaseAdapter{
		private LayoutInflater mInflater; 
		 
		public MyAdapter(Context context)
		{
			
			mInflater=LayoutInflater.from(context);
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
			
			 if (current.getClass().getName().equals("ChildListUri")){
			     if (convertView == null) {
                     convertView = mInflater.inflate(R.layout.photolist,null);
                	 holderImg = new ViewHolderImg();
                	 holderImg.img[0]=(ImageView) convertView.findViewById(R.id.img1);
                	 holderImg.img[1]=(ImageView) convertView.findViewById(R.id.img2);
                	 holderImg.img[2]=(ImageView) convertView.findViewById(R.id.img3);
                	 holderImg.img[3]=(ImageView) convertView.findViewById(R.id.img4);
                     convertView.setTag(holderImg);
			     }
			     else {
			    	 holderImg=(ViewHolderImg)convertView.getTag();
			    
			     }
			     ListChildUri p=(ListChildUri) getItem(position);
			     for (int i=0;i<=3;i++){
			    	 holderImg.img[i].setImageBitmap(getLoacalBitmap(p.uri[i].toString()));
			     }
				 
		     }
			 else {
				 if (convertView == null) {
                     convertView = mInflater.inflate(R.layout.photolist,null);
                	 holderText = new ViewHolderText();
                	 holderText.title=(TextView) convertView.findViewById(R.id.title);
                     convertView.setTag(holderText);
			     }
			     else {
			    	 holderText=(ViewHolderText)convertView.getTag();
			    
			     }
				 ListChildTitle p=(ListChildTitle) getItem(position);
				 holderText.title.setText(p.title);
			  
			 }
			 return convertView;
	     }
	 
	     public class ViewHolderText{
		    public TextView title=null;
	     }
	 
	     public class ViewHolderImg{
		    public ImageView img[]={null,null,null,null};
	     }
	     
	     public Bitmap getLoacalBitmap(String url) {
	         try {
	              FileInputStream fis = new FileInputStream(url);
	              return BitmapFactory.decodeStream(fis);
	         } catch (FileNotFoundException e) {
	              e.printStackTrace();
	              return null;
	         }
	    }
	 }
}
