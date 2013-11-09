package org.iiitb.process.controller;
import java.util.Comparator;
import org.iiitb.model.bean.ProcessBean;

public class BurstTimeComparator implements Comparator<ProcessBean>  {
	public int compare(ProcessBean process1, ProcessBean process2) {
		
        return (int) ( process1.getBurstList().get(0).getQuantum() -  process2.getBurstList().get(0).getQuantum());
    }

}
