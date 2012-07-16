package fr.itris.glips.library.widgets;

import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import java.awt.event.*;
import java.awt.*;

/**
 * the class of the component used to choose a color
 * @author ITRIS, Jordi SUC
 */
public class ColorChooserWidget extends Widget{
	
	/**
	 * the color chooser
	 */
	private static JColorChooser colorChooser=new JColorChooser();

	/**
	 * the button
	 */
	private JButton button;
	
	/**
	 * the listener to the button
	 */
	private ActionListener colorButtonListener;
	
	/**
	 * the current color
	 */
	private Color currentColor=Color.black;

	/**
	 * the preference size of the button
	 */
	private Dimension prefSize=new Dimension(24, 16);
	
	/**
	 * the constructor of the class
	 */
	public ColorChooserWidget() {

		build();
	}
	
	/**
	 * initializes the widget with the given value
	 * @param objectValue a new value
	 */
	public void init(Object objectValue){
		
		button.removeActionListener(colorButtonListener);
		
		if(objectValue!=null) {
			
			if(objectValue instanceof Color){
				
				currentColor=(Color)objectValue;
				currentValue=ColorsAndBlinkingsToolkit.getColorString(currentColor);
				
			}else{
				
				String colorString=objectValue.toString();
				
				if(colorString.equals("")){
					
					currentColor=Color.black;
					currentValue="#000000";
					
				}else{
					
					Color color=ColorsAndBlinkingsToolkit.getColor(colorString);
					
					if(color!=null) {
						
						currentColor=color;
						currentValue=colorString;
					}
				}
			}
		}
		
		button.repaint();
		button.addActionListener(colorButtonListener);
	}
	
	@Override
	protected void build() {
		
		//creating the button
		button=new JButton(){
			
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				
				if(currentColor!=null){
					
					int diff=3;
					
					g.setColor(currentColor);
					g.fillRect(diff, diff, prefSize.width-2*diff, prefSize.height-2*diff);
				}
			}
		};
		
		//setting the properties of the button
		Insets buttonInsets=new Insets(0, 0, 0, 0);
		button.setMargin(buttonInsets);
		button.setPreferredSize(prefSize);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setSize(prefSize);
		setBackground(Color.red);
		
		//creating the listener to the button
		colorButtonListener=new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				//showing the color chooser
            	Color color=showColorChooserDialog(currentColor);

				if(color!=null){
					
					currentColor=color;
					currentValue=ColorsAndBlinkingsToolkit.getColorString(currentColor);
					notifyListeners();
				}
			}
		};
		
		//adding the button to the widget
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(button);
	}

	/**
	 * @return the current color
	 */
	public Color getCurrentColor() {
		
		return currentColor;
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		button.setEnabled(enable);
	}
	
	@Override
	public void dispose() {
		
		super.dispose();
		button.removeActionListener(colorButtonListener);
	}
	
    /**
     * shows a color chooser dialog
     * @param initialColor the initial Color set when the color-chooser is shown
     * @return the selected color or <code>null</code> if the user opted out
     */
    public Color showColorChooserDialog(Color initialColor) {

    	colorChooser.setColor(initialColor);
        SVGColorTracker ok = new SVGColorTracker(colorChooser);
        JDialog dialog=JColorChooser.createDialog(
        			getTopLevelAncestor(), "", true, colorChooser, ok, null);
        dialog.setVisible(true);

        return ok.getColor();
    }
    
    /**
     * the class of the svg color tracker
     * @author ITRIS, Jordi SUC
     */
    protected class SVGColorTracker implements ActionListener {
    	
    	/**
    	 * the color chooser
    	 */
       private JColorChooser chooser;
        
       /**
        * the color
        */
       private Color color;

       /**
        * the constructor of the class
        * @param c the color chooser
        */
        public SVGColorTracker(JColorChooser c) {
        	
            chooser=c;
        }

        public void actionPerformed(ActionEvent e) {
            color=chooser.getColor();
        }

        /**
         * @return the color
         */
        public Color getColor() {
            return color;
        }
    }
}
