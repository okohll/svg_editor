/*
 * Created on 21 févr. 2005
 */
package fr.itris.glips.rtda.colorsblinkings;

import fr.itris.glips.rtda.*;

/**
 * the class of a blinking
 * 
 * @author ITRIS, Jordi SUC
 */
public class Blinking {
    
    /**
     * the constant describing the min value of the blinking
     */
    public final static int VALUE_DOWN=0;
    
    /**
     * the constant describing the min value of the blinking
     */
    public final static int VALUE_UP=1;

    /**
     * the id of the blinking
     */
    private String id="";
    
    /**
     * the first time
     */
    private double t1=0;
    
    /**
     * the second time
     */
    private double t2=0;
    
    /**
     * the period
     */
    private double period=1;
    
    /**
     * the current value for the blinking
     */
    private double currentValue=VALUE_DOWN;

    /**
     * the constructor of the class
     * @param id the id of the blinking
     * @param t1 the first time
     * @param t2 the second time
     * @param period the period
     */
    public Blinking(String id, double t1, double t2, double period){
        
        this.id=id;
        this.t1=t1;
        this.t2=t2;
        this.period=period;
    }
    
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return Returns the period.
     */
    public double getPeriod() {
        return period;
    }
    
    /**
     * @return Returns the t1.
     */
    public double getT1() {
        return t1;
    }
    
    /**
     * @return Returns the t2.
     */
    public double getT2() {
        return t2;
    }
    
    /**
     * computes the current value of the blinking
     * @param time the current time
     */
    public void computeCurrentValue(double time){
        
        int value=VALUE_DOWN;
        
        //gets the value of the function
        double functionValue=AnimationsToolkit.square(time, t1, t2, period);
        
        //computes the value that will be returned
        if(functionValue<0){
            
            value=VALUE_DOWN;
            
        }else{
            
            value=VALUE_UP;
        }
        
        synchronized(this){
            
            currentValue=value;
        }
    }

    /**
     * @return Returns the currentValue.
     */
    public double getCurrentValue() {
        return currentValue;
    }
}
