/*
 * Created on 24 févr. 2005
 */
package fr.itris.glips.rtda;

import java.util.*;
import java.util.concurrent.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the object handling the computation of the blinking values and the dispatch of them
 * 
 * @author ITRIS, Jordi SUC
 */
public class BlinkingsHandler {

    /**
     * the handler of the animations
     */
    protected AnimationsHandler animationsHandler;
    
    /**
     * the map associating a svg picture to the set of 
     * blinking value modifiers it is linked with
     */
    private Map<SVGPicture, Set<BlinkingValueModifier>> blinkingValueModifiers=
    	new ConcurrentHashMap<SVGPicture, Set<BlinkingValueModifier>>();
    
    /**
     * the map associating a blinking object to the counter 
     * specifying the number of modifiers that have registered this blinking
     */
    private Map<Blinking, Integer> blinkings=
    	new ConcurrentHashMap<Blinking, Integer>();
    
    /**
     * the blinking thread
     */
    private Thread blinkingThread;
    
    /**
     * the constructor of the class
     * @param animationsHandler the handler of the animations
     */
    public BlinkingsHandler(AnimationsHandler animationsHandler){
        
        this.animationsHandler=animationsHandler;
        
        //the thread refreshing the values of the blinkings
        blinkingThread=new Thread(){

        	@Override
            public void run(){

	            //the time stamp for the computation of the value of a function
	            long timeStamp=-1;
	            long timer=50;
	            double time=0;

                //the list of the runnables that will be executed in each runnable queue
                Collection<Runnable> runnablesList=null;
                Runnable runnable=null, blinkingRunnable=null;
                
                //the set of runnables that are to be executed in each runnable queue
                final Set<Runnable> blinkingRunnableSet=new CopyOnWriteArraySet<Runnable>();
	
	            while(true){

    	            if(! getAnimationsHandler().isPaused() && ! getAnimationsHandler().isStopped()){

    	                if(blinkingRunnableSet.size()>0){

    	                    try{sleep(15);}catch(Exception ex){}
    	                    
    	                }else{

    	                    //computing the number of seconds since the animations 
    	                	//were started (initially or after they were stopped)
    	                    time=0;
    	                    
    	                    if(timeStamp!=-1){
    	                        
    	                        time=((double)(System.currentTimeMillis()-timeStamp))/1000;
    	                        
    	                    }else{
    	                        
    	                        timeStamp=System.currentTimeMillis();
    	                    }

    	                    //computes the new values for the blinkings
    	                    for(Blinking blinking : blinkings.keySet()){

    	                        if(blinking!=null){
    	                            
    	                            blinking.computeCurrentValue(time);
    	                        }
    	                    }

    	                    //for svg picture, computes the values of each function tag in each listener
	   	                    for(SVGPicture picture : blinkingValueModifiers.keySet()){

    	                        if(picture!=null){
    	                            
    	                            //retrieving the set of the listeners
    	                            final Set<BlinkingValueModifier> fset=blinkingValueModifiers.get(picture);
    	                            
    	                            if(fset!=null){
    	                                
    	                                runnablesList=new LinkedList<Runnable>();
    	                                
    	                                //computes the new values for the tags and adds the returned runnable to the list of runnables
                                        for(BlinkingValueModifier modifier : fset){

                                            if(modifier!=null && modifier.isActive() && 
                                            		modifier.getPicture().getCanvas()!=null){
                                                
                                                runnable=modifier.applyBlinkingChanges();
                                                
                                                if(runnable!=null){
                                                    
                                                    runnablesList.add(runnable);
                                                }
                                            }
                                        }

                                        final Collection<Runnable> frunnablesList=runnablesList;

    	                                //the runnable into which all the runnables from the runnable list will be modified so that
    	                                //the parent element is modified
    	                                blinkingRunnable=new Runnable(){

    	                                    public void run() {
    	                                        
    	                                        blinkingRunnableSet.remove(this);

    	                                        for(Runnable run : frunnablesList){
  
    	                                            if(run!=null){
    	                                                
    	                                                run.run();
    	                                            }
    	                                        }
    	                                    } 
    	                                };

    	                                blinkingRunnableSet.add(blinkingRunnable);
    	                                
    	                                //adding the runnable to the runnable queue
    	                                try{picture.enqueue(blinkingRunnable, true);}catch (Exception ex){}
    	                            }
    	                        }
    	                    }

	   	                    runnable=null;
        	                blinkingRunnable=null;
	   	                    
        	                if(runnablesList!=null){
            	                
        	                	runnablesList.clear();
        	                }
	   	                    
    		                try{sleep(timer);}catch(Exception ex){}
    	                }

    	            }else{
    	                    
    	                if(getAnimationsHandler().isStopped()){
    	                    
        	                timeStamp=-1;
        	                blinkingRunnableSet.clear();
        	                
        	                try{sleep(500);}catch(Exception ex){}
        	                
    	                }else {
    	                	
        	                try{sleep(timer);}catch(Exception ex){}
    	                }
    	            }
                }
            }
        };
        
        blinkingThread.start();
    }

    /**
     * @return Returns the animationsHandler.
     */
    public AnimationsHandler getAnimationsHandler(){
        return animationsHandler;
    }
    
    /**
     * adds a blinking value modifier
     * @param modifier a blinking value modifier
     */
    public void addBlinkingValueModifier(BlinkingValueModifier modifier){

        if(modifier!=null){
            
            //registering the blinking linked with the modifier
            if(blinkings.containsKey(modifier.getBlinking())){
                
                //modifies the counter of the blinking in the map
                Integer counter=blinkings.get(modifier.getBlinking());
                
                //incrementing the counter
                counter=new Integer(counter.intValue()+1);
                
                //setting the new counter for the blinking
                blinkings.put(modifier.getBlinking(), counter);
                
            }else{
                
                //if the blinking has not been registered yet, it is added 
                blinkings.put(modifier.getBlinking(), new Integer(1));
            }
            
            //getting the set linked with the runnable queue of the given modifier
            Set<BlinkingValueModifier> set=blinkingValueModifiers.get(
            		modifier.getPicture());
            
            if(set==null){
                
                set=new CopyOnWriteArraySet<BlinkingValueModifier>();
                blinkingValueModifiers.put(modifier.getPicture(), set);
            }
            
            //adding the modifier to the set
            set.add(modifier);
        }
    }
    
    /**
     * adds the list of the blinking value modifiers contained in the map
     * @param blinkingValueModifiersList the list of the blinking value modifiers
     */
    public void addBlinkingValueModifiers(Collection<BlinkingValueModifier> blinkingValueModifiersList){
    	
        if(blinkingValueModifiersList!=null){
            
            for(BlinkingValueModifier modifier : blinkingValueModifiersList){

                if(modifier!=null){
                    
                    addBlinkingValueModifier(modifier);
                }
            }
        }
    }
    
    /**
     * removes a blinking value modifier
     * @param modifier a blinking value modifier
     */
    public void removeBlinkingValueModifier(BlinkingValueModifier modifier){
        
        if(modifier!=null){
            
            //unregistering the blinking linked with the modifier
            if(blinkings.containsKey(modifier.getBlinking())){
                
                //modifies the counter of the blinking in the map
                Integer counter=blinkings.get(modifier.getBlinking());
                
                //decrementing the counter
                counter=new Integer(counter.intValue()-1);
                
                //if the counter equals 0, the blinking object is removed
                if(counter.intValue()==0){
                    
                    blinkings.remove(modifier.getBlinking());
                    
                }else{

                    //setting the new counter for the blinking
                    blinkings.put(modifier.getBlinking(), counter);
                }
            }
            
            //getting the set linked with the runnable queue of the given modifier
            Set<BlinkingValueModifier> set=blinkingValueModifiers.get(modifier.getPicture());
            
            if(set!=null){
                
                //removes the listener from the set
                set.remove(modifier);
                
                if(set.size()==0){
                    
                    blinkingValueModifiers.remove(modifier.getPicture());
                }
            }
        }
    }
    
    /**
     * removes the list of the blinking value modifiers contained in the map
     * @param blinkingValueModifiersList the list of the blinking value modifiers
     */
    public void removeBlinkingValueModifiers(
    		Collection<BlinkingValueModifier> blinkingValueModifiersList){

        if(blinkingValueModifiersList!=null){
            
            for(BlinkingValueModifier modifier : blinkingValueModifiersList){
            	
                if(modifier!=null){
                    
                    removeBlinkingValueModifier(modifier);
                }
            }
        }
    }
}
