/*
 * Created on 15 févr. 2005
 */
package fr.itris.glips.rtda;

import java.util.*;
import java.util.concurrent.*;

import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the object that will handle the computation 
 * of the funcion values and the dispatch of these values
 * @author ITRIS, Jordi SUC
 */
public class FunctionsHandler {
    
    /**
     * the handler of the animations
     */
    protected AnimationsHandler animationsHandler;
    
    /**
     * the map associating a svg picture to the set of listeners it is linked with
     */
    private Map<SVGPicture, List<DataChangedListener>> functionTagListeners=
    	new ConcurrentHashMap<SVGPicture, List<DataChangedListener>>();
    
    /**
     * the function thread
     */
    private Thread functionThread;
    
    /**
     * the constructor of the class
     * @param animationsHandler the handler of the animations
     */
    public FunctionsHandler(AnimationsHandler animationsHandler){
        
        this.animationsHandler=animationsHandler;
        
        //the thread refreshing the values of the tags that are described by a function
        functionThread=new Thread(){

        	@Override
            public void run(){

	            //the time stamp for the computation of the value of a function
	            long timeStamp=-1;
	            long timer=50;
	            double time=0;

	            Map<String, Object> map=null;
                Runnable runnable=null, animationsRunnable=null;
                //the list of the runnables that will be executed in each runnable queue
                Collection<Runnable> runnablesList=null;
                
                //the set of runnables that are to be executed in each runnable queue
                final Set<Runnable> animationsRunnableSet=
                	new CopyOnWriteArraySet<Runnable>();
	
	            while(true){

    	            if(! getAnimationsHandler().isPaused() && ! getAnimationsHandler().isStopped()){

    	                if(animationsRunnableSet.size()>0){

    	                    try{sleep(15);}catch(Exception ex){}
    	                    
    	                }else{

    	                    //computing the number of seconds since the animations were started (initially or after they were stopped)
    	                    time=0;
    	                    
    	                    if(timeStamp!=-1){
    	                        
    	                        time=((double)(System.currentTimeMillis()-timeStamp))/1000;
    	                        
    	                    }else{
    	                        
    	                        timeStamp=System.currentTimeMillis();
    	                    }

    	                    //for each svg picture, computes the values of each function tag in each listener
    	                    for(SVGPicture picture : functionTagListeners.keySet()){

    	                        if(picture!=null){
    	                            
    	                            //retrieving the set of the listeners
    	                            final List<DataChangedListener> listeners=functionTagListeners.get(picture);
    	                            
    	                            if(listeners!=null){
    	                                
    	                                runnablesList=new LinkedList<Runnable>();
    	                                
    	                                //computes the new values for the tags and adds the returned runnable to the list of runnables
    	                                for(DataChangedListener listener : listeners){

                                            if(listener!=null){
                                                
                                                map=listener.computeFunctionValues(time);

                                                if(map!=null){
                                                    
                                                    runnable=listener.dataChanged(new DataEvent(map, null));
                                                    
                                                    if(runnable!=null){
                                                        
                                                        runnablesList.add(runnable);
                                                    }
                                                }
                                            }
                                        }

                                        final Collection<Runnable> frunnablesList=runnablesList;

    	                                //the runnable into which all the runnables from the runnable list will be modified so that
    	                                //the parent element is modified
    	                                animationsRunnable=new Runnable(){

    	                                    public void run() {
    	                                        
    	                                        animationsRunnableSet.remove(this);

    	                                        for(Runnable run : frunnablesList){

    	                                            if(run!=null){
    	                                                
    	                                            	run.run();
    	                                            }
    	                                        }
    	                                    } 
    	                                };

    	                                animationsRunnableSet.add(animationsRunnable);
    	                                picture.enqueue(animationsRunnable, true);
    	                            }
    	                        }
    	                    }
    	                    
    	                    runnable=null;
    	                    animationsRunnable=null;
        	                runnablesList=null;
        		            map=null;

    		                try{sleep(timer);}catch(Exception ex){}
    	                }

    	            }else{
    	                
    	                if(getAnimationsHandler().isStopped()){
    	                    
        	                timeStamp=-1;
        	                animationsRunnableSet.clear();
        	                
        	                try{sleep(500);}catch(Exception ex){}
        	                
        	                
    	                }else {
    	                	
        	                try{sleep(timer);}catch(Exception ex){}
    	                }
    	            }
                }
            }
        };
        
        functionThread.start();
    }

    /**
     * @return Returns the animationsHandler.
     */
    public AnimationsHandler getAnimationsHandler(){
        return animationsHandler;
    }
    
    /**
     * adds a data changed listener
     * @param listener a data changed listener
     */
    public void addFunctionTagListener(DataChangedListener listener){
        
        if(listener!=null){
            
            //getting the set linked with the runnable queue of the given listener
            List<DataChangedListener> list=
            	functionTagListeners.get(listener.getPicture());
            
            if(list==null){
                
            	list=new CopyOnWriteArrayList<DataChangedListener>();
                functionTagListeners.put(listener.getPicture(), list);
            }
            
            //adding the listener to the set
            if(! list.contains(listener)){
            	
                list.add(listener);
            }
        }
    }
    
    /**
     * removes a data changed listener
     * @param listener a data changed listener
     */
    public void removeFunctionTagListener(DataChangedListener listener){
        
        if(listener!=null){
            
            //getting the list linked with the runnable queue of the given listener
            List<DataChangedListener> list=
            	functionTagListeners.get(listener.getPicture());
            
            if(list!=null){
                
                //removes the listener from the list
            	list.remove(listener);
                
                if(list.size()==0){
                    
                    functionTagListeners.remove(listener.getPicture());
                }
            }
        }
    }
    
}
