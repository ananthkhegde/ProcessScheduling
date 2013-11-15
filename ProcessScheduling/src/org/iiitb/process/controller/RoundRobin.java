package org.iiitb.process.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.iiitb.model.bean.ProcessBean;
import org.iiitb.process.model.OutputProcessBean;
import org.iiitb.process.model.ProcessOutputParamaters;

public class RoundRobin implements IScheduler{
	
	int StartingTime[];
	int RunningTime[];
	int FinishingTime[];
	
	@SuppressWarnings("deprecation")
	public ProcessOutputParamaters Schedule(List<ProcessBean> processList){
		
		Collections.sort(processList, new ArrivalTimeComparator());
		
		List<ProcessBean> readylist = new ArrayList<ProcessBean>();
		List<ProcessBean> blockedlist = new ArrayList<ProcessBean>();
		List<ProcessBean> templist = new ArrayList<ProcessBean>();
		ProcessBean current= null;
		ProcessBean blank=null;
		ProcessBean tempVar;
		
		StartingTime= new int[processList.size()];
		RunningTime = new int[processList.size()];
		FinishingTime = new int[processList.size()];
		
		int location;
		int j = 1 ;
		long quantum;
		
		//copying all the values from processlist to templist
		for(int i=0; i< processList.size(); i++){
			templist.add(processList.get(i));
		}
		
		//sorting templist according to arrival time
		Collections.sort(templist, new ArrivalTimeComparator());
		
		for(int i=0 ; i< processList.size(); i++)
		{
			StartingTime[processList.get(i).getPid() -1] = processList.get(i).getArrivalTime().getMinutes();
			System.out.println("starting time of" + i + "is" + StartingTime[i]);
		}
		
		while(templist.size()>0){
			
			System.out.println("templist size " + templist.size());
			
			if((templist.get(0).getBurstList().get(0).getQuantum())== 0){
				FinishingTime[templist.get(0).getPid()]=j-2;
				templist.remove(0);
			}

			j++;
			
			//populating readylist
			for(int i =0; i< templist.size(); i++){
				//populating ready list if arrival time i greater than time
				if((templist.get(i).getArrivalTime().getMinutes() < j)){
					readylist.add(templist.get(i));
				}
			}
			
			System.out.println("ready list size "+readylist.size());														// infinite loop detected  must be removed
			//assigning process to current
			if(readylist.size()== 0 && templist.size() == 0){
				SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, j-2);
			    JOptionPane.showMessageDialog(null, "No process is left.");
				System.exit(0);
			}
			
			if(readylist.size()>0)
			{
				current = new ProcessBean(readylist.get(0).getPid(),readylist.get(0).getpName());
				RunningTime[current.getPid()-1]++;
			
				quantum = templist.get(0).getBurstList().get(0).getQuantum();
				
				templist.get(0).getBurstList().get(0).setQuantum(quantum-1);
				tempVar= templist.get(0);
				templist.remove(0);
				readylist.remove(0);
				location= templist.size();
				SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, j-1);
				
				if(((int)(tempVar.getBurstList().get(0).getQuantum()))!=0){
					templist.add(location, tempVar);
				}
				else
					FinishingTime[tempVar.getPid()-1]=j;

				//removing all elements from readylist
				readylist.clear();
			}
		    System.out.println("j's value" + j);
		}	
		current = blank;
		SnapShotUtility.ViewSnapHot(readylist, current, blockedlist, j);
		
		System.out.println("hello");
		for(int i=0 ; i< processList.size(); i++)
		{
			System.out.println("for process "+ i);
			System.out.println("Arrivaltime " + StartingTime[i]);
			System.out.println("runningtime " + RunningTime[i]);
			System.out.println("finishtime " + FinishingTime[i]);
			System.out.println();
		}
		ProcessOutputParamaters processoutputparameters;
		processoutputparameters =	CalculateParameters(processList);
		return processoutputparameters;
	}
	
	public ProcessOutputParamaters CalculateParameters(List<ProcessBean> processlist){
		
		int count;
		long waitingtime,totalwaitingtime = 0,bursttime,totalbursttime=0,turnaroundtime,totalturnaroundtime = 0;
		ProcessOutputParamaters processoutputparameters;
		List<OutputProcessBean>ProcessoutputList;
		processoutputparameters = new ProcessOutputParamaters();
		
		if (!processlist.isEmpty()){
			OutputProcessBean processoutput;
			ProcessoutputList = new ArrayList<OutputProcessBean>();
			
			for(count = 0;count<processlist.size();count++){
				processoutput = new OutputProcessBean(processlist.get(count).getPid(),processlist.get(count).getpName());
				totalbursttime += RunningTime[count];
				waitingtime = FinishingTime[count] - StartingTime[count] - RunningTime[count];
				processoutput.setWaitingTime(waitingtime);
				totalwaitingtime+=waitingtime; 
				turnaroundtime = waitingtime + RunningTime[count];
				processoutput.setTurnaroundTime(turnaroundtime);
				processoutput.setArrivalTime(processlist.get(count).getArrivalTime());
				processoutput.setBurstList(processlist.get(count).getBurstList());
				totalturnaroundtime += turnaroundtime;
				ProcessoutputList.add(processoutput);
				
			}
			
			processoutputparameters.setProcessoutputList(ProcessoutputList);
			processoutputparameters.setAveragewaitingTime(totalwaitingtime/count);
			processoutputparameters.setNetTurnaroundTime(totalturnaroundtime/count);
		}
		return processoutputparameters;	
	}	
}