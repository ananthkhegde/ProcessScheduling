package org.iiitb.process.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.iiitb.model.bean.ProcessBean;
import org.iiitb.process.model.ProcessOutputParamaters;
import org.iiitb.process.model.OutputProcessBean;

public class PreemptivePriority implements IScheduler{
	List<ProcessBean> orderofprocesscomplete = new ArrayList<ProcessBean>();
	List<ProcessBean> orderofprocess = new ArrayList<ProcessBean>();
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters Schedule(List<ProcessBean> processList)
	{
		
		
		Collections.sort(processList, new ArrivalTimeComparator());
		// trying populate common utilities Data structure
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		List<ProcessBean> blockedlist = new ArrayList<ProcessBean>();
		List<ProcessBean> currentsortlist = new ArrayList<ProcessBean>();
		int[] completiontime= new int[(processList.size()+1)];
		ProcessBean current,previous =new ProcessBean(-1,"null");
		
		int count = 0,totalbursttime=0,totalrunningtime=0;
		int[] bursttimeremaining=new int[processList.size()+1];
		for(count = 0;count<processList.size();count++)
		{
			readylist.add(processList.get(count));
			bursttimeremaining[processList.get(count).getPid()]=(int) processList.get(count).getBurstList().get(0).getQuantum();
			totalbursttime+=processList.get(count).getBurstList().get(0).getQuantum();
		}
	
		totalrunningtime=(int) (totalbursttime+processList.get(0).getArrivalTime().getMinutes());
		int k=0;
		
		
		processintervalexecutiontime[] processinterval1=new processintervalexecutiontime[totalrunningtime];
		for( int i=0; i<totalrunningtime;i++)
			processinterval1[i]=new processintervalexecutiontime();
		
		int time = 3,flag=1,s=-1;
		int j;
		int[] firsttime=new int[processList.size()+1];
		int i=0;
		while(i<processList.size()+1)
		{firsttime[i]=0;
			i++;
		}
		k=0;
		for(i=readylist.get(0).getArrivalTime().getMinutes();i<totalrunningtime;i++)
		{
			
				
			
			for(j=0;j<readylist.size();j++)
			{
				if (i>=(readylist.get(j).getArrivalTime().getMinutes()))
				{
					currentsortlist.add(readylist.get(j));
				}
			}
			
			Collections.sort(currentsortlist, new prioritycomparator());
			
			for(count = 0;count<readylist.size();count++)
			{
				if(currentsortlist.get(0).getPid()==readylist.get(count).getPid())
				{
					s=count;
					
				}
			
			}
			orderofprocesscomplete.add(readylist.get(s));
			
			readylist.remove(s);
			current=new ProcessBean(currentsortlist.get(0).getPid(),currentsortlist.get(0).getpName());
			current.setArrivalTime(currentsortlist.get(0).getArrivalTime());
			
			if(firsttime[currentsortlist.get(0).getPid()]==0)
			{
			bursttimeremaining[currentsortlist.get(0).getPid()]=(int) (currentsortlist.get(0).getBurstList().get(0).getQuantum()-1);
			firsttime[currentsortlist.get(0).getPid()]=1;
			}
			else
			{
				bursttimeremaining[currentsortlist.get(0).getPid()]=bursttimeremaining[currentsortlist.get(0).getPid()]-1;
			}
			current.setBurstList(currentsortlist.get(0).getBurstList());
			current.setPriority(currentsortlist.get(0).getPriority());
			
			SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, time);
			
			
			
			
			if(flag==1)
			{processinterval1[k].setPid(current.getPid());
			processinterval1[k].setArrivaltime(i);
		
			processinterval1[k].setFinishtime(i+1);
				flag=0;
				previous=current;
				
				
			}
			else if(current.getPid()==previous.getPid())
			{
				processinterval1[k].setFinishtime(i+1);
			
			}
			else
			{k++;
				processinterval1[k].setPid(current.getPid());
			processinterval1[k].setArrivaltime(i);
		
			processinterval1[k].setFinishtime(i+1);
			previous=current;
			}
			
			if(bursttimeremaining[currentsortlist.get(0).getPid()]==0)
			{
			completiontime[current.getPid()]=i+1;
			orderofprocess.add(current);
			}
			else
			{		readylist.add(current);
		
			}
			currentsortlist.clear();
		}
		
		System.out.println("Order in which process completed");
		for(count = 0;count<orderofprocess.size();count++)
		{
			
			System.out.println(orderofprocess.get(count).getpName()+" "+orderofprocess.get(count).getArrivalTime().getMinutes()+" "+completiontime[orderofprocess.get(count).getPid()]);
		}	
		

	
		System.out.println("Order in which process executed with time");
		for(count = 0;count<=k;count++)
		{
			
			System.out.println(processinterval1[count].getArrivaltime()+" "+processinterval1[count].getPid()+" "+processinterval1[count].getFinishtime());
		}
		
		
		
		float waitingtime,bursttime,turnaroundtime;
		float totalwaitingtime = 0,totalturnaroundtime = 0;
		totalbursttime=0;
		
		
		List<OutputProcessBean>ProcessoutputList;
		
		
			OutputProcessBean processoutput;
			ProcessoutputList = new ArrayList<OutputProcessBean>();
			for(count = 0;count<orderofprocess.size();count++)
			{
			processoutput = new OutputProcessBean(orderofprocess.get(count).getPid(),orderofprocess.get(count).getpName());
			bursttime = orderofprocess.get(count).getBurstList().get(0).getQuantum();
			totalbursttime += bursttime;
			waitingtime = completiontime[orderofprocess.get(count).getPid()]- orderofprocess.get(count).getArrivalTime().getMinutes()-bursttime ;
			totalwaitingtime+=waitingtime; 
			
			
			
			processoutput.setWaitingTime(waitingtime);
			processoutput.setCompletiontime((completiontime[orderofprocess.get(count).getPid()]));
			processoutput.setTurnaroundTime(processoutput.getCompletiontime()-orderofprocess.get(count).getArrivalTime().getMinutes());
			processoutput.setArrivalTime(orderofprocess.get(count).getArrivalTime());
			processoutput.setBurstList(orderofprocess.get(count).getBurstList());
		
			
			ProcessoutputList.add(processoutput);
			turnaroundtime =(processoutput.getCompletiontime()-orderofprocess.get(count).getArrivalTime().getMinutes());
			totalturnaroundtime += turnaroundtime;
			processoutput.setArrivalTime(orderofprocess.get(count).getArrivalTime());
			processoutput.setBurstList(orderofprocess.get(count).getBurstList());

			
				System.out.println(orderofprocess.get(count).getpName()+" "+processoutput.getWaitingTime()+" "+processoutput.getCompletiontime()+" "+processoutput.getTurnaroundTime());
				
			} 
			
			ProcessOutputParamaters outputparameters = new ProcessOutputParamaters();
			outputparameters.setProcessoutputList(ProcessoutputList);
			outputparameters.setAveragewaitingTime(totalwaitingtime/count);
			outputparameters.setNetTurnaroundTime(totalturnaroundtime/count);
			return outputparameters;
}

}