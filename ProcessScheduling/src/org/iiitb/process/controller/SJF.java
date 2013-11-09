package org.iiitb.process.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.iiitb.model.bean.ProcessBean;
import org.iiitb.process.model.ProcessOutputParamaters;
import org.iiitb.process.model.OutputProcessBean;

public class SJF implements IScheduler{
	List<ProcessBean> orderofprocesscomplete = new ArrayList<ProcessBean>();
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters Schedule(List<ProcessBean> processList)
	{
		
		Collections.sort(processList, new ArrivalTimeComparator());
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		List<ProcessBean> currentsortlist = new ArrayList<ProcessBean>();
		List<ProcessBean> blockedlist = new ArrayList<ProcessBean>();
		
		ProcessBean current;
		int count;
		for(count = 0;count<processList.size();count++)
		{
			
			readylist.add(processList.get(count));
		}
		int i=1,j=1;
		currentsortlist.add(processList.get(0));
		while(i<processList.size())
		{
			if(processList.get(0).getArrivalTime().compareTo(processList.get(i).getArrivalTime())==0)
			{
				currentsortlist.add(processList.get(i));
				
			}
			
			i++;
		}
		Collections.sort(currentsortlist, new BurstTimeComparator());
		for(count = 0;count<processList.size();count++)
		{
			if(currentsortlist.get(0).getPid()==readylist.get(count).getPid())
			{
				j=count;
				
			}
		
		}
		orderofprocesscomplete.add(readylist.get(j));
		
		readylist.remove(j);
		current=new ProcessBean(currentsortlist.get(0).getPid(),currentsortlist.get(0).getpName());
		current.setArrivalTime(currentsortlist.get(0).getArrivalTime());
		current.setBurstList(currentsortlist.get(0).getBurstList());
		int time = 3;
		SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, time);		
		i=1;
		
		while(readylist.size()>0)
		{
			currentsortlist.clear();
			int flag;
			flag=0;
			for(count = 0;count<(readylist.size());count++)
			{
				if((current.getArrivalTime().getMinutes()+current.getBurstList().get(0).getQuantum())>=(readylist.get(count).getArrivalTime().getMinutes()))
				{
					
					currentsortlist.add(readylist.get(count));
					flag=1;
				}
			
			}
			if(flag==1)
			{
				
			Collections.sort(currentsortlist, new BurstTimeComparator());
			for(count = 0;count<(readylist.size());count++)
			{
				if(currentsortlist.get(0).getPid()==readylist.get(count).getPid())
				{j=count;
					break;
				}
			
			}
			orderofprocesscomplete.add(readylist.get(j));
			readylist.remove(j);
			current=new ProcessBean(currentsortlist.get(0).getPid(),currentsortlist.get(0).getpName());
			current.setArrivalTime(currentsortlist.get(0).getArrivalTime());
			current.setBurstList(currentsortlist.get(0).getBurstList());
	
			
			}
			else
			{
				Collections.sort(readylist,  new BurstTimeComparator());
			
			current=new ProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());	
			current.setArrivalTime(readylist.get(0).getArrivalTime());
			current.setBurstList(readylist.get(0).getBurstList());
			orderofprocesscomplete.add(readylist.get(0));
			readylist.remove(0);
			
			}
			i++;	
			SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, time);
			
		}
		ProcessOutputParamaters outputparameters;
		outputparameters = CalculateParameters(orderofprocesscomplete);
		return outputparameters;
	}
	
	
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters CalculateParameters(List<ProcessBean> processlist)
	{int count;
		
	ProcessOutputParamaters outputparameters = new ProcessOutputParamaters();	
	float waitingtime,totalwaitingtime = 0,bursttime,totalbursttime,turnaroundtime,totalturnaroundtime = 0;		
		List<OutputProcessBean>ProcessoutputList;
		if (!processlist.isEmpty())
		{
			OutputProcessBean processoutput;
			ProcessoutputList = new ArrayList<OutputProcessBean>();
			processoutput = new OutputProcessBean(processlist.get(0).getPid(),processlist.get(0).getpName());
			processoutput.setWaitingTime(0);
			processoutput.setCompletiontime(processlist.get(0).getBurstList().get(0).getQuantum()+processlist.get(0).getArrivalTime().getMinutes());
			processoutput.setTurnaroundTime(processlist.get(0).getBurstList().get(0).getQuantum());
			totalbursttime = processlist.get(0).getBurstList().get(0).getQuantum();
			
			processoutput.setArrivalTime(processlist.get(0).getArrivalTime());
			processoutput.setBurstList(processlist.get(0).getBurstList());
			
			ProcessoutputList.add(processoutput);
			
			totalturnaroundtime += totalbursttime;
			
			for(count = 1;count<processlist.size();count++)
			{
				processoutput = new OutputProcessBean(processlist.get(count).getPid(),processlist.get(count).getpName());
				bursttime = processlist.get(count).getBurstList().get(0).getQuantum();
				totalbursttime += bursttime;
				waitingtime = totalbursttime+processlist.get(0).getArrivalTime().getMinutes() - processlist.get(count).getArrivalTime().getMinutes()-bursttime ;
				totalwaitingtime+=waitingtime; 
				
				processoutput.setArrivalTime(processlist.get(count).getArrivalTime());
				processoutput.setBurstList(processlist.get(count).getBurstList());
				
				processoutput.setWaitingTime(waitingtime);
				processoutput.setCompletiontime(totalbursttime+processlist.get(0).getArrivalTime().getMinutes());
				processoutput.setTurnaroundTime(processoutput.getCompletiontime()-processlist.get(count).getArrivalTime().getMinutes());
											
				ProcessoutputList.add(processoutput);
				turnaroundtime =(processoutput.getCompletiontime()-processlist.get(count).getArrivalTime().getMinutes());
				totalturnaroundtime += turnaroundtime;
				
				
											
			} 
			outputparameters.setProcessoutputList(ProcessoutputList);
			outputparameters.setAveragewaitingTime(totalwaitingtime/count);
			outputparameters.setNetTurnaroundTime(totalturnaroundtime/count);
		}
		
		
		return outputparameters;
	}
}