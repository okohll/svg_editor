package fr.itris.glips.extension.jwidget.trends.runtime.view.component;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools.*;

/**
 * the class of the tool bar component
 * @author ITRIS, Jordi SUC
 */
public class ToolBarComponent extends JToolBar{

	/**
	 * the components handler
	 */
	private ComponentsHandler handler;
	
	/**
	 * the play tool
	 */
	private PlayTool playTool;
	
	/**
	 * the pause tool
	 */
	private PauseTool pauseTool;
	
	/**
	 * the switch mode tool
	 */
	private SwitchModeTool switchModeTool;
	
	/**
	 * the time zoom in tool
	 */
	private TimeZoomInTool timeZoomInTool;
	
	/**
	 * the regular cursor tool
	 */
	private RegularTool regularTool;
	
	/**
	 * the time zoom out tool
	 */
	private TimeZoomOutTool timeZoomOutTool;

	/**
	 * the mouse time zoom tool
	 */
	private MouseTimeZoomTool mouseTimeZoomTool;
	
	/**
	 * the time zoom dialog tool
	 */
	private TimeZoomDialogTool timeZoomDialogTool;
	
	/**
	 * the time initial zoom tool
	 */
	private TimeZoomInitialTool timeZoomInitialTool;

	/**
	 * the mouse zoom tool
	 */
	private MouseZoomTool mouseZoomTool;
	
	/**
	 * the vertical zoom initial tool
	 */
	private VerticalZoomInitialTool verticalZoomInitialTool;
	
	/**
	 * the tool used to display the curves properties frame
	 */
	private DisplayCurvesPropertiesTool displayCurvesPropertiesTool;
	
	/**
	 * the cursor line tool
	 */
	private CursorTool cursorLineTool;
	
	/**
	 * the cursor cross tool
	 */
	private CursorTool cursorCrossTool;
	
	/**
	 * the date pickers dialog tool
	 */
	private DatePickersDialogTool datePickersDialogTool;
	
	/**
	 * the mouse button group
	 */
	private ButtonGroup mouseButtonGroup;
	
	/**
	 * the constructor of the class
	 * @param componentsHandler the components handler
	 */
	public ToolBarComponent(ComponentsHandler componentsHandler) {

		setFloatable(true);
		this.handler=componentsHandler;
	}
	
	/**
	 * initializes this component
	 */
	public void initialize(){
		
		//creating the tools
		playTool=new PlayTool(this);
		pauseTool=new PauseTool(this);
		switchModeTool=new SwitchModeTool(this);
		regularTool=new RegularTool(this);
		timeZoomInTool=new TimeZoomInTool(this);
		timeZoomOutTool=new TimeZoomOutTool(this);
		timeZoomDialogTool=new TimeZoomDialogTool(this);
		timeZoomInitialTool=new TimeZoomInitialTool(this);
		mouseTimeZoomTool=new MouseTimeZoomTool(this);
		mouseZoomTool=new MouseZoomTool(this);
		verticalZoomInitialTool=new VerticalZoomInitialTool(this);
		displayCurvesPropertiesTool=new DisplayCurvesPropertiesTool(this);
		cursorLineTool=new CursorTool(this, false);
		cursorCrossTool=new CursorTool(this, true);
		datePickersDialogTool=new DatePickersDialogTool(this);
		
		//creating the button group for handling the state of the play/pause buttons
		ButtonGroup group=new ButtonGroup();
		group.add(playTool);
		group.add(pauseTool);
		
		//creating the button group for handling the state zoom/cursor buttons
		mouseButtonGroup=new ButtonGroup();
		mouseButtonGroup.add(regularTool);
		mouseButtonGroup.add(cursorLineTool);
		mouseButtonGroup.add(cursorCrossTool);
		
		//adding the buttons to the tool bar
		add(switchModeTool);
		addSeparator();
		add(playTool);
		add(pauseTool);
		addSeparator();
		add(datePickersDialogTool);
		addSeparator();
		add(regularTool);
		add(cursorLineTool);
		add(cursorCrossTool);
		addSeparator();
		add(timeZoomInitialTool);
		add(timeZoomInTool);
		add(timeZoomOutTool);
		add(timeZoomDialogTool);
		add(mouseTimeZoomTool);
		add(verticalZoomInitialTool);
		add(mouseZoomTool);
		addSeparator();
		add(displayCurvesPropertiesTool);
		
		//initializes some buttons
		timeZoomDialogTool.initialize();
		
		//selects the regular tool by default
		regularTool.select();
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){//TODO
		
		playTool.dispose();
		pauseTool.dispose();
		switchModeTool.dispose();
		datePickersDialogTool.dispose();
		regularTool.dispose();
		timeZoomInTool.dispose();
		timeZoomOutTool.dispose();
		timeZoomDialogTool.dispose();
		timeZoomInitialTool.dispose();
		mouseTimeZoomTool.dispose();
		mouseZoomTool.dispose();
		verticalZoomInitialTool.dispose();
		displayCurvesPropertiesTool.dispose();
		cursorLineTool.dispose();
		cursorCrossTool.dispose();
		
		
		playTool=null;
		pauseTool=null;
		switchModeTool=null;
		datePickersDialogTool=null;
		regularTool=null;
		timeZoomInTool=null;
		timeZoomOutTool=null;
		timeZoomDialogTool=null;
		timeZoomInitialTool=null;
		mouseTimeZoomTool=null;
		mouseZoomTool=null;
		verticalZoomInitialTool=null;
		displayCurvesPropertiesTool=null;
		cursorLineTool=null;
		cursorCrossTool=null;
		handler=null;
		mouseButtonGroup=null;
	}

	/**
	 * @return the display curves properties tool
	 */
	public DisplayCurvesPropertiesTool getDisplayCurvesPropertiesTool() {
		
		return displayCurvesPropertiesTool;
	}
	
	/**
	 * @return the cursor line tool
	 */
	public CursorTool getCursorLineTool() {
		return cursorLineTool;
	}
	
	/**
	 * @return the cursor cross tool
	 */
	public CursorTool getCursorCrossTool() {
		return cursorCrossTool;
	}

	/**
	 * deselects all the mouse tools
	 */
	public void deselectMouseTools(){

		mouseButtonGroup.remove(regularTool);
		mouseButtonGroup.remove(cursorLineTool);
		mouseButtonGroup.remove(cursorCrossTool);
		
		regularTool.setSelected(false);
		cursorLineTool.setSelected(false);
		cursorCrossTool.setSelected(false);
		
		mouseButtonGroup.add(regularTool);
		mouseButtonGroup.add(cursorLineTool);
		mouseButtonGroup.add(cursorCrossTool);
		
		regularTool.setSelected(true);
		handler.setCurrentExclusiveAction(RegularTool.id);
	}

	/**
	 * @return handler
	 */
	public ComponentsHandler getComponentsHandler() {
		return handler;
	}
	
	/**
	 * called when the current mode or sub mode has changed
	 */
	public void modeOrSubModeChanged(){
		
		playTool.modeOrSubModeChanged();
		pauseTool.modeOrSubModeChanged();
		switchModeTool.modeOrSubModeChanged();
		datePickersDialogTool.modeOrSubModeChanged();
	}
}
