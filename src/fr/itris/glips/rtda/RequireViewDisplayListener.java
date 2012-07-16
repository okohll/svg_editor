/*
 * Created on 14 mars 2005
 */
package fr.itris.glips.rtda;

/**
 * the interface of the listener to the request of the display of a view
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class RequireViewDisplayListener implements 
	Comparable<RequireViewDisplayListener>{

    /**
     * requires to display a view having the given uri
     * @param id the id of the view browser 
     * @param uri the uri of the view that should be displayed
     */
    public abstract void displayView(String id, String uri);
    
    /**
     * requires to display the view that has been displayed before the current view
     * @param id the id of a view browser
     */
    public abstract void displayPreviousView(String id);
    
    /**
     * requires to exit from the program
     */
    public abstract void quitProgram();
    
    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(RequireViewDisplayListener o) {

    	return 0;
    }
}
