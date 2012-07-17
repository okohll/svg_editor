package fr.itris.glips.library.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the widget used to choose a line dash by means of a combo box
 * @author ITRIS, Jordi SUC
 */
public class DashChooserWidget extends Widget {

	/**
	 * the array of the possible line dashes
	 */
	protected static String[] strokes={"", "5 2", "2 3", "3 1 1 1", "3 1 1 1 1 1"};
	
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
	public DashChooserWidget() {
		
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
		
		for(String val : strokes){
			
			if(val.equals(newValue)){
				
				//selecting the corresponding combo item
				combo.setSelectedIndex(i);
				currentValue=strokes[i];
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
		ComboItem[] items=new ComboItem[strokes.length];
		int i=0;
		
		for(String value : strokes){
			
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
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(
				JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			//getting the stroke value for the item
			String stVal="";
			
			if(value!=null){
				
				stVal=((ComboItem)value).getValue();
				stVal=stVal.trim();
			}

			float[] dashes=getDashes(stVal);
			
			//creating the stroke object
			Stroke stroke=null;
			
			if(dashes.length>0){
				
				stroke=new BasicStroke(0.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_ROUND, 1.0f, dashes, 0.0f);
			}
			
			final Stroke fstroke=stroke;
			
			//creating the panel that will be returned
			JPanel panel=new JPanel(){
				
				@Override
				protected void paintComponent(Graphics g) {
					
					super.paintComponent(g);
					
					//painting the line with a stroke or not
					Graphics2D g2=(Graphics2D)g.create();
					g2.setColor(Color.black);
					
					if(fstroke!=null){
						
						g2.setStroke(fstroke);
					}

					g2.drawLine(4, getHeight()/2, getWidth()-4, getHeight()/2);
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
	        
	        panel.setPreferredSize(new Dimension(30, 12));

			return panel;
		}
	}
	
	/**
	 * returns the array of dashes corresponding to the provided string
	 * @param value a string 
	 * @return the array of the dash factors
	 */
	public static float[] getDashes(String value){
		
		//computing the list of the dash numbers
		LinkedList<Float> dashes=new LinkedList<Float>();
		String val="";
		float dash=0.0f;
		int pos=0;
		
		while(value.length()>0){
			
			pos=value.indexOf(" ");
			
			//getting a dash string
			if(pos!=-1){
				
				val=value.substring(0, pos);
				value=value.substring(pos+1, value.length());
				
			}else{
				
				val=value;
				value="";
			}
		
			//computing the float value of the found dash string
			try{
				dash=Float.parseFloat(val);
			}catch (NumberFormatException ex) {
				System.err.println("Error parsing float " + val + ": " + ex);
				ex.printStackTrace();
				dash=Float.NaN;
			}
			
			if(! Float.isNaN(dash)){
				
				dashes.add(dash);
			}
		}
		
		//creating the array of the float numbers
		float[] dashesArray=new float[dashes.size()];
		
		int i=0;
		
		for(float dsh : dashes){
			
			dashesArray[i++]=dsh;
		}
		
		return dashesArray;
	}
}
