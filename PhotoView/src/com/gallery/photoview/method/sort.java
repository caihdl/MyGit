package com.gallery.photoview.method;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.gallery.photoview.data.ListChild.ListChildTitle;
import com.gallery.photoview.data.ListChild.ListChildUri;

public class sort {
    private static ArrayList<Object> Childlist;
	public static ArrayList<Object> sortphoto(ArrayList<String> list,int sortType) {
		
		switch (sortType)
		{
		case 0:
		case 1:
		case 2:
		case 3:Childlist=getdaylist(list);
			
		}
		
		return Childlist;
	}
	
	public static ArrayList<Object> getdaylist(ArrayList<String> list){
		int index=0;
		ListChildUri p;
		ListChildTitle q;
		ArrayList<Object> tmp=new ArrayList<Object>();
		Calendar cal;
		Calendar c=null;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		p=new ListChildUri();
		for (String i:list)
		{
			File f=new File(i);
			cal=Calendar.getInstance (Locale.CHINA);
			cal.setTimeInMillis(f.lastModified());
			if (c==null)
			{
				c=Calendar.getInstance (Locale.CHINA);
				c.setTimeInMillis(f.lastModified());
				q=new ListChildTitle();
				q.title=bartDateFormat.format(c.getTime());
				tmp.add(q);
			}
			if (equal(cal,c,3))
			{
				p.uri[index]=i;
				index++;
				if (index==4) {
					index=0; 
					tmp.add(p);
					p=new ListChildUri();
				}	
				c=cal;
			}
			else 
			{
				if (index>0) 
					{
					   index=0;
					   tmp.add(p);
					}
				c=cal;
				c.setTimeInMillis(f.lastModified());
				q=new ListChildTitle();
				q.title=bartDateFormat.format(c.getTime());
				tmp.add(q);
				p=new ListChildUri();
				index=1;
				p.uri[0]=i;
			}
		}
		if (index>0) tmp.add(p);
		return tmp;
	}
	
	static boolean equal(Calendar x,Calendar y,int num)
	{
		boolean check=true;
		if (x.get(Calendar.YEAR)!=y.get(Calendar.YEAR)) check=false;
		if (num==1) return check;
		if (x.get(Calendar.MONTH)!=y.get(Calendar.MONTH)) check=false;
		if (num==2) return check;
		if (x.get(Calendar.DATE)!=y.get(Calendar.DATE)) check=false;
		if (num==3) return check;
		return true;
	}
	
	public static  ArrayList<String> qsort(ArrayList<String> list)
	{
		 ArrayList<String> mylist=new ArrayList<String>();
		 long [] time=new long[list.size()];
		 int [] order=new int[list.size()];
		 int now=0;
		 for (String s:list)
		 {
			 File f=new File(s);
			 time[now]=f.lastModified();
			 order[now]=now;
			 now++;
		 }
		 for (int i=0;i<now-1;i++)
		 {
		    for (int j=0;j<now-1-i;j++)
		    if (time[j]<time[j+1])
		    {
		    	long l=time[j];
		    	time[j]=time[j+1];
		    	time[j+1]=l;
		    	int q=order[j];
		    	order[j]=order[j+1];
		    	order[j+1]=q;
		    }
		 }
		 for (int i=0;i<now;i++)
		 {
			 mylist.add(list.get(order[i]));
		 }
	     return mylist;
	}
};