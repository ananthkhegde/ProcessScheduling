package org.iiitb.process.controller;

public class processintervalexecutiontime {
	
	private int pid;
	private String pname;
	private int arrivaltime;
	private int finishtime;
	
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getArrivaltime() {
		return arrivaltime;
	}
	public void setArrivaltime(int arrivaltime) {
		this.arrivaltime = arrivaltime;
	}
	public int getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(int finishtime) {
		this.finishtime = finishtime;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}

}
