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
		int processCount,count = 0,pId,arrivalTime,burstTime,SheduleTyp,priority;
		String pName;
		ProcessOutputParamaters processoutputparameters;
		List<ProcessBean> processList=new ArrayList<ProcessBean>();
		List<TimeQuantum> burstList;
		TimeQuantum timequant;
		
		
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Please Choose the algorithm from the below choice");
        System.out.println("1-FCFS\n2-SJF\n3-SRT\n4-Preemptive Priority\n5-NonPreemptivePriority\n6-RR\n7-HRRN");
        SheduleTyp = inputReader.nextInt();
        ScheduleType st = GetScheduler(SheduleTyp);
        //Getting input in integer format
        System.out.println("Please enter number of process");
        processCount = inputReader.nextInt();
        int processid = 0;
        for(count = 0;count<processCount;++count)
        {
        	pName = "p" + (count+1);
        	processid++;
        	pId = processid;//inputReader.nextInt();
        	ProcessBean processbean  = new ProcessBean(pId,pName);
        	System.out.println("Please arrival time for the process p" + (count+1));
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
        	if((st == ScheduleType.PREMPTIVEPRIORITY) || (st == ScheduleType.NONPREMPTIVEPRIORITY))
        	{
        		System.out.println("Please Priority for the process p" + (count+1));
        		priority = inputReader.nextInt();
        		processbean.setPriority(priority);
        	}
        	processList.add(processbean);
        	
        }
      if(processList.size()>0)
      {
        ProcessesScheduler ps = new ProcessesScheduler();
        processoutputparameters= ps.Schedule(processList,st);
        TableView tv = new TableView();
       tv.start(processoutputparameters);
       if(!(processoutputparameters.getProcessinterval()==null))
       {
       GanttChartView gv = new GanttChartView();
       gv.start(processoutputparameters);
       }
      }
       
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
