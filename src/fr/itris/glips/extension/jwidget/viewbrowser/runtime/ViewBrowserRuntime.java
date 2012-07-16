package fr.itris.glips.extension.jwidget.viewbrowser.runtime;

import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.viewbrowser.edition.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.components.viewbrowser.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class ViewBrowserRuntime extends JWidgetRuntime{

	/**
	 * the jwidget id
	 */
	public static String jwidgetId="ViewBrowserWidget";
	
	/**
	 * the initial view attribute name
	 */
	protected static final String initialViewAttribute="initialView", 
		showHeaderAttribute="showHeader";
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public ViewBrowserRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {
 
    	boolean showHeader=false;
    	
    	try {
    		showHeader=Boolean.parseBoolean(element.getAttribute(showHeaderAttribute));
    	}catch (Exception ex) {}
    	
    	ViewBrowser viewBrowser=new ViewBrowser(
    		picture.getMainDisplay(), element.getAttribute(
    				jwidgetIdAttributeName), showHeader);
    	
    	//registers the view browser
    	picture.getMainDisplay().getViewBrowsersManager().
    		registerViewBrowser(viewBrowser);
    	component=viewBrowser;
    }
    
    @Override
    public void initializeWhenCanvasDisplayed() {

    	//getting the xml path of the initial view
    	String viewPath=element.getAttribute(initialViewAttribute);

    	if(viewPath!=null && ! viewPath.equals("")) {
    		
        	//getting the uri of the view 
        	String uri=picture.getMainDisplay().getPictureManager().getViewUri(
        			projectName, viewPath);

        	//setting the new picture for this view browser
        	((ViewBrowser)component).getPictureLoader().setCurrentPicture(uri, false);
    	}
    }

    @Override
    public Action createAction(Element actionElement) {
    	
    	return null;
    }
    
    @Override
    public void refreshComponentState() {}

    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass(){
    	
    	return ViewBrowserEdition.class;
    }
    
    @Override
    public void registerListeners() {}
    
    @Override
    public void unregisterListeners() {}
    
    @Override
    public void dispose() {
    	
    	super.dispose();
    	((ViewBrowser)component).dispose();
    }
}
