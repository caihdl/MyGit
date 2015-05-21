package com.gallery.photoview.mfragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gallery.photoview.R;
import com.gallery.photoview.ShowImageActivity;
import com.gallery.photoview.R.drawable;
import com.gallery.photoview.R.id;
import com.gallery.photoview.R.layout;
import com.gallery.photoview.data.ImageBean;
import com.gallery.photoview.madapter.GroupAdapter;
import com.gallery.photoview.madapter.VideoAdapter;
import com.gallery.photoview.method.inflater;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class Fragment_Byfile extends Fragment{

	private View mview;
	private char media;
	private GroupAdapter adapter;
	private static GridView mGroupGridView;
	private ArrayList<String> photolist;
	private static Point mPoint=new Point(180,180);
	private static Bitmap bmp;
	private Context mcontext;
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private List<ImageBean> list = new ArrayList<ImageBean>();

	public Fragment_Byfile() {
		super();
	}

	 public Fragment_Byfile(ArrayList<String> a,Context context,char x)
	 {
		super();
	    photolist = a;
	    mcontext=context;
	    media=x;
	 }
	 
	@Override  
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  
	 {  	    
		      mview =inflater.inflate(R.layout.file, container, false);  
	          mGroupGridView = (GridView) mview.findViewById(R.id.main_grid);
		       //显示图片文件
				if(media == 'i') {
					getImages();
					adapter = new GroupAdapter(mcontext,
							list = subGroupOfImage(mGruopMap), mGroupGridView);
					mGroupGridView.setAdapter(adapter);
				    mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

				    	@Override
					    public void onItemClick(AdapterView<?> parent, View view,
					    		int position, long id) {
				    		List<String> childList = mGruopMap.get(list.get(position).getFolderName());
						
						    Intent mIntent = new Intent(mcontext, ShowImageActivity.class);
						    mIntent.putStringArrayListExtra("data", (ArrayList<String>)childList);
						    mIntent.putExtra("media", 'i');
						    
						    startActivity(mIntent);
						    }
				    	});
		
				}
				else 
				{
					getImages();
					//设置ListView的Adapter，使用自定义的Adatper  
			        VideoAdapter adapter = new VideoAdapter(mcontext, 
			        		     list = subGroupOfImage(mGruopMap),mGroupGridView);  
			        mGroupGridView.setAdapter(adapter);
			        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
				    	@Override
					    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				    		List<String> childList = mGruopMap.get(list.get(position).getFolderName());
						    Intent mIntent = new Intent(mcontext, ShowImageActivity.class);
						    mIntent.putStringArrayListExtra("data", (ArrayList<String>)childList);
						    mIntent.putExtra("media", 'v');
						    
						    startActivity(mIntent);
						    }
				    	});
				}
				return mview;
	 }  
	
	
	private void getImages() {
		
				for (String path:photolist)
				{
					String parentName = new File(path).getParentFile().getName();
					//根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(path);
					}
				}
	}
	
	private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap){
		if(mGruopMap.size() == 0){
			return null;
		}
		List<ImageBean> list = new ArrayList<ImageBean>();
		
		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			
			//int topIndex = value.size() > 0 ? value.size() - 1 : 0;
			int topIndex = 0;
			mImageBean.setTopImagePath(value.get(topIndex)); //获取该组的第一张图片
			
			list.add(mImageBean);
		}
		
		return list;
	}
	
	
    
}