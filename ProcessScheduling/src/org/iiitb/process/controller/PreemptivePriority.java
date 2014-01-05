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
		ProcessBean current,noprocess=new ProcessBean(-1," "),previous =new ProcessBean(-1,"null");
		
		int count = 0,totalbursttime=0,totalrunningtime=0;
		int[] bursttimeremaining=new int[processList.size()+1];
		for(count = 0;count<processList.size();count++)
		{
			readylist.add(processList.get(count));
			bursttimeremaining[processList.get(count).getPid()]=(int) processList.get(count).getBurstList().get(0).getQuantum();
			
			if(count==0)
			totalbursttime+=(processList.get(count).getBurstList().get(0).getQuantum()+processList.get(0).getArrivalTime().getMinutes());
			else
			{if(processList.get(count).getArrivalTime().getMinutes()>totalbursttime)
			{
				totalbursttime=processList.get(count).getArrivalTime().getMinutes()	;
			}
			totalbursttime+=(processList.get(count).getBurstList().get(0).getQuantum());
			}
		}
	
		totalrunningtime=(int) (totalbursttime);
		int k=0;
		
		
		processintervalexecutiontime[] processinterval1=new processintervalexecutiontime[totalrunningtime];
		for( int i=0; i<totalrunningtime;i++)
			processinterval1[i]=new processintervalexecutiontime();
		
		int flag=1,s=-1;
		int j;
		int[] firsttime=new int[processList.size()+1];
		int i=0;
		while(i<processList.size()+1)
		{firsttime[i]=0;
			i++;
		}
		readylist.clear();
		k=0;
		int noofprocessintime;
		noofprocessintime=0;
		for(i=processList.get(0).getArrivalTime().getMinutes();i<totalrunningtime;i++)
		{
			
				
			
			for(j=0;j<processList.size();j++)
			{
				if (i>=(processList.get(j).getArrivalTime().getMinutes()))
				{noofprocessintime++;
					currentsortlist.add(processList.get(j));
					readylist.add(processList.get(j));
				}
			}
			if(noofprocessintime!=0)
			{Collections.sort(currentsortlist, new prioritycomparator());
			Collections.sort(readylist, new prioritycomparator());
			for(count = 0;count<processList.size();count++)
			{
				if(currentsortlist.get(0).getPid()==processList.get(count).getPid())
				{
					s=count;
					
				}
			
			}
			orderofprocesscomplete.add(readylist.get(0));
			processList.remove(s);
			readylist.remove(0);
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
			
			SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, i);
			
			
			
			
			if(flag==1)
			{processinterval1[k].setPid(current.getPid());
			processinterval1[k].setArrivaltime(i);
			processinterval1[k].setPname(current.getpName());
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
			processinterval1[k].setPname(current.getpName());
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
			{		processList.add(current);
		
			}
			currentsortlist.clear();
			readylist.clear();
			noofprocessintime=0;
			}
			else
			{//current=null;previous=null;
				if(noprocess.getPid()==previous.getPid())
				{
					processinterval1[k].setFinishtime(i+1);
				
				}
				else{
					k++;
				
				processinterval1[k].setPid(noprocess.getPid());
				processinterval1[k].setPname(noprocess.getpName());
			processinterval1[k].setArrivaltime(i);
		
			processinterval1[k].setFinishtime(i+1);
			previous=noprocess;
			}
				SnapShotUtility.ViewSnapHot(readylist,noprocess, blockedlist, i);
			}
			if((i+1)==totalrunningtime)
			{
				//current=null;
				SnapShotUtility.ViewSnapHot(readylist,noprocess, blockedlist, (i+1));
			}
		}
		
		System.out.println("Order in which process completed");
		for(count = 0;count<orderofprocess.size();count++)
		{
			
			System.out.println(orderofprocess.get(count).getpName()+" "+orderofprocess.get(count).getArrivalTime().getMinutes()+" "+completiontime[orderofprocess.get(count).getPid()]);
		}	
		

	
		processintervalexecutiontime[] processinterval=new processintervalexecutiontime[(k+1)];
		
		System.out.println("Order in which process executed with time");
		for(count = 0;count<=k;count++)
		{processinterval[count]=new processintervalexecutiontime();
		processinterval[count].setArrivaltime(processinterval1[count].getArrivaltime());
		processinterval[count].setFinishtime(processinterval1[count].getFinishtime());
		processinterval[count].setPid(processinterval1[count].getPid());
		processinterval[count].setPname(processinterval1[count].getPname());
			
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
			outputparameters.setProcessinterval(processinterval);
			return outputparameters;
			
}

}