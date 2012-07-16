/*
 * Created on 3 mars 2005
 */
package fr.itris.glips.rtda;

/**
 * the cl	ass for executing code after a given time
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class TimerRunnable {

	/**
	 * the time after which the run method should be executed
	 */
	private double timerTime;
	
	/**
	 * the constructor of the class
	 * @param timer the timer in milliseconds
	 */
	public TimerRunnable(double timer){
		
		this.timerTime=timer+System.currentTimeMillis();
	}
	
	/**
	 * the method that should be run when the current time is greater than the timer
	 */
	public abstract void run();

	/**
	 * @return Returns the timerTime.
	 */
	public double getTimerTime() {
		return timerTime;
	}
}
