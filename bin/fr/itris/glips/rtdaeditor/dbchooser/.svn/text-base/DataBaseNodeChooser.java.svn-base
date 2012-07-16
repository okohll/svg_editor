package fr.itris.glips.rtdaeditor.dbchooser;

import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.svgeditor.resources.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to choose a tag or a view in a tree
 * @author ITRIS, Jordi SUC
 */
public class DataBaseNodeChooser extends TitledDialog{
	
	/**
	 * the document of the svg the tag chooser is required for
	 */
	protected Document document;
	
	/**
	 *  the filter for displaying the data base nodes
	 */
	protected DataBaseNodeFilter filter;
	
	/**
	 * whether to show the nodes only used for the view
	 */
	protected  boolean showViewNodes;
	
	/**
	 * whether to show the node for the "close popup dialog" option
	 */
	protected  boolean showClosePopupDialogNode;
	
	/**
	 * the chooser panel
	 */
	protected DataBaseNodeChooserPanel chooserPanel;
	
	/**
	 * the content panel
	 */
	protected JPanel theContentPanel;
	
	/**
	 * whether the info object should be returned
	 */
	protected boolean discard=false;
	
	/**
	 * the constructor of the class
	 * @param parent the parent container
	 * @param document the document of the svg the tag chooser is required for
     * @param filter the filter for displaying the data base nodes
     * @param showViewNodes shows the nodes only used for the view
     * @param showClosePopupDialogNode shows the node for the "close popup dialog" option
	 */
	protected DataBaseNodeChooser(Frame parent, Document document, 
			DataBaseNodeFilter filter, boolean showViewNodes, 
			boolean showClosePopupDialogNode){
		
		super(parent, true, true);
		this.document=document;
		this.filter=filter;
		this.showViewNodes=showViewNodes;
		this.showClosePopupDialogNode=showClosePopupDialogNode;
		buildRemaining();
	}
	
	/**
	 * the constructor of the class
	 * @param parent the parent container
	 * @param document the document of the svg the tag chooser is required for
     * @param filter the filter for displaying the data base nodes
     * @param showViewNodes shows the nodes only used for the view
     * @param showClosePopupDialogNode shows the node for the "close popup dialog" option
	 */
	protected DataBaseNodeChooser(JDialog parent, Document document, 
			DataBaseNodeFilter filter, boolean showViewNodes, 
			boolean showClosePopupDialogNode){
		
		super(parent, true);
		this.document=document;
		this.filter=filter;
		this.showViewNodes=showViewNodes;
		this.showClosePopupDialogNode=showClosePopupDialogNode;
		buildRemaining();
	}
	
	/**
	 * builds the remaining features
	 */
	protected void buildRemaining(){
		
		chooserPanel=new DataBaseNodeChooserPanel(
				document, filter, showViewNodes, showClosePopupDialogNode);
		theContentPanel.add(chooserPanel);
		chooserPanel.setPreferredSize(new Dimension(300, 250));
		pack();
		
        //setting the message
        setMessage(getComment(), TitledDialog.INFORMATION_TYPE);
        
        //the listener to the state of the chooser
        DataBaseNodeChooserStateListener listener=
        	new DataBaseNodeChooserStateListener(){
        	
        	@Override
        	public void notifySelectionCorrect(boolean correct) {
        		
        		okButton.setEnabled(correct);
        	}
        };
        
        chooserPanel.addSelectionStateListener(listener);
	}
	
	@Override
	protected JPanel buildContentPanel() {

		//creating the content panel
		theContentPanel=new JPanel();
		
        //getting the labels
		ResourceBundle bundle=ResourcesManager.bundle;
        String dialogTitleLabel=bundle.getString("rtdaanim_tagchooserdialogtitle");
        
        //setting the title dialog
        setTitleMessage(dialogTitleLabel);
        
        //setting the layout for the panel
        theContentPanel.setLayout(new BoxLayout(
        		theContentPanel, BoxLayout.X_AXIS));
        
		//the ok and cancel buttons
        ActionListener buttonsListener=new ActionListener(){
        	
        	public void actionPerformed(ActionEvent e) {
        		
        		if(e.getSource().equals(cancelButton)){
        			
        			discard=true;
        		}
        		
        		setVisible(false);
        	}
        };
        
        okButtonListener=buttonsListener;
        cancelButtonListener=buttonsListener;
   		okButton.addActionListener(okButtonListener);
   		cancelButton.addActionListener(cancelButtonListener);
		
		return theContentPanel;
	}
	
	/**
	 * @return the comment
	 */
	protected String getComment(){
		
        //getting the labels
		ResourceBundle bundle=ResourcesManager.bundle;
        String commentStart=bundle.getString("rtdaanim_tagchoosercommentstart");
        String commentEnumeratedTag=bundle.getString("rtdaanim_tagchoosercommentenumeratedtag");
        String commentFloatTag=bundle.getString("rtdaanim_tagchoosercommentfloattag");
        String commentIntegerTag=bundle.getString("rtdaanim_tagchoosercommentintegertag");
        String commentAnalogicTag=bundle.getString("rtdaanim_tagchoosercommentanalogictag");
        String commentStringTag=bundle.getString("rtdaanim_tagchoosercommentstringtag");
        String commentView=bundle.getString("rtdaanim_tagchoosercommentview");
        String commentNotATag=bundle.getString("rtdaanim_tagchoosercommentdefault");
        
		//creating the comment for the dialog
		String comment=commentStart+" ";
		
		switch(filter.getTagType()){
		
			case TagToolkit.ENUMERATED :
				
				comment+=commentEnumeratedTag;
				break;
				
			case TagToolkit.ANALOGIC_FLOAT :
				
				comment+=commentFloatTag;
				break;
				
			case TagToolkit.ANALOGIC_INTEGER:
				
				comment+=commentIntegerTag;
				break;
				
			case TagToolkit.ANALOGIC :
				
				comment+=commentAnalogicTag;
				break;
				
			case TagToolkit.STRING :
				
				comment+=commentStringTag;
				break;
				
			case TagToolkit.VIEW :
				
				comment+=commentView;
				break;
				
			default :
				
				comment+=commentNotATag;
		}
		
		return comment;
	}
	
	/**
	 * @return the information object that is computed
	 */
	public DataBaseNodeInformation getInfo(){
		
		return discard?null:chooserPanel.getInfo();
	}
	
	@Override
	public void disposeDialog() {
		
		chooserPanel.dispose();
		super.disposeDialog();
	}
	
	/**returns an instance of a data base node chooser
	 * @param parent the parent container
	 * @param document the document of the svg the tag chooser is required for
     * @param filter the filter for displaying the data base nodes
     * @param showViewNodes shows the nodes only used for the view
     * @param showClosePopupDialogNode shows the node for the "close popup dialog" option
     * @return an instance of a data base node chooser
	 */
	public static DataBaseNodeChooser getDataBaseNodeChooser(
			Container parent, Document document, 
					DataBaseNodeFilter filter, boolean showViewNodes, 
						boolean showClosePopupDialogNode){
		
		if(parent instanceof Frame){
			
			return new DataBaseNodeChooser((Frame)parent, 
					document, filter, showViewNodes, showClosePopupDialogNode);
			
		}else{
			
			return new DataBaseNodeChooser((JDialog)parent, 
					document, filter, showViewNodes, showClosePopupDialogNode);
		}
	}
}
