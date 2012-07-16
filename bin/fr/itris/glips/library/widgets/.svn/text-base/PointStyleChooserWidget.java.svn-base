package fr.itris.glips.library.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class of the widget used to choose a point style by means of a combo box
 * @author ITRIS, Jordi SUC
 */
public class PointStyleChooserWidget extends Widget {

	/**
	 * the array of the possible point styles
	 */
	public static final String[] pointStyles=
						{"", "plus", "cross", "disc", "circle", "square", "rect", "filledTriangle", "triangle"};
	
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
	public PointStyleChooserWidget() {
		
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
		currentValue="";
		
		if(newValue==null){
			
			newValue="";
		}
		
		//removing the listeners from the widget
		combo.removeActionListener(comboListener);
		
		//selecting the combo item corresponding to the given value
		int i=0;
		
		for(String val : pointStyles){
			
			if(val.equals(newValue)){
				
				//selecting the corresponding combo item
				combo.setSelectedIndex(i);
				currentValue=pointStyles[i];
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
		ComboItem[] items=new ComboItem[pointStyles.length];
		int i=0;
		
		for(String value : pointStyles){
			
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
		 * the size of each point shape
		 */
		private final Dimension pointShapeSize=new Dimension(6, 6);
		
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
			
			final String pointType=stVal;

			//creating the panel that will be returned
			JPanel panel=new JPanel(){
				
				@Override
				protected void paintComponent(Graphics g) {
					
					super.paintComponent(g);
					
					//painting the point shape
					Graphics2D g2=(Graphics2D)g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.black);

					//computing the location at which the shape should be drawn
					Point location=new Point((getWidth()-pointShapeSize.width)/2, 
															(getHeight()-pointShapeSize.height)/2);

					if(pointType.equals("plus")){
						
						g2.drawLine(location.x+pointShapeSize.width/2, location.y, location.x+pointShapeSize.width/2, 
								location.y+pointShapeSize.height);
						
						g2.drawLine(location.x, location.y+pointShapeSize.height/2, location.x+pointShapeSize.width, 
								location.y+pointShapeSize.height/2);
						
					}else if(pointType.equals("cross")){
						
						g2.drawLine(location.x, location.y, location.x+pointShapeSize.width, 
								location.y+pointShapeSize.height);
						
						g2.drawLine(location.x+pointShapeSize.width, location.y, location.x, 
								location.y+pointShapeSize.height);
						
					}else if(pointType.equals("disc")){
						
						g2.fillOval(location.x, location.y, pointShapeSize.width, pointShapeSize.height);
						//g2.drawOval(location.x, location.y, pointShapeSize.width, pointShapeSize.height);
						
					}else if(pointType.equals("circle")){
						
						g2.drawOval(location.x, location.y, pointShapeSize.width, pointShapeSize.height);
						
					}else if(pointType.equals("square")){
						
						g2.fillRect(location.x, location.y, pointShapeSize.width, pointShapeSize.height);
						//g2.drawRect(location.x, location.y, pointShapeSize.width, pointShapeSize.height);
						
					}else if(pointType.equals("rect")){
						
						g2.drawRect(location.x, location.y, pointShapeSize.width, pointShapeSize.height);
						
					}else if(pointType.equals("filledTriangle") || pointType.equals("triangle")){
						
						int[] xPoints={location.x+pointShapeSize.width/2, location.x+pointShapeSize.width, location.x};
						int[] yPoints={location.y, location.y+pointShapeSize.height, location.y+pointShapeSize.height};
						int nPoints=3;
						
						if(pointType.equals("filledTriangle")){
							
							g2.fillPolygon(xPoints, yPoints, nPoints);
							//g2.drawPolygon(xPoints, yPoints, nPoints);
							
						}else{
							
							g2.drawPolygon(xPoints, yPoints, nPoints);
						}
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
	        
	        panel.setPreferredSize(new Dimension(17, 15));

			return panel;
		}
	}
}
