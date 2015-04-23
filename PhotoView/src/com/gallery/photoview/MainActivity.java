package com.gallery.photoview;


import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.support.v4.app.Fragment;    
import android.support.v4.app.FragmentActivity; 
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends  FragmentActivity   {
	
    private ArrayList<URI> List;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mViews; 
    private LayoutInflater mInflater;
    private TextView mphotoview;  
    private TextView mvideoview;  
    private FragAdapter mAdapter;
    private PhotoFragment photofragment;
    private VideoFragment videofragment;
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startSearch();
		initview();
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOnPageChangeListener(new pageChange());
		mViewPager.setAdapter(mAdapter);  
	}
	
	protected void startSearch(){
	    try {
		    List = new ProgressTask().execute(0).get();
	    } catch (InterruptedException e) {
		    // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (ExecutionException e) {
	     	// TODO Auto-generated catch block
		   e.printStackTrace();
	    }
	}
	
	protected void initview(){
		toolbar = (Toolbar) findViewById(R.id.Mytoolbar);
		mInflater = LayoutInflater.from(this);
		mphotoview = (TextView) findViewById(R.id.button_photo);
		mphotoview.setOnClickListener(new click());
		mvideoview = (TextView) findViewById(R.id.button_video);
		mViews = new ArrayList<Fragment>();
		photofragment= new PhotoFragment(List);
		mViews.add(photofragment);
		videofragment= new VideoFragment();
		mViews.add(videofragment);
		mAdapter = new FragAdapter(getSupportFragmentManager(),mViews);
	
		
	}
	
	public class FragAdapter extends FragmentPagerAdapter {  
		  
	    private ArrayList<Fragment> mFragments;  
	    
	    public FragAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {  
	        super(fm);  
	        // TODO Auto-generated constructor stub  
	        mFragments=fragments;  
	    }  
	    
	    @Override  
	    public Fragment getItem(int arg0) {  
	        // TODO Auto-generated method stub  
	        return mFragments.get(arg0);  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        // TODO Auto-generated method stub  
	        return mFragments.size();  
	    }  
	}  
	
	public class click implements OnClickListener{
		
		 @Override  
		 public void onClick(View v){
			 switch (v.getId())
			 {
			     case R.id.button_photo :
			    	 mViewPager.setCurrentItem(0);			   
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
            	 mphotoview.setTextColor(Color.argb(200,0,0,0));
            	 mvideoview.setTextColor(Color.argb(100,0,0,0));
                 break;  
             case 1:  
            	 mphotoview.setTextColor(Color.argb(100,0,0,0));
            	 mvideoview.setTextColor(Color.argb(200,0,0,0));
                 break;  
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
	
}
