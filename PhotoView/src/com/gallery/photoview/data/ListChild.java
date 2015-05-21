package com.gallery.photoview.data;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListChild {

	public static class ListChildTitle {

	    public boolean isUri=false;
		public String title;
	}

	public static class ListChildUri extends ListChildTitle{
				public String uri[];
				public ListChildUri(){
				    isUri=true;
				    uri=new String[4];
				}
				
				public ListChildUri(int x){
				    isUri=true;
				    uri=new String[x];
				}
	}
	
	   public static class ViewHolderText{
		    public TextView title=null;
		    public LinearLayout lin;
	     }
	 
	     public static class ViewHolderImg{
		    public ImageView img[]={null,null,null,null};
		    public TextView text[]={null,null,null,null};
		    public CheckBox box[]={null,null,null,null};
		    public LinearLayout lin;
	     }
	
	   
}
