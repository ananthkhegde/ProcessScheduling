package org.iiitb.process.view;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.iiitb.process.model.ProcessOutputParamaters;


@SuppressWarnings("serial")
public class GanttChartView extends JFrame
{
    DefaultTableModel model;
    JTable table;
    String col[] = {"   ","  ","  ","   ","  "};
public static void main(String args[])
{
	ProcessOutputParamaters outputparameters = new ProcessOutputParamaters();
new TableView().start(outputparameters); 
}
 
public void start(ProcessOutputParamaters outputparameters)
{
     
     model = new DefaultTableModel(col,outputparameters.getProcessoutputList().size()); 
        table=new JTable(model){@Override
        public boolean isCellEditable(int arg0, int arg1) {
         
            return false;
        }};
       
    JScrollPane pane = new JScrollPane(table);
     
    int rowcount = 0, count = 0,arrivaltime,bursttime,prevarrivaltime,totalbursttime = 0,ggap = 0;
    while(count<outputparameters.getProcessinterval().length)
    {
    	for(int colcnt = 0;colcnt<5;colcnt++)
    	{
    		arrivaltime = outputparameters.getProcessinterval()[count].getArrivaltime();
    		bursttime = (int)outputparameters.getProcessoutputList().get(count).getBurstList().get(0).getQuantum();
    		
    		if(arrivaltime ==0)
    		{
    			bursttime = bursttime-1;
    			totalbursttime = -1;
    		}
    		
    		if(count ==0)
    		{
    			table.setValueAt(outputparameters.getProcessinterval()[count].getPname(),rowcount,colcnt);
    		table.setValueAt("(" +arrivaltime + "-" +bursttime + ")",rowcount+1,colcnt);
    		}
    		else
    		{
    			totalbursttime = totalbursttime +bursttime;
    			arrivaltime = outputparameters.getProcessinterval()[count].getArrivaltime();
    			prevarrivaltime = outputparameters.getProcessinterval()[count-1].getArrivaltime();
        		bursttime = (int)outputparameters.getProcessoutputList().get(count).getBurstList().get(0).getQuantum();
        		//finf if there is a gap
        		int gap = (int)(outputparameters.getProcessoutputList().get(count-1).getBurstList().get(0).getQuantum() +prevarrivaltime) -arrivaltime ;
        		if(gap < 0 )
        		{
        			table.setValueAt("("+ (totalbursttime +ggap)+ "-"
            	    		+(totalbursttime+ggap+(-1*gap)) + ")",rowcount+1,colcnt);
        			ggap = ggap + (-1*gap);
        			colcnt++;
        		}
        		else
        		{
        			gap = 0;
        		}
        		table.setValueAt(outputparameters.getProcessinterval()[count].getPname(),rowcount,colcnt);
    			table.setValueAt("("+ (totalbursttime +ggap) + "-"
        	    		+(totalbursttime+bursttime+ggap) + ")",rowcount+1,colcnt);
    			
    		}
    		
    		if(count+1 == outputparameters.getProcessinterval().length)
    		{
    			count++;
    			break;
    		}
    		//check for gap in the process
    		
    		count++;
    	}
    	rowcount++;
    	rowcount++;
    }
   
    JPanel panel = new JPanel();
    panel.setVisible(true);
  
   
    add(pane);
    add(panel);
    setVisible(true);
    setSize(470,500);
    setLayout(new FlowLayout());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
}
public void start1(ProcessOutputParamaters outputparameters)
{
     
     model = new DefaultTableModel(col,outputparameters.getProcessoutputList().size()); 
        table=new JTable(model){@Override
        public boolean isCellEditable(int arg0, int arg1) {
         
            return false;
        }};
       
    JScrollPane pane = new JScrollPane(table);
     
    int rowcount = 0, count = 0,arrivaltime,bursttime,prevarrivaltime,totalbursttime = 0,ggap = 0,noofinterval=0;
    while(count<outputparameters.getProcessinterval().length)
    {
    	for(int colcnt = 0;colcnt<5;colcnt++)
    	{
    		arrivaltime = outputparameters.getProcessinterval()[noofinterval].getArrivaltime();
    		bursttime = (int)outputparameters.getProcessinterval()[noofinterval].getFinishtime();
    		
    		
    		
    		if(count ==0)
    		{
    			table.setValueAt(outputparameters.getProcessinterval()[noofinterval].getPname(),rowcount,colcnt);
    		table.setValueAt("(" +arrivaltime + "-" +bursttime + ")",rowcount+1,colcnt);
    		noofinterval++;
    		}
    		else
    		{
    			
    			arrivaltime = outputparameters.getProcessinterval()[noofinterval].getArrivaltime();
    			bursttime  = outputparameters.getProcessinterval()[noofinterval].getFinishtime();
        		
        		
        		table.setValueAt(outputparameters.getProcessinterval()[noofinterval].getPname(),rowcount,colcnt);
        		table.setValueAt("(" +arrivaltime + "-" +bursttime + ")",rowcount+1,colcnt);
        		noofinterval++;	
    		}
    		
    		if(count+1 == outputparameters.getProcessinterval().length)
    		{
    			count++;
    			break;
    		}
    		//check for gap in the process
    		
    		count++;
    	}
    	rowcount++;
    	rowcount++;
    }
   
    JPanel panel = new JPanel();
    panel.setVisible(true);
  
   
    add(pane);
    add(panel);
    setVisible(true);
    setSize(470,500);
    setLayout(new FlowLayout());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
}}




	


