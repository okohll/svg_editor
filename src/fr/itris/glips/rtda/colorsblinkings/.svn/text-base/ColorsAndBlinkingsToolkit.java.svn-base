/*
 * Created on 21 févr. 2005
 */
package fr.itris.glips.rtda.colorsblinkings;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.color.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * a toolkit class for the rtda animations
 * 
 * @author ITRIS, Jordi SUC
 */
public class ColorsAndBlinkingsToolkit {
    
    /**
     * the name of the colors and blinkings file
     */
    public static String colorsBlinkingsFileName=
    		"colorsBlinkings"+AnimationsToolkit.XML_FILE_EXTENSION;
    
    /**
     * the constant for the predefined colors
     */
    protected static Integer PREDEFINED_COLORS=new Integer(0);
    
    /**
     * the constant for the blinking colors
     */
    protected static Integer BLINKING_COLORS=new Integer(1);
    
    /**
     * the constant for the blinkings
     */
    protected static Integer BLINKINGS=new Integer(2);
    
    /**
     * the map associating the name of a project to its map of the colors and blinkings
     */
    private Map<String, Map<Integer, Map<String, Object>>> 
    	projectNameToColorsAndBlinkingsMap=
    		new ConcurrentHashMap<String, Map<Integer, Map<String, Object>>>();
    
    /**
     * the main display
     */
    private MainDisplay mainDisplay;
    
    /**
     * the constructor of the class
     * @param mainDisplay the main display
     */
    public ColorsAndBlinkingsToolkit(MainDisplay mainDisplay){
    	
    	this.mainDisplay=mainDisplay;
    }
    
    /**
     * returns the map associating the type of a data defined in the colors and blinkings file
     * @param projectFile a project file
     * @return the map associating the type of a data defined in the colors and blinkings file
     */
    protected Map<Integer, Map<String, Object>> getColorsAndBlinkings(File projectFile){
    	
        //the map that will be returned
        Map<Integer, Map<String, Object>> map=null;
        String projectName=getProjectName(projectFile);

        if(projectFile!=null && projectName!=null && ! projectName.equals("") && 
                ! projectNameToColorsAndBlinkingsMap.containsKey(projectName)){
            
            //getting the file of the colors and blinkings 
		    File[] children=projectFile.listFiles();
            File file=null;
		    
		    if(children!=null){
		        
    		    for(int i=0; i<children.length; i++){
    		        
    		        if(children[i]!=null && children[i].getName().equals(colorsBlinkingsFileName)){
    		            
    		            file=children[i];
    		            break;
    		        }
    		    }
		    }

            if(file!=null && file.exists()){

                //the map of the predefined colors
                Map<String, Object> predefinedColorsMap=new ConcurrentHashMap<String, Object>();
                
                //the map of the blinking colors
                Map<String, Object> blinkingColorsMap=new ConcurrentHashMap<String, Object>();
                
                //the map of the blinkings
                Map<String, Object> blinkingsMap=new ConcurrentHashMap<String, Object>();
                
                //retrieving the xml document corresponding to the computed path
                Document doc=getXMLDocument(file.getPath());

                if(doc!=null && doc.getDocumentElement()!=null){
                    
                    Node parent=null, color=null, blinking=null;
                    Element el=null;
                    String id="", t1="", t2="", period="", color1="", color2="", blinkingName="";
                    double dT1=0, dT2=0, dPeriod=1;
                    Color cColor1=null, cColor2=null;
                    
                    //for each "blinkings" or "colors" node
                    for(parent=doc.getDocumentElement().getFirstChild(); 
                    	parent!=null; parent=parent.getNextSibling()){
                        
                        if(parent instanceof Element){
                            
                            if(parent.getNodeName().equals("blinkings")){
                                
                                //creates the blinking objects
                                for(blinking=parent.getFirstChild(); blinking!=null; blinking=blinking.getNextSibling()){
                                    
                                    if(blinking instanceof Element && blinking.getNodeName().equals("blinking")){
                                        
                                        el=(Element)blinking;
                                        
                                        id=el.getAttribute("id");
                                        t1=el.getAttribute("t1");
                                        t2=el.getAttribute("t2");
                                        period=el.getAttribute("period");
                                        
                                        try{
                                            dT1=Double.parseDouble(t1);
                                            dT2=Double.parseDouble(t2);
                                            dPeriod=Double.parseDouble(period);
                                        }catch (Exception ex){dT1=0; dT2=0; dPeriod=1;}
                                        
                                        blinkingsMap.put(id, new Blinking(id, dT1, dT2, dPeriod));
                                    }
                                }
                                
                            }else if(parent.getNodeName().equals("colors")){

                                //creates the color objects
                                for(color=parent.getFirstChild(); color!=null; color=color.getNextSibling()){
                                    
                                    if(color instanceof Element){
                                        
                                        if(color.getNodeName().equals("color")){
                                            
                                            el=(Element)color;
                                            
                                            //creates a predefined color
                                            id=el.getAttribute("id");
                                            color1=el.getAttribute("value");
                                            
                                            cColor1=getColor(color1);
                                            
                                            if(cColor1!=null){
                                                
                                                predefinedColorsMap.put(id, new PredefinedColor(id, cColor1));
                                            }

                                        }else if(color.getNodeName().equals("blinkingColor")){
                                            
                                            el=(Element)color;
                                            
                                            //creates a blinking color
                                            id=el.getAttribute("id");
                                            color1=el.getAttribute("color1");
                                            color2=el.getAttribute("color2");
                                            blinkingName=el.getAttribute("blinkingName");
                                            
                                            cColor1=getColor(color1);
                                            cColor2=getColor(color2);
                                            
                                            if(cColor1!=null && cColor2!=null){
                                                
                                                blinkingColorsMap.put(id, new BlinkingColor(id, cColor1, cColor2, blinkingName));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //creating the map
                map=new ConcurrentHashMap<Integer, Map<String, Object>>();
                
                //filling the map
                map.put(PREDEFINED_COLORS, predefinedColorsMap);
                map.put(BLINKING_COLORS, blinkingColorsMap);
                map.put(BLINKINGS, blinkingsMap);
   
                //putting this map in the map associating a canvas to the map of the colors and blinkings
                projectNameToColorsAndBlinkingsMap.put(projectName, map);
                
            }
            
        }else if(projectFile!=null && projectName!=null && ! projectName.equals("")){
            
            map=projectNameToColorsAndBlinkingsMap.get(projectName);
        }

        return map;
    }
    
    /**
     * returns the name of the project
     * @param projectFile the file of a project
     * @return the name of a project
     */
    public static String getProjectName(File projectFile){
        
        return Toolkit.getFileName(projectFile);
    }
    
    /**
     * returns the map associating the id of a predefined color to this color
     * @param projectFile a project file
     * @return the map associating the id of a predefined color to this color
     */
    public Map<String, Object> getPredefinedColorsMap(File projectFile){
        
        Map<String, Object> map=null;
        
        if(projectFile!=null){
            
            //getting the map of the colors and blinking
            Map<Integer, Map<String, Object>> colorsAndBlinkings=
            	getColorsAndBlinkings(projectFile);
            
            if(colorsAndBlinkings!=null){

                map=colorsAndBlinkings.get(PREDEFINED_COLORS);
            }
        }
        
        if(map==null){
        	
        	map=new LinkedHashMap<String, Object>();
        }
        
        return map;
    }
    
    /**
     * returns the map associating the id of a blinking color to this color
     * @param projectFile a project file
     * @return the map associating the id of a blinking color to this color
     */
    public Map<String, Object> getBlinkingColorsMap(File projectFile){
        
        Map<String, Object> map=null;
        
        if(projectFile!=null){
            
            //getting the map of the colors and blinking for the given canvas
            Map<Integer, Map<String, Object>> colorsAndBlinkings=
            	getColorsAndBlinkings(projectFile);

            if(colorsAndBlinkings!=null){
                
                map=colorsAndBlinkings.get(BLINKING_COLORS);
            }
        }
        
        if(map==null){
        	
        	map=new LinkedHashMap<String, Object>();
        }

        return map;
    }
    
    /**
     * returns the map associating the id of a blinking to this blinking
     * @param projectFile a project file
     * @return the map associating the id of a blinking to this blinking
     */
    public Map<String, Object> getBlinkingsMap(File projectFile){
        
        Map<String, Object> map=null;
        
        if(projectFile!=null){
            
            //getting the map of the colors and blinking
            Map<Integer, Map<String, Object>> colorsAndBlinkings=
            	getColorsAndBlinkings(projectFile);
            
            if(colorsAndBlinkings!=null){

                map=colorsAndBlinkings.get(BLINKINGS);
            }
        }
        
        if(map==null){
        	
        	map=new LinkedHashMap<String, Object>();
        }
        
        return map;
    }
    
    /** returns the map associating the id of a color to this color
    * @param projectFile a project file
    * @return the map associating the id of a color to this color
    */
    public LinkedHashMap<String, Object> getAllColorsMap(File projectFile){
        
        LinkedHashMap<String, Object> allColorsMap=new LinkedHashMap<String, Object>();
        
        //adding the predefined colors
        Map<String, Object> map=getPredefinedColorsMap(projectFile);
        
        if(map!=null){
            
            allColorsMap.putAll(map);
        }
        
        //adding the blinking colors
        map=getBlinkingColorsMap(projectFile);
        
        if(map!=null){
            
            allColorsMap.putAll(map);
        }
        
        return allColorsMap;
    }
    
    /**
     * returns the blinking color corresponding to the given string
     * @param projectFile a project file
     * @param value a string that may contain a blinking color id
     * @return the blinking color or null if the string does not describe a blinking color
     */
    public BlinkingColor getBlinkingColor(File projectFile, String value){
        
        BlinkingColor color=null;

        if(projectFile!=null && value!=null && ! value.equals("")){
            
            int pos=value.indexOf("/*");
            
            if(pos!=-1){
                
                String colorId=value.substring(pos+2, value.length()-2);
                
                if(colorId!=null && ! colorId.equals("")){
                    
                    Map<String, Object> blinkingColorsMap=getBlinkingColorsMap(projectFile);

                    if(blinkingColorsMap!=null){
                        
                        color=(BlinkingColor)blinkingColorsMap.get(colorId);
                    }
                }
            }
        }
        
        return color;
    }
    
    /**
     * returns the blinking that has the given id
     * @param projectFile a project file
     * @param blinkingId the id of blinking
     * @return the blinking that has the given id
     */
    public Blinking getBlinking(File projectFile, String blinkingId){
        
        Blinking blinking=null;
        
        if(projectFile!=null && blinkingId!=null && ! blinkingId.equals("")){
            
            Map<String, Object> blinkingsMap=getBlinkingsMap(projectFile);
            
            if(blinkingsMap!=null){
                
                blinking=(Blinking)blinkingsMap.get(blinkingId);
            }
        }
        
        return blinking;
    }
    
    /**
     * removes the map of the colors and blinkings linked with the given canvas
     * @param projectName the name of a project
     */
    public void removeColorsAndBlinkings(String projectName){
        
        if(projectName!=null && ! projectName.equals("")){
            
            projectNameToColorsAndBlinkingsMap.remove(projectName);
        }
    }
    
    /**
     * @return the list of the names of the projects
     */
    public Set<String> getProjectNames(){
    	
        return projectNameToColorsAndBlinkingsMap.keySet();
    }
    
    /**
     * returns the collection of the blinking value modifiers that handle blinking colors that are not in
     * rtda animation nodes
     * @param doc a document
     * @param picture a svg picture
     * @param projectFile the file of a project
     * @return the collection of the blinking value modifiers that handle blinking colors that are not in
     * rtda animation nodes
     */
    public java.util.List<BlinkingValueModifier> 
    	getNotInAnimationsNodeBlinkingValueModifiers(
    			Document doc, SVGPicture picture, File projectFile){

    	LinkedList<BlinkingValueModifier> blinkingValueModifierList=
    		new LinkedList<BlinkingValueModifier>();
    	
    	if(doc!=null && picture!=null){
    		
    		if(doc.getDocumentElement()!=null){
    			
    			//the root element of the canvas
    			Element root=doc.getDocumentElement(), el=null;
    			
    			Node cur=null, att=null;
                String value="";
    			BlinkingValueModifier modifier=null;
    			NamedNodeMap attributes=null;
    			int i;

    			for(NodeIterator it=new NodeIterator(root); it.hasNext();){
    				
    				cur=it.next();
    				
    				if(cur!=null && cur instanceof Element && 
    						! cur.getNodeName().startsWith("rtda:")){

                        el=(Element)cur;
                        
    		            //getting all the attributes of the element
    		            attributes=el.getAttributes();
    		            
    		            if(attributes!=null && attributes.getLength()>0){
    		 
    		                //for each attribute of the node
    		                for(i=0; i<attributes.getLength(); i++){
    		                    
    		                    att=attributes.item(i);
    		                    value=att.getNodeValue();
    		                    
    		                    if(value!=null && ! value.equals("") && value.indexOf("/*")!=-1){
    		                        
    		                        if(att.getNodeName().equals("style")){

    		                            //handling the style properties
    		                        	blinkingValueModifierList.addAll(
    		                        			getNotInAnimationsNodeBlinkingValueModifiersFromStyleAttribute(
    		                        					picture, projectFile, el, value));
    		                        	
    		                        }else{

    		                        	//handling another attribute
    		                            modifier=getNotInAnimationsNodeBlinkingValueModifiersFromAttribute(
    		                            		picture, projectFile, el, att.getNodeName(), value);
    		                            
    		                            if(modifier!=null){
    		                            	
    		                            	blinkingValueModifierList.add(modifier);
    		                            }
    		                        }
    		                    }
    		                }
    		            }
    				}
    			}
    		}
    	}

        return blinkingValueModifierList;
    }
    
    /**
     * returns the collection of the blinking value modifiers that handle blinking colors that are not in
     * rtda animation nodes
     * @param element an element
     * @param picture a svg picture
     * @param projectFile the file of a project
     * @return the collection of the blinking value modifiers that handle blinking colors that are not in
     * rtda animation nodes
     */
    public java.util.List<BlinkingValueModifier> getNotInAnimationsNodeBlinkingValueModifiers(
    		Element element, SVGPicture picture, File projectFile){

    	LinkedList<BlinkingValueModifier> blinkingValueModifierList=
    		new LinkedList<BlinkingValueModifier>();
    	
    	if(element!=null && picture!=null){

			Node att=null;
            String value="";
			BlinkingValueModifier modifier=null;

            //getting all the attributes of the element
			NamedNodeMap attributes=element.getAttributes();
            
            if(attributes!=null && attributes.getLength()>0){
 
                //for each attribute of the node
                for(int i=0; i<attributes.getLength(); i++){
                    
                    att=attributes.item(i);
                    value=att.getNodeValue();
                    
                    if(value!=null && ! value.equals("") && value.indexOf("/*")!=-1){

                        if(att.getNodeName().equals("style")){

                            //handling the style properties
                        	blinkingValueModifierList.addAll(
                        			getNotInAnimationsNodeBlinkingValueModifiersFromStyleAttribute(
                        					picture, projectFile, element, value));
                        	
                        }else{

                        	//handling another attribute
                            modifier=getNotInAnimationsNodeBlinkingValueModifiersFromAttribute(
                            		picture, projectFile, element, att.getNodeName(), value);

                            if(modifier!=null){

                            	blinkingValueModifierList.add(modifier);
                            }
                        }
                    }
                }
            }
    	}

        return blinkingValueModifierList;
    }
    
    /**
     * returns a list of blinking value modifiers that handle the blinking colors that 
     * could be found in the given string
     * @param picture a svg picture
     * @param projectFile the file of a project
     * @param element the element to which the styleValue belongs
     * @param styleValue the value of style attribute
     * @return a list of blinking value modifiers that handle the blinking colors that 
     * could be found in the given string
     */
    private java.util.List<BlinkingValueModifier> 
    	getNotInAnimationsNodeBlinkingValueModifiersFromStyleAttribute(
    		SVGPicture picture, File projectFile, Element element, String styleValue){
    	
    	LinkedList<BlinkingValueModifier> blinkingValueModifierList=new LinkedList<BlinkingValueModifier>();

    	if(picture!=null && styleValue!=null && ! styleValue.equals("")){
    		
            //splitting the property value string into an array of strings
            String[] propertyValues=styleValue.split(";");

            if(propertyValues!=null){
                
                String name="", value="";
                int pos=-1;
                BlinkingValueModifier modifier=null;
                BlinkingColor blinkingColor=null;
                
                for(int i=0; i<propertyValues.length; i++){
                    
                    if(propertyValues[i]!=null){
                        
                        //getting the position of the limit between the name and the value of the property
                        pos=propertyValues[i].indexOf(":");
                        
                        if(pos!=-1){
                            
                            //retrieving the name and the value of the property
                            name=propertyValues[i].substring(0, pos);
                            value=propertyValues[i].substring(pos+1, propertyValues[i].length());
                            
                            if(name!=null && value!=null && ! name.equals("") && ! value.equals("")){

                            	//retrieving the associated blinking color
                            	blinkingColor=getBlinkingColor(projectFile, value);

                            	if(blinkingColor!=null){

                                    //creating a blinking value modifier
                                	modifier=getNotInAnimationsNodeBlinkingValueModifier(
                                			picture, projectFile, blinkingColor, element, name);
                                	
                                	if(modifier!=null){
                                		
                                		//adding the modifier
                                		blinkingValueModifierList.add(modifier);
                                	}
                            	}
                            }
                        }
                    }
                }
            }
    	}
    	
        return blinkingValueModifierList;
    }
    
    /**
     * returns a blinking value modifier that handles the blinking color that could be found in the given string
     * @param picture a svg picture
     * @param projectFile the file of a project
     * @param element the element that will be modified
     * @param attributeName the name of an attribute
     * @param attributeValue the value of an attribute
     * @return a blinking value modifier that handles the blinking color that could be found in the given string
     */
    private BlinkingValueModifier getNotInAnimationsNodeBlinkingValueModifiersFromAttribute(	
    				SVGPicture picture, File projectFile, Element element, 
    					String attributeName, String attributeValue){
    	
    	BlinkingValueModifier modifier=null;
    	
    	if(picture!=null && attributeValue!=null && ! attributeValue.equals("")){

    		//retrieving the blinking color
    		BlinkingColor blinkingColor=getBlinkingColor(projectFile, attributeValue);

    		if(blinkingColor!=null){
    			
    			modifier=getNotInAnimationsNodeBlinkingValueModifier(
    					picture, projectFile, blinkingColor, element, attributeName);
    		}
    	}
    	
        return modifier;
    }
    
    /**
     * returns a blinking value modifier that handles the blinking color that has the given id
     * @param picture a svg picture
     * @param projectFile the file of a project
     * @param blinkingColor a blinking color
     * @param element the element that will be modified
     * @param propertyName the name of the property that will be modified
     * @return a blinking value modifier that handles the blinking color that could be found in the given string
     */
    private BlinkingValueModifier getNotInAnimationsNodeBlinkingValueModifier(	
    		SVGPicture picture, File projectFile, BlinkingColor blinkingColor, 
    		Element element, String propertyName){

    	BlinkingValueModifier modifier=null;
    	
    	if(picture!=null && blinkingColor!=null && element!=null && 
    			propertyName!=null && ! propertyName.equals("")){
    		
    		modifier=new BlinkingValueModifier(
    			mainDisplay, picture, projectFile, blinkingColor.getBlinkingId(), element, 
    				propertyName, blinkingColor.getColorValue(), blinkingColor.getColorValue2());
    		
    		modifier.setActive(true);
    	}
    	
        return modifier;
    }
    
    /**
     * create a document from that has the given file path
     * @param filePath the path of the xml file
     * @return the document
     */
    protected Document getXMLDocument(String filePath){
        
        Document doc=null;
        
        if(filePath!=null && ! filePath.equals("")){
            
            DocumentBuilderFactory docBuildFactory=DocumentBuilderFactory.newInstance();
            
            try{
                //parses the XML file
                DocumentBuilder docBuild=docBuildFactory.newDocumentBuilder();
                doc=docBuild.parse(filePath);
            }catch (Exception ex){}
        }
        
        return doc;
    }

    /**
     * returns the color corresponding to the given string
     * @param colorString a string representing a color
     * @return the color corresponding to the given string
     */
    public static Color getColor(String colorString){

        Color color=null;
        
        if(colorString==null){
            
            colorString="";
        }
        
        int pos=colorString.indexOf("/*");
        
        if(pos!=-1) {
        	
        	colorString=colorString.substring(0, pos);
        }
        
        try{color=Color.getColor(colorString);}catch (Exception ex){}
        
        if(color==null && colorString.length()==7){
            
            int r=0, g=0, b=0;
            
            try{
                r=Integer.decode("#"+colorString.substring(1,3)).intValue();
                g=Integer.decode("#"+colorString.substring(3,5)).intValue();
                b=Integer.decode("#"+colorString.substring(5,7)).intValue();
                
                color=new Color(r,g,b);
            }catch (Exception ex){}
            
        }else if(color==null && colorString.indexOf("rgb(")!=-1){
            
            String tmp=colorString.replaceAll("\\s*[rgb(]\\s*", "");
            
            tmp=tmp.replaceAll("\\s*[)]\\s*", "");
            tmp=tmp.replaceAll("\\s+", ",");
            tmp=tmp.replaceAll("[,]+", ",");
            
            int r=0, g=0, b=0;
            
            try{
                r=new Integer(tmp.substring(0, tmp.indexOf(","))).intValue();
                tmp=tmp.substring(tmp.indexOf(",")+1, tmp.length());
                
                g=new Integer(tmp.substring(0, tmp.indexOf(","))).intValue();
                tmp=tmp.substring(tmp.indexOf(",")+1, tmp.length());
                
                b=new Integer(tmp).intValue();
                
                color=new Color(r,g,b);
            }catch (Exception ex){}
            
        }else{
        	
        	color=SVGColorsManager.getColor(colorString);
        }

        return color;
    }
    
    /**
     * Returns the string representation of the given color
     * @param color a color
     * @return the string representation of the given color
     */
    public static String getColorString(Color color){
        
        if(color==null){
            
            color=Color.black;
        }
        
        String	sr=Integer.toHexString(color.getRed()),
			        sg=Integer.toHexString(color.getGreen()),
			        sb=Integer.toHexString(color.getBlue());
        
        if(sr.length()==1){
            
            sr="0".concat(sr);
        }
        
        if(sg.length()==1){
            
            sg="0".concat(sg);
        }
        
        if(sb.length()==1){
            
            sb="0".concat(sb);
        }
        
        return (("#".concat(sr)).concat(sg)).concat(sb);
    }
}
