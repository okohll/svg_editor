package fr.itris.glips.library.monitor;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.library.widgets.*;

/**
 * the class used to monitor the progress of actions
 * @author Jordi SUC
 */
public class Monitor {
	
	/**
	 * the timer used to schedule the monitor dialog display
	 */
	protected static final java.util.Timer timer=new java.util.Timer();

	/**
	 * the wait dialog
	 */
	protected WaitDialog waitDialog;
	
	/**
	 * the component relatively to which the dialog will be shown
	 */
	protected JComponent relativeComponent;
	
	/**
	 * whether the monitor is cancelled
	 */
	protected boolean isCancelled=false;
	
	/**
	 * the current message
	 */
	protected String currentMessage="";
	
	/**
	 * the min and max for the progress value
	 */
	protected int min=0, max=0, value=0;
	
	/**
	 * whether the monitor has been stopped
	 */
	private boolean isStopped=false;
	
	/**
	 * whether the monitor is in error
	 */
	private boolean isInError=false;
	
	/**
	 * the constructor of the class
	 * @param parent the parent component used to 
	 * display the progress dialog
	 * @param relativeComponent the component relatively to 
	 * which the dialog will be shown
	 * @param min the min value of the progress
	 * @param max the max value of the progress
	 */
	public Monitor(Component parent, 
			JComponent relativeComponent, int min, int max){
		
		this.relativeComponent=relativeComponent;
		
		//the min and max values for the progress monitor
		this.min=min;
		this.max=max;
		
		//creating the cancel listener
		ActionListener cancelListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				synchronized(Monitor.this){
					
					isCancelled=true;
				}
				
				stop();
			}
		};
		
		//creating the wait dialog
		if(parent instanceof Frame){
			
			waitDialog=new WaitDialog((Frame)parent, cancelListener);
			
		}else if(parent instanceof JDialog){
			
			waitDialog=new WaitDialog((JDialog)parent, cancelListener);
		}
	}
	
	/**
	 * disposes this monitor
	 */
	public void dispose() {

		enqueue(new Runnable(){
			
			public void run() {
				
				waitDialog.disposeDialog();
			}
		});
	}
	
	/**
	 * initializes the monitor
	 */
	protected void initialize(){}
	
	/**
	 * sets the progress message
	 * @param message a message
	 */
	public void setProgressMessage(String message){
		
		this.currentMessage=message;
		
		enqueue(new Runnable(){
			
			public void run() {
				
				waitDialog.setProgressMessage(currentMessage, min, max, value);
			}
		});
	}
	
	/**
	 * sets the error message
	 * @param message the error message
	 */
	public void setErrorMessage(final String message){
		
		synchronized (this) {
			isInError=true;
		}
		
		enqueue(new Runnable(){
			
			public void run() {
				
				waitDialog.setInError();
				waitDialog.setErrorMessage(message);
			}
		});
	}
	
	/**
	 * sets whether the progress dialog should be in an indeterminate state
	 * @param indeterminate whether the progress dialog 
	 * should be in an indeterminate state
	 */
	public void setIndeterminate(final boolean indeterminate){
		
		enqueue(new Runnable(){
			
			public void run() {
				
				waitDialog.setIndeterminate(indeterminate);
			}
		});
	}
	
	/**
	 * sets the current progress value
	 * @param value the progress value
	 */
	public void setProgress(final int value){
		
		this.value=value;
		
		enqueue(new Runnable(){
			
			public void run() {

				waitDialog.setProgressMessage(
						currentMessage, value, min, max);
			}
		});
	}
	
	/**
	 * increments the progress value
	 */
	public void incrementProgressValue(){
		
		value++;
		
		enqueue(new Runnable(){
			
			public void run() {
				
				waitDialog.setProgressMessage(
						currentMessage, Monitor.this.value, min, max);
			}
		});
	}
	
	/**
	 * @return whether the action monitored 
	 * by this object has been cancelled
	 */
	public boolean isCancelled(){
		
		return isCancelled;
	}
	
	/**
	 * @return whether the monitor has been stopped
	 */
	public boolean isStopped() {
		return isStopped;
	}
	
	/**
	 * sets the component relatively to which the dialog should be shown
	 * @param relativeComponent a component
	 */
	public void setRelativeComponent(JComponent relativeComponent) {
		
		this.relativeComponent=relativeComponent;
	}
	
	/**
	 * starts the monitor
	 */
	public void start(){
		
		TimerTask task=new TimerTask(){
			
			@Override
			public void run() {

				if(! isStopped){
					
					enqueue(new Runnable(){
						
						public void run() {

							if(! isStopped){
								
								waitDialog.showDialog(relativeComponent, isInError);
							}
						}
					});
				}
			}
		};
		
		timer.schedule(task, 1000);
	}
	
	/**
	 * stops the monitor
	 */
	public void stop(){
		
		synchronized(this) {isStopped=true;}

		enqueue(new Runnable(){
			
			public void run() {

				waitDialog.setVisible(false);
			}
		});
		
		dispose();
	}
	
	/**
	 * executes the provided runnable into the awt thread
	 * @param runnable a runnable
	 */
	protected void enqueue(Runnable runnable){
		
		if(SwingUtilities.isEventDispatchThread()){
			
			runnable.run();
			
		}else{
			
				SwingUtilities.invokeLater(runnable);
		}
	}
}
