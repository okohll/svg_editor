package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import java.awt.*;

/**
 * the class used to configure the cursor on the trend box
 * @author ITRIS, Jordi SUC
 */
public class VerticalGridLinesPanel extends JPanel{
	
	/**
	 * the jwidget edition
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the panel used to configure the vertical grid lines divisions
	 */
	private VerticalGridLinesDivisionsPanel verticalGridLinesDivisionsPanel;
	
	/**
	 * the panel used to configure the vertical grid lines sub divisions
	 */
	private VerticalGridLinesSubDivisionsPanel verticalGridLinesSubDivisionsPanel;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
	public VerticalGridLinesPanel(JWidgetEdition jwidgetEdition) {
		
		this.jwidgetEdition=jwidgetEdition;
		build();
	}
	
	/**
	 * initializes the panel
	 * @param element an element
	 */
	public void initialize(Element element) {

		//initializing the sub panels
		verticalGridLinesDivisionsPanel.initialize(element);
		verticalGridLinesSubDivisionsPanel.initialize(element);
	}
	
	/**
	 * building the panel
	 */
	protected void build() {
		
		//getting the labels
		/*String verticalGridLinesLabel="";

		try {
			verticalGridLinesLabel=jwidgetEdition.getBundle().getString("verticalGridLines");
		}catch (Exception ex) {ex.printStackTrace();}*/
		
		//creating the vertical grid lines divisions panel
		verticalGridLinesDivisionsPanel=new VerticalGridLinesDivisionsPanel(jwidgetEdition);
		
		//creating the vertical grid lines sub divisions panel
		verticalGridLinesSubDivisionsPanel=new VerticalGridLinesSubDivisionsPanel(jwidgetEdition);
		
		//creating the panel used for handling the properties
		//TitledBorder border=new TitledBorder(verticalGridLinesLabel);
		//setBorder(border);
		
		GridBagLayout gridBag1=new GridBagLayout();
		setLayout(gridBag1);
		GridBagConstraints c1=new GridBagConstraints();
		c1.insets=new Insets(1, 1, 1, 1);
		c1.fill=GridBagConstraints.HORIZONTAL;
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(verticalGridLinesDivisionsPanel, c1);
		add(verticalGridLinesDivisionsPanel);
		
		c1.gridwidth=GridBagConstraints.REMAINDER;
		c1.anchor=GridBagConstraints.WEST;
		gridBag1.setConstraints(verticalGridLinesSubDivisionsPanel, c1);
		add(verticalGridLinesSubDivisionsPanel);
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		verticalGridLinesDivisionsPanel.setEnabled(enable);
		verticalGridLinesSubDivisionsPanel.setEnabled(enable);
	}
}
