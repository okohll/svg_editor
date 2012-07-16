/*
 * Created on 13 avr. 2005
 */
package fr.itris.glips.rtda.resources;

import java.awt.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;
import java.net.*;

/**
 * the manager of the resources
 * 
 * @author ITRIS, Jordi SUC
 */
public class RtdaResources {
	
	/**
	 * the bundle of this resources manager
	 */
	public static ResourceBundle bundle;
	
	static{
		
		try{
			bundle=ResourceBundle.getBundle("fr.itris.glips.rtda.resources.properties.strings");
		}catch (Exception ex){}
	}

    /**
     * the HashMap associating a an icon's name to an icon
     */
    private static final HashMap<String, ImageIcon> icons=new HashMap<String, ImageIcon>();
    
    /**
     * the HashMap associating a an icon's name to a gray icon
     */
    private static final HashMap<String, ImageIcon> grayIcons=new HashMap<String, ImageIcon>();
    
    /**
     * gives an ImageIcon object given the name of it as it is witten in the SVGEditorIcons.properties file
     * @param name the name of an icon
     * @param isGrayIcon true if the icon should be used for a disabled widget
     * @return an image icon 
     */
    public static ImageIcon getIcon(String name, boolean isGrayIcon){
        
        ImageIcon icon=null;
        
        if(name!=null && ! name.equals("")){
            
            if(icons.containsKey(name)){
                
                try{
                    if(isGrayIcon){
                        
                        icon=grayIcons.get(name);
                        
                    }else{
                        
                        icon=icons.get(name);
                    }
                }catch(Exception ex){}
                
            }else{
                
                //gets the name of the icons from the resources
                ResourceBundle iconsBundle=null;
                
                try{
                    iconsBundle=ResourceBundle.getBundle("fr.itris.glips.rtda.resources.properties.icons");
                }catch (Exception ex){}
                
                String path="";
                
                if(iconsBundle!=null){
                    
                    try{path=iconsBundle.getString(name);}catch (Exception ex){path="";}
                    
                    if(path!=null && ! path.equals("")){
                        
                        try{
                            icon=new ImageIcon(new URL(getPath("icons/"+path)));
                        }catch (Exception ex){}
                        
                        
                        if(icon!=null){
                            
                            icons.put(name, icon);
                            Image image=icon.getImage();
                            
                            ImageIcon grayIcon=new ImageIcon(GrayFilter.createDisabledImage(image));
                            grayIcons.put(name, grayIcon);
                            
                            if(isGrayIcon){
                                
                                icon=grayIcon;
                            }
                        }
                    }
                }
            }
        }
        
        return icon;
    }
    
    /**
     * create a document from the given file in the resource files
     * @param name the name of the xml file
     * @return the document 
     */
    public static Document getXMLDocument(String name){
        
        Document doc=null;
        
        if(name!=null && ! name.equals("")){
            
        	DocumentBuilderFactory docBuildFactory=DocumentBuilderFactory.newInstance();
            
            String path="";
            
            try{
                //parses the XML file
                DocumentBuilder docBuild=docBuildFactory.newDocumentBuilder();
                path=getPath("xml/".concat(name));
                doc=docBuild.parse(path);
            }catch (Exception ex){}
        }
        
        return doc;
    }
    
    /**
     * computes the path of a resource given its name
     * @param resource the name of a resource 
     * @return the full path of a resource
     */
    public static String getPath(String resource){
        
        String path="";
        
        try{
            path=RtdaResources.class.getResource(resource).toExternalForm();
        }catch (Exception ex){path="";}
        
        return path;
    }

}
