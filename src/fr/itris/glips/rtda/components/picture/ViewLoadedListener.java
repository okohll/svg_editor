package fr.itris.glips.rtda.components.picture;

/**
 * the class of the listener to the loading of a view
 * @author ITRIS, Jordi SUC
 */
public interface ViewLoadedListener {

	/**
	 * notifies that a view denoted by the given xml path has been loaded
	 * @param xmlViewPath the xml path of a view
	 */
	public void viewLoaded(String xmlViewPath);
}
