package org.iiitb.process.controller;
import java.util.Comparator;
import org.iiitb.model.bean.ProcessBean;


public class prioritycomparator implements Comparator<ProcessBean>  {
public int compare(ProcessBean process1, ProcessBean process2) {
		
        return (int) ( process1.getPriority() -  process2.getPriority());
    }	 
}
