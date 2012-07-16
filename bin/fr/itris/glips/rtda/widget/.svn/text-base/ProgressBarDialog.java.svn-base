package fr.itris.glips.rtda.widget;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * the dialog that will display a progress bar
 * @author ITRIS, Jordi SUC
 */
public class ProgressBarDialog extends JDialog{

    /**
     * the progress bar
     */
    private JProgressBar progressBar;
    
    /**
     * the dispose runnable
     */
    private Runnable disposeRunnable;
    
    /**
     * the label
     */
    private String label;

    /**
     * the constructor of the class
     * @param mainFrame the main frame
     * @param label the label of the action
     */
    public ProgressBarDialog(Frame mainFrame, String label){
        
        super(mainFrame, false);
        this.label=label;
        build();
    }
    
    /**
     * the constructor of the class
     * @param mainDialog the main dialog
     * @param label the label of the action
     */
    public ProgressBarDialog(JDialog mainDialog, String label){
        
        super(mainDialog, false);
        this.label=label;
        build();
    }
    
    /**
     * builds the dialog
     */
    protected void build(){
    	
        setAlwaysOnTop(true);

        //creating the progress bar
        progressBar=new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString(label);
        progressBar.setPreferredSize(new Dimension(400, 30));
        progressBar.setIndeterminate(true);
        
        //creating the panel containing the progress bar
        JPanel progressBarPanel=new JPanel();
        progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.X_AXIS));
        progressBarPanel.add(progressBar);
        progressBarPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

        //building the dialog
        getContentPane().setLayout(new BorderLayout(0, 0));

        //the listener to the close button
        final WindowAdapter windowAdapter=new WindowAdapter(){

        	@Override
            public void windowClosing(WindowEvent evt) {

        		setVisible(false);
            }
        }; 
        
        addWindowListener(windowAdapter);
        
        //the runnable used to remove the listeners
        disposeRunnable=new Runnable(){

	        public void run() {
	        	
                removeWindowListener(windowAdapter);
	        } 
        };

        getContentPane().add(progressBarPanel, BorderLayout.CENTER);
        
        //setting the bounds of the dialog
        pack();
        
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2-getWidth()/2, screenSize.height/2-getHeight()/2);
    }
    
    /**
     * disposes the dialog
     */
    public void disposeDialog() {
    	
        if(disposeRunnable!=null) {
        	
        	disposeRunnable.run();
        }
    }

    @Override
    public void setVisible(boolean b) {
    	
    	disposeDialog();
    	super.setVisible(b);
    }
}
