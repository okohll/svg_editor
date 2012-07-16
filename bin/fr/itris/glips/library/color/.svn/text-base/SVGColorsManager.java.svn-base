package fr.itris.glips.library.color;

import java.awt.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * the class handling the svg colors
 * 
 * @author Jordi SUC
 */
public class SVGColorsManager {

    /**
     * the map associating the name of a w3c color to a svg w3c color object
     */
    private static LinkedHashMap<String, SVGW3CColor> w3cColorsMap=
    	new LinkedHashMap<String, SVGW3CColor>();
    
    static{
    	
    	//getting the document
        DocumentBuilderFactory docBuildFactory=
        	DocumentBuilderFactory.newInstance();
        
        Document doc=null;
        
        try{
            //parses the XML file
            DocumentBuilder docBuild=docBuildFactory.newDocumentBuilder();
            doc=docBuild.parse(
            	SVGColorsManager.class.getResourceAsStream("xml/svgColors.xml"));
        }catch (Exception ex){}
    	
    	//getting the W3C colors
        retrieveW3CColors(doc);
    }
    
    /**
     * retrieves all the colors in the given svg document and fills the list and the map of the colors
     * @param svgColorsDocument the document containing the name of each and its associated rgb value
     */
    protected static void retrieveW3CColors(Document svgColorsDocument){
        
        if(svgColorsDocument!=null && svgColorsDocument.getDocumentElement()!=null){
            
            String id="", value="";
            Color color=null;
            SVGW3CColor svgColor=null;
            LinkedList<SVGW3CColor> colorsList=new LinkedList<SVGW3CColor>();
            
            //for each svg colors, gets the name and the value and adds them to the list
            for(Node cur=svgColorsDocument.getDocumentElement().getFirstChild();
            	cur!=null; cur=cur.getNextSibling()){
                
                if(cur instanceof Element && cur.getNodeName().equals("svgColor")){
                    
                    id=((Element)cur).getAttribute("id");
                    value=((Element)cur).getAttribute("value");
                    
                    if(id!=null && ! id.equals("") && value!=null && ! value.equals("")){
                        
                        color=getColor(value);
                        
                        if(color!=null){
                            
                            svgColor=new SVGW3CColor(id, color);
                            colorsList.add(svgColor);
                        }
                    }
                }
            }
            
            //puts the colors and the ids to the map of the colors
            for(SVGW3CColor svgColor2 : colorsList){

                if(svgColor2!=null){
                    
                    w3cColorsMap.put(svgColor2.getId(), svgColor2);
                }
            }
        }
    }
    
    /**
     *@return the w3c color objects list
     */
    public static LinkedList<SVGW3CColor> getW3CColors(){
        
        return new LinkedList<SVGW3CColor>(w3cColorsMap.values());
    }
    
    /**
     *@return the w3c colors map
     */
    public static Map<String, SVGW3CColor> getW3CColorsMap(){
        
        return new HashMap<String, SVGW3CColor>(w3cColorsMap);
    }
    
    /**
     * Returns the color corresponding to the given string
     * @param colorString a string representing a color
     * @return the color corresponding to the given string
     */
    public static Color getColor(String colorString){
        
    	Color color=null;
        
        if(colorString==null){
            
            colorString="";
            
        }else{
            
            //checking if the given string represents a w3c color
            color=w3cColorsMap.get(colorString);
        }
        
        if(color==null){
            
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
            }
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
        
        if(color instanceof SVGW3CColor){
            
            return ((SVGW3CColor)color).getId();
        }
        
        String	sr=Integer.toHexString(color.getRed()),
        sg=Integer.toHexString(color.getGreen()),
        sb=Integer.toHexString(color.getBlue());
        
        if(sr.length()==1){
            
            sr="0"+sr;
        }
        
        if(sg.length()==1){
            
            sg="0"+sg;
        }
        
        if(sb.length()==1){
            
            sb="0"+sb;
        }
        
        return "#"+sr+sg+sb;
    }
}
