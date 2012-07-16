package fr.itris.glips.library.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * the class of the widget used to choose an interpolation type by means of a combo box
 * @author ITRIS, Jordi SUC
 */
public class LineInterpolationChooserWidget extends Widget {

	/**
	 * the array of the possible interpolation types
	 */
	protected static String[] interpolationTypes={"dot", "square", "triangle"};
	
	/**
	 * the combo box
	 */
	private JComboBox combo;
	
	/**
	 * the listener to the combo box
	 */
	private ActionListener comboListener;
	
	/**
	 * the constructor of the class
	 */
	public LineInterpolationChooserWidget() {
		
		build();
	}
	
	@Override
	public void dispose(){
		
		super.dispose();
		combo.removeActionListener(comboListener);
	}
	
	/**
	 * initializes the widget with the given value
	 * @param newValue a new value
	 */
	public void init(String newValue){
		
		//reinitializing the value of the widget
		currentValue=interpolationTypes[0];
		
		if(newValue==null || newValue.equals("")){
			
			newValue=interpolationTypes[0];
		}
		
		//removing the listeners from the widget
		combo.removeActionListener(comboListener);
		
		//selecting the combo item corresponding to the given value
		int i=0;
		
		for(String val : interpolationTypes){
			
			if(val.equals(newValue)){
				
				//selecting the corresponding combo item
				combo.setSelectedIndex(i);
				currentValue=interpolationTypes[i];
				break;
			}
			
			i++;
		}
	
		//adding the listener to the widget
		combo.addActionListener(comboListener);
	}
	
	@Override
	protected void build(){
		
		//creating the combo box items
		ComboItem[] items=new ComboItem[interpolationTypes.length];
		int i=0;
		
		for(String value : interpolationTypes){
			
			items[i++]=new ComboItem(value);
		}
		
		//creating the combo box
		combo=new JComboBox(items);
		combo.setRenderer(new ComboCellRenderer());
		
		//creating the listener to the combo box
		comboListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				currentValue="";
				
				if(combo.getSelectedItem()!=null){
					
					currentValue=((ComboItem)combo.getSelectedItem()).getValue();
				}
				
				notifyListeners();
			}
		};
		
		//adding the widgets to the time chooser widget
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(combo);
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		combo.setEnabled(enable);
	}
	
	/**
	 * the class of the combo items
	 * @author ITRIS, Jordi SUC
	 */
	protected class ComboItem{
		
		/**
		 * the value and the label of the combo item
		 */
		private String value="";
		
		/**
		 * the constructor of the class
		 * @param value the value of the combo item
		 */
		private ComboItem(String value){
			
			this.value=value;
		}
		
		/**
		 * @return the value of the combo item
		 */
		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	/**
	 * the class of the combo cell renderer
	 * @author ITRIS, Jordi SUC
	 */
	protected class ComboCellRenderer implements ListCellRenderer{
		
		/**
		 * the size of each interpolation type shape
		 */
		private final Dimension drawingSize=new Dimension(18, 8);
		
		/**
		 * the path shape for showing an interpolation type
		 */
		private GeneralPath dotPath, squarePath, trianglePath, curvePath;
		
		/**
		 * the constructor of the class
		 */
		public ComboCellRenderer() {
			
			//creating the dot path
			dotPath=new GeneralPath();
			dotPath.moveTo(0, 0);
			dotPath.lineTo(0, 1);
			dotPath.moveTo(drawingSize.width/3, drawingSize.height);
			dotPath.lineTo(drawingSize.width/3, drawingSize.height-1);
			dotPath.moveTo(drawingSize.width/3*2, drawingSize.height/2);
			dotPath.lineTo(drawingSize.width/3*2, drawingSize.height/2-1);
			dotPath.moveTo(drawingSize.width, drawingSize.height);
			dotPath.lineTo(drawingSize.width, drawingSize.height-1);
			
			//creating the square path
			squarePath=new GeneralPath();
			squarePath.moveTo(0, 0);
			squarePath.lineTo(drawingSize.width/3, 0);
			squarePath.lineTo(drawingSize.width/3, drawingSize.height);
			squarePath.lineTo(drawingSize.width/3*2, drawingSize.height);
			squarePath.lineTo(drawingSize.width/3*2, drawingSize.height/2);
			squarePath.lineTo(drawingSize.width, drawingSize.height/2);
			squarePath.lineTo(drawingSize.width, drawingSize.height);
			
			//creating the triangle path
			trianglePath=new GeneralPath();
			trianglePath.moveTo(0, 0);
			trianglePath.lineTo(drawingSize.width/3, drawingSize.height);
			trianglePath.lineTo(drawingSize.width/3*2, drawingSize.height/2);
			trianglePath.lineTo(drawingSize.width, drawingSize.height);
			
			//creating the curve path
			curvePath=new GeneralPath();
			curvePath.moveTo(0, 0);
			curvePath.quadTo(	drawingSize.width/3, drawingSize.height/2*3, 
											drawingSize.width/2, drawingSize.height*3/4);
			curvePath.quadTo(	drawingSize.width/3*2, 0, 
											drawingSize.width, drawingSize.height);
		}
		
		/**
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(JList list, Object value, int index, 
																							boolean isSelected, boolean cellHasFocus) {
			
			//getting the value for the item
			String stVal="";
			
			if(value!=null){
				
				stVal=((ComboItem)value).getValue();
				stVal=stVal.trim();
			}
			
			final String interpolationType=stVal;

			//creating the panel that will be returned
			JPanel panel=new JPanel(){
				
				@Override
				protected void paintComponent(Graphics g) {
					
					super.paintComponent(g);
					
					//painting the line with a stroke or not
					Graphics2D g2=(Graphics2D)g.create();
					g2.setColor(Color.black);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
					//computing the location at which the shape should be drawn
					Point location=new Point((getWidth()-drawingSize.width)/2, 
															(getHeight()-drawingSize.height)/2);
					
					//getting the shape to draw
					Shape shape=null;
					
					if(interpolationType.equals("dot")){
						
						shape=dotPath;
						
					}else if(interpolationType.equals("square")){
						
						shape=squarePath;
						
					}else if(interpolationType.equals("triangle")){
						
						shape=trianglePath;
						
					}else if(interpolationType.equals("curve")){
						
						shape=curvePath;
					}
					
					if(shape!=null){
						
						//transforming the shape so that it is located at the accurate position
						AffineTransform af=AffineTransform.getTranslateInstance(location.x, location.y);
						
						//applying the affine transform to the shape
						try{
							shape=af.createTransformedShape(shape);
						}catch (Exception ex){}
						
						//drawing the shape
						g2.draw(shape);
					}

					g2.dispose();
				}
			};
			
			//handling the colors
	        if (isSelected) {
	        	
	        	panel.setBackground(list.getSelectionBackground());
	        	panel.setForeground(list.getSelectionForeground());
	            
	        }else {
	        	
	        	panel.setBackground(list.getBackground());
	        	panel.setForeground(list.getForeground());
	        }
	        
	        panel.setPreferredSize(new Dimension(17, 17));

			return panel;
		}
	}
}
