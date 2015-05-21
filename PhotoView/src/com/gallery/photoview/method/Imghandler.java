package com.gallery.photoview.method;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import com.gallery.photoview.method.NativeImageLoader;

public class Imghandler {
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);
	private int x,y;
	public  static Stack<setimgdata> mstack=null;
	private static setimgdata msetimgdata;
	private ArrayList<String> list;
	private ArrayList<String> mimg;
	private ArrayList<String> mvid; 
	private long time;
	private long time2;
	private Point mPoint=new Point(240,240);
	public Imghandler(ArrayList<String> img,ArrayList<String> vid,Point pp) {
		// TODO Auto-generated constructor stub
		mimg=img;
		mvid=vid; 
		time=0;
		time2=0;
		x=0;
		y=0;
		mPoint=pp;
		mstack=new Stack<setimgdata>();
		list=new ArrayList<String>();
		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				setimgdata newdata=(setimgdata) msg.obj;
				long start_time = System.currentTimeMillis();
				newdata.mcallback.setBit(newdata.path, newdata.mbitmap);
				long end_time = System.currentTimeMillis();
				time2+=end_time-start_time;
			}
			
		};
		
		mImageThreadPool.execute(new Runnable() {
			
			
			@Override
			public void run() {
				while (true)
				{
					if (setStack(1,null))
					{
						if (!list.contains(msetimgdata.path))
						{
			              //先获取图片的缩略图
				          Bitmap mBitmap;
				          long start_time = System.currentTimeMillis();
				          if (msetimgdata.type==0) mBitmap= NativeImageLoader.decodeThumbBitmapForFile(msetimgdata.type,msetimgdata.path, mPoint.x, mPoint.y);
				              else mBitmap=NativeImageLoader.getVideoThumbnail(msetimgdata.path, mPoint.x, mPoint.y);
				          long end_time = System.currentTimeMillis();
				          time+=end_time-start_time;
				          list.add(msetimgdata.path);
				          Message msg = mHander.obtainMessage();
				          msetimgdata.setbit(mBitmap);
						  msg.obj = msetimgdata; 
						  //NativeImageLoader.addBitmapToFile(msetimgdata.path, mBitmap);
						  //NativeImageLoader.addBitmapToMemoryCache(msetimgdata.path,mBitmap);
						  mHander.sendMessage(msg);
						}
				     }
					else 
					{    
						 if (x<mimg.size())
						 {
							String z=mimg.get(x);
							if (!list.contains(mimg.get(x)))
							{
						    Bitmap mBitmap;
				            mBitmap= NativeImageLoader.decodeThumbBitmapForFile(0,z, mPoint.x, mPoint.y);
				            NativeImageLoader.addBitmapToFile(z, mBitmap);
							NativeImageLoader.addBitmapToMemoryCache(z,mBitmap);
				            list.add(mimg.get(x));
							}
				            x++;
						 }
						 if (y<mvid.size())
						 {
							 String z=mimg.get(y);
							 if (!list.contains(mimg.get(y)))
							 {
						     	Bitmap mBitmap;
					            mBitmap= NativeImageLoader.decodeThumbBitmapForFile(0,z, mPoint.x, mPoint.y);
					            NativeImageLoader.addBitmapToFile(z, mBitmap);
								NativeImageLoader.addBitmapToMemoryCache(z,mBitmap);
					            list.add(mimg.get(y));
							 }
					         y++;
						 }
					}
					//if (x==mimg.size() && y==mimg.size()) break;
				}
			}
		});
	}
    
	public void add(int type,String path)
	{
		if (type==0)
		{
			mimg.add(path);
		}
		else 
		{
		    mvid.add(path);
		}
	}
 	
    public void delete(int type,String path)
    {
    	if (type==0)
    	{
    		int y=mimg.indexOf(path);
    		if (y<x) x--; 
            mimg.remove(y);
           
    	}
    	else 
    	{
    		int y=mvid.indexOf(path);
    		if (y<x) x--; 
            mvid.remove(y);
           
    	}
    	 list.remove(path);
    }
    
	public interface addBitCallback
	{
		 void setBit(String a,Bitmap b);
	}
	
	public static void addStack(String path,Point mPoint,int type,addBitCallback mcallback)
	{
		if (path==null) return;
		setimgdata d=new setimgdata(path,mPoint,type,mcallback,null);
		setStack(0,d);
	}
	
	
	private synchronized static boolean setStack(int t,setimgdata d)
	{
		  if (t==0)
		  {
			  mstack.push(d);
			  return true;
		  }
		  else 
		  {
			  if (mstack.isEmpty()) 
				  {
				    msetimgdata=null;
				    return false;
				  }
			  else
				  {
				      msetimgdata=mstack.pop();
				      return true;
				  }
		  }
	}
	
	
    public static class setimgdata
    {
    	String path;
    	Point mPoint;
    	int type;
    	addBitCallback mcallback;
    	Bitmap mbitmap;
    	public setimgdata(String path,Point mPoint,int type,addBitCallback mcallback,Bitmap bitmap)
    	{
    	  this.path=path;
    	  this.mPoint=mPoint;
    	  this.type=type;
    	  this.mcallback=mcallback;
    	  this.mbitmap=bitmap;
    	}
    	
    	public void setbit(Bitmap bitmap)
    	{
    		mbitmap=bitmap;
    	}
    }
    
   
}
