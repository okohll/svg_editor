package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.geom.*;
import org.w3c.dom.*;

/**
 * the class of the widget used to choose points on the canvas
 * @author ITRIS, Jordi SUC
 */
public class PointChooser extends Widget{
	
	/**
	 * the jlabels
	 */
	private JLabel xLbl, yLbl, xPxLbl, yPxLbl;
	
	/**
	 * the textfields
	 */
	private JTextField abscissTextField, ordinateTextField;
	
	/**
	 * the listener to the changes on the textfields
	 */
	private CaretListener caretListener;
	
	/**
	 * the button 
	 */
	private JButton button;
	
	/**
	 * the listener to the button
	 */
	private ActionListener actionListener;
	
	/**
	 * the listener used to chooser the point
	 */
	private AWTEventListener awtEventListener;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected PointChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String xLabel="", yLabel="", pxLabel="";
		
		try{
			xLabel=ResourcesManager.bundle.getString("rtdaanim_x");
			yLabel=ResourcesManager.bundle.getString("rtdaanim_y");
			pxLabel=ResourcesManager.bundle.getString("rtdaanim_px");
		}catch (Exception ex){}
		
		//the jlabels
		xLbl=new JLabel(xLabel+" :");
		xPxLbl=new JLabel(pxLabel);
		yLbl=new JLabel(yLabel+" :");
		yPxLbl=new JLabel(pxLabel);
		
		xLbl.setBorder(new EmptyBorder(0, 1, 0, 2));
		xPxLbl.setBorder(new EmptyBorder(0, 2, 0, 0));
		yLbl.setBorder(new EmptyBorder(0, 5, 0, 2));
		yPxLbl.setBorder(new EmptyBorder(0, 2, 0, 5));
		
		//the textfields
		abscissTextField=new JTextField(10);
		ordinateTextField=new JTextField(10);
		abscissTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		ordinateTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		abscissTextField.setOpaque(false);
		ordinateTextField.setOpaque(false);
		
		//the button
		//final ImageIcon pointChooserIcon=SVGResource.getIcon("PointChooserSmall", false);
		button=new JButton(/*pointChooserIcon*/"+");
		button.setMargin(new Insets(1, 1, 1, 1));
		
		//filling the panel
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(xLbl);
		add(abscissTextField);
		add(xPxLbl);
		add(yLbl);
		add(ordinateTextField);
		add(yPxLbl);
		add(button);
		
		if(isEditor) {
			
			//setting the selection color for the textfields
			Color selectionColor=new JTable().getSelectionBackground();
			abscissTextField.setSelectionColor(selectionColor);
			ordinateTextField.setSelectionColor(selectionColor);

			//the listener to the changes on the textfields
			caretListener=new CaretListener() {
				
				public void caretUpdate(CaretEvent e) {

					getItem().setValue(	abscissTextField.getText()+EditableItem.separator+
													ordinateTextField.getText()+EditableItem.separator);
					getItem().validateChanges();
				}
			};
			
			//the listener to the button
			actionListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					SwingUtilities.invokeLater(new Runnable() {
						
						public void run() {
							
							//getting the current canvas
				        	SVGCanvas canvas=Editor.getEditor().getHandlesManager().
										getCurrentHandle().getScrollPane().getSVGCanvas();
						
							Editor.getEditor().getSelectionManager().setToNoneMode();
							canvas.setSVGCursor(Cursor.getPredefinedCursor(
									Cursor.CROSSHAIR_CURSOR));
							Toolkit.getDefaultToolkit().addAWTEventListener(
									awtEventListener, AWTEvent.MOUSE_EVENT_MASK);
							button.removeActionListener(actionListener);
						}
					});
				}
			};
			
			awtEventListener=new AWTEventListener(){
				
	            public void eventDispatched(AWTEvent evt) {
	            	
	                if(evt instanceof MouseEvent){

	                    MouseEvent mevt=(MouseEvent)evt;
	                    mevt.consume();
	                    Point2D point=mevt.getPoint();
	                    
	                    if(mevt.getID()==MouseEvent.MOUSE_CLICKED){
	                    	
	    					//getting the current canvas
	    		        	SVGCanvas canvas=Editor.getEditor().getHandlesManager().
	    								getCurrentHandle().getScrollPane().getSVGCanvas();
	                        
	                        //converting the point
	                        point=SwingUtilities.convertPoint(
	                        	(Component)mevt.getSource(), 
	                        		new Point((int)point.getX(), (int)point.getY()), canvas);
	                        
	                        //converting the point if the align with rulers mode is activated
	                        point=canvas.getSVGHandle().getTransformsManager().
	                        	getAlignedWithRulersPoint(point, true);

	                        //relativizes and scales the selected point
	                        Point2D selectedPoint=setToRelative(point);

	                        //setting the new point
	                        String xTxt=FormatStore.format(selectedPoint.getX());
	                        String yTxt=FormatStore.format(selectedPoint.getY());

	    					getItem().setValue(xTxt+EditableItem.separator+
	    							yTxt+EditableItem.separator);
	    					getItem().validateChanges();
	    					
							//validating
							if(validateRunnable!=null) {
								
								validateRunnable.run();
							}
							
							//refreshing the points painter TODO
							Toolkit.getDefaultToolkit().removeAWTEventListener(this);
							canvas.setSVGCursor(Cursor.getDefaultCursor());
							Editor.getEditor().getSelectionManager().setToRegularMode();
	                    }
	                }
	            } 
	        };
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);

		if(isEditor){
			
			abscissTextField.removeCaretListener(caretListener);
			ordinateTextField.removeCaretListener(caretListener);
			button.removeActionListener(actionListener);
		}
		
		//getting the two values of the group item
		String value=item.getValue(), defaultValue=item.getDefaultValue();
		String[] defaultSplitValue=defaultValue.split(EditableItem.separatorRegex);
		String[] splitValue=value.split(EditableItem.separatorRegex);
		
		String value1=defaultSplitValue[0];
		String value2=defaultSplitValue[1];
		
		if(splitValue!=null && splitValue.length>1){
			
			value1=splitValue[0];
			value2=splitValue[1];
		}

		abscissTextField.setText(value1);
		ordinateTextField.setText(value2);
		
		//the colors for the labels
		String colorStr=currentItemReference.get().getColor();
		String[] colorSplitStrs=colorStr.split(EditableItem.separatorRegex);

		//getting the colors corresponding to these strings
		Color xcolor=Editor.getColorChooser().getColor(null, colorSplitStrs[0]);
		Color ycolor=Editor.getColorChooser().getColor(null, colorSplitStrs[1]);
		
		if(xcolor!=null && ycolor!=null) {
			
			xLbl.setForeground(xcolor);
			yLbl.setForeground(ycolor);
		}

		if(isEditor){
			
			abscissTextField.addCaretListener(caretListener);
			ordinateTextField.addCaretListener(caretListener);
			button.addActionListener(actionListener);
		}
	}
	
	/**
	 * relativizes the given point according to the upper left corner of the 
	 * representation of the parent node, the point is scaled to the 1.0 factor
	 * @param point a point
	 * @return the relativized point
	 */
	protected Point2D setToRelative(Point2D point){
		
		Point2D resPoint=null;
		Element parentNode=currentItemReference.get().getSVGElement();
		
		if(parentNode!=null){
			
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			Rectangle2D elementBounds=handle.getSvgElementsManager().
				getNodeGeometryBounds(parentNode);
			
			if(point!=null && elementBounds!=null){
				
				//scaling to base scale the point
				resPoint=handle.getTransformsManager().getScaledPoint(point, true);
				resPoint=new Point2D.Double(point.getX()-elementBounds.getX(), 
						point.getY()-elementBounds.getY());
			}
		}
		
		return resPoint;
	}
}
