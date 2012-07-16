/*
 * Created on 18 févr. 2005
 */
package fr.itris.glips.rtda.colorsblinkings;

import java.awt.*;

/**
 * the class describing a blinking color
 * 
 * @author ITRIS, Jordi SUC
 */
public class BlinkingColor extends Color {
    
    /**
     * the id of the color
     */
    private String id="";
    
    /**
     * the second color for the blinking
     */
    private Color color2;
    
    /**
     * the id of a blinking
     */
    private String blinkingId="";
    
    /**
     * the color value string for the first color
     */
    private String colorValue="";
    
    /**
     * the color value string for the second color
     */
    private String colorValue2="";

    /**
     * the constructor of the class
     * @param id the id of the color
     * @param shownColor the color that should be shown if it fills a shape
     * @param color2 the second color for the blinking
     * @param blinkingId the id of a blinking
     */
    public BlinkingColor(String id, Color shownColor, Color color2, String blinkingId){
        
        super(shownColor.getRed(), shownColor.getGreen(), shownColor.getBlue());
        this.id=id;
        this.color2=color2;
        this.blinkingId=blinkingId;
        
        colorValue=ColorsAndBlinkingsToolkit.getColorString(shownColor);
        colorValue2=ColorsAndBlinkingsToolkit.getColorString(color2);
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        
        return id;
    }

    /**
     * @return Returns the blinkingName.
     */
    public String getBlinkingId() {
        return blinkingId;
    }
    
    /**
     * @return Returns the color2.
     */
    public Color getColor2() {
        return color2;
    }

    /**
     * @return Returns the colorValue.
     */
    public String getColorValue() {
        return colorValue;
    }

    /**
     * @return Returns the colorValue2.
     */
    public String getColorValue2() {
        return colorValue2;
    }
    
    /**
     * @return a string representation of this color
     */
    public String getStringRepresentation(){
        
        return id+" : rgb("+getRed()+", "+getGreen()+" ,"+getBlue()+") & "
        			+"rgb("+color2.getRed()+", "+color2.getGreen()+" ,"+color2.getBlue()+")";
    }
}
