package org.iiitb.process.view;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.iiitb.model.bean.ProcessBean;
import org.iiitb.model.bean.TimeQuantum;
import java.util.Date;
import org.iiitb.model.consts.BurstType;
import org.iiitb.process.model.ProcessOutputParamaters;
import org.iiitb.process.controller.ProcessesScheduler;
import org.iiitb.process.model.ScheduleType;

public class ProcessView {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		//user input in command prompt
		int processCount,count = 0,pId,arrivalTime,burstTime,SheduleTyp;
		String pName;
		ProcessOutputParamaters processoutputparameters;
		List<ProcessBean> processList=new ArrayList<ProcessBean>();
		List<TimeQuantum> burstList;
		TimeQuantum timequant;
		System.out.println("Please enter number of process");
        Scanner inputReader = new Scanner(System.in);
     
        //Getting input in integer format
        processCount = inputReader.nextInt();
        
        for(count = 0;count<processCount;++count)
        {
	
        	System.out.println("Please enter process name");
        	pName = inputReader.next();
        	System.out.println("Please enter process id");
        	pId = inputReader.nextInt();
        	ProcessBean processbean  = new ProcessBean(pId,pName);
        	System.out.println("Please arrival time for the process");
        	arrivalTime = inputReader.nextInt();
        	@SuppressWarnings("deprecation")
			Date date = new Date(2013,10,0,0,arrivalTime);
        	processbean.setArrivalTime(date);
        	System.out.println("Please burst time for the process");
        	timequant = new TimeQuantum();
        	timequant.setType(BurstType.CPU);
        	burstTime = inputReader.nextInt();
        	timequant.setQuantum(burstTime);
        	burstList = new ArrayList<TimeQuantum>();
        	burstList.add(timequant);
        	processbean.setBurstList(burstList);
        	processList.add(processbean);
        	
        }
        System.out.println("Please Choose the algorithm from the below choice");
        System.out.println("1-FCFS\n2-SJF\n3-SRT\n4-Preemptive Priority\n5-NonPreemptivePriority\n6-RR\n7-HRRN");
        SheduleTyp = inputReader.nextInt();
        ProcessesScheduler ps = new ProcessesScheduler();
       ScheduleType st = GetScheduler(SheduleTyp);
        processoutputparameters= ps.Schedule(processList,st);
        TableView tv = new TableView();
       tv.start(processoutputparameters);
       
	}
	
	
	
	static ScheduleType GetScheduler(int scheduletype)
	{
		ScheduleType scheduletyp;
	
		switch (scheduletype) {
        case 1: scheduletyp = ScheduleType.FCFS;
                 break;
        case 2:  scheduletyp = ScheduleType.SJF;
        break;
        case 3:   scheduletyp = ScheduleType.SRT;
        break;
        case 4:   scheduletyp = ScheduleType.PREMPTIVEPRIORITY;
        break;
        case 5:   scheduletyp = ScheduleType.NONPREMPTIVEPRIORITY;
        break;
        case 6:   scheduletyp = ScheduleType.RR;
        break;
        case 7:   scheduletyp = ScheduleType.HRRN;
        break;
        default: 
        	throw new IllegalArgumentException("Invalid Scheduler name: " + scheduletype);   		
	}
		return scheduletyp; 
}
}
