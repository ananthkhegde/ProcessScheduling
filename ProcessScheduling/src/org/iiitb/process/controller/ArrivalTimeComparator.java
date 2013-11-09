package org.iiitb.process.controller;

import java.util.Comparator;

import org.iiitb.model.bean.ProcessBean;

/*
 * Utility class to compare arrival time between the processes
 */

public class ArrivalTimeComparator implements Comparator<ProcessBean> 
{
    @SuppressWarnings("deprecation")
	
public int compare(ProcessBean process1, ProcessBean process2) 
{
        
return process1.getArrivalTime().getMinutes() - process2.getArrivalTime().getMinutes();
   
 }

}
