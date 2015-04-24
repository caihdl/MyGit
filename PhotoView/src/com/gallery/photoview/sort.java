package com.gallery.photoview;

import java.net.URI;
import java.util.ArrayList;

public class sort {
    private static ArrayList<Object> Childlist;
	public static ArrayList<Object> sortphoto(ArrayList<URI> list,int sortType) {
		
		switch (sortType)
		{
		case 0:
		case 1:
		case 2:
		default:Childlist=getdefaultlist(list);
			
		}
		
		return Childlist;
	}
    
	
	public static ArrayList<Object> getdefaultlist(ArrayList<URI> list){
		int index=0;
		ListChildUri p=new ListChildUri();
		ArrayList<Object> tmp=new ArrayList<Object>();
		for (URI i:list)
		{
			p.uri[index]=i;
			index++;
			if (index==4) {
				index=0; 
				tmp.add(p);
			}
		}
		if (index>0) tmp.add(p);
		return tmp;
	}
	
}