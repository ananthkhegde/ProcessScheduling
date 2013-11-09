package org.iiitb.process.view;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.iiitb.process.model.ProcessOutputParamaters;


@SuppressWarnings("serial")
public class TableView extends JFrame
{
    DefaultTableModel model;
    JTable table;
    String col[] = {"Process Name","Arrival Time","Burst Time","Waiting Time","Turn Around Time"};
    private static  JLabel label;
    private static  JLabel label1;
public static void main(String args[])
{
	ProcessOutputParamaters outputparameters = new ProcessOutputParamaters();
new TableView().start(outputparameters); 
}
 
@SuppressWarnings("deprecation")
public void start(ProcessOutputParamaters outputparameters)
{
     
     model = new DefaultTableModel(col,outputparameters.getProcessoutputList().size()); 
        table=new JTable(model){@Override
        public boolean isCellEditable(int arg0, int arg1) {
         
            return false;
        }};
       
    JScrollPane pane = new JScrollPane(table);
     
    int rowcount = 0;
    for(rowcount = 0;rowcount<outputparameters.getProcessoutputList().size();rowcount++)
    {
    	
    		table.setValueAt(outputparameters.getProcessoutputList().get(rowcount).getpName(),rowcount,0);
    		table.setValueAt(outputparameters.getProcessoutputList().get(rowcount).getArrivalTime().getMinutes(),rowcount,1);
    		table.setValueAt(outputparameters.getProcessoutputList().get(rowcount).getBurstList().get(0).getQuantum(),rowcount,2);
    		table.setValueAt(outputparameters.getProcessoutputList().get(rowcount).getWaitingTime(),rowcount,3);
    		table.setValueAt(outputparameters.getProcessoutputList().get(rowcount).getTurnaroundTime(),rowcount,4);
    
    }
    ///
    JPanel panel = new JPanel();
    label = new JLabel();
    label.setText("Average Waiting time = " + outputparameters.getAveragewaitingTime());
    label1 = new JLabel();
    label1.setText("Average Turn Around time = " + outputparameters.getNetTurnaroundTime());
    label1.setVisible(true); 
    panel.add(label);
    panel.add(label1);
    panel.setVisible(true);
  
   
    add(pane);
    add(panel);
    setVisible(true);
    setSize(470,500);
    setLayout(new FlowLayout());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
}
}



	


