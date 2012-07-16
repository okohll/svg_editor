package fr.itris.glips.extension.jwidget.trends.runtime.configuration;

/**
 * the class of the listeners to the changes on the configuration objects
 * @author ITRIS, Jordi SUC
 */
public abstract class CurvesConfigurationChangeListener {

	/**
	 * notifies that the style of the curve has changed
	 */
	public void curveStyleChanged(){}
	
	/**
	 * notifies that the scale has changed
	 */
	public void curveColorChanged(){}
	
	/**
	 * notifies that the scale has changed
	 */
	public void curveScaleChanged(){}
}
