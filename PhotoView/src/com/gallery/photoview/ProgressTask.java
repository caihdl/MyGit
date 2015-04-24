package com.gallery.photoview;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

public class ProgressTask extends AsyncTask<Integer, Void, ArrayList<URI>>{
    private ArrayList<URI> plist;
	
	public ProgressTask() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	protected ArrayList<URI> doInBackground(Integer... type) {
		switch (type[0])
		{
		   
		   case 0:
		   {
			   Log.v("0","00000000");
			   File files=new File("/storage/emulated/0/DCIM/Camera");
			   plist = find(files,1);
		   }
		   case 1:
		   {
			   File files=new File("/storage/emulated/0/DCIM/Camera");
			   plist = find(files,0);
		   }
		   default:
		   {
			   
		   }
		}
		return plist;
    }

    protected void onProgressUpdate() {
        
    }

    protected void onPostExecute(ArrayList<URI> result) {
        
    }

    
    protected ArrayList<URI> find(File f,int check){
    	ArrayList<URI> qlist= new ArrayList<URI>();
    	String filename;
    	File[] flist = f.listFiles();
    	if (flist!=null){
    		
    	    for (File ff:flist)
    	    {
    		    filename=ff.getName();
    		    if (!ff.isDirectory()) {
    			      if (check==0)
    			      {
    				      if (isimg(filename))			 
   		                  {
   		                      qlist.add(ff.toURI());
   		                  }
    			      }
    			      else if (isvideo(filename))			 
    		           {
    		              qlist.add(ff.toURI());
    		           }
    		    }
    		    else 
    		    {
    			    qlist.addAll(find(ff,check));
    		    }
    	    }
    	}
    	return qlist;
    }
    
    protected boolean isimg(String s){
    	int x=s.lastIndexOf('.');
    	boolean res=false;;
    	String y;
    	if (x!=-1)
    	{
    		y=s.substring(x+1);
    	    if (y.equals("jpg") || y.equals("bmp") || y.equals("jpeg") || y.equals("png"))
    	    {
    	       res=true;
    	    }
    	}
    	return res;
    }
    
    protected boolean isvideo(String s){
    	int x=s.lastIndexOf('.');
    	boolean res=false;;
    	String y;
    	if (x!=-1)
    	{
    		y=s.substring(x+1);
    	    if (y.equals("mp4") || y.equals("wmv") || y.equals("mkv") || y.equals("rmvb") || y.equals("avi") || y.equals("mpg"))
    	    {
    	       res=true;
    	    }
    	}
    	return res;
    }
}
