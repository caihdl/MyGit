package com.gallery.photoview;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gallery.photoview.photolib.PhotoView;

public class ViewPage extends Activity{

	private ViewPager mViewPager;
    private List<String> imagesPath;
    private int pos1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagesPath = getIntent().getStringArrayListExtra("imgPath");
        pos1=getIntent().getIntExtra("pos", 0);
        
        //if(mViewPager!= null)
        //{
        	//设置layout
        	mViewPager = new ViewPager(this);
            mViewPager.setId(R.id.view_pager);
            setContentView(mViewPager);

            //设置adaptor
            mViewPager.setAdapter(new SamplePagerAdapter());
            mViewPager.setCurrentItem(pos1);
        //}
        
        //回调设置
        if (savedInstanceState != null) {
        }
    }

     class SamplePagerAdapter extends PagerAdapter {
   
    	 //获得图片数量
    	 @Override
         public int getCount() {
         	return imagesPath.size();
         }

    	 //实例化
        @Override
        public View instantiateItem(ViewGroup container, int position) {
        	
            PhotoView photoView = new PhotoView(container.getContext());
            //int key=pos1+position;
            
            Uri uri=Uri.parse(imagesPath.get(position));
            photoView.setImageURI(uri);

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        //移除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        //判断object与当前视图是否一致
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    //回调
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
