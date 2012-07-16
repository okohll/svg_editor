package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the header component of the animations and actions frame
 * @author ITRIS, Jordi SUC
 */
public class HeaderPanel extends JPanel{

	/**
	 * the animations and actions module
	 */
	private RtdaAnimationsAndActionsModule rtdaAnimationsAndActions=null;
	
	/**
	 * the label displaying the type of the selected node
	 */
	private JLabel selectedNodeTypeLbl=new JLabel();
	
	/**
	 * the description label
	 */
	private String descriptionLabel="", noHandleOpenLabel="", tagEventModeLabel="";
	
	/**
	 * the test launch button
	 */
	private JButton testLaunchButton;
	
	/**
	 * the separator
	 */
	private JSeparator separator=new JSeparator();

	/**
	 * the constructor of the class
	 * @param rtdaAnimationsAndActions the animations and actions module
	 * @param testLaunchButton the button used to launch the test dialog
	 */
	public HeaderPanel(RtdaAnimationsAndActionsModule rtdaAnimationsAndActions, JButton testLaunchButton) {
		
		this.rtdaAnimationsAndActions=rtdaAnimationsAndActions;
		this.testLaunchButton=testLaunchButton;
		
		descriptionLabel=ResourcesManager.bundle.getString("rtdaanim_headerElementName");
		noHandleOpenLabel=ResourcesManager.bundle.getString("rtdaanim_noHandleOpen");
		tagEventModeLabel=ResourcesManager.bundle.getString("rtdaanim_tagEventMode");
		
		//setting the properties of the jlabel
		selectedNodeTypeLbl.setHorizontalAlignment(SwingConstants.LEFT);
		selectedNodeTypeLbl.setBorder(new EmptyBorder(0, 5, 0, 0));
		
		//building the panel
		setLayout(new BorderLayout(5, 2));
		setBorder(new EmptyBorder(2, 2, 2, 2));
		add(selectedNodeTypeLbl, BorderLayout.CENTER);
		add(testLaunchButton, BorderLayout.EAST);
		add(separator, BorderLayout.SOUTH);
	}
	
	/**
	 * sets the currently edited element
	 * @param element an element
	 */
	public void setCurrentElement(Element element) {
		
		separator.setVisible(false);
		testLaunchButton.setVisible(false);
		
		if(element!=null && element.getOwnerDocument().
				getDocumentElement().equals(element)){
			
			selectedNodeTypeLbl.setText(tagEventModeLabel);
			separator.setVisible(true);
			testLaunchButton.setVisible(true);
		
		}else if(element!=null) {
			
			selectedNodeTypeLbl.setText("<html><body>"+descriptionLabel+
					" <b>"+getLabel(element)+"</b></body></html>");		
			separator.setVisible(true);
			testLaunchButton.setVisible(true);
			
		}else {
			
			selectedNodeTypeLbl.setText(noHandleOpenLabel);
		}
	}
	
    /**
     * returns the label corresponding to the given element
     * @param element an element
     * @return the label corresponding to the given element
     */
    public String getLabel(Element element) {
    	
    	String label="";
    	
    	if(element!=null) {
    		
    		if(fr.itris.glips.library.Toolkit.hasJWidgetChildElement(element)) {
    			
    			//getting the jwidget element corresponding to this element
    			Element jwidgetElement=JWidgetManager.getJWidgetElement(element);
    			JWidgetEdition jwidgetEdition=rtdaAnimationsAndActions.getJwidgetManager().
    				getJWidgetEdition(jwidgetElement.getAttribute(fr.itris.glips.library.Toolkit.nameAttribute));
    			
    			if(jwidgetEdition!=null) {
    				
    				//the label is the description of the jwidget element
    				label=jwidgetEdition.getDescription();
    				
    			}else {
    				
    				label=EditorToolkit.unknownShapeLabel;
    			}
    			
    		}else {
    			
    			label=EditorToolkit.getElementLabel(element);
    		}
    	}
    	
    	return label;
    }
}
