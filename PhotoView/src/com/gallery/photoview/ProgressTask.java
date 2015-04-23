package com.gallery.photoview;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import android.os.AsyncTask;

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
    	for (File ff:flist)
    	{
    		filename=ff.getName();
    		if (ff.listFiles()==null) {
    			  if (check==0)
    			  {
    				  if (isvedio(filename))			 
   		              {
   		                  qlist.add(ff.toURI());
   		              }
    			  }
    			  else 
    			      if (isimg(filename))			 
    		           {
    		              qlist.add(ff.toURI());
    		           }
    		}
    		else 
    		{
    			qlist.addAll(find(ff,check));
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
    	    if ((y=="jpg") || (y=="bmp") || (y=="jpeg") || (y=="png"))
    	    {
    	       res=true;
    	    }
    	}
    	return res;
    }
    
    protected boolean isvedio(String s){
    	int x=s.lastIndexOf('.');
    	boolean res=false;;
    	String y;
    	if (x!=-1)
    	{
    		y=s.substring(x+1);
    	    if ((y=="mp4") || (y=="wmv") || (y=="mkv") || (y=="rmvb") || (y=="avi") || (y=="mpg"))
    	    {
    	       res=true;
    	    }
    	}
    	return res;
    }
}
