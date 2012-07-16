package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the simple tag chooser
 * @author ITRIS, Jordi SUC
 */
public class ViewChooser extends Widget{
	
	/**
	 * the label
	 */
	private final JLabel jlabel=new JLabel();
	
	/**
	 * the button used to launch the dialog
	 */
	private final JButton moreButton=new JButton();
	
	/**
	 * the labels
	 */
	private String backLabel="", quitLabel="", noneLabel="", 
		closePopupDialogLabel="";
	
	/**
	 * whether to show the close popup dialog option
	 */
	private boolean showClosePopupDialogOption;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 * @param showClosePopupDialogOption whether to show the close popup dialog option
	 */
	protected ViewChooser(boolean isEditor, 
			boolean showClosePopupDialogOption){
		
		super(isEditor);
		this.showClosePopupDialogOption=showClosePopupDialogOption;
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String viewChooserLabel="";
		ResourceBundle bundle=ResourcesManager.bundle;
		
		try{
			viewChooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
			backLabel="<"+bundle.getString("rtdaanim_back")+">";
			quitLabel="<"+bundle.getString("rtdaanim_quit")+">";
			noneLabel="<"+bundle.getString("rtdaanim_none")+">";
			closePopupDialogLabel="<"+bundle.getString("rtdaanim_closePopupDialog")+">";
		}catch (Exception ex){}
			
		moreButton.setText(viewChooserLabel);
		moreButton.setMargin(new Insets(1, 1, 1, 1));
		jlabel.setOpaque(false);
		
		setLayout(new BorderLayout(1, 0));
		add(jlabel, BorderLayout.CENTER);
		add(moreButton, BorderLayout.EAST);
		
		if(isEditor) {
			
			//adding the listener to the button
			moreButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					//getting the current svg handle
					SVGHandle svgHandle=
						Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(svgHandle!=null){
						
						//creating the filter for choosing a tag
						DataBaseNodeFilter filter=new DataBaseNodeFilter(	
								getItem().getValue(), 0, TagToolkit.VIEW, true, false, svgHandle.getName());
						
						//getting the information object about the selected tag
						DataBaseNodeChooser nodeChooser=DataBaseNodeChooser.getDataBaseNodeChooser(
								Editor.getParent(), getItem().getDocument(), filter, true, showClosePopupDialogOption);
						nodeChooser.showDialog(((JComponent)evt.getSource()));
						nodeChooser.disposeDialog();
						DataBaseNodeInformation info=nodeChooser.getInfo();
						
						if(info!=null){
							
							String newValue=info.getXmlPath();
							
							if(newValue==null){
								
								newValue="";
							}
							
							getItem().setValue(newValue);
						}
					}

					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			});
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		
		String value=item.getValue();
		
		if(value.equals(DataBaseNodeToolkit.backValue)){
			
			value=backLabel;
			
		}else if(value.equals(DataBaseNodeToolkit.closePopupDialogValue)){
			
			value=closePopupDialogLabel;
			
		}else if(value.equals(DataBaseNodeToolkit.quitValue)){
			
			value=quitLabel;
			
		}else if(value.equals("")){
			
			value=noneLabel;
		}
		
		jlabel.setText(value);
	}
}
