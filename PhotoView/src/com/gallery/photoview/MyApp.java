package com.gallery.photoview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Application;

public class MyApp extends Application{
	private boolean State;
	public HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private int count;
	private static MyApp instance;
	private ArrayList<String> needtodel;
    public static MyApp getinstance()
    {
    	return instance;
    }
	
    public boolean getState()
    {
    	return State;
    }
    
    public void setState(boolean b)
    {
    	State=b;
    }
    
    public HashMap<Integer, Boolean> getmap()
    {
    	return mSelectMap;
    }
    
    public void newhash()
    {
         mSelectMap.clear();
         count=0;
    }
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        count=0;
        instance = this;
        needtodel=new ArrayList<String>();
    }
    
    public void addcount()
    {
    	count++;
    }
    
    public void subcount()
    {
    	count--;
    }
    
    public void addlist(String path)
    {
        needtodel.add(path); 
    }
    
    public String poplist()
    {
    	if (!needtodel.isEmpty())
    	{
    	String s=needtodel.get(0);
    	needtodel.remove(0);
    	return s;
    	}
    	else return null;
    }
    
    public int getcount()
    {
    	return count;
    }
    
    public List<Integer> getSelectItems() {
 	   List<Integer> list = new ArrayList<Integer>();
 	   for(Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
 			Map.Entry<Integer, Boolean> entry = it.next();
 			if(entry.getValue()){
 				list.add(entry.getKey());
 			}
 		}
 	   return list;
 	}
    
    public boolean check(int k)
    {
    	if (mSelectMap.containsKey(k)) return true;
    	else return false;
    
    }
}
