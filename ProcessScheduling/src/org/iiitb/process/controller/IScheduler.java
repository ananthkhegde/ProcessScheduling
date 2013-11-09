package org.iiitb.process.controller;
import java.util.List;
import  org.iiitb.process.model.ProcessOutputParamaters;
import org.iiitb.model.bean.ProcessBean;
/*
 * Interface to implement basic functionalities in Scheduling
 */
public interface IScheduler {
	
public ProcessOutputParamaters Schedule(List<ProcessBean> processList);
//public ProcessOutputParamaters CalculateParameters(List<ProcessBean> processlist);

}
