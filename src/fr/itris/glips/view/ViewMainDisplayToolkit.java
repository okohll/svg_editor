package fr.itris.glips.view;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the toolkit for the main display test
 * @author ITRIS, Jordi SUC
 */
public class ViewMainDisplayToolkit extends MainDisplayToolkit{

	/**
	 * the svg view object
	 */
	private View view;
	
	/**
	 * the constructor of the class
	 * @param view the svg view object
	 */
	public ViewMainDisplayToolkit(View view){
		
		this.view=view;
	}
	
	@Override
	public void quitProgram() {

		view.getMainDisplay().getAnimationsHandler().dispose();
		System.exit(0);
	}

	@Override
	public void refresh(SVGPicture currentPicture) {
		
		if(view.getRegularFrame()!=null && 
			view.getRegularFrame() instanceof JFrame && 
				currentPicture!=null){
			
			view.getRegularFrame().setPreferredSize(null);
			view.getRegularFrame().pack();
		}
	}

	@Override
	public Frame getTopLevelFrame() {
		
		return view.getRegularFrame();
	}
}
