package com.gallery.photoview.method;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gallery.photoview.MyApp;
import com.gallery.photoview.R;
import com.gallery.photoview.method.Imghandler.addBitCallback;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;

public class NativeImageLoader {
	private static LruCache<String, Bitmap> mMemoryCache;
	private static NativeImageLoader mInstance = new NativeImageLoader();
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);
	private Context mcontext;
	private NativeImageLoader(){
		//获取应用程序的最大内存
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		//用最大内存的1/4来存储图片
		final int cacheSize = maxMemory / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			
			//获取每张图片的大小
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}
	
	/**
	 * 通过此方法来获取NativeImageLoader的实例
	 * @return
	 */
	public static NativeImageLoader getInstance(){
		return mInstance;
	}
	
	
	/**
	 * 加载本地图片，对图片不进行裁剪
	 * @param path
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack){
		return this.loadNativeImage(null,path, null, 0,mCallBack);
	}
	
	/**
	 * 此方法来加载本地图片，这里的mPoint是用来封装ImageView的宽和高，我们会根据ImageView控件的大小来裁剪Bitmap
	 * 如果你不想裁剪图片，调用loadNativeImage(final String path, final NativeImageCallBack mCallBack)来加载
	 * @param path
	 * @param mPoint
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(Context mcontext,final String path, final Point mPoint, final int type, final NativeImageCallBack mCallBack){
		//先获取内存中的Bitmap
		this.mcontext=mcontext;
		Bitmap bitmap = getBitmapFromMemCache(path);
		if (bitmap== null) 	bitmap=getBitmapFromFile(path);
			
		
		//若该Bitmap不在内存缓存中，则启用线程去加载本地的图片，并将Bitmap加入到mMemoryCache中
		if(bitmap == null){
		addBitCallback p=new addBitCallback(){
				
				public void setBit (String path,Bitmap mBitmap)
				{
					
					addBitmapToFile(path, mBitmap);
					addBitmapToMemoryCache(path,mBitmap);
					mCallBack.onImageLoader(mBitmap, path);
				}
			};
		    Imghandler.addStack(path,mPoint,type,p);
			
		}
		//if ((bitmap!=null) && (mPoint.x>0) && (mPoint.y>0)) bitmap=ThumbnailUtils.extractThumbnail(bitmap, mPoint.x, mPoint.y);
		return bitmap;
		
	}

	
	public static void addBitmapToFile(String path,Bitmap bitmap)
	{
		 if (bitmap==null) return;
		 String s=Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM";
		 File dir =new File(s);
		 dir=new File(dir,"photoview");
		 if (!dir.exists()) dir.mkdir();
		 dir=new File(dir,"thumb");
		 if (!dir.exists()) dir.mkdir();
         String name=path.replaceAll("/", "").replaceAll("[.]", "")+".jpeg";
         dir=new File(dir,name);
         if (!dir.exists())
         {
         try {  
             FileOutputStream b = new FileOutputStream(dir);  
             if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b))// 把数据写入文件 
             {
               b.flush();  
               b.close();
             }
             //MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, name, null);
            } catch (FileNotFoundException e) {  
             e.printStackTrace();  
            } catch (IOException e) {  
             e.printStackTrace();  
            }
         }
          
	}
	
	
	/**
	 * 往内存缓存中添加Bitmap
	 * 
	 * @param key
	 * @param bitmap
	 */
	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 根据key来获取内存中的图片
	 * @param key
	 * @return
	 */
	private static Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	
	private Bitmap getBitmapFromFile(String path) {
		String s=Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/photoview/thumb/"; 
		String name=s+path.replaceAll("/", "").replaceAll("[.]","")+".jpeg";
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = false;
		Bitmap mBitmap;
		try
		{
			   mBitmap=BitmapFactory.decodeFile(name, op);
		}
		catch (OutOfMemoryError e)
		{
				mBitmap=null;
		}
		if (mBitmap!=null) addBitmapToMemoryCache(path, mBitmap);  
		return mBitmap;
        
	}
	
	
	/**
	 * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	public static Bitmap decodeThumbBitmapForFile(int type,String path, int viewWidth, int viewHeight){
		
		//type为0则decode图片，1为视频。
		if (type ==0)
		{
		    BitmapFactory.Options options = new BitmapFactory.Options();
	    	//设置为true,表示解析Bitmap对象，该对象不占内存
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(path, options);
		    //设置缩放比例
		    options.inSampleSize = computeScale(options, viewWidth, viewHeight);
		    //设置为false,解析Bitmap对象加入到内存中
		    options.inJustDecodeBounds = false;
		   // options.inPreferredConfig=Bitmap.Config.ARGB_4444;
	    	Bitmap bitmap=BitmapFactory.decodeFile(path, options);
	    	if (viewWidth>0 && viewHeight>0)
	    	{
		       bitmap=ThumbnailUtils.extractThumbnail(bitmap, viewWidth, viewWidth,
			    	 ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	    	}
		    return bitmap;
		}
		else 
		{
			Bitmap bitmap = null;
    		// 获取视频的缩略图
    		bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
    		//System.out.println("w"+bitmap.getWidth());
    		//System.out.println("h"+bitmap.getHeight());
    		bitmap = ThumbnailUtils.extractThumbnail(bitmap, viewWidth, viewWidth,
    		    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			return bitmap;
		}
	}
	
	
	/**
	 * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
	 * @param options
	 * @param width
	 * @param height
	 */
	private static int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
		int inSampleSize = 1;
		if(viewWidth == 0 || viewWidth == 0){
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		
		//假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
		if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);
			
			//为了保证图片不缩放变形，我们取宽高比例最小的那个
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		if (inSampleSize<1) inSampleSize=1;
		    
		return inSampleSize;
	}
	
	
	 public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
 		Bitmap bitmap = null;
 		// 获取视频的缩略图
 		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
 		//System.out.println("w"+bitmap.getWidth());
 		//System.out.println("h"+bitmap.getHeight());
 		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
 	     	ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
 		if (bitmap!=null)
 		{
 		  Bitmap newb=Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888);
 		  Canvas cv = new Canvas( newb );
 		  cv.drawBitmap(bitmap, 0, 0, null );
 		  Context c=MyApp.getinstance();
 		  Resources res=c.getResources();
 		  Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.circle_play);
 		  cv.drawBitmap(bmp,(width-bmp.getWidth())/2,(height-bmp.getHeight())/2,null);
 		  bitmap.recycle();
 		  return newb;
 		}
 		else return null;
 }
	
	/**
	 * 加载本地图片的回调接口
	 * 
	 * @author xiaanming
	 *
	 */
	public interface NativeImageCallBack{
		/**
		 * 当子线程加载完了本地的图片，将Bitmap和图片路径回调在此方法中
		 * @param bitmap
		 * @param path
		 */
		public void onImageLoader(Bitmap bitmap, String path);
	}
}
