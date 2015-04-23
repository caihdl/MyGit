package com.gallery.photoview;

import java.net.URI;
import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PhotoFragment extends Fragment{
      
	 private ArrayList<URI> photolist;
	 
	 public PhotoFragment(ArrayList<URI> a)
	 {
		super();
	    photolist = a;
	 }
	 
	 @Override  
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  
	 {  
		    
	        return  inflater.inflate(R.layout.photolayout, container, false);  
	        
	 }  

}
