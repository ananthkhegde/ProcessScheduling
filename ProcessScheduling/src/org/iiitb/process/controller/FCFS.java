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
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters Schedule(List<ProcessBean> processList)
	{
		Collections.sort(processList, new ArrivalTimeComparator());
		// trying populate common utilities Data structure
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		List<ProcessBean> blockedlist = new ArrayList<ProcessBean>();
		ProcessBean emptyprocess=new ProcessBean(-1," ");
		ProcessBean current;
		readylist.add(processList.get(0));
		int time = 0,timeslice,processcnt = 0;
		timeslice = processList.get(0).getArrivalTime().getMinutes();
		boolean isprocessearly = true;
		while(readylist.size()>0)
		{
			
			current=new ProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());
			current = readylist.get(0);
			readylist.remove(0);
			for(time = 0;time<current.getBurstList().get(0).getQuantum();time++)
			{
				if(isprocessearly == true)
				{
				List<ProcessBean> arrivedprocesslist = checkforprocess(timeslice,processList);
				for(int cnt = 0;cnt < arrivedprocesslist.size();cnt++)
				{
					readylist.add(arrivedprocesslist.get(cnt));
				
				}
				}
				SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, timeslice);
				timeslice++;
				isprocessearly = true;
				
			}
			
			if(( processcnt+1 < processList.size()) && (processList.get(processcnt+1).getArrivalTime().getMinutes() - processList.get(processcnt).getArrivalTime().getMinutes()
					> processList.get(processcnt).getBurstList().get(0).getQuantum()))
			{
				
				for(int cnt =timeslice;cnt<processList.get(processcnt+1).getArrivalTime().getMinutes();cnt++ )
				{
					
					SnapShotUtility.ViewSnapHot(readylist, emptyprocess, blockedlist, timeslice);
					timeslice++;
				}
				readylist.add(processList.get(processcnt+1));
				isprocessearly = false;
			}
			else
			{
				isprocessearly=true;
			}
			processcnt++;
		}
		SnapShotUtility.ViewSnapHot(readylist, emptyprocess, blockedlist, timeslice);
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
		float netturnaroundtime,averagewaitingtime;
		ProcessOutputParamaters processoutputparameters;
		List<OutputProcessBean>ProcessoutputList;
		processoutputparameters = new ProcessOutputParamaters();
		processintervalexecutiontime[] processinterval=new processintervalexecutiontime[processlist.size()];
		if (!processlist.isEmpty())
		{
			OutputProcessBean processoutput;
			ProcessoutputList = new ArrayList<OutputProcessBean>();
			processoutput = new OutputProcessBean(processlist.get(0).getPid(),processlist.get(0).getpName());
			processoutput.setWaitingTime(0);
			totalbursttime = 0;
			processoutput.setArrivalTime(processlist.get(0).getArrivalTime());
			processoutput.setBurstList(processlist.get(0).getBurstList());
			processoutput.setTurnaroundTime(processlist.get(0).getBurstList().get(0).getQuantum());
			totalturnaroundtime = processlist.get(0).getBurstList().get(0).getQuantum(); 
			processinterval[0]=new processintervalexecutiontime();
			processinterval[0].setArrivaltime(processlist.get(0).getArrivalTime().getMinutes());
			processinterval[0].setFinishtime((int)processlist.get(0).getBurstList().get(0).getQuantum());
			processinterval[0].setPid(processlist.get(0).getPid());
			processinterval[0].setPname(processlist.get(0).getpName());
			ProcessoutputList.add(processoutput);
	
			for(count = 1;count<processlist.size();count++)
			{
				processinterval[count]=new processintervalexecutiontime();
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
				turnaroundtime = waitingtime + processlist.get(count).getBurstList().get(0).getQuantum();
				processoutput.setTurnaroundTime(turnaroundtime);
				totalturnaroundtime += turnaroundtime;
				processoutput.setArrivalTime(processlist.get(count).getArrivalTime());
				processoutput.setBurstList(processlist.get(count).getBurstList());
				processinterval[count].setArrivaltime(processlist.get(count).getArrivalTime().getMinutes());
				processinterval[count].setFinishtime((int)turnaroundtime);
				processinterval[count].setPid(processlist.get(count).getPid());
				processinterval[count].setPname(processlist.get(count).getpName());
				ProcessoutputList.add(processoutput);
			} 
			
			processoutputparameters.setProcessoutputList(ProcessoutputList);
			netturnaroundtime = 0;
			averagewaitingtime =(float) totalwaitingtime/count;
			netturnaroundtime =(float) totalturnaroundtime/count;
			processoutputparameters.setAveragewaitingTime(averagewaitingtime);
			processoutputparameters.setNetTurnaroundTime(netturnaroundtime);
			processoutputparameters.setProcessinterval(processinterval);
			
		}
		return processoutputparameters;
	}
	@SuppressWarnings("deprecation")
	List<ProcessBean> checkforprocess(int time,List<ProcessBean> processList)
	{
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		for(int count = 1;count < processList.size();count++)
		{
			if(time == processList.get(count).getArrivalTime().getMinutes())
			{
				readylist.add(processList.get(count));
			}
		}
		return readylist;
	}
	
}
