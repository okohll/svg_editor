/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.rtdp;

/**
 * the interface of the providers of the data modification
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class RealTimeDataProvider{

    /**
     * whether the provider is started or not
     */
    protected boolean isStarted=false;

    /**
     * @return whether the provideris started
     */
    public boolean isStarted() {
        return isStarted;
    }
    /**
     * @param isStarted whether the provider should start or stop
     */
    public void setIsStarted(boolean isStarted) {
        
        this.isStarted=isStarted;
    }
    
    /**
     * @return the id of the data provider
     */
    public abstract String getDataProviderId();
    
    /**
     * adds the name of a data to listen to its changes
     * @param name the name of a data
     */
    public abstract void addDataToListen(String name);

    /**
     * removes the name of a data that was listened to
     * @param name the name of a data
     */
    public abstract void removeDataToListen(String name);
    
    /**
     * sets the new value for the given data from this rtdp to the animations handler
     * @param name the name of a data
     * @param newValue the new value
     */
    public abstract void setDataValueForAnimationsHandler(String name, Object newValue);
    
    /**
     * sets the new value for the given data from this rtdp to the external data source
     * @param name the name of a data
     * @param newValue the new value
     */
    public abstract void setDataValue(String name, Object newValue);
    
    /**
     * loads the view denoted by the given path
     * @param path a view path
     */
    public void loadView(String path) {}
    
    /**
     * unloads the view denoted by the given path
     * @param path a view path
     */
    public void unLoadView(String path) {}
    
	/**
	 * disposes the animations handler
	 */
	public abstract void dispose();
}
