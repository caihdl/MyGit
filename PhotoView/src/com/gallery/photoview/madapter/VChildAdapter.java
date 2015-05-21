package com.gallery.photoview.madapter;

import java.io.File;
import java.util.List;

import com.gallery.photoview.MyApp;
import com.gallery.photoview.R;
import com.gallery.photoview.ShowImageActivity;
import com.gallery.photoview.data.MyImageView;
import com.gallery.photoview.data.Myintent;
import com.gallery.photoview.data.MyImageView.OnMeasureListener;
import com.gallery.photoview.data.reflasher;
import com.gallery.photoview.method.inflater;
import com.gallery.photoview.method.inflater.Notice;

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
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VChildAdapter extends BaseAdapter{  
	private GridView cGridView;
	protected LayoutInflater cInflater;  
    private List<String> videoPath;
	private Bitmap bmp;  
	private Point mPoint=new Point(240,240);
	private Myintent mintent;
    private Notice notice;
    private MyApp appState;
	private Context mcontext;
	private reflasher mreflash;
    
	
    public VChildAdapter(Context context, List<String> videoPath, GridView cGridView,Myintent mintent,reflasher mreflash) {  
    	cInflater = LayoutInflater.from(context); 
    	this.videoPath = videoPath;
		this.cGridView = cGridView; 
		this.mintent=mintent;
		this.mreflash=mreflash;
		this.mcontext=context;
		WindowManager wm = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE); 
		DisplayMetrics  dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);     
		float scale = mcontext.getResources().getDisplayMetrics().density;
	    int ImgWidth = dm.widthPixels/4-(int)(5 * scale + 0.5f);
	    mPoint=new Point(ImgWidth,ImgWidth);
		Resources res = context.getResources();  
	    bmp= BitmapFactory.decodeResource(res, R.drawable.friends_sends_pictures_no);  
	    bmp = ThumbnailUtils.extractThumbnail(bmp, ImgWidth, ImgWidth,
		    	 ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	   
	    appState = MyApp.getinstance(); 
	    notice=new Notice(){

			@Override
			public void notce() {
				
				notifyDataSetChanged();
			}
	      
	    };
	    
	   /* ShowImageActivity.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MyApp.getinstance().getState())
					Toast.makeText(mcontext, "分享", Toast.LENGTH_LONG).show();
			}
			
		});
		
		
	    ShowImageActivity.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MyApp.getinstance().getState()) {
				    deleteOperation();
				    
				}
			}
		});
		*/
    }  
    
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return videoPath.size();  
    }  
    @Override  
    public Object getItem(int p) {  
        // TODO Auto-generated method stub  
        return videoPath.get(p);  
    }  
    @Override  
    public long getItemId(int p) {  
        // TODO Auto-generated method stub  
        return p;  
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        ViewHolder holder = null;
		String path = videoPath.get(position).toString();
		String name = new File(path).getName();
		
        if(convertView == null){  
            holder = new ViewHolder();  
            convertView = cInflater.inflate(R.layout.videoinfile, null);  
            holder.thumbImage = (MyImageView)convertView.findViewById(R.id.video_image);
            holder.videoName = (TextView)convertView.findViewById(R.id.video_name);
        	holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			holder.mTextView = (TextView) convertView.findViewById(R.id.grey);
			
			//用来监听ImageView的宽和高
			holder.thumbImage.setOnMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
            convertView.setTag(holder);  
        }else{  
            holder = (ViewHolder)convertView.getTag();
      
        }  
        
        holder.videoName.setText(name);
        inflater.setcheck(position, mintent, cGridView, (ImageView)holder.thumbImage, holder.mTextView, holder.mCheckBox, notice, appState);
		inflater.loadBit(mcontext,holder.thumbImage, bmp, path, mPoint, 1, cGridView);
   
        return convertView;  
    }  
    
    
    public static class  ViewHolder{  
        public MyImageView thumbImage;
        public TextView videoName;
        public CheckBox mCheckBox;
		public TextView mTextView;   
    }  
    
    public void deleteOperation() {
		//deleteFile.clear();
		final List<Integer> selectPos =  MyApp.getinstance().getSelectItems();
		
		if(!selectPos.isEmpty()) {
			AlertDialog.Builder ad = new AlertDialog.Builder (mcontext);
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
		else
			Toast.makeText(mcontext, "未选择任意一项", Toast.LENGTH_SHORT).show();
	}
	
	public void doDelete(List<Integer> selectPos) {
		
		while(!selectPos.isEmpty()) {
			File f= new File(videoPath.get(selectPos.get(0)));
		    if(f.exists()) {
		    	f.delete();
		    	MyApp.getinstance().addlist(videoPath.get(selectPos.get(0)));
		    	videoPath.set(selectPos.get(0), null);
			    //notifyDataSetChanged();
					
					/*MediaScannerConnection.scanFile(mInflater.getContext(),
							new String[] { f.getAbsolutePath() }, null,
							new OnScanCompletedListener() {
		                @Override
		                public void onScanCompleted(String path, Uri uri) {
		                }
		            });*/
		            
				mcontext.sendBroadcast(new Intent
						(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
			}
		    
		    selectPos.remove(0);
	    }
		 for (int i=videoPath.size()-1; i>=0; i--){  
	            if (videoPath.get(i)==null) videoPath.remove(i);
	        }  
		Toast.makeText(mcontext, "删除成功", Toast.LENGTH_SHORT).show();
		mreflash.reflash();
	}
	
}