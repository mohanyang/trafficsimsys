package traffic.console.graphic;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

 

import traffic.external.generate.GenerateController;

public class GenerateConsole extends JFrame implements ActionListener{
	
	protected static final String[] tag=new String[5];
	JTextField[] textField=new JTextField[4]; 
	JLabel[] textFieldLabel=new JLabel[4];
    protected JLabel actionLabel;
    
	public GenerateConsole(){
		tag[0]="maxspeed";
		tag[1]="initspeed";
		tag[2]="type";
		tag[3]="bornpoint";
		for(int i=0;i<4;i++){
			textField[i]=new JTextField(10);
			textField[i].setActionCommand(tag[i]);
			textField[i].addActionListener(this);
			textFieldLabel[i]=new JLabel(tag[i]+": ");
			textFieldLabel[i].setLabelFor(textField[i]);
		}
        
        actionLabel = new JLabel("Config the vehicle for adding");
        actionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
 
        JPanel paraPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        paraPane.setLayout(gridbag);

        JLabel[] labels = {textFieldLabel[0], textFieldLabel[1], textFieldLabel[2], textFieldLabel[3]};
        JTextField[] textFields = {textField[0], textField[1], textField[2], textField[3]};
        addLabelTextRows(labels, textFields, gridbag, paraPane);

        c.gridwidth = GridBagConstraints.REMAINDER; 
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        gridbag.setConstraints(actionLabel, c);
        paraPane.add(actionLabel);
        paraPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Parameter"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        JButton confirmButton = new JButton("confirm");
        confirmButton.setActionCommand("confirm");
        confirmButton.addActionListener(new confirmListener());
        
        JPanel commPane = new JPanel();
        GridBagLayout gridbag2 = new GridBagLayout();
        GridBagConstraints c2 = new GridBagConstraints();
        JButton[] buttons = {confirmButton};
        
        commPane.setLayout(gridbag2);
        
        addcommRows(buttons, gridbag2, commPane);

        c2.gridwidth = GridBagConstraints.REMAINDER; 
        c2.anchor = GridBagConstraints.WEST;
        c2.weightx = 1.0;
        commPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Commands"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        JPanel contentPane = new JPanel();
        BoxLayout box = new BoxLayout(contentPane, BoxLayout.X_AXIS);
        contentPane.setLayout(box);
        contentPane.add(paraPane);
        contentPane.add(commPane);
        setContentPane(contentPane);
    }

    class confirmListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	for(int i=0;i<4;i++){
        		int tmp=Integer.valueOf(textField[i].getText());
        		paraconfig(i,tmp);
        	}
        	if(GenerateController.generateVehicle()<0){
        		System.out.print("fail in adding vehicle\n");
        	}
        	setVisible(false);   
        }
    }

    void paraconfig(int index,int value){
    	switch(index){
    	case 0:
    		GenerateController.setmaxspeed(value);
    		break;
    	case 1:
    		GenerateController.setinitspeed(value);
    		break;
    	case 2:
    		if(GenerateController.settype(value)<0){
    			 actionLabel.setText("error type");
    		}
    		break;
    	case 3:
    		if(GenerateController.setbornpoint(value)<0){
    			 actionLabel.setText("error born point");
    		}
    		break;
    	default:
    		System.out.print("error action\n");
    	break;
    	}
    }
    private void addLabelTextRows(JLabel[] labels, JTextField[] textFields, GridBagLayout gridbag, Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
        	if(textFields!=null){
        		c.gridwidth = GridBagConstraints.RELATIVE;
        	}else{
        		c.gridwidth = GridBagConstraints.REMAINDER;
        	}
            c.fill = GridBagConstraints.NONE;       
            c.weightx = 0.0;                        
            gridbag.setConstraints(labels[i], c);
            container.add(labels[i]);

            if(textFields!=null){
            	c.gridwidth = GridBagConstraints.REMAINDER;     
            	c.fill = GridBagConstraints.HORIZONTAL;
            	c.weightx = 1.0;
            	gridbag.setConstraints(textFields[i], c);
            	container.add(textFields[i]);
            }
        }
    }
    
    private void addcommRows(JButton[] buttons, GridBagLayout gridbag, Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numbuttons = buttons.length;
        
        for (int i = 0; i < numbuttons; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE;  
            c.fill = GridBagConstraints.NONE;       
            c.weightx = 0.0;                       
            gridbag.setConstraints(buttons[i], c);
            container.add(buttons[i]);
        }
    }
    
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals(tag[0])) {
            JTextField source = (JTextField)e.getSource();
            GenerateController.setmaxspeed(Integer.valueOf(source.getText()));
//            actionLabel.setText(source.getText());
        } else if(e.getActionCommand().equals(tag[1])){
            JTextField source = (JTextField)e.getSource();
            GenerateController.setinitspeed(Integer.valueOf(source.getText()));
//            actionLabel.setText(source.getText());
        } else if(e.getActionCommand().equals(tag[2])){
            JTextField source = (JTextField)e.getSource();
            GenerateController.settype(Integer.valueOf(source.getText()));
//            actionLabel.setText(source.getText());
        } else if(e.getActionCommand().equals(tag[3])){
            JTextField source = (JTextField)e.getSource();
            GenerateController.setbornpoint(Integer.valueOf(source.getText()));
        } else {
        	System.out.print("error action\n");
        }
    }
}
