package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import fr.itris.glips.library.widgets.LineInterpolationChooserWidget;
import fr.itris.glips.rtdaeditor.anim.*;
import java.awt.event.*;

/**
 * the class of the point style chooser
 * @author ITRIS, Jordi SUC
 */
public class InterpolationChooser extends Widget{

	/**
	 * the chooser
	 */
	private LineInterpolationChooserWidget chooser=new LineInterpolationChooserWidget();
	
	/**
	 * the listener
	 */
	private ActionListener listener=null;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected InterpolationChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget(){
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(chooser);

		if(isEditor) {
			
			//adding the listener to the combo
			listener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					if(getItem()!=null){
						
						getItem().setValue(chooser.getValue());
					}
					
					//validating
					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			};
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		
		if(isEditor) {
			
			chooser.removeListener(listener);
		}
		
		chooser.init(getItem().getValue());
		
		if(isEditor) {
			
			chooser.addListener(listener);
		}
	}
}
