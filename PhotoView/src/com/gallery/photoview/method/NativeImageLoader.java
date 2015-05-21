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
		//��ȡӦ�ó��������ڴ�
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		//������ڴ��1/4���洢ͼƬ
		final int cacheSize = maxMemory / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			
			//��ȡÿ��ͼƬ�Ĵ�С
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}
	
	/**
	 * ͨ���˷�������ȡNativeImageLoader��ʵ��
	 * @return
	 */
	public static NativeImageLoader getInstance(){
		return mInstance;
	}
	
	
	/**
	 * ���ر���ͼƬ����ͼƬ�����вü�
	 * @param path
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack){
		return this.loadNativeImage(null,path, null, 0,mCallBack);
	}
	
	/**
	 * �˷��������ر���ͼƬ�������mPoint��������װImageView�Ŀ�͸ߣ����ǻ����ImageView�ؼ��Ĵ�С���ü�Bitmap
	 * ����㲻��ü�ͼƬ������loadNativeImage(final String path, final NativeImageCallBack mCallBack)������
	 * @param path
	 * @param mPoint
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(Context mcontext,final String path, final Point mPoint, final int type, final NativeImageCallBack mCallBack){
		//�Ȼ�ȡ�ڴ��е�Bitmap
		this.mcontext=mcontext;
		Bitmap bitmap = getBitmapFromMemCache(path);
		if (bitmap== null) 	bitmap=getBitmapFromFile(path);
			
		
		//����Bitmap�����ڴ滺���У��������߳�ȥ���ر��ص�ͼƬ������Bitmap���뵽mMemoryCache��
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
             if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b))// ������д���ļ� 
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
	 * ���ڴ滺�������Bitmap
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
	 * ����key����ȡ�ڴ��е�ͼƬ
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
	 * ����View(��Ҫ��ImageView)�Ŀ�͸�����ȡͼƬ������ͼ
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	public static Bitmap decodeThumbBitmapForFile(int type,String path, int viewWidth, int viewHeight){
		
		//typeΪ0��decodeͼƬ��1Ϊ��Ƶ��
		if (type ==0)
		{
		    BitmapFactory.Options options = new BitmapFactory.Options();
	    	//����Ϊtrue,��ʾ����Bitmap���󣬸ö���ռ�ڴ�
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(path, options);
		    //�������ű���
		    options.inSampleSize = computeScale(options, viewWidth, viewHeight);
		    //����Ϊfalse,����Bitmap������뵽�ڴ���
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
    		// ��ȡ��Ƶ������ͼ
    		bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
    		//System.out.println("w"+bitmap.getWidth());
    		//System.out.println("h"+bitmap.getHeight());
    		bitmap = ThumbnailUtils.extractThumbnail(bitmap, viewWidth, viewWidth,
    		    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			return bitmap;
		}
	}
	
	
	/**
	 * ����View(��Ҫ��ImageView)�Ŀ�͸�������Bitmap���ű�����Ĭ�ϲ�����
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
		
		//����Bitmap�Ŀ�Ȼ�߶ȴ��������趨ͼƬ��View�Ŀ�ߣ���������ű���
		if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);
			
			//Ϊ�˱�֤ͼƬ�����ű��Σ�����ȡ��߱�����С���Ǹ�
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		if (inSampleSize<1) inSampleSize=1;
		    
		return inSampleSize;
	}
	
	
	 public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
 		Bitmap bitmap = null;
 		// ��ȡ��Ƶ������ͼ
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
	 * ���ر���ͼƬ�Ļص��ӿ�
	 * 
	 * @author xiaanming
	 *
	 */
	public interface NativeImageCallBack{
		/**
		 * �����̼߳������˱��ص�ͼƬ����Bitmap��ͼƬ·���ص��ڴ˷�����
		 * @param bitmap
		 * @param path
		 */
		public void onImageLoader(Bitmap bitmap, String path);
	}
}
