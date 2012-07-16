/*
 * Created on 18 févr. 2005
 */
package fr.itris.glips.rtda.colorsblinkings;

import java.awt.*;

/**
 * the class describing a predefined color
 * 
 * @author ITRIS, Jordi SUC
 */
public class PredefinedColor extends Color {
    
    /**
     * the name of the color
     */
    private String id="";
    
    /**
     * the color value string
     */
    private String colorValue="";

    /**
     * the constructor of the class
     * @param id the id of the color
     * @param shownColor the color that should be shown
     */
    public PredefinedColor(String id, Color shownColor){
        
        super(shownColor.getRed(), shownColor.getGreen(), shownColor.getBlue());
        this.id=id;
        
        colorValue=ColorsAndBlinkingsToolkit.getColorString(shownColor);
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        
        return id;
    }
    
    /**
     * @return Returns the colorValue.
     */
    public String getColorValue() {
        return colorValue;
    }
    
    /**
     * @return a string representation of this color
     */
    public String getStringRepresentation(){
        
        return id+" : rgb("+getRed()+", "+getGreen()+" ,"+getBlue()+")";
    }
}
