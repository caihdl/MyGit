package com.gallery.photoview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.gallery.photoview.data.Myintent;
import com.gallery.photoview.data.reflasher;
import com.gallery.photoview.madapter.ChildAdapter;
import com.gallery.photoview.madapter.VChildAdapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ShowImageActivity extends AppCompatActivity {

	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;
	private char media;
	private VChildAdapter vAdapter;
    private Bitmap bmp;
	private MyApp appState;
	private Myintent mintent;
	private reflasher mreflash;
	private static Menu menu;
	private void playVideo(String videoPath) {  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        String strend="";
        
        if(videoPath.toLowerCase().endsWith(".mp4")) {  
            strend="mp4";  
        }  
        else if(videoPath.toLowerCase().endsWith(".3gp")) {  
            strend="3gp";  
        }  
        else if(videoPath.toLowerCase().endsWith(".mov")) {  
            strend="mov";  
        }  
        else if(videoPath.toLowerCase().endsWith(".wmv")) {  
            strend="wmv";  
        }  
          
        intent.setDataAndType(Uri.parse(videoPath), "video/"+strend);  
        startActivity(intent);  
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.Mytoolbar);  
		setSupportActionBar(toolbar);
		
		toolbar.setLogo(R.drawable.iphoto2);
		toolbar.setNavigationIcon(R.drawable.arrow_left);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {  
			 @Override  
			 public void onClick(View v) {  
				 onBackPressed();
			 }  
		});
		
		mreflash=new reflasher()
		{
	           @Override
	           public void reflash()
	           {
	        	  MyApp.getinstance().setState(false);
	        	  MyApp.getinstance().newhash();
	        	  re();
	           }
		};
		
		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");
		media = getIntent().getCharExtra("media", 'v');
		mintent=new Myintent(){
		    	public void startintent(int position)
		    	{
				    if(media == 'i') {
				    	Intent intent = new Intent(ShowImageActivity.this, ViewPage.class);
						intent.putStringArrayListExtra("imgPath", (ArrayList<String>)list);
						intent.putExtra("pos", position);
						startActivity(intent);
				    }
				    else {
				    	String videoPath = list.get(position).toString();
				    	playVideo(videoPath);
				    }
		    	}  
		    };
		Resources res = this.getResources();  
		bmp= BitmapFactory.decodeResource(res, R.drawable.friends_sends_pictures_no);
		appState = ((MyApp) getApplicationContext()); 
		if(media == 'i') {
			adapter = new ChildAdapter(this, list, mGridView,mintent,mreflash);
		    mGridView.setAdapter(adapter);
		}
		else {
			vAdapter = new VChildAdapter(this, list, mGridView,mintent,mreflash);
			mGridView.setAdapter(vAdapter);
		}
	}

	public void re()
	{
		if(media == 'i') {
			adapter = new ChildAdapter(this, list, mGridView,mintent,mreflash);
		    mGridView.setAdapter(adapter);
		}
		else {
			vAdapter = new VChildAdapter(this, list, mGridView,mintent,mreflash);
			mGridView.setAdapter(vAdapter);
		}
		mGridView.invalidate();
	}
	
	@Override
	public void onBackPressed() {
		if (MyApp.getinstance().getState())
        {
        	MyApp.getinstance().setState(false);
        	MyApp.getinstance().newhash();
        	
        	if (media=='i') adapter.notifyDataSetChanged();
        	else vAdapter.notifyDataSetChanged();
        }
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (media=='i') getMenuInflater().inflate(R.menu.file, menu);
		else getMenuInflater().inflate(R.menu.file2, menu);
		this.menu=menu;
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.share:
			if (MyApp.getinstance().getState())
			{
				if (media=='i') //分享图片
				{
				    List<Integer> mlist=MyApp.getinstance().getSelectItems();
				    if(mlist.isEmpty()) {
				    	Toast.makeText(this, "未选择任意一项", Toast.LENGTH_SHORT).show();
				    	return true;
				    }
				    
			    	ArrayList plist=new ArrayList<String>();
				    for (int i:mlist)
				    {
					   plist.add(list.get(i));
				    }
				    if(!plist.isEmpty())
				    	showAllApp(plist);
				}
				else //分享视频
				{
					List<Integer> mlist=MyApp.getinstance().getSelectItems();
				    if(mlist.isEmpty()) {
				    	Toast.makeText(this, "未选择任意一项", Toast.LENGTH_SHORT).show();
				    	return true;
				    }
				    
				    ArrayList plist=new ArrayList<String>();
				    for (int i:mlist)
				    {
					   plist.add(list.get(i));
				    }
				    if(!plist.isEmpty())
				    	showAllApp(plist);
				}
				
				MyApp.getinstance().setState(false);
            	MyApp.getinstance().newhash();

            	if(media == 'i')
            		adapter.notifyDataSetChanged();
            	else
            		vAdapter.notifyDataSetChanged();
			}
			else
				Toast.makeText(this, "未选择任意一项", Toast.LENGTH_SHORT).show();
			
			return true;
		
		case R.id.add:
			/*---------------照片--------------------*/
			if (media=='i')
			{
			Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(mIntent, 1);
			}
			else
			{
			/*-----------------视频--------------------*/
			Intent mintent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			mintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			
			File dir = new File(Environment.getExternalStorageDirectory(), "/VideoView");
            if(!dir.exists())
            	dir.mkdir();
			
            // 以日期时间命名
			new DateFormat();  
            String name = DateFormat.format("yyyyMMdd_hhmmss",
            		Calendar.getInstance(Locale.CHINA)) + ".jpg";
            File file = new File(dir, name); // 在指定目录下创建文件
            mintent.putExtra(MediaStore.EXTRA_OUTPUT, file.getAbsolutePath());
            
			startActivityForResult(mintent, 2);
		    }
			
			return true;	
			
		case R.id.delete:
			if (media=='i') 
			{
			    adapter.deleteOperation();
			}
		    else
		    {
		      	vAdapter.deleteOperation();
		    }
		    MyApp.getinstance().setState(false);
    	    MyApp.getinstance().newhash();

    	    if (media=='i')
    	    {
    	    	 adapter.notifyDataSetChanged();
    	    }
    	    else vAdapter.notifyDataSetChanged();
    	    
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void showAllApp(ArrayList<String> path) {
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);

		if(!path.isEmpty()) {
			ArrayList<Uri> uriList = new ArrayList<Uri>();
			for(String p : path) {
				File f = new File(p);
				uriList.add(Uri.fromFile(f));
			}

			if(media == 'i')
				intent.setType("image/*");
			else
				intent.setType("video/*");
		    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
		
		    startActivity(Intent.createChooser(intent,"分享到"));
		}
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            if (MyApp.getinstance().getState())
            {
            	MyApp.getinstance().setState(false);
            	MyApp.getinstance().newhash();
            	
            	if (media=='i') adapter.notifyDataSetChanged();
            	else vAdapter.notifyDataSetChanged();
            }
            else 
            {
            	super.onKeyDown(keyCode, event);
            }
            return true;  
        } else  
            return super.onKeyDown(keyCode, event);  
    }  
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == RESULT_OK) {
			// 目录
            File dir = new File(Environment.getExternalStorageDirectory(), "ImageView");
            if(!dir.exists())
            	dir.mkdir();
			
            // 以日期时间命名
			new DateFormat();  
            String name = DateFormat.format("yyyyMMdd_hhmmss",
            		Calendar.getInstance(Locale.CHINA)) + ".jpg";
            File file = new File(dir, name); // 在指定目录下创建文件

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap newBitmap = zoomBitmap(bitmap, bitmap.getWidth() * 4,
            		bitmap.getHeight() * 4);
            bitmap.recycle();
            
            try {  
                FileOutputStream b = new FileOutputStream(file);  
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件 
                b.flush();  
                b.close();
                
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
            
            // 通知系统更新图库
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
            		Uri.fromFile(file)));
            
            Toast.makeText(this, "已保存为文件" + file.getAbsolutePath(),
            		Toast.LENGTH_SHORT).show();
		}
		else if(requestCode == 2 && resultCode == RESULT_OK) {
			Uri uri = Uri.parse(data.getData().toString());
            
			ContentResolver cr = this.getContentResolver();    
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			String str = cursor.getString(1);
            String videofileName = cursor.getString(2);
            cursor.close(); 
			
            File destfile = new File(Environment.getExternalStorageDirectory() + "/VideoView",
            		videofileName);
            File srcfile = new File(str);
            
            try {
				moveFileTo(srcfile, destfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
            		Uri.fromFile(srcfile))); //删掉的源文件也有扫，否则会在camera目录下有个无效文件
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
            		Uri.fromFile(destfile)));
            
			Toast.makeText(this, "已保存为文件" + destfile.getAbsolutePath(),
            		Toast.LENGTH_SHORT).show();
		}
	}
	
	/** 缩放Bitmap图片 **/
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        
        return newbmp;
    }
	
	public boolean moveFileTo(File srcFile, File destFile) throws IOException {
		boolean iscopy = copyFileTo(srcFile, destFile);
		
		if(!iscopy)
			return false;
		
		delFile(srcFile);
		return true;
	}
	
	public boolean copyFileTo(File srcFile, File destFile) throws IOException {  
		if(srcFile.isDirectory() || destFile.isDirectory())
			return false;
		
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		
		int readLen = 0;
		byte[] buf = new byte[1024];
		
	    while((readLen = fis.read(buf)) != -1)
	    	fos.write(buf, 0, readLen);
	    
	    fos.flush(); 
        fos.close(); 
        fis.close();
        
        return true;
	}
	
	public boolean delFile(File file) {
		if(file.isDirectory())
			return false;
		
		return file.delete();
	}

	public static void onlongclick()
	{
		String s=MyApp.getinstance().getcount()+"";
		menu.getItem(R.id.count).setTitle(s);
	}
	
    public static void resetlongclick() 
	{
    	menu.getItem(R.id.count).setTitle("");
	}
}