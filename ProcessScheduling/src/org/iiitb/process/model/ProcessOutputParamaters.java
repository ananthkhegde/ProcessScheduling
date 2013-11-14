package org.iiitb.process.model;

import java.util.List;

import org.iiitb.process.controller.processintervalexecutiontime;
/*
 * Class which holds Output parameters after all the process has been scheduled
 * it contains list of processes and parameters applicable to all process like total waiting time
 */
public class ProcessOutputParamaters {
	/*
	 * List of process successfully come out after completing 
	 */
	private List<OutputProcessBean> ProcessoutputList;
	/*
	 * Net turn around time of list of out put processes
	 */
	private float netTurnaroundTime;
	/*
	 * Average waiting time of list of out put processes
	 */
	private float averagewaitingTime;
	
	private processintervalexecutiontime[] processinterval;

	public List<OutputProcessBean> getProcessoutputList() {
		return ProcessoutputList;
	}

	public void setProcessoutputList(List<OutputProcessBean> processoutputList) {
		ProcessoutputList = processoutputList;
	}

	public float getNetTurnaroundTime() {
		return netTurnaroundTime;
	}

	public void setNetTurnaroundTime(float netTurnaroundTime) {
		this.netTurnaroundTime = netTurnaroundTime;
	}

	public float getAveragewaitingTime() {
		return averagewaitingTime;
	}

	public void setAveragewaitingTime(float averagewaitingTime) {
		this.averagewaitingTime = averagewaitingTime;
	}

	public processintervalexecutiontime[] getProcessinterval() {
		return processinterval;
	}

	public void setProcessinterval(processintervalexecutiontime[] processinterval) {
		this.processinterval = processinterval;
	}

	
}
