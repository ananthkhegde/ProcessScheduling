package org.iiitb.process.model;
import org.iiitb.model.bean.ProcessBean;
/*
 * This class holds output parameters related to processes
 */
public class OutputProcessBean extends ProcessBean {
	public OutputProcessBean(int pid, String pName) {
		super(pid, pName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * process completion time
	 */
	private float completiontime;
	/**
	 * process turn around time
	 */
	private float turnaroundTime;
	/**
	 * waiting time of process
	 */
	private float waitingTime;
	
	
	public float getCompletiontime() {
		return completiontime;
	}
	public void setCompletiontime(float completiontime) {
		this.completiontime = completiontime;
	}
	public float getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(float waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	public float getTurnaroundTime() {
		return turnaroundTime;
	}
	public void setTurnaroundTime(float turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}
	
}
