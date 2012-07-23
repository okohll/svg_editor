package fr.itris.glips.svgeditor.display.selection;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

/**
 * the class of the listener to the key events used for doing 
 * actions on elements on the canvas
 * @author Jordi SUC
 */
public class KeySelectionListener extends KeyAdapter {

	/**
	 * the timer
	 */
	private Timer timer=new Timer();
	
	/**
	 * the selection manager
	 */
	private Selection selection;
	
	/**
	 * the time that defines two different actions 
	 */
	private final long maxTime=100;
	
	/**
	 * the last time the key was released
	 */
	private long lastReleasedTime=0;
	
	/**
	 * the current key code
	 */
	private int currentKeyCode=0;
	
	/**
	 * the awt event listener
	 */
	private AWTEventListener awtEventListener;
	
	/**
	 * the translation thread
	 */
	private TranslationThread translationThread;
	
	/**
	 * the current task
	 */
	private TimerTask currentTask;
	
	/**
	 * the constructor of the class
	 * @param selection the selection manager
	 */
	public KeySelectionListener(Selection selection) {
		
		this.selection=selection;
		
		//creating the AWT event listener
		awtEventListener=new AWTEventListener(){
			
			public void eventDispatched(AWTEvent event) {
				
				if(event instanceof KeyEvent){
					
					KeyEvent keyEvent=(KeyEvent)event;
					
					if((keyEvent.getID()==KeyEvent.KEY_RELEASED ||
							keyEvent.getID()==KeyEvent.KEY_PRESSED) && 
							KeySelectionListener.this.selection.getSVGHandle().
								getSVGFrame().isSelected() && 
							isTranslationEvent(keyEvent.getKeyCode())){

						keyEvent.consume();
						
						if(keyEvent.getID()==KeyEvent.KEY_RELEASED){
							
							keyReleased(keyEvent);
							
						}else{
							
							keyPressed(keyEvent);
						}
					}
				}
			}
		};
		
		Toolkit.getDefaultToolkit().addAWTEventListener(
			awtEventListener, AWTEvent.KEY_EVENT_MASK);
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		
		if(currentTask!=null){
			
			currentTask.cancel();
			timer.purge();
		}
		
		if(lastReleasedTime==0 || lastReleasedTime!=evt.getWhen()){
			
			if(translationThread==null){
				
				translationThread=new TranslationThread();
				translationThread.start();
			}
			
			synchronized(this){currentKeyCode=evt.getKeyCode();}
			translationThread.startTranslating();
		}
	}

	@Override
	public void keyReleased(final KeyEvent evt) {
		
		lastReleasedTime=evt.getWhen();
		
		currentTask=new TimerTask(){
			
			@Override
			public void run() {
				
				translationThread.stopTranslating();
			}
		};
		
		timer.schedule(currentTask, maxTime);
	}
	
	/**
	 * translates the currently selected elements by the delta
	 * @param keyCode the key code
	 * @param delta the translation factor
	 */
	protected void translate(int keyCode, int delta){
		
		Point2D deltaFactors=getTranslationFactors(keyCode, delta);
		
		//translating all the selected files
		selection.translate(deltaFactors);
	}
	
	/**
	 * returns whether the provided key code denotes a UP, DOWN, LEFT or right key
	 * @param keyCode the key code
	 * @return whether the provided key code denotes a UP, DOWN, LEFT or right key
	 */
	protected boolean isTranslationEvent(int keyCode){
		
		return keyCode==KeyEvent.VK_UP|| keyCode==KeyEvent.VK_DOWN ||
			keyCode==KeyEvent.VK_LEFT || keyCode==KeyEvent.VK_RIGHT ||
				keyCode==KeyEvent.VK_KP_UP|| keyCode==KeyEvent.VK_KP_DOWN ||
					keyCode==KeyEvent.VK_KP_LEFT || keyCode==KeyEvent.VK_KP_RIGHT;
	}
	
	/**
	 * returns the translation factors corresponding to the
	 * provided key code and delta value
	 * @param keyCode the key code
	 * @param delta the delta value
	 * @return the translation factors corresponding to the
	 * provided key code and delta value
	 */
	protected Point2D getTranslationFactors(int keyCode, int delta){
		
		int x=0;
		int y=0;
		
		switch (keyCode){
			
			case KeyEvent.VK_UP :
			case KeyEvent.VK_KP_UP :
				
				y=-delta;
				break;
				
			case KeyEvent.VK_DOWN :
			case KeyEvent.VK_KP_DOWN :
				
				y=delta;
				break;
				
			case KeyEvent.VK_LEFT :
			case KeyEvent.VK_KP_LEFT :
				
				x=-delta;
				break;
				
			case KeyEvent.VK_RIGHT :
			case KeyEvent.VK_KP_RIGHT :
				
				x=delta;
				break;
		}
		
		return new Point(x, y);
	}
	
	/**
	 * disposes the listener
	 */
	public void dispose(){//TODO
		
		Toolkit.getDefaultToolkit().removeAWTEventListener(awtEventListener);
		
		timer=null;
		selection=null;
		awtEventListener=null;
		translationThread=null;
		currentTask=null;
	}
	
	/**
	 * the class used to handle the translations
	 */
	protected class TranslationThread extends Thread{
		
		/**
		 * whether the translation is running
		 */
		private boolean running=false;
		
		@Override
		public void run() {

			int currentLoopNumber=1;
			
			while(true){
				
				if(running){
					
					final int fcurrentLoopNumber=currentLoopNumber;
					
					try{
						SwingUtilities.invokeAndWait(new Runnable(){
							
							public void run() {

								translate(currentKeyCode, (int)Math.floor(fcurrentLoopNumber/2+1));
							}
						});	
					}catch (InvocationTargetException ex) {
						System.err.println("TranslationThread exception: " + ex);
					} catch (InterruptedException e) {
						System.out.println("Interrupted while translating: " + e);
						Thread.currentThread().interrupt();
					}

					currentLoopNumber++;
				
				}else{
					
					currentLoopNumber=1;
				}
				
				try{sleep(150);}catch (InterruptedException ex) {
					System.out.println("Sleep interrupted while translating: " + ex);
					Thread.currentThread().interrupt();
				}
			}
		}
		
		/**
		 * starts translating
		 */
		public synchronized void startTranslating(){
			
			this.running=true;
		}
		
		/**
		 * stops translating
		 */
		public synchronized void stopTranslating(){
			
			this.running=false;
		}
	}
}
