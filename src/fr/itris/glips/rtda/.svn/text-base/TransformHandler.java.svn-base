/*
 * Created on 25 mars 2005
 */
package fr.itris.glips.rtda;

import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.*;

import fr.itris.glips.rtda.animaction.*;

import java.awt.geom.*;

/**
 * the handler of the transforms
 * 
 *@author ITRIS, Jordi SUC
 */
public class TransformHandler {

    /**
     * the map associating an element to a linked list of data changed listeners
     */
    private Map<Element, List<DataChangedListener>> elementToDataChangedListeners=
    	new ConcurrentHashMap<Element, List<DataChangedListener>>();
    
    /**
     * the constructor of the class
     * @param animationsHandler the animations handler
     */
    public TransformHandler(AnimationsHandler animationsHandler){}
    
    /**
     * adding a data changed listener
     * @param listener a data changed listener
     */
    public void addDataChangedListener(DataChangedListener listener){
        
        if(listener!=null){
            
            Element parentElement=listener.getParentElement();
            
            //adding the listener to the map associating an element to 
            //the list of the data changed listeners related to it
            List<DataChangedListener> listeners=
            	elementToDataChangedListeners.get(parentElement);
            
            if(listeners==null){
                
                listeners=new CopyOnWriteArrayList<DataChangedListener>();
                elementToDataChangedListeners.put(parentElement, listeners);
            }
            
            listeners.add(listener);
        }
    }
    
    /**
     * removing a data changed listener
     * @param listener a data changed listener
     */
    public void removeDataChangedListener(DataChangedListener listener){
        
        if(listener!=null){
            
            Element parentElement=listener.getParentElement();
            
            //removing the listener from the map associating an element to the list of the data changed listeners related to it
            List<DataChangedListener> listeners=elementToDataChangedListeners.get(parentElement);
            
            if(listeners!=null){

                listeners.remove(listener);
                
                //if the size of the lit of the listeners is empty, the entry is removed
                if(listeners.size()==0){
                    
                    elementToDataChangedListeners.remove(parentElement);
                }
            }
        }
    }
    
    /**
     * computes and returns the affine transform of an element that is the concatenation 
     * of the transforms of its listeners
     * @param element an element
     * @return the new transform for the element
     */
    public AffineTransform computeTransform(Element element){
    	
    	//the transform
    	AffineTransform transform=new AffineTransform();
    	
       	List<DataChangedListener> dataChangedListeners=
       		elementToDataChangedListeners.get(element);
        
        if(dataChangedListeners!=null && dataChangedListeners.size()>0){
            
            //setting the initial value for the transform
            DataChangedListener firstListener=dataChangedListeners.get(0);
            
            if(firstListener!=null){
                
            	transform.preConcatenate(
            			firstListener.getInitialAffineTransform());
            }

            //computes the affine transform
            for(DataChangedListener dataChangedListener : dataChangedListeners){

                if(dataChangedListener!=null){

                    transform.preConcatenate(dataChangedListener.getAffineTransform());
                }
            }
        }
        
        return transform;
    }
}
