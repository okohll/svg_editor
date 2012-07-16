/*
 * Created on 26 août 2004
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
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 Contact : jordi.suc@itris.fr; philippe.gil@itris.fr
 
 =============================================
 */
package fr.itris.glips.svgeditor.visualresources;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the dialog for modifying a resource
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceResourceModifier {

    /**
     * the constant used when the ok button is clicked
     */
    public static final int OK=0;
    
    /**
     * the constant used when the cancel button is clicked
     */
    public static final int CANCEL=1;
    
    /**
     * a small font
     */
    protected static final Font smallFont=new Font("smallFont", Font.ROMAN_BASELINE, 9);
    
    /**
     * the font
     */
    protected static final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);

    /**
     * the labels
     */
    private static String okLabel="", cancelLabel="";
    
    /**
     * the value that will be returned
     */
    private static int returnValue=CANCEL;

    /**
     * the bundle
     */
    private static ResourceBundle bundle=null;
    
    static{
        
        bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
        	
            try{
                okLabel=bundle.getString("labelok");
                cancelLabel=bundle.getString("labelcancel");
            }catch (Exception ex){}
        }
    }
    
    /**
     * displays a dialog modifying a resource object
     * @param resourceObject a resource object
     * @return OK or CANCEL whether the ok or cancel button has been clicked
     */
    public static int showPropertyModifierDialog(SVGVisualResourceObject resourceObject){

        returnValue=CANCEL;
        
    	if(resourceObject!=null){

		    //the list containing the widgets
		    final LinkedList widgets=new LinkedList();
		    
		    Iterator it;
		    SVGVisualResourceWidget widget=null;
		    
		    if(resourceObject.canCreateChildren()){
		        
			    //the widget for the child
			    widget=getChildWidget(resourceObject);
			    
			    if(widget!=null){
			        
			        widgets.add(widget);
			    }
		    }
		    
		    SVGVisualResourceObjectAttribute att=null;
		    
		    //getting all the widgets linked with the attributes of the resource object
		    for(it=resourceObject.getAttributes().iterator(); it.hasNext();){
		        
		        try{
		            att=(SVGVisualResourceObjectAttribute)it.next();
		        }catch (Exception ex){att=null;}
		        
		        if(att!=null){
		            
		            widget=getAttributeWidget(att);
		            widgets.add(widget);
		        }
		    }
		    
		    //building the dialog//
		    
		    //creating the title of the dialog
		    String title="";
		    
		    if(bundle!=null){
		        
		        try{
		            title=bundle.getString(resourceObject.getResourceModel().getVisualResources().getAbsoluteString(resourceObject.getResourceModel().getName()));
		            title=title.concat(" ".concat(bundle.getString("vresource_properties")));
		        }catch (Exception ex){}
		    }

		    //getting the parent of the dialog
		    Frame parentFrame=null;
		    Container parentContainer=Editor.getParent();
		    
		    if(parentContainer instanceof Frame){
		    	
		    	parentFrame=(Frame)parentContainer;
		    	
		    }else{
		    	
		    	parentFrame=new JFrame("");
		    }

		    final JDialog dialog=new JDialog(parentFrame, title, true);
		    dialog.setResizable(false);
		    JPanel dialogPanel=new JPanel();
		    dialogPanel.setLayout(new BorderLayout());
		    
		    //the panel containing the widgets
		    JPanel content=new JPanel();

		    //sets the content's layout
		    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		    //adding the widgets to the content panel
		    JComponent cmp=null;
		    String label="";
		    TitledBorder border=null;
		    
		    for(it=widgets.iterator(); it.hasNext();){
		        
		        try{
		            widget=(SVGVisualResourceWidget)it.next();
		        }catch (Exception ex){widget=null;}
		        
		        if(widget!=null){

		            cmp=widget.getComponent();
		            label=widget.getLabel();
		            
		            if(label==null){
		                
		                label="";
		            }
		            
		            //setting the component's border
		            border=new TitledBorder(label);
		            cmp.setBorder(border);

		            //adding the component to the content panel
		            content.add(cmp);
		        }
		    }
		    
		    //the panel containing the buttons
		    JPanel buttons=new JPanel();
		    buttons.setLayout(new FlowLayout(FlowLayout.CENTER));

		    //the buttons
		    final JButton okBt=new JButton(okLabel), cancelBt=new JButton(cancelLabel);
		    
		    Insets buttonInsets=new Insets(2, 2, 2, 2);
		    okBt.setMargin(buttonInsets);
		    cancelBt.setMargin(buttonInsets);
		    
		    //the listener to the dialog
		    final WindowAdapter windowAdapter=new WindowAdapter(){
		    	
		    	@Override
		    	public void windowClosing(WindowEvent evt) {
		    		
		    		cancelBt.doClick();
		    		dialog.removeWindowListener(this);
		    	}
		    };
		    
		    dialog.addWindowListener(windowAdapter);
		    
		    //the listener to the buttons
		    ActionListener buttonsListener=new ActionListener(){
		        
		        public void actionPerformed(ActionEvent evt) {

		            //removes all the listeners from the widgets
		            SVGVisualResourceWidget widget=null;
		            
		            for(Iterator it=widgets.iterator(); it.hasNext();){
		                
		                try{
		                    widget=(SVGVisualResourceWidget)it.next();
		                }catch (Exception ex){widget=null;}
		                
		                if(widget!=null){
		                    
		                    widget.dispose();
		                }
		            }
		            
		            widgets.clear();
		            
		            if(evt.getSource().equals(okBt)){
		                
		                returnValue=OK;
		            }
		            
		            //removes the listener to the buttons
		            okBt.removeActionListener(this);
		            cancelBt.removeActionListener(this);
				    dialog.removeWindowListener(windowAdapter);

		            //removes all that is used to display the dialog
		            dialog.setVisible(false);
		            
		            dialog.dispose();
		        }
		    };
		    
		    //adds the action listeners
		    okBt.addActionListener(buttonsListener);
		    cancelBt.addActionListener(buttonsListener);
		    
		    //adds the button to the button panel
		    buttons.add(okBt);
		    buttons.add(cancelBt);
		    
		    //dealing with the dialog close button
			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		    //adds the panels to the dialog panel
		    dialogPanel.add(content, BorderLayout.CENTER);
		    dialogPanel.add(buttons, BorderLayout.SOUTH);
		    
		    //adds the dialog panel to the dialog
		    dialog.getContentPane().add(dialogPanel);
		    
		    dialog.pack();
		    
		    //sets the location for the window
		    dialog.setLocation(
		            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-dialog.getSize().width/2), 
		            (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-dialog.getSize().height/2));
		    
		    //displays the dialog
		    dialog.setVisible(true);
		    
            while(dialog.isVisible()){
                
                try{
                    Thread.sleep((long)100.0);
                }catch (Exception ex){} 
            }
    	}
    	
    	return returnValue;
    }

	/**
	 * @return Returns the bundle.
	 */
	protected static ResourceBundle getBundle() {
		
		return bundle;
	}
	
	/**
	 * returns a widget allowing to modify the children of the given resource object
	 * @param resourceObject a resource object
	 * @return a widget allowing to modify the children of the given resource object
	 */
	protected static SVGVisualResourceWidget getChildWidget(SVGVisualResourceObject resourceObject){
		
        SVGVisualResourceWidget widget=null;
        
        if(resourceObject.getChildren().size()>0){
            
            SVGVisualResourceObjectChild child=null;
            
            try{
                child=(SVGVisualResourceObjectChild)resourceObject.getChildren().getFirst();
            }catch (Exception ex){}
            
            if(child!=null){
                
                //getting the type of the child
                String type=child.getChildModel().getType();
                
                if(type!=null){
                    
                    //creating the accurate widget
                    if(type.equals("colorchooser")){
                        
                        widget=new SVGVisualResourceGradientColorChooserChildWidget(resourceObject);
                    }
                }
            }
        }
	    
		return widget;
	}
	
	/**
	 * returns a widget allowing to modify the given resource attribute
	 * @param resourceObjectAttribute a resource attribute
	 * @return a widget allowing to modify the given resource attribute
	 */
	protected static SVGVisualResourceWidget getAttributeWidget(SVGVisualResourceObjectAttribute resourceObjectAttribute){
		
        SVGVisualResourceWidget widget=null;
        
        if(resourceObjectAttribute!=null && resourceObjectAttribute.getModel()!=null){
 
            SVGVisualResourceModelAttribute attributeModel=resourceObjectAttribute.getModel();
            
            if(attributeModel.isInGroup()){
                
                return null;
            }

            //the type of the attribute
            String type=attributeModel.getType();
            
            if(type!=null){
                
                if(type.equals("combo")){
                    
                    widget=new SVGVisualResourceComboAttributeWidget(resourceObjectAttribute);
                    
                }else if(type.equals("validatedentry")){
                    
                    widget=new SVGVisualResourceValidatedEntryAttributeWidget(resourceObjectAttribute);
                    
                }else if(type.equals("entry")){
                    
                    widget=new SVGVisualResourceEntryAttributeWidget(resourceObjectAttribute);
                    
                }else if(type.equals("vectorchooser") || type.equals("circlechooser")){
                    
                    widget=new SVGVisualResourceVectorCircleChooserAttributeWidget(resourceObjectAttribute);
                    
                }else if(type.equals("slider")){
                    
                    widget=new SVGVisualResourceSliderAttributeWidget(resourceObjectAttribute);
                    
                }else if(type.equals("patternsizechooser")){
                    
                    widget=new SVGVisualResourcePatternSizeChooserAttributeWidget(resourceObjectAttribute);
                    
                }else if(type.equals("editablecombo")){
                    
                    widget=new SVGVisualResourceEditableComboAttributeWidget(resourceObjectAttribute);
                }
            }
        }
        
        return widget;
	}
	
}
