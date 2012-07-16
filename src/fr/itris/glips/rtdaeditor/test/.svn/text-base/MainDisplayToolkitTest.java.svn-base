package fr.itris.glips.rtdaeditor.test;

import java.awt.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtdaeditor.test.display.*;

/**
 * the class of the toolkit for the main display test
 * @author ITRIS, Jordi SUC
 */
public class MainDisplayToolkitTest extends MainDisplayToolkit{

	/**
	 * the dialog test
	 */
	private DialogTest dialogTest;
	
	/**
	 * the constructor of the class
	 * @param dialogTest the dialog for the dialog
	 */
	public MainDisplayToolkitTest(DialogTest dialogTest) {
		
		this.dialogTest=dialogTest;
	}
	
	@Override
	public void quitProgram() {

		dialogTest.refreshDialogState(false);
	}

	@Override
	public void refresh(SVGPicture currentPicture) {

		dialogTest.refreshSimulationValuesPanel();
	}

	@Override
	public Frame getTopLevelFrame() {

		return dialogTest.getFrame();
	}

	/**
	 * @return the dialogTest
	 */
	public DialogTest getDialogTest() {
		return dialogTest;
	}
}
