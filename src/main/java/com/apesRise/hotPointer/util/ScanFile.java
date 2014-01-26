package com.apesRise.hotPointer.util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class ScanFile {
	
	public ArrayList<String> seanFile(String dirName){
        
		ArrayList<String> result = new ArrayList<String>();
		
        File dir = new File(dirName);
        
        if(!dir.exists() || !dir.isDirectory()) return result;
        
        LinkedList<File> list = new LinkedList<File>();
        
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else
            	result.add(file[i].getAbsolutePath());
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);
                    else
                    	result.add(file[i].getAbsolutePath());
                }
            } else {
            	result.add(tmp.getAbsolutePath());
            }
        }

        return result;
        
	}
	
}
