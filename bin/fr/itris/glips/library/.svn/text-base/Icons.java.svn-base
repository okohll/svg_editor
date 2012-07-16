package fr.itris.glips.library;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.*;

/**
 * the class of the icons manager
 * @author ITRIS, Jordi SUC
 */
public class Icons {
	
	/**
	 * the resource bundle
	 */
	private static ResourceBundle bundle;
	
	static{
		
        try{
            bundle=ResourceBundle.getBundle(
            		"fr.itris.glips.library.properties.icons");
        }catch (Exception ex){bundle=null;}
	}
	
	/**
	 * the map of the icons
	 */
	private static Map<String, ImageIcon> icons=new HashMap<String, ImageIcon>();
	
	/**
	 * the map of the disabled icons
	 */
	private static Map<String, ImageIcon> disabledIcons=new HashMap<String, ImageIcon>();

	/**
	 * returns the icon corresponding to the given name
	 * @param iconName the name of an icon
	 * @return the icon corresponding to the given name
	 * @param isGrayIcon whether the icon is a gray one
	 */
	public static ImageIcon getIcon(String iconName, boolean isGrayIcon){
		
        ImageIcon icon=null;
        
        if(iconName!=null && ! iconName.equals("")){
            
            if(icons.containsKey(iconName)){
                
            	 icon=isGrayIcon?disabledIcons.get(iconName):icons.get(iconName);
                
            }else if(bundle!=null){

                String path="";

                try{path=bundle.getString(iconName);}catch (Exception ex){}
                
                if(path!=null && ! path.equals("")){
                    
                    try{
                        icon=new ImageIcon(new URL(
                        		Icons.class.getResource("icons/"+path).toExternalForm()));
                    }catch (Exception ex){}

                    if(icon!=null){
                        
                        icons.put(iconName, icon);
                        Image image=icon.getImage();
                        ImageIcon grayIcon=new ImageIcon(GrayFilter.createDisabledImage(image));
                        disabledIcons.put(iconName, grayIcon);
                        
                        if(isGrayIcon){
                            
                            icon=grayIcon;
                        }
                    }
                }
            }
        }
        
        return icon;
	}
}
