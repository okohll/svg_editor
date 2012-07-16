package fr.itris.glips.library.widgets;

import javax.swing.*;
import fr.itris.glips.library.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class of the widget used to choose an interpolation type by means of a combo box
 * @author ITRIS, Jordi SUC
 */
public class EqualityChooserWidget extends Widget {

	/**
	 * the array of the possible equality types
	 */
	public static String[] equalityTypes={"<", "<=", "=", ">=", ">"};
	
	/**
	 * the icons
	 */
	private static ImageIcon[] icons=new ImageIcon[equalityTypes.length];
	
	static{
		
		//loading the icons
		icons[0]=Icons.getIcon("Inf", false);
		icons[1]=Icons.getIcon("InfEq", false);
		icons[2]=Icons.getIcon("Eq", false);
		icons[3]=Icons.getIcon("SupEq", false);
		icons[4]=Icons.getIcon("Sup", false);
	}
	
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
	public EqualityChooserWidget() {
		
		build();
	}
	
	/**
	 * checks whether the value1 and the value2 respect the equality mode
	 * @param value1 a value
	 * @param value2 a value
	 * @param equalityMode the equality mode, ie : "<", "<=", "=", ">=", ">"
	 * @return whether the value1 and the value2 respect the equality mode
	 */
	public static boolean checkCondition(
			double value1, double value2, String equalityMode){
		
		if(equalityMode.equals(equalityTypes[0])){
			
			return value1<value2;
		}
		
		if(equalityMode.equals(equalityTypes[1])){
			
			return value1<=value2;	
		}
		
		if(equalityMode.equals(equalityTypes[2])){
			
			return value1==value2;	
		}
		
		if(equalityMode.equals(equalityTypes[3])){
			
			return value1>=value2;	
		}
		
		if(equalityMode.equals(equalityTypes[4])){
			
			return value1>value2;
		}
		
		return false;
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
		currentValue=equalityTypes[0];
		
		if(newValue==null || newValue.equals("")){
			
			newValue=equalityTypes[0];
		}
		
		//removing the listeners from the widget
		combo.removeActionListener(comboListener);
		
		//selecting the combo item corresponding to the given value
		int i=0;
		
		for(String val : equalityTypes){
			
			if(val.equals(newValue)){
				
				//selecting the corresponding combo item
				combo.setSelectedIndex(i);
				currentValue=equalityTypes[i];
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
		ComboItem[] items=new ComboItem[equalityTypes.length];
		int i=0;
		
		for(String value : equalityTypes){
			
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
		 * the constructor of the class
		 */
		public ComboCellRenderer() {
			

		}
		
		/**
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(
				JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			//getting the value for the item
			String stVal="";
			
			if(value!=null){
				
				stVal=((ComboItem)value).getValue();
				stVal=stVal.trim();
			}
			
			final String equalityType=stVal;

			//creating the panel that will be returned
			JPanel panel=new JPanel(){
				
				@Override
				protected void paintComponent(Graphics g) {
					
					super.paintComponent(g);
					
					//painting the line with a stroke or not
					Graphics2D g2=(Graphics2D)g.create();
					g2.setColor(Color.black);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
							RenderingHints.VALUE_ANTIALIAS_ON);
					
					//computing the location at which the image should be drawn
					Point location=new Point((getWidth()-drawingSize.width)/2, 
															(getHeight()-drawingSize.height)/2);
					
					//getting the image to draw
					Image image=null;
					
					if(equalityType.equals(equalityTypes[0])){
						
						image=icons[0].getImage();
						
					}else if(equalityType.equals(equalityTypes[1])){
						
						image=icons[1].getImage();
						
					}else if(equalityType.equals(equalityTypes[2])){
						
						image=icons[2].getImage();
						
					}else if(equalityType.equals(equalityTypes[3])){
						
						image=icons[3].getImage();
						
					}else if(equalityType.equals(equalityTypes[4])){
						
						image=icons[4].getImage();
					}
					
					if(image!=null){
						
						//drawing the image
						g2.drawImage(image, location.x, location.y, null);
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
