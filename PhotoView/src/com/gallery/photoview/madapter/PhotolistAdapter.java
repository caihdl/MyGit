package com.gallery.photoview.madapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gallery.photoview.MainActivity;
import com.gallery.photoview.MyApp;
import com.gallery.photoview.R;
import com.gallery.photoview.data.ListChild.ListChildTitle;
import com.gallery.photoview.data.ListChild.ListChildUri;
import com.gallery.photoview.data.ListChild.ViewHolderImg;
import com.gallery.photoview.data.ListChild.ViewHolderText;
import com.gallery.photoview.data.Myintent;
import com.gallery.photoview.data.reflasher;
import com.gallery.photoview.method.inflater;
import com.gallery.photoview.method.sort;
import com.gallery.photoview.method.inflater.Notice;

public class PhotolistAdapter extends BaseAdapter{
	private LayoutInflater mInflater; 
	private Context mcontext;
	private ArrayList<Object> photolist;
	private ListView listview;
	private Bitmap bmp;
	private Point mPoint;
	private ArrayList<String> mlist;
	private Myintent mintent;
	private MyApp appState;
	private Notice notice;
	private int type;
	private ViewHolderImg ep=new ViewHolderImg();
	private reflasher mreflash;
	
	public PhotolistAdapter(int type,Context context, ArrayList<Object> photolist, ArrayList<String> mlist,ListView listview,Myintent mintent,reflasher mreflash)
	{
		mcontext=context;
		this.type=type;
		this.mreflash=mreflash;
		mInflater=LayoutInflater.from(context);
		this.mlist=mlist;
		this.photolist=photolist;
		this.listview=listview;
		this.mintent=mintent;
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
	    
	    appState = MyApp.getinstance(); 
	    notice=new Notice(){

			@Override
			public void notce() {
				if (appState.getState()) MainActivity.onlongclick();
				else MainActivity.resetlongclick();
				notifyDataSetChanged();
			}
	      
	    };
	    
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

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
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
				Object o=convertView.getTag();
				if (o.getClass()==ep.getClass())
				{
				 holderImg=(ViewHolderImg) o;
				}
				else
				{
                	 holderImg = new ViewHolderImg();
                	 convertView=inflater.inflate(mInflater,convertView,holderImg);
				}
			}
            ListChildUri p=(ListChildUri) current;
            String path;
    		for (int i=0;i<=3;i++) {
    		     if (p.uri[i]!=null)
    		        {
    			    	  path=p.uri[i];
    			    	  inflater.setcheck(mlist.indexOf(p.uri[i]), mintent, listview, holderImg.img[i], holderImg.text[i],holderImg.box[i], notice,appState);
    			    	  inflater.loadBit(mcontext,holderImg.img[i],bmp,path,mPoint,type,listview);
    			    	  holderImg.img[i].setVisibility(View.VISIBLE);
    		        }
    		    	else 
    		    	{
    		    		     holderImg.img[i].setVisibility(View.GONE);
    		    		     holderImg.img[i].setClickable(false);
    		    		     holderImg.text[i].setVisibility(View.GONE);
    		    		     holderImg.box[i].setVisibility(View.GONE);
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
	
	public void delete()
	{
        final List<Integer> selectPos = MyApp.getinstance().getSelectItems();
		
		if(!selectPos.isEmpty()) {
			AlertDialog.Builder ad = new AlertDialog.Builder (mInflater.getContext());
	        ad.setTitle("删除");
		    ad.setMessage("确认删除?");
		        
		    ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
		    	@Override
				public void onClick(DialogInterface dialog, int i) {
		    		doDelete(selectPos);
				}
		    });
		        
		    ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int i) {
					//selectPos.removeAll(deleteFile);
				}
			});
				
			ad.show();
	    }
	}
	
	public void doDelete(List<Integer> selectPos) {
	
		while(!selectPos.isEmpty()) {
			File f= new File(mlist.get(selectPos.get(0)));
		    if(f.exists()) {
		    	f.delete();
		    	mlist.set(selectPos.get(0), null);
			   
					
					/*MediaScannerConnection.scanFile(mInflater.getContext(),
							new String[] { f.getAbsolutePath() }, null,
							new OnScanCompletedListener() {
		                @Override
		                public void onScanCompleted(String path, Uri uri) {
		                }
		            });*/
		            
				mInflater.getContext().sendBroadcast(new Intent
						(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
			}
		    
		    selectPos.remove(0);
	    }
		 for (int i=mlist.size()-1; i>=0; i--){  
	            if (mlist.get(i)==null) mlist.remove(i);
	        }  
		 Toast.makeText(mInflater.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
		 photolist = sort.sortphoto(mlist,3);
		 mreflash.reflash();
		 
	}
	public boolean delFile(File file) {
		if(file.isDirectory())
			return false;
		
		return file.delete();
	}
}
