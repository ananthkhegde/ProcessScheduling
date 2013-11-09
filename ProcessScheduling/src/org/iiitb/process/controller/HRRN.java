package org.iiitb.process.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.iiitb.model.bean.ProcessBean;
import org.iiitb.model.bean.TimeQuantum;
import org.iiitb.process.model.OutputProcessBean;
import org.iiitb.process.model.ProcessOutputParamaters;

public class HRRN implements IScheduler {
	
	
	
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters Schedule(List<ProcessBean> processList)
	{
		System.out.println(processList.size());
		int pid;
		String pName;
		int p,index,burstTime;
		java.util.Date arrivalTime;
		TimeQuantum timequant;
		List<TimeQuantum> burstList;
		float completionTime,waitingTime,totalwaitingtime = 0,turnaroundTime,totalturnaroundTime = 0;
		Collections.sort(processList, new ArrivalTimeComparator());
	
		// trying populate common utilities Data structure
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		List<ProcessBean> blockedlist = new ArrayList<ProcessBean>();
		List<ProcessBean> mainlist = new ArrayList<ProcessBean>();
		ProcessBean current;
		current = new ProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
	
		for(int count = 0;count<processList.size();count++)
		{
			readylist.add(processList.get(count));
			
			
		}
	
		List<OutputProcessBean>ProcessoutputList;
		ProcessoutputList = new ArrayList<OutputProcessBean>();
		OutputProcessBean processoutput;
		
		processoutput = new OutputProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
				
		ProcessOutputParamaters processoutputparameters;
		processoutputparameters = new ProcessOutputParamaters();
		while(!readylist.isEmpty())
		{
			
			if(ProcessoutputList.isEmpty())
			{
				if(readylist.get(0).getArrivalTime().getMinutes()==0)
				{
					current = new ProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
					processoutput = new OutputProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());
					completionTime=readylist.get(0).getBurstList().get(0).getQuantum();
					processoutput.setCompletiontime(completionTime);
					ProcessoutputList.add(processoutput);
					pid=readylist.get(0).getPid();
					current.setPid(pid);
					pName=readylist.get(0).getpName();
					current.setpName(pName);
					arrivalTime=readylist.get(0).getArrivalTime();
					current.setArrivalTime(arrivalTime);
					burstTime=(int)readylist.get(0).getBurstList().get(0).getQuantum();
					current.getBurstList().get(0).setQuantum(burstTime);
					blockedlist.add(current);
					readylist.remove(0);
				}
				else{
					current = new ProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
					processoutput = new OutputProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());
					completionTime=(readylist.get(0).getArrivalTime().getMinutes()-0)+readylist.get(0).getBurstList().get(0).getQuantum();;
					processoutput.setCompletiontime(completionTime);
					ProcessoutputList.add(processoutput);
					pid=readylist.get(0).getPid();
					current.setPid(pid);
					pName=readylist.get(0).getpName();
					current.setpName(pName);
					arrivalTime=readylist.get(0).getArrivalTime();
					current.setArrivalTime(arrivalTime);
					burstTime=(int)readylist.get(0).getBurstList().get(0).getQuantum();;
					timequant = new TimeQuantum();
					timequant.setQuantum(burstTime);
					burstList = new ArrayList<TimeQuantum>();
		        	burstList.add(timequant);
		        	current.setBurstList(burstList);
					blockedlist.add(current);
					readylist.remove(0);
				}
				
			}
			else
			{
				current = new ProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
				processoutput = new OutputProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());			
				p=priority(readylist,ProcessoutputList, readylist.size());
				
				index=ProcessoutputList.size()-1;
			
				completionTime=ProcessoutputList.get(index).getCompletiontime()+readylist.get(p).getBurstList().get(0).getQuantum();
				
				processoutput.setCompletiontime(completionTime);
				ProcessoutputList.add(processoutput);
				
				pid=readylist.get(p).getPid();
				current.setPid(pid);
				pName=readylist.get(p).getpName();
				current.setpName(pName);
				arrivalTime=readylist.get(p).getArrivalTime();
				current.setArrivalTime(arrivalTime);
				burstTime=(int)readylist.get(0).getBurstList().get(0).getQuantum();
				current.getBurstList().get(0).setQuantum(burstTime);
				//current.setburstTime(burstTime);
				blockedlist.add(current);
				
				readylist.remove(p);		
				
				
				
			}
			
		}
			
	for(int m=0;m<blockedlist.size();m++)
		{
			current = new ProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
			processoutput = new OutputProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
		
			if(m==0)
			{
				waitingTime=0;
				
				processoutput.setWaitingTime(waitingTime);
				turnaroundTime=waitingTime+blockedlist.get(0).getBurstList().get(0).getQuantum();
				processoutput.setTurnaroundTime(turnaroundTime);
				ProcessoutputList.add(processoutput);
			}
			else
			{	
				waitingTime = ProcessoutputList.get(m-1).getCompletiontime() - blockedlist.get(m).getArrivalTime().getMinutes() ;
				
				totalwaitingtime+=waitingTime;
				processoutput.setWaitingTime(waitingTime);
				turnaroundTime=waitingTime+blockedlist.get(m).getBurstList().get(0).getQuantum();
				processoutput.setTurnaroundTime(turnaroundTime);
				totalturnaroundTime+=turnaroundTime;
				ProcessoutputList.add(processoutput);
			}
			
			
	}	
	
	processoutputparameters.setProcessoutputList(ProcessoutputList);
	processoutputparameters.setAveragewaitingTime(totalwaitingtime/ProcessoutputList.size());
	processoutputparameters.setNetTurnaroundTime(totalturnaroundTime/ProcessoutputList.size());
	System.out.println("avgwt "+processoutputparameters.getAveragewaitingTime()+"\tavgtat "+processoutputparameters.getNetTurnaroundTime());
	
		blockedlist.remove(0);
	
		current=new ProcessBean(processList.get(0).getPid(),processList.get(0).getpName());
		int time = 3;
		SnapShotUtility.ViewSnapHot(blockedlist, current, mainlist, time);
		
		while(blockedlist.size()>0)
		{
			current=new ProcessBean(blockedlist.get(0).getPid(),blockedlist.get(0).getpName());
			blockedlist.remove(0);
			SnapShotUtility.ViewSnapHot(blockedlist, current, mainlist, time);
			
		}
	
		return processoutputparameters;
	}
	@SuppressWarnings("deprecation")
	public int priority(List<ProcessBean> readylist,List<OutputProcessBean>mainlist, int size)
	{
		int l=0,index;
		float waitingtime=0,bursttime;
		double p, min=0.0;
		
		
	
		index=mainlist.size()-1;
		for(int i=0;i<size;i++)
		{
	
			waitingtime=mainlist.get(index).getCompletiontime()-readylist.get(i).getArrivalTime().getMinutes();
	
			bursttime=readylist.get(i).getBurstList().get(0).getQuantum();
			p=(waitingtime+bursttime)/bursttime;
			if(min<p)
			{
				min=p;
				l=i;
			}
		}
		return l;
		
	}
	
	
}