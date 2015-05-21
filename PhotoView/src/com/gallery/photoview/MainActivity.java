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
import com.gallery.photoview.method.Imghandler;
import com.gallery.photoview.method.ProgressTask;
import com.gallery.photoview.method.sort;
import com.gallery.photoview.mfragment.PhotoFragment;
import com.gallery.photoview.mfragment.Fragment_Byfile;

import android.support.v4.app.Fragment;    
import android.support.v4.app.FragmentActivity; 
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends  AppCompatActivity   {
	
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mViews; 
    private LayoutInflater mInflater;
    private TextView mphotoview;  
    private TextView mvideoview;  
    private FragAdapter mAdapter;
    private PhotoFragment photofragment;
    private PhotoFragment videofragment;
    private Fragment_Byfile photofragment2;
    private Fragment_Byfile videofragment2;
    private ProgressTask progress;
    public boolean TaskOk;
    private ProgressDialog mProgressDialog;
    private ArrayList<String> ListImg;
	private ArrayList<String> ListVid;
	private static int type;
	private int ImgWidth;
	private Point mPoint;
	private Myintent mintentvid;
	private Myintent mintentimg;
	public static int state;
	private static Menu menu;
	private static Drawable res1;
	private static Drawable res2;
	public reflasher mreflash;
	private long exitTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progress=new ProgressTask(this);
		ListImg=sort.qsort(progress.findimg());
		ListVid=sort.qsort(progress.findvid());
		
		initview();
	}
	
	protected void initview(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.Mytoolbar);  
		toolbar.setLogo(R.drawable.iphoto2);
		setSupportActionBar(toolbar);
		
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); 
		DisplayMetrics  dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);     
		float scale = getResources().getDisplayMetrics().density;
	    ImgWidth = dm.widthPixels/4-(int)(5 * scale + 0.5f);
	    mPoint=new Point(ImgWidth,ImgWidth);
	    new Imghandler(ListImg,ListVid,mPoint);
	    
	     mreflash=new reflasher()
	    		{
	    	           @Override
	    	           public void reflash()
	    	           {
	    	        	   changeto(type);
	    	           }
	    		};
	    
		mintentimg=new Myintent(){
			@Override
	    	public void startintent(int position)
	    	{
			    
			    	Intent intent = new Intent(MainActivity.this, ViewPage.class);
					intent.putStringArrayListExtra("imgPath", (ArrayList<String>) ListImg);
					intent.putExtra("pos", position);
					startActivity(intent);
	    	}  
	    };
	    
	    mintentvid=new Myintent(){
	    	public void startintent(int position)
	    	{   
	    		String videoPath = ListVid.get(position).toString();
		    	playVideo(videoPath);
	    	}  
	    };
	    type=1;
	    state=0;
		mInflater = LayoutInflater.from(this);
		mphotoview = (TextView) findViewById(R.id.button_photo);
		mphotoview.setOnClickListener(new click());
		mvideoview = (TextView) findViewById(R.id.button_video);
		mvideoview.setOnClickListener(new click());
		changeto(type);
		
		mAdapter = new FragAdapter(getSupportFragmentManager(),mViews);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOnPageChangeListener(new pageChange());
		mViewPager.setAdapter(mAdapter); 
	}
	
	private void changeto(int type)
	{

		String s;
		while (true)
			{
			    s=MyApp.getinstance().poplist();
			    if (s==null) break;
			    if (ListImg.contains(s))
			    {
			    	ListImg.remove(s);
			    }
			    else if (ListVid.contains(s))
			    {
			      ListVid.remove(s); 
			    }
			}
		
		mViews = new ArrayList<Fragment>();
		if (type==1)
		{
		photofragment= new PhotoFragment(ListImg,this,mintentimg,0,mreflash);
	    mViews.add(photofragment);
		videofragment= new PhotoFragment(ListVid,this,mintentvid,1,mreflash);
		mViews.add(videofragment);
		}
		else 
		{
		photofragment2=new Fragment_Byfile(ListImg,this,'i');
	    mViews.add(photofragment2);
		videofragment2=new Fragment_Byfile(ListVid,this,'v');
		mViews.add(videofragment2);
		}

		if (mAdapter==null) return;
		
		mAdapter.setFragments(mViews);
		
	}
	
    
	
	public class FragAdapter extends FragmentPagerAdapter {  
		  
	    private ArrayList<Fragment> fragments;  
	    private FragmentManager fm;
	    private boolean[] fragmentsUpdateFlag;
	    public FragAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {  
	        super(fm);  
	        // TODO Auto-generated constructor stub 
	        this.fm=fm;
	        this.fragments=fragments;  
	        fragmentsUpdateFlag=new boolean[fragments.size()];
	        fragmentsUpdateFlag[0]=false;
	    	fragmentsUpdateFlag[1]=false;
	    }  
	    
	    
	    @Override  
	    public Fragment getItem(int arg0) {  
	        // TODO Auto-generated method stub  
	        return fragments.get(arg0);  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        // TODO Auto-generated method stub  
	        return fragments.size();  
	    }  
	    
	    
	    public void setFragments(ArrayList<Fragment> fragments) {
	    	this.fragments=fragments;
	    	fragmentsUpdateFlag[0]=true;
	    	fragmentsUpdateFlag[1]=true;
			notifyDataSetChanged();
		}
	    
	    @Override
	    public int getItemPosition(Object object) {
	     return POSITION_NONE;
	    }
	    
	    @Override
	    public Object instantiateItem(ViewGroup container,int position) {
	       //�õ������fragment
	        Fragment fragment = (Fragment)super.instantiateItem(container,
	               position);
	       //�õ�tag 
	       String fragmentTag = fragment.getTag();         
	       if (fragmentsUpdateFlag[position]) {
	          //������fragment��Ҫ����	          
	           FragmentTransaction ft =fm.beginTransaction();
	          //�Ƴ��ɵ�fragment
	           ft.remove(fragment);
	          //�����µ�fragment
	           fragment =fragments.get(position);
	          //�����fragmentʱ������ǰ���õ�tag 
	           ft.add(container.getId(), fragment, fragmentTag);
	           ft.attach(fragment);
	           ft.commit();	    
	          //��λ���±�־
	         fragmentsUpdateFlag[position %fragmentsUpdateFlag.length] =false;
	        }
           return fragment;
	    }
	    
	}  
	
	public class click implements OnClickListener{
		
		 @Override  
		 public void onClick(View v){
			 switch (v.getId())
			 {
			     case R.id.button_photo :
			    	 mViewPager.setCurrentItem(0);	
			    	 break;
			     default :  
			    	 mViewPager.setCurrentItem(1);
			 }
			 
		 }
	}
	
	public class pageChange implements OnPageChangeListener {
		 private int currentIndex = 0;  
		  
         @Override  
         public void onPageSelected(int position)  
         {  
             switch (position)  
             {  
             case 0:  
            	 mphotoview.setTextColor(Color.argb(255,0,0,0));
            	 mvideoview.setTextColor(Color.argb(64,0,0,0));
            	 menu.findItem(R.id.add).setIcon(R.drawable.camera);
            	 state=0;
                 break;  
             case 1:  
            	 mphotoview.setTextColor(Color.argb(64,0,0,0));
            	 mvideoview.setTextColor(Color.argb(255,0,0,0));
              	 menu.findItem(R.id.add).setIcon(R.drawable.video);
              	 state=1;
            	 break;  
             }  
             if (MyApp.getinstance().getState())
 			 {
 				MyApp.getinstance().setState(false);
             	MyApp.getinstance().newhash();
             	resetlongclick();
             	mAdapter.notifyDataSetChanged();
             	
 			}
             
             currentIndex = position;  
         }  

         @Override  
         public void onPageScrolled(int arg0, float arg1, int arg2)  
         {  
         }  

         @Override  
         public void onPageScrollStateChanged(int arg0)  
         {  
         }  
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu=menu;
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.sort:
			if (!MyApp.getinstance().getState())
			{
			  if (type==1)
		    	{
				item.setIcon(R.drawable.windows);
				type=2;
			   }
			  else 
			  {
				item.setIcon(R.drawable.book_2);
				type=1;
			  }
			  changeto(type);
			}
			else
			{
				if (state==0) 
					{
					   photofragment.delete();
					}
				else
				{
					 videofragment.delete();
				}
				MyApp.getinstance().setState(false);
            	MyApp.getinstance().newhash();
            	resetlongclick();
            	mAdapter.notifyDataSetChanged();
			}
			
			return true;
		case R.id.add:
			if (MyApp.getinstance().getState()) //��ǰ�ǡ�����ͼ��
			{
				if (state==0) //����ͼƬ
				{
				    List<Integer> mlist=MyApp.getinstance().getSelectItems();
				    if(mlist.isEmpty()) {
				    	Toast.makeText(this, "δѡ������һ��", Toast.LENGTH_SHORT).show();
				        return true;
				    }
			    	
				    ArrayList plist = new ArrayList<String>(); // ѡ�е�ͼƬ·��
				    for (int i:mlist)
				    {
					   plist.add(ListImg.get(i));
				    }
				    if(!plist.isEmpty())
				    	showAllApp(plist);
				}
				else // ������Ƶ
				{
					List<Integer> mlist=MyApp.getinstance().getSelectItems();
					if(mlist.isEmpty()) {
				    	Toast.makeText(this, "δѡ������һ��", Toast.LENGTH_SHORT).show();
				        return true;
				    }
					
					ArrayList plist = new ArrayList<String>();
					for (int i:mlist)
					{
						plist.add(ListVid.get(i));
					}
					if(!plist.isEmpty())
				    	showAllApp(plist);
				}
				
				MyApp.getinstance().setState(false);
            	MyApp.getinstance().newhash();
            	resetlongclick();
            	mAdapter.notifyDataSetChanged();	
            	//Toast.makeText(this, "�ѷ���", Toast.LENGTH_SHORT).show();
            	
            	return true;
			}
			else {                                 // ��ǰ�ǡ���ӡ�ͼ��
            if (state==0)
            {
			/*---------------��Ƭ--------------------*/
			Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(mIntent, 1);
            }
            else
            {
            	/*-----------------��Ƶ--------------------*/
    			Intent mintent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    			mintent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
    			
    			File dir = new File(Environment.getExternalStorageDirectory(), "/VideoView");
                if(!dir.exists())
                	dir.mkdir();
    			
                // ������ʱ������
    			new DateFormat();  
                String name = DateFormat.format("yyyyMMdd_hhmmss",
                		Calendar.getInstance(Locale.CHINA)) + ".jpg";
                File file = new File(dir, name); // ��ָ��Ŀ¼�´����ļ�
                mintent.putExtra(MediaStore.EXTRA_OUTPUT, file.getAbsolutePath());
    			startActivityForResult(mintent, 2);
            }
			}
			
			return true;
			
		case R.id.reflash:
			Toast.makeText(this, "ˢ��", Toast.LENGTH_SHORT).show();
			changeto(type);
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void showAllApp(ArrayList<String> path) {
		// һ�����������app
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		if(!path.isEmpty()) {
			ArrayList<Uri> uriList = new ArrayList<Uri>();
			for(String p : path) {
				File f = new File(p);
				uriList.add(Uri.fromFile(f));
			}

			if(state == 0)
				intent.setType("image/*");
			else
				intent.setType("video/*");
			
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
		
		    startActivity(Intent.createChooser(intent,"����"));
		}
	}
	
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            if (MyApp.getinstance().getState())
            {
            	MyApp.getinstance().setState(false);
            	MyApp.getinstance().newhash();
            	resetlongclick();
            	mAdapter.notifyDataSetChanged();
            }
            
    		if((System.currentTimeMillis()-exitTime) > 2000) { 
    			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
    					Toast.LENGTH_SHORT).show(); 
    	         exitTime = System.currentTimeMillis(); 
    	    } 
    		else { 
    			finish(); 
    			System.exit(0); 
    		}
            
            return true;  
        }
        else
        	return super.onKeyDown(keyCode, event);  
    }  
	
	
	public static void onlongclick()
	{
		String s=MyApp.getinstance().getcount()+" ";
		CharSequence c=s;
		menu.findItem(R.id.count).setTitleCondensed(c);
		menu.findItem(R.id.sort).setIcon(R.drawable.bin);
		menu.findItem(R.id.add).setIcon(R.drawable.share2);
	}
	
    public static void resetlongclick() 
	{
    	menu.findItem(R.id.count).setTitleCondensed(" ");
    	menu.findItem(R.id.sort).setIcon(type==1?R.drawable.book_2:R.drawable.windows);
		menu.findItem(R.id.add).setIcon(state==0?R.drawable.camera:R.drawable.video);
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == RESULT_OK) {
			// Ŀ¼
            File dir = new File(Environment.getExternalStorageDirectory(), "ImageView");
            if(!dir.exists())
            	dir.mkdir();
			
            // ������ʱ������
			new DateFormat();  
            String name = DateFormat.format("yyyyMMdd_hhmmss",
            		Calendar.getInstance(Locale.CHINA)) + ".jpg";
            File file = new File(dir, name); // ��ָ��Ŀ¼�´����ļ�
            
          //��ȡ��Ļ�ߴ��С���ǳ������ڲ�ͬ��С���ֻ����и��õļ�����
            WindowManager wm=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
            int wwidth=wm.getDefaultDisplay().getWidth();//�ֻ���Ļ�Ŀ��
            int hheight=wm.getDefaultDisplay().getHeight();//�ֻ���Ļ�ĸ߶�
            
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap newBitmap = zoomBitmap(bitmap, bitmap.getWidth() * 4,
            		bitmap.getHeight() * 4);
            bitmap.recycle();
            
            try {  
                FileOutputStream b = new FileOutputStream(file);  
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ� 
                b.flush();  
                b.close();
                //MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, name, null);
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }

            // ֪ͨϵͳ����ͼ��
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
            		Uri.fromFile(file)));

            Toast.makeText(this, "�ѱ���Ϊ�ļ�" + file.getAbsolutePath(),
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
            		Uri.fromFile(srcfile))); //ɾ����Դ�ļ�Ҳ��ɨ���������cameraĿ¼���и���Ч�ļ�
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
            		Uri.fromFile(destfile)));
            
			Toast.makeText(this, "�ѱ���Ϊ�ļ�" + destfile.getAbsolutePath(),
            		Toast.LENGTH_SHORT).show();
		}
	}
	
	/** ����BitmapͼƬ **/
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// ���þ���������Ų�������ڴ����
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
}
