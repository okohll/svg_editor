package fr.itris.glips.extension.jwidget.trends.runtime.configuration;

import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;
import java.text.*;
import java.util.*;
import java.awt.*;

/**
 * the class of the objects that store and provide the configuration 
 * values for the trends component
 * @author ITRIS, Jordi SUC
 */
public class TrendsConfiguration {
	
	/**
	 * the real time mode constant
	 */
	public static final int REAL_TIME_MODE=0;
	
	/**
	 * the history mode constant
	 */
	public static final int HISTORY_MODE=1;
	
	/**
	 * the UPDATE sub mode
	 */
	public static final int UPDATE=0;
	
	/**
	 * the SCROLL sub mode
	 */
	public static final int SCROLL=1;
	
	/**
	 * the map associating the string representation of a unit to its 
	 * corresponding number of milliseconds
	 */
	protected static Map<String, Long> timeUnitsToMs=new HashMap<String, Long>();
	
    /**
     * the decimal format
     */
	public static DecimalFormat format;
	
	static{
		
		//filling the map of the time units
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[0], 31536000000L);
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[1], 2592000000L);
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[2], 86400000L);
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[3], 3600000L);
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[4], 60000L);
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[5], 1000L);
		timeUnitsToMs.put(TimeChooserWidget.timeUnits[6], 1L);
		
		//creating a decimal format object
		DecimalFormatSymbols symbols=new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format=new DecimalFormat("############.################", symbols);
	}
	
	/**
	 * the controller
	 */
	private TrendsRuntimeController controller;
	
	/**
	 * the jwidget element
	 */
	private Element jwidgetElement;
	
	/**
	 * the list of the curves configuration object
	 */
	private LinkedList<TrendsCurveConfiguration> curvesConfigurationList=
				new LinkedList<TrendsCurveConfiguration>();
	
	/**
	 * the map associating a tag name to a trends curve configuration object
	 */
	private Map<String, TrendsCurveConfiguration> curvesConfigurationMap=
				new HashMap<String, TrendsCurveConfiguration>();
	
	/**
	 * the set of the listeners
	 */
	private Set<ConfigurationChangeListener> listeners=
		new HashSet<ConfigurationChangeListener>();
	
	/**
	 * the time that will be displayed in the trends window 
	 */
	private long duration=0;
	
	/**
	 * the refresh period
	 */
	private long refreshPeriod=0;
	
	/**
	 * the number of points that each buffer will contain
	 */
	private int bufferPointsNumber=0;
	
	/**
	 * the maximum time that can be displayed when in real time mode
	 */
	private long maxDisplayableTime=0;
	
	/**
	 * the start mode
	 */
	private int startMode;
	
	/**
	 * the current mode
	 */
	private int currentMode;
	
	/**
	 * the current sub mode
	 */
	private int currentSubMode;
	
	/**
	 * the background color
	 */
	private Color backgroundColor=Color.black;
	
	/**
	 * the font
	 */
	private Font font=new Font("trendsSmallFont", Font.ROMAN_BASELINE, 12);
	
	/**
	 * the distance between two vertical grid division lines in milliseconds
	 */
	private long verticalLinesDivisionDuration=0;
	
	/**
	 * the color of the grid vertical division lines
	 */
	private Color verticalLinesDivisionColor=Color.black;
	
	/**
	 * the distance between two vertical grid sub division lines in milliseconds
	 */
	private long verticalLinesSubDivisionDuration=0;
	
	/**
	 * the color of the grid vertical sub division lines
	 */
	private Color verticalLinesSubDivisionColor=Color.black;
	
	/**
	 * the number of digits of the decimal part that should be displayed 
	 * when displaying a string representation of a value of a tag
	 */
	private int valuesDecimalDigitsNumber=0;
	
	/**
	 * the higher limit from which the values should be written with an exponent
	 */
	private double higherLimit=0;
	
	/**
	 * the lower limit under which the values should be written with an exponent
	 */
	private double lowerLimit=0;
	
	/**
	 * whether to keep on recording the tag values of the real time mode 
	 * when switching to the history mode
	 */
	private boolean keepTagRecordInHistory;
	
	/**
	 * the vertical zoom factor
	 */
	private double verticalZoomFactor=1;
	
	/**
	 * the vertical zoom origin
	 */
	private double verticalZoomOrigin=0;
	
	/**
	 * the trends curve configuration corresponding to the curve whose grid horizontal lines
	 * and/or cursor value should be displayed
	 */
	private TrendsCurveConfiguration trendsCurveConfiguration;
	
	/**
	 * whether the handled canvases are widgets
	 */
	private boolean isWidget;
	
	/**
	 * the configuration document for the current canvases
	 */
	private ConfigurationDocument confDoc;
	
	/**
	 * whether the event dispatching is true
	 */
	private boolean allowEventDispatch=true;
	
	/**
	 * the start and end date tags
	 */
	private String startDateTag="", endDateTag="";
	
	/**
	 * whether to display the toolbar
	 */
	private boolean displayToolBar=true;

	/**
	 * the constructor of the class
	 * @param controller the trends runtime controller
	 * @param jwidgetElement the jwidget element defining the configuration
	 */
	public TrendsConfiguration(TrendsRuntimeController controller, Element jwidgetElement){
		
		this.controller=controller;
		this.jwidgetElement=jwidgetElement;
		
		SVGPicture picture=controller.getJwidgetRuntime().getPicture();
		confDoc=picture.getMainDisplay().getPictureManager().getConfigurationDocument(picture);
		isWidget=confDoc.isWidget();
		
		computeValues();
		createCurvesConfigurationObjects();
	}
	
	/**
	 * retrieves and computes all the non string values that are specified in the jwidget element
	 */
	protected void computeValues(){
		
		//getting the time that will be displayed in the trends window
		duration=getTime(jwidgetElement.getAttribute("duration"));
		
		//getting the refresh period
		refreshPeriod=getTime(jwidgetElement.getAttribute("refreshPeriod"));
		
		//getting the number of points that each buffer will contain
		try{
			bufferPointsNumber=Integer.parseInt(jwidgetElement.getAttribute("pointsNumber"));
		}catch (Exception ex){}
		
		//getting the maximum time that can be displayed when in real time mode
		maxDisplayableTime=getTime(jwidgetElement.getAttribute("maxDisplayableTime"));
		
		//getting the start mode
		if(isWidget){
			
			startMode=REAL_TIME_MODE;
			
		}else{
			
			String startModeStr=jwidgetElement.getAttribute("startMode");

			if(startModeStr.equals("realTime")){
				
				startMode=REAL_TIME_MODE;
				
			}else if(startModeStr.equals("history")){
				
				startMode=HISTORY_MODE;
			}
		}

		//handling the mode and the submode
		currentMode=startMode;
		
		if(currentMode==REAL_TIME_MODE){
			
			currentSubMode=UPDATE;
			
		}else{
			
			currentSubMode=SCROLL;
		}
		
		//getting the background color
		backgroundColor=ColorsAndBlinkingsToolkit.getColor(jwidgetElement.getAttribute("backgroundColor"));
		
		//getting the font
		try{
			float fontSize=Float.parseFloat(jwidgetElement.getAttribute("fontSize"));
			font=font.deriveFont(fontSize);
		}catch (Exception ex){}
		
		//getting the distance between two vertical grid division lines in milliseconds
		verticalLinesDivisionDuration=getTime(jwidgetElement.getAttribute("verticalLinesDivisionDuration"));
		
		//getting the color of the grid vertical division lines
		verticalLinesDivisionColor=ColorsAndBlinkingsToolkit.
					getColor(jwidgetElement.getAttribute("verticalLinesDivisionColor"));
		
		//getting the distance between two vertical grid sub division lines in milliseconds
		verticalLinesSubDivisionDuration=getTime(jwidgetElement.getAttribute("verticalLinesSubDivisionDuration"));
		
		//getting the color of the grid vertical sub division lines
		verticalLinesSubDivisionColor=ColorsAndBlinkingsToolkit.
					getColor(jwidgetElement.getAttribute("verticalLinesSubDivisionColor"));
		
		//getting the number of digits of the decimal part that should be displayed 
		//when displaying a string representation of a value of a tag
		try{
			valuesDecimalDigitsNumber=Integer.parseInt(jwidgetElement.getAttribute("decimalDigitsNumber"));
		}catch (Exception ex){valuesDecimalDigitsNumber=0;}
		
		//getting the higher limit from which the values should be written with an exponent
		try{
			higherLimit=format.parse(jwidgetElement.getAttribute("higherLimit")).doubleValue();
		}catch (Exception ex){}
		
		//getting the lower limit under which the values should be written with an exponent
		try{
			lowerLimit=format.parse(jwidgetElement.getAttribute("lowerLimit")).doubleValue();
		}catch (Exception ex){}
		
		keepTagRecordInHistory=
			Boolean.parseBoolean(jwidgetElement.getAttribute("keepTagRecordInHistory"));
		
		//getting the start and end date tags
		startDateTag=jwidgetElement.getAttribute("tag1");
		endDateTag=jwidgetElement.getAttribute("tag2");
		controller.registerTag(startDateTag, false);
		controller.registerTag(endDateTag, false);
		
		displayToolBar=Boolean.parseBoolean(
				jwidgetElement.getAttribute("displayToolBar"));
	}
	
	/**
	 * @return whether the handled canvases are widgets
	 */
	public boolean isWidget() {
		return isWidget;
	}

	/**
	 * @return the configuration document
	 */
	public ConfigurationDocument getConfigurationDocument() {
		return confDoc;
	}

	/**
	 * creates the configuration objecst for each curve
	 */
	protected void createCurvesConfigurationObjects(){
		
		//getting all the curves nodes associated to the jwidget element
		NodeList childNodes=jwidgetElement.getParentNode().getChildNodes();
		Node node;
		Element element;
		TrendsCurveConfiguration trendsCurveConfig=null;
		
		for(int i=0; i<childNodes.getLength(); i++){
			
			node=childNodes.item(i);

			if(node!=null && node instanceof Element && 
				! node.getNodeName().equals(JWidgetRuntime.jwidgetTagName) &&
					node.getNodeName().startsWith(AnimationsToolkit.rtdaPrefix)){

				element=(Element)node;
				trendsCurveConfig=new TrendsCurveConfiguration(controller, this, element);
				curvesConfigurationList.add(trendsCurveConfig);
				curvesConfigurationMap.put(trendsCurveConfig.getTagName(), trendsCurveConfig);
			}
		}
		
		computeCurrentTrendsCurveConfiguration();
	}
	
	/**
	 * computes the trends curve configuration corresponding to the curve whose grid horizontal lines
	 * and/or cursor value should be displayed
	 */
	protected void computeCurrentTrendsCurveConfiguration(){
		
		TrendsCurveConfiguration theConfig=null;

		//getting the curve configuration whose scale is displayed
		LinkedList<TrendsCurveConfiguration> displayedScaleCurves=
			new LinkedList<TrendsCurveConfiguration>();
		
		for(TrendsCurveConfiguration config : curvesConfigurationList){
			
			if(config.displayScale()){
				
				displayedScaleCurves.add(config);
			}
		}
		
		//getting the last curve config of the list
		if(displayedScaleCurves.size()>0){
			
			theConfig=displayedScaleCurves.getLast();
		}
		
		//getting the first configuration object for the curve
		if(theConfig==null && curvesConfigurationList.size()>0){
			
			theConfig=curvesConfigurationList.getFirst();
		}
		
		trendsCurveConfiguration=theConfig;
	}
	
	/**
	 * @return the trends curve configuration corresponding to the curve whose grid horizontal lines
	 * and/or cursor value should be displayed
	 */
	public TrendsCurveConfiguration getTrendsCurveConfiguration() {
		return trendsCurveConfiguration;
	}
	
	/**
	 * returns the trends curve configuration corresponding to the given tag name
	 * @param tagName a tag name
	 * @return the trends curve configuration corresponding to the given tag name
	 */
	public TrendsCurveConfiguration getTrendsCurveConfiguration(String tagName){
		
		return curvesConfigurationMap.get(tagName);
	}
	
	/**
	 * adds a new listener to the list of the listeners
	 * @param listener a new configuration change listener
	 */
	public void addListener(ConfigurationChangeListener listener){
		
		if(listener!=null){
			
			listeners.add(listener);
		}
	}
	
	/**
	 * removes a listener from the list of the listeners
	 * @param listener a configuration change listener
	 */
	public void removeListener(ConfigurationChangeListener listener){
		
		if(listener!=null){
			
			listeners.remove(listener);
		}
	}
	
	/**
	 * @return the current mode
	 */
	public int getCurrentMode() {
		return currentMode;
	}

	/**
	 * sets the new current mode
	 * @param currentMode the current mode
	 */
	public void setCurrentMode(int currentMode) {
		
		if(this.currentMode!=currentMode){
			
			this.currentMode=currentMode;
			notifyModeOrSubModeChanged();
		}
	}
	
	/**
	 * @return the current submode
	 */
	public int getCurrentSubMode() {
		return currentSubMode;
	}

	/**
	 * sets the new current submode
	 * @param currentSubMode the current submode
	 */
	public synchronized void setCurrentSubMode(int currentSubMode) {
		
		if(this.currentSubMode!=currentSubMode){
			
			this.currentSubMode=currentSubMode;
			notifyModeOrSubModeChanged();
		}
	}
	
	/**
	 * notifies that the mode or the submode has been changed
	 */
	protected void notifyModeOrSubModeChanged(){
		
		if(allowEventDispatch){
			
			for(ConfigurationChangeListener listener : listeners){
			
				listener.modeOrSubModeChanged();
			}
		}
	}
	
	/**
	 * notifies that changes have been done
	 */
	protected void notifyDurationChanged(){
		
		if(allowEventDispatch){
			
			for(ConfigurationChangeListener listener : listeners){
				
				listener.durationChanged();
			}
		}
	}

	/**
	 * @return whether to allow or not the dispacth 
	 * of configuration modification events
	 */
	public boolean allowEventDispatch() {
		return allowEventDispatch;
	}

	/**
	 * sets whether to allow or not the dispatch 
	 * of configuration modification events
	 * @param allowEventDispatch whether to allow or not the dispacth 
	 * of configuration modification events
	 */
	public void setAllowEventDispatch(boolean allowEventDispatch) {
		this.allowEventDispatch = allowEventDispatch;
	}

	/**
	 * @return the time that will be displayed in the trends window in milliseconds
	 */
	public long getHorizontalAxisDuration(){
		
		return duration;
	}
	
	/**
	 * sets a new duration for the horizontal axis
	 * @param duration a duration
	 */
	public synchronized void setHorizontalAxisDuration(long duration){
	
		this.duration=duration;
		notifyDurationChanged();
	}
	
	/**
	 * sets a new duration for the horizontal axis
	 * @param durationStr the string representation of a duration
	 */
	public synchronized void setHorizontalAxisDuration(String durationStr){
	
		//getting the time that will be displayed in the trends window
		duration=getTime(durationStr);
		notifyDurationChanged();
	}

	/**
	 * @return the initial time that will be displayed in the trends 
	 * 				window in milliseconds
	 */
	public long getInitialHorizontalAxisDuration(){
		
		return getTime(jwidgetElement.getAttribute("duration"));
	}
	
	/**
	 * @return the vertical zoom factor
	 */
	public double getVerticalZoomFactor() {
		
		return verticalZoomFactor;
	}

	/**
	 * sets the new vertical zoom factor
	 * @param verticalZoomFactor the new vertical zoom factor
	 * @param notify whether to notify or not that the zoom factor has changed
	 */
	public void setVerticalZoomFactor(double verticalZoomFactor, boolean notify) {
		
		this.verticalZoomFactor=verticalZoomFactor;
		notifyVerticalZoomChanged();
	}
	
	/**
	 * @return the vertical origin
	 */
	public double getVerticalZoomOrigin() {
		
		return verticalZoomOrigin;
	}

	/**
	 * sets the new vertical zoom origin
	 * @param verticalZoomOrigin the new vertical zoom origin
	 */
	public void setVerticalZoomOrigin(double verticalZoomOrigin) {
		
		this.verticalZoomOrigin=verticalZoomOrigin;
	}
	
	/**
	 * notifies that changes have been done
	 */
	protected void notifyVerticalZoomChanged(){
		
		for(ConfigurationChangeListener listener : listeners){
			
			listener.verticalZoomChanged();
		}
	}
	
	/**
	 * @return whether to display the toolbar
	 */
	public boolean displayToolBar() {
		return displayToolBar;
	}

	/**
	 * reinitializes the values of the properties that have been changed
	 */
	public void reinitialize(){
		
		//getting the time that will be displayed in the trends window
		duration=getTime(jwidgetElement.getAttribute("duration"));
		
		currentMode=startMode;
		verticalZoomOrigin=0;
		verticalZoomFactor=1.0;
		notifyDurationChanged();
	}
	
	/** 
	 * @return the list of the tag names used in this trends widget
	 */
	public Set<String> getTagNames() {
		
		return curvesConfigurationMap.keySet();
	}

	/**
	 * @return the list of the curves configuration object
	 */
	public LinkedList<TrendsCurveConfiguration> getCurvesConfigurationList() {
		
		return curvesConfigurationList;
	}
	
	/**
	 * @return whether to display the time graduations
	 */
	public boolean displayHorizontalAxisTimeGraduations() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("displayTimeGrad"));
	}
	
	/**
	 * @return whether to display the horodates on the horizontal axis
	 */
	public boolean displayHorizontalAxisHorodates() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("displayHorizHorodate"));
	}
	
	/**
	 * @return the format of the horodates on the horizontal axis
	 */
	public String getHorizontalAxisHorodatesFormat(){
		
		return jwidgetElement.getAttribute("horizHorodateFormat");
	}
	
	/**
	 * @return whether to display the horodates under the cursor
	 */
	public boolean displayCursorHorodates() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("displayTimeGrad"));
	}
	
	/**
	 * @return the format of the horodates under the cursor
	 */
	public String getCursorHorodatesFormat(){
		
		return jwidgetElement.getAttribute("cursorHorodateFormat");
	}
	
	/**
	 * @return the refresh period
	 */
	public long getRefreshPeriod(){
		
		return refreshPeriod;
	}
	
	/**
	 * @return the number of points that each buffer will contain
	 */
	public int getBufferPointsNumber(){
		
		return bufferPointsNumber;
	}
	
	/**
	 * @return the maximum time that can be displayed when in real time mode
	 */
	public long getMaxDisplayableTime() {
		return maxDisplayableTime;
	}
	
	/**
	 * @return whether to display the legend of each curve
	 */
	public boolean displayCurvesLegend() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("displayCurvesLegend"));
	}
	
	/**
	 * @return the start mode
	 */
	public int getStartMode() {
		return startMode;
	}

	/**
	 * @return the background color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * @return whether to display the grid
	 */
	public boolean displayGrid() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("showGrid"));
	}
	
	/**
	 * @return whether to automatically compute the distance between two vertical grid division lines
	 */
	public boolean isVerticalLinesDivisionAutomaticPeriod() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("verticalLinesDivisionAutomaticPeriod"));
	}
	
	/**
	 * @return the distance between two vertical grid division lines in milliseconds
	 */
	public long getVerticalLinesDivisionDuration() {
		return verticalLinesDivisionDuration;
	}
	
	/**
	 * @return the color of the vertical division lines 
	 */
	public Color getVerticalLinesDivisionColor() {
		return verticalLinesDivisionColor;
	}
	
	/**
	 * @return the dash of the vertical division lines
	 */
	public String getVerticalLinesDivisionDash(){
		
		return jwidgetElement.getAttribute("verticalLinesDivisionDash");
	}
	
	/**
	 * @return whether to automatically compute the distance between two vertical grid sub division lines
	 */
	public boolean isVerticalLinesSubDivisionAutomaticPeriod() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("verticalLinesSubDivisionAutomaticPeriod"));
	}
	
	/**
	 * @return the distance between two vertical grid sub division lines in milliseconds
	 */
	public long getVerticalLinesSubDivisionDuration() {
		return verticalLinesSubDivisionDuration;
	}
	
	/**
	 * @return the color of the vertical sub division lines 
	 */
	public Color getVerticalLinesSubDivisionColor() {
		return verticalLinesSubDivisionColor;
	}
	
	/**
	 * @return the dash of the vertical lines division
	 */
	public String getVerticalLinesSubDivisionDash(){
		
		return jwidgetElement.getAttribute("verticalLinesSubDivisionDash");
	}
	
	/**
	 * @return whether to keep on recording the tag values of the real time mode 
	 * when switching to the history mode
	 */
	public boolean keepTagRecordInHistory() {//TODO
		
		return keepTagRecordInHistory;
	}
	
	/**
	 * @return whether to integrate the values that are constant for a moment when computing the interpolation
	 */
	public boolean interpolationWithPeriod() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("interpolationWithPeriod"));
	}
	
	/**
	 * @return whether to display the horizontal scroll bar
	 */
	public boolean displayScrollBar() {
		
		return Boolean.parseBoolean(jwidgetElement.getAttribute("displayScrollBar"));
	}
	
	/**
	 * @return the number of digits of the decimal part that should be displayed 
	 * when displaying a string representation of a value of a tag
	 */
	public int getValuesDecimalDigitsNumber() {
		return valuesDecimalDigitsNumber;
	}
	
	/**
	 * @return the higher limit from which the values should be written with an exponent
	 */
	public double getHigherLimit() {
		return higherLimit;
	}
	
	/**
	 * @return the lower limit under which the values should be written with an exponent
	 */
	public double getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * @return the tag defining the start date 
	 */
	public String getEndDateTag() {
		return endDateTag;
	}

	/**
	 * @return the tag defining the end date
	 */
	public String getStartDateTag() {
		return startDateTag;
	}

	/**
	 * returns the number of milliseconds denoted by the given string
	 * @param strValue a value representating a duration
	 * @return the number of milliseconds denoted by the given string
	 */
	public static long getTime(String strValue){
		
		long timeMilliSeconds=0;
		
		if(strValue!=null && ! strValue.equals("")){
			
			//splitting the string into a time integer value and a unit string
			int pos=strValue.indexOf(" ");
			
			if(pos!=-1){
				
				String timeStr=strValue.substring(0, pos);
				String unit=strValue.substring(pos+1, strValue.length());
				
				//getting the time value relatively to the unit that can be found in the string
				long time=-1;
				
				try{
					time=Long.parseLong(timeStr);
				}catch (Exception ex){}
				
				if(time!=-1){
					
					//getting the time in milliseconds according to the units
					Long msNumber=timeUnitsToMs.get(unit);
					
					if(msNumber!=null){
						
						timeMilliSeconds=time*msNumber;
					}
				}
			}
		}

		return timeMilliSeconds;
	}
	
}