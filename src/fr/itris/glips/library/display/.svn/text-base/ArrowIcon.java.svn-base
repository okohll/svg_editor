package fr.itris.glips.library.display;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import fr.itris.glips.library.*;

/**
 * the class enabling to create icons having an arrow in their right part
 * @author Jordi SUC
 */
public class ArrowIcon {

	/**
	 * the transparent color
	 */
	private static final Color transparentColor=new Color(0, 0, 0, 0);
	
	/**
	 * the default size of icons that should be painting in the icon
	 */
	public static final Dimension defaultIconSize=new Dimension(16, 16);
	
	/**
	 * the bounds of the arrow icon
	 */
	public static final Rectangle arrowIconBounds=
		new Rectangle(defaultIconSize.width, 0, 8, 7);
	
	/**
	 * the space between the user icon and the arrow icon
	 */
	public static final int offset=2;
	
	/**
	 * the image icon
	 */
	private ImageIcon icon;
	
	/**
	 * the disabled icon
	 */
	private ImageIcon disabledIcon;
	
	/**
	 * the image to create the icon
	 */
	private BufferedImage iconImage;
	
	/**
	 * the arrow image
	 */
	private Image arrowImage;
	
	/**
	 * the constructor of the class
	 */
	public ArrowIcon(){
		
		//getting the arrow icon
		arrowImage=Icons.getIcon("Arrow", false).getImage();
		
		//creating the image used to draw the whole icon
		iconImage=new BufferedImage(
			defaultIconSize.width+arrowIconBounds.width+offset, 
				defaultIconSize.height, BufferedImage.TYPE_INT_ARGB);
		
		//creating the icons
		icon=new ImageIcon(iconImage);
		disabledIcon=new ImageIcon();
		
		//drawing the arrow icon
		Graphics2D g=(Graphics2D)iconImage.getGraphics();
		g.drawImage(arrowImage, defaultIconSize.width+offset, 
				(defaultIconSize.height-arrowIconBounds.height)/2, null);
		
		g.dispose();
	}
	
	/**
	 * sets a new icon to paint in the icon of an instanceof this class
	 * @param newIcon the new icon
	 */
	public void setIcon(ImageIcon newIcon){
		
		Graphics2D g=(Graphics2D)iconImage.getGraphics();
		
		//clearing the image
		g.setComposite(AlphaComposite.Clear);
		g.setColor(transparentColor);
		g.fillRect(0, 0, defaultIconSize.width, defaultIconSize.height);
		
		if(newIcon!=null){
			
			//painting the new icon
			g.setComposite(AlphaComposite.DstOver);
			g.drawImage(newIcon.getImage(), 0, 0, null);
		}
		
		//creating the disabled icon
		disabledIcon=new ImageIcon(GrayFilter.createDisabledImage(iconImage));
		
		g.dispose();
	}
	
	/**
	 * @return the arrow image icon
	 */
	public ImageIcon getIcon(){
		
		return icon;
	}
	
	/**
	 * @return the arrow disabled image icon
	 */
	public ImageIcon getDisabledIcon(){
		
		return disabledIcon;
	}
}
