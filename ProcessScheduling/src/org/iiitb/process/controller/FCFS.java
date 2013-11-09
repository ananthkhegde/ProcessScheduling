package org.iiitb.process.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.iiitb.model.bean.ProcessBean;
import org.iiitb.process.model.OutputProcessBean;
import org.iiitb.process.model.ProcessOutputParamaters;

/*
 * Class which implements FCFS algorithms
 */
public class FCFS implements IScheduler {
	/*
	 * Function schedules FCFS algorithm
	 */
	public ProcessOutputParamaters Schedule(List<ProcessBean> processList)
	{
		
		
		Collections.sort(processList, new ArrivalTimeComparator());
		// trying populate common utilities Data structure
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		List<ProcessBean> blockedlist = new ArrayList<ProcessBean>();
		ProcessBean current;
		int count = 0;
		for(count = 0;count<processList.size();count++)
		{
			readylist.add(processList.get(count));
		}
		
		int time = 3;
		while(readylist.size()>0)
		{
			current=new ProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());
			readylist.remove(0);
			SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, time);	
		}
		ProcessOutputParamaters processoutputparameters;
		processoutputparameters = CalculateParameters(processList);
		
		return processoutputparameters;
	}
	/*
	 * Methods calculates all the output parameters
	 */
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters CalculateParameters(List<ProcessBean> processlist)
	{
		int count;
		long waitingtime,totalwaitingtime = 0,bursttime,totalbursttime,turnaroundtime,totalturnaroundtime = 0;
		ProcessOutputParamaters processoutputparameters;
		List<OutputProcessBean>ProcessoutputList;
		processoutputparameters = new ProcessOutputParamaters();
		if (!processlist.isEmpty())
		{
			OutputProcessBean processoutput;
			ProcessoutputList = new ArrayList<OutputProcessBean>();
			processoutput = new OutputProcessBean(processlist.get(0).getPid(),processlist.get(0).getpName());
			processoutput.setWaitingTime(0);
			totalbursttime = 0;//processlist.get(0).getBurstList().get(0).getQuantum();
			processoutput.setArrivalTime(processlist.get(0).getArrivalTime());
			processoutput.setBurstList(processlist.get(0).getBurstList());
			ProcessoutputList.add(processoutput);
			//http://www.cwithabhas.com/2012/03/fcfc-first-come-first-serve-with.html
			for(count = 1;count<processlist.size();count++)
			{
				processoutput = new OutputProcessBean(processlist.get(count).getPid(),processlist.get(count).getpName());
				bursttime = processlist.get(count-1).getBurstList().get(0).getQuantum();
				totalbursttime += bursttime;
				waitingtime = totalbursttime - processlist.get(count).getArrivalTime().getMinutes() ;
				if(waitingtime<0)
				{
					waitingtime = 0;
				}
				processoutput.setWaitingTime(waitingtime);
				totalwaitingtime+=waitingtime; 
				processoutput.setWaitingTime(waitingtime);
				turnaroundtime = waitingtime + bursttime;
				processoutput.setTurnaroundTime(turnaroundtime);
				totalturnaroundtime += turnaroundtime;
				processoutput.setArrivalTime(processlist.get(count).getArrivalTime());
				processoutput.setBurstList(processlist.get(count).getBurstList());
				ProcessoutputList.add(processoutput);
			} 
			
			processoutputparameters.setProcessoutputList(ProcessoutputList);
			processoutputparameters.setAveragewaitingTime(totalwaitingtime/count);
			processoutputparameters.setNetTurnaroundTime(totalturnaroundtime/count);
		}
		return processoutputparameters;
	}
	
	
}
