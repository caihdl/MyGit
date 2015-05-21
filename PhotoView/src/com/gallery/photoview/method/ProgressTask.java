package com.gallery.photoview.method;

import java.util.ArrayList;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ProgressTask {
    private ArrayList<String> plist;
    private Context mcontext;
	
	public ProgressTask(Context context) {
		// TODO Auto-generated constructor stub
		super();
		mcontext=context;
	}
	
	public ArrayList<String> findimg() {
		plist=new ArrayList<String>();
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = mcontext.getContentResolver();

		//ֻ��ѯjpeg, jpg��png��ͼƬ
		Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, null);
		
		while (mCursor.moveToNext()) {
			//��ȡͼƬ��·��
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			plist.add(path);
		}
		mCursor.close();
		mImageUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
	    mContentResolver = mcontext.getContentResolver();

		//ֻ��ѯjpeg, jpg��png��ͼƬ
		mCursor = mContentResolver.query(mImageUri, null, null, null, null);
		
		while (mCursor.moveToNext()) {
			//��ȡͼƬ��·��
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			plist.add(path);
		}
		mCursor.close();
		return plist;
    }
	
	
	public ArrayList<String> findvid() {
		  plist=new ArrayList<String>();
		  String[] mediaColumns = new String[]{  
	                MediaStore.Video.Media.DATA,  
	                MediaStore.Video.Media._ID,  
	                MediaStore.Video.Media.TITLE,  
	                MediaStore.Video.Media.MIME_TYPE};  
	        
	        ContentResolver mContentResolver = mcontext.getContentResolver();
	       
	        Cursor cursor = mContentResolver.query(MediaStore.Video.Media.INTERNAL_CONTENT_URI,
	        		mediaColumns, null, null, null); 
	        if(cursor.moveToFirst()){
	            do{  
	            	String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
	                plist.add(filePath);      
	            }while(cursor.moveToNext());  
	        }  
	        
	        cursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
	        		mediaColumns, null, null, null);  
	        if(cursor.moveToFirst()){
	            do{  
	            	String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
	                plist.add(filePath);      
	            }while(cursor.moveToNext());  
	        }  
	        cursor.close();
		return plist;
    }
}


