/*
 * Created on 22 févr. 2005
 *
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warrFanty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.rtdaeditor.colorchooser;

import java.awt.*;
import java.awt.datatransfer.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.colorchooser.*;
import fr.itris.glips.svgeditor.display.handle.*;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import org.w3c.dom.*;

/**
 * the class of the color chooser that handles specific colors for the rtda animations
 * 
 * @author ITRIS, Jordi SUC
 */
public class RtdaColorChooserModule extends ColorChooser implements Module{

    /**
     * the colors and blinkings toolkit
     */
    private ColorsAndBlinkingsToolkit colorsAndBlinkingsToolkit;
	
    /**
     * the set containing the handles from which colors and blinking have been retrieved
     */
    private Set<SVGHandle> usedHandles=new HashSet<SVGHandle>();
    
    /**
     * the flavor of a predefined color
     */
    private DataFlavor predefinedColorFlavor=null;
    
    /**
     * the flavor of a blinking color
     */
    private DataFlavor blinkingColorFlavor=null;

    /**
     * the constructor of the class
     * @param editor the editor
     */
    public RtdaColorChooserModule(Editor editor){
        
        super(editor);
        
        //sets the new color chooser
        Editor.setColorChooser(this);
        
        //creating the colors and blinkings toolkit
        colorsAndBlinkingsToolkit=new ColorsAndBlinkingsToolkit(null);
        
        //a listener that listens to the changes of the svg handles
        HandlesListener svgHandleListener=new HandlesListener(){

        	@Override
        	public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

                Set<SVGHandle> storedHandles=new HashSet<SVGHandle>(usedHandles);
                
                //for each stored handle, checks if it is in the handles collection of the handles manager,
                //if it is not, the handle is removed
                for(SVGHandle handle : storedHandles){

                    if(handle!=null && ! handles.contains(handle)){
                        
                        storedHandles.remove(handle);
                    }
                }
            }
        };
        
        //adds the SVGFrame change listener
        editor.getHandlesManager().addHandlesListener(svgHandleListener);
        
        //adds the predefined color chooser panel
        PredefinedColorChooserPanel predefinedColorChooserPanel=new PredefinedColorChooserPanel(this);
        addChooserPanel(predefinedColorChooserPanel);
        
        //adds the blinking color chooser panel
        BlinkingColorChooserPanel blinkingColorChooserPanel=new BlinkingColorChooserPanel(this);
        addChooserPanel(blinkingColorChooserPanel);

        //creating the color flavors
        try{
            predefinedColorFlavor=new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType+";class=fr.itris.glips.rtda.colorsblinkings.PredefinedColor");
            blinkingColorFlavor=new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType+";class=fr.itris.glips.rtda.colorsblinkings.BlinkingColor");
        }catch (Exception ex){predefinedColorFlavor=DataFlavor.stringFlavor; blinkingColorFlavor=DataFlavor.stringFlavor;}
    }
    
    @Override
    public Color getColor(SVGHandle handle, String colorString){

        Color color=null;
        
        if(handle!=null && colorString!=null && ! colorString.equals("")){

            //tests if the color string represents a rtda color
            int pos=colorString.indexOf("/*");
            
            //the color is potentially a rtda color
            if(pos!=-1){
                
                String colorId=colorString.substring(pos+2, colorString.length()-2);
                
                if(colorId!=null && ! colorId.equals("")){
                    
                    //gets the rtda color that has the found id//
                    
                    //the list of the predefined and blinking colors
                	File projectFile=handle.getScrollPane().getSVGCanvas().getProjectFile();
                    Collection<Object> predefinedColors=new LinkedList<Object>(
                    		colorsAndBlinkingsToolkit.getPredefinedColorsMap(projectFile).values());
                    Collection<Object> blinkingColors=new LinkedList<Object>(
                    		colorsAndBlinkingsToolkit.getBlinkingColorsMap(projectFile).values());

                    PredefinedColor predefinedColor=null;
                    BlinkingColor blinkingColor=null;
                    
                    //checks if the id corresponds to a predefined color
                    for(Object object : predefinedColors){
                        
                        predefinedColor=(PredefinedColor)object;
                        
                        if(predefinedColor!=null && predefinedColor.getId().equals(colorId)){
                            
                            color=predefinedColor;
                            break;
                        }
                    }
                    
                    //checks if the id corresponds to a blinking color
                    if(color==null){
                        
                        for(Object object : blinkingColors){
                            
                            blinkingColor=(BlinkingColor)object;
                            
                            if(blinkingColor!=null && blinkingColor.getId().equals(colorId)){
                                
                                color=blinkingColor;
                                break;
                            }
                        }
                    }
                }
            }
            
            //the color does not represent a rtda color
            if(color==null){
                
                //calls the super method
                color=super.getColor(handle, colorString);
            }
            
        }else if(handle==null) {
        	
            //calls the super method
            color=super.getColor(handle, colorString);
        }
        
        return color;
    }
    
    @Override
    public String getColorString(Color color){

        String colorString="";

        if(color instanceof PredefinedColor){
            
            colorString=((PredefinedColor)color).getColorValue()+"/*"+((PredefinedColor)color).getId()+"*/";
            
        }else if(color instanceof BlinkingColor){
            
            colorString=((BlinkingColor)color).getColorValue()+"/*"+((BlinkingColor)color).getId()+"*/";
            
        }else{
            
            colorString=super.getColorString(color);
        }

        return colorString;
    }
    
    @Override
    public void checkColorString(SVGHandle handle, Element element){

        if(handle!=null && element!=null){
                
            //getting all the attributes of the element
            NamedNodeMap attributes=element.getAttributes();
            
            if(attributes!=null && attributes.getLength()>0){
                
                String value="", consistentValue="";
                Node node=null;
 
                //for each attribute of the node
                for(int i=0; i<attributes.getLength(); i++){
                    
                    node=attributes.item(i);
                    value=node.getNodeValue();
                    
                    if(value!=null && ! value.equals("") && value.indexOf("/*")!=-1){
                        
                        if(node.getNodeName().equals("style")){
                            
                            //handling the style properties//
                            consistentValue=checkStyleValueColor(handle, value);
   
                        }else{
                            
                            //handling the attributes values
                            consistentValue=checkAttributeValueColor(handle, value);
                        }

                        //if one of the color values found in the element attribute are not consistent with the 
                        //current color values, the value of the attribute is modified
                        if(consistentValue!=null){
                            
                            node.setNodeValue(consistentValue);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * checks the consistency of the colors in the given string
     * @param handle a svg handle 
     * @param value a string
     * @return a string containing color values that are consistent
     */
    protected String checkAttributeValueColor(SVGHandle handle, String value){
        
        String returnValue=null;
        
        if(handle!=null && value!=null && ! value.equals("")){
            
            //the map containing all the rtda color
            Map<String, Object> idToColor=
            	colorsAndBlinkingsToolkit.getAllColorsMap(handle.getScrollPane().getSVGCanvas().getProjectFile());
            
            if(idToColor!=null){
                
                String attValue=new String(value);
                String foundId="", consistentColorValue="";
                Color color=null;
                
                //checks if the current part of the value of the attribute contains a comment
                int pos=attValue.indexOf("/*");

                if(pos!=-1){
                    
                    //as the value contains a comment, the consistency of the value of the color is checked//
                    
                    //getting the id of the specified color
                    foundId=attValue.substring(pos+2, attValue.length()-2);
                    
                    if(foundId!=null && ! foundId.equals("")){
                        
                        //gets the color corresponding to this id
                        color=(Color)idToColor.get(foundId);
                        
                        if(color!=null){
                            
                            //retrieving the current value for the color
                            if(color instanceof PredefinedColor){

                                consistentColorValue=((PredefinedColor)color).getColorValue();
                                
                            }else if(color instanceof BlinkingColor){
                                
                                consistentColorValue=((BlinkingColor)color).getColorValue();
                            }
                            
                            if(attValue.indexOf(consistentColorValue)==-1){
                                
                                returnValue=consistentColorValue+"/*"+foundId+"*/";
                            }
                        }
                    }
                }
            }
        }
        
        return returnValue;
    }
    
    /**
     * checks the consistency of the colors in the style attribute value
     * @param handle a handle 
     * @param styleValue a string
     * @return a string containing color values that are consistent
     */
    protected String checkStyleValueColor(SVGHandle handle, String styleValue){
        
        String returnValue=null;
        
        if(handle!=null && styleValue!=null && ! styleValue.equals("")){
            
            //splitting the property value string into an array of strings
            String[] propertyValues=styleValue.split(";");
            StringBuffer buffer=new StringBuffer("");
            
            //whether the property value has been changed or not
            boolean hasChanged=false;

            if(propertyValues!=null){
                
                String name="", value="", consistentValue="";
                int pos=-1;
                
                for(int i=0; i<propertyValues.length; i++){
                    
                    if(propertyValues[i]!=null){
                        
                        //getting the position of the limit between the name and the value of the property
                        pos=propertyValues[i].indexOf(":");
                        
                        if(pos!=-1){
                            
                            //retrieving the name and the value of the property
                            name=propertyValues[i].substring(0, pos);
                            value=propertyValues[i].substring(pos+1, propertyValues[i].length());
                            
                            if(name!=null && value!=null && ! name.equals("") && ! value.equals("")){
                                
                                //checking if the color values in the string are consistent, if not, they are modified
                                consistentValue=checkAttributeValueColor(handle, value);

                                if(consistentValue!=null){
                                    
                                    //if the property value has been modified, the consistent value is used
                                    hasChanged=true;
                                    buffer.append(name);
                                    buffer.append(":");
                                    buffer.append(consistentValue);
                                    buffer.append(";");
                                    
                                }else{
                                    
                                    //otherwise the first value is used
                                    buffer.append(propertyValues[i]);
                                    buffer.append(";");
                                }
                            }
                        }
                    }
                }
            }
            
            if(hasChanged){
                
                //if the property has been modified, the returned values is set
                returnValue=buffer.toString();
            }
        }
        
        return returnValue;
    }
    
    @Override
    public DataFlavor getColorFlavor(Color color){
        
        DataFlavor flavor=null;
        
        if(color!=null){
            
            if(color instanceof PredefinedColor){
                
                flavor=predefinedColorFlavor;
                
            }else if(color instanceof BlinkingColor){
                
                flavor=blinkingColorFlavor;
                
            }else{
                
                flavor=super.getColorFlavor(color);
            }
        }
        
        return flavor;
    }
    
    @Override
    public boolean isColorDataFlavor(DataFlavor flavor){
        
        boolean isColorDataFlavor=false;
        
        if(flavor!=null){
            
            isColorDataFlavor=	(flavor.isMimeTypeEqual(predefinedColorFlavor) || 
                    						flavor.isMimeTypeEqual(blinkingColorFlavor) || 
                    						super.isColorDataFlavor(flavor));
        }
        
        return isColorDataFlavor;
    }
    
    @Override
    public Collection<DataFlavor> getColorDataFlavors(){
        
        LinkedList<DataFlavor> dataFlavors=new LinkedList<DataFlavor>();
        
        dataFlavors.add(predefinedColorFlavor);
        dataFlavors.add(blinkingColorFlavor);
        dataFlavors.addAll(super.getColorDataFlavors());
        
        return dataFlavors;
    }
    
    @Override
    public File getProjectFile(String uri) {
    	
    	File projectFile=null;
    	
    	try {
    		projectFile=AnimationsToolkit.getProjectFile(new URI(uri), true);
    	}catch (Exception ex) {}
    	
    	return projectFile;
    }
   
	/**
	 * @return the colors and blinkings toolkit
	 */
	public ColorsAndBlinkingsToolkit getColorsAndBlinkingsToolkit() {
		return colorsAndBlinkingsToolkit;
	}

	/**
	 * @see fr.itris.glips.svgeditor.Module#getMenuItems()
	 */
	public HashMap<String, JMenuItem> getMenuItems() {

		return null;
	}

	/**
	 * @see fr.itris.glips.svgeditor.Module#getPopupItems()
	 */
	public Collection<PopupItem> getPopupItems() {

		return null;
	}

	/**
	 * @see fr.itris.glips.svgeditor.Module#getToolItems()
	 */
	public HashMap<String, AbstractButton> getToolItems() {

		return null;
	}

	/**
	 * @see fr.itris.glips.svgeditor.Module#initialize()
	 */
	public void initialize() {}

}
