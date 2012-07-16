/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.rtdp;

import fr.itris.glips.rtda.*;
import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.Element;
import fr.itris.glips.rtda.components.picture.SVGPicture;
import fr.itris.glips.rtda.config.ConfigurationDocument;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * the simulator of a data change listener
 * 
 * @author ITRIS, Jordi SUC
 */
public class RealTimeDataProviderTestSimulator extends RealTimeDataProvider{
    
    /**
     * the handler of the animations
     */
    private AnimationsHandler handler;
    
    /**
     * the set containing the names of the data that should be listened to 
     */
    private Set<String> dataNames=new CopyOnWriteArraySet<String>();
    
    /**
     * the map associating the name of a tag to the list of the "TestSimulationTagValues" objects providing 
     * to the simulator the values of the tags specified by the user
     */
    private Map<String, TestSimulationTagValues> testSimulationValuesSpecifiersMap=
    		new ConcurrentHashMap<String, TestSimulationTagValues>();
    
    /**
     * the current test interactor
     */
    private TestInteractor currentTestInteractor;
    
    /**
     * the thread simulating a database
     */
    private Thread simulatorThread;

    /**
     * the constructor of the class
     * @param handler the handler of the animations
     */
    public RealTimeDataProviderTestSimulator(AnimationsHandler handler){
        
        this.handler=handler;
        
        simulatorThread=new Thread(){

        	@Override
	        public void run() {
	            
	            //the time stamp for the computation of the value of a function
	            long timeStamp=-1;
	            long timer=100;
                double time=0;

	            while(true){
	                
	                if(isStarted()){

	                    //computing the number of seconds
	                    time=0;
	                    
	                    if(timeStamp!=-1){
	                        
	                        time=((double)(System.currentTimeMillis()-timeStamp))/1000;
	                        
	                    }else{
	                        
	                        timeStamp=System.currentTimeMillis();
	                    }

		                //for each specifier, sets the new value of its tag
	                    for(TestSimulationTagValues testValues : 
		                	testSimulationValuesSpecifiersMap.values()){

		                    if(testValues!=null){

			                    if(dataNames.contains(testValues.getTagName()) && 
			                    		testValues.computeTagValue(time)){

			                        setDataValueForAnimationsHandler(
			                        	testValues.getTagName(), testValues.getTagValue());
			                    }
		                    }
		                }

		                try{sleep(timer);}catch(Exception ex){}
		                
	                }else{
	                    
	                    if(! RealTimeDataProviderTestSimulator.this.handler.isPaused()){

	                        timeStamp=-1;
	                    }
	                    
		                try{sleep(100);}catch(Exception ex){}
	                }
	            }
	        } 
        };
        
        simulatorThread.start();
    }
    
	@Override
    public void dispose() {}
    
	@Override
    public void addDataToListen(String name){

        if(name!=null && ! name.equals("")){
            
            dataNames.add(name);
        }
    }
    
	@Override
    public void removeDataToListen(String name){

        if(name!=null && ! name.equals("")){
            
            dataNames.remove(name);
        }
    }
    
	@Override
    public void setDataValueForAnimationsHandler(String name, Object newValue) {
        
		if(name!=null && ! name.equals("") && dataNames.contains(name)){

            handler.setData(name, newValue);
		}
    }

	@Override
	public void setDataValue(String name, Object newValue) {
		
		if(name!=null && ! name.equals("") && dataNames.contains(name)){
            
            //getting the test simulation object corresponding to the short name
            TestSimulationTagValues testValues=
            	testSimulationValuesSpecifiersMap.get(name);
   
        	if(testValues!=null){
        		
        		//sets the new value for the tag
        		testValues.setNewValueFromRtdp(name, newValue);

        		if(currentTestInteractor!=null){
        			
        			//refreshes the test interactor
        			currentTestInteractor.refresh();
        		}
        		
        	}else{
        		
        		setDataValueForAnimationsHandler(name, newValue);
        	}
		}
	}
    
	@Override
    public String getDataProviderId(){
        
        return "";
    }
    
    /**
     * refreshes the list of the "TestSimulationTagValues" describing the values the user should provide
     * to simulate the animations
     * @return the list of the "TestSimulationTagValues" describing the values the user should provide
     * to simulate the animations
     */
    public LinkedList<TestSimulationTagValues> refreshSimulationValuesSpecifiers(){
        
    	Map<String, TestSimulationTagValues> map=new HashMap<String, TestSimulationTagValues>();
        LinkedList<TestSimulationTagValues> list=new LinkedList<TestSimulationTagValues>();
        Map<String, Set<TestTagInformation>> infos=handler.getDataToInformationMap();
        
        if(infos!=null){
            
            //the list of the tag names that will be modified
            List<String> tagNamesToHandle=
            	new LinkedList<String>(infos.keySet());
            
            //sorting the tag names in the alphabetical order
            Collections.sort(tagNamesToHandle);

            TestSimulationTagValues testValues=null;
            TestTagInformation rightInfo=null;
            Set<TestTagInformation> testInfos=null;
            Set<String> enumeratedTagValues=null;
            LinkedList<String> enumValues=null;
            List<String> infoEnumValues=null;
            Object value=null;
            SVGPicture picture=null;
            double minVal=0, maxVal=0;
            ConfigurationDocument configDoc=null;
            Element tagElement=null;
            int tagType=-1;
            
            //for each tag name, builds the test values object allowing 
            //the users to specify the values for the simulator.
            for(String tagName : tagNamesToHandle){
                
            	rightInfo=null;
            	testValues=null;
            	
                if(tagName!=null){
                	
                	//getting the type of the tag
                	tagType=handler.getTagType(tagName);
                	
                	//getting the simulator if it exists
                	testValues=testSimulationValuesSpecifiersMap.get(tagName);
                	testInfos=infos.get(tagName);

                    if(testInfos!=null){
                        
                        //builds the set of the possible values of a tag if it is an enumerated tag
                        enumeratedTagValues=new HashSet<String>();
                        
                        for(TestTagInformation info : testInfos){

                            if(info!=null && info.canBeUsed()){
                            	
                            	rightInfo=info;
                                infoEnumValues=info.getEnumeratedValues();
                                
                                if(infoEnumValues!=null){
                                    
                                    enumeratedTagValues.addAll(infoEnumValues);
                                }
                            }
                        }
                        
                        if(testValues!=null){
                        	
                        	//as the simulator already exists, the enumerated values that already existed 
                        	//and the newly found ones are merged
                        	enumeratedTagValues.addAll(testValues.getEnumeratedValues());
                        }
                        
                        enumValues=new LinkedList<String>(enumeratedTagValues);
                        
                        //sorting the enumerated tag values
                        if(enumValues.size()>0){
                            
                            Collections.sort(enumValues);
                        }
                        
                        if(testValues!=null){
                        	
                        	//adding the existing simulators in the list
                        	testValues.setEnumeratedValues(enumValues);
                        	map.put(tagName, testValues);
                        	list.add(testValues);
                        	
                        }else{
                        	
                            //creating the object that will be used for specifying 
                        	//the values for the simulation
                            if(rightInfo!=null ){
                                
                            	value=handler.getData(tagName);
                        		minVal=0;
                        		maxVal=0;
                            	
                            	if(tagType==TagToolkit.ANALOGIC_FLOAT || 
                            			tagType==TagToolkit.ANALOGIC_INTEGER){
                            		
                                	//getting the min and max values for the tag
                            		picture=rightInfo.getPicture();
                            		configDoc=picture.getMainDisplay().getPictureManager().
                            			getConfigurationDocument(picture);
                            		
                            		if(configDoc!=null){
                            			
                            			tagElement=configDoc.getElement(tagName);
                            			
                            			if(tagElement!=null){
                            				
                            				//computing the min and the max values
                            				try{
                            					minVal=Double.parseDouble(tagElement.getAttribute("min"));
                            				}catch (Exception ex){minVal=0;}
                            				
                            				try{
                            					maxVal=Double.parseDouble(tagElement.getAttribute("max"));
                            				}catch (Exception ex){maxVal=0;}
                            			}
                            		}	
                            	}

                            	//creating the test object
                                testValues=new TestSimulationTagValues(tagName, value, 
                                		tagType, enumValues, minVal, maxVal);
                                
                                map.put(tagName, testValues);
                                list.add(testValues);
                            }
                        }
                    }
                }
            }
        }
        
        testSimulationValuesSpecifiersMap.clear();
        testSimulationValuesSpecifiersMap.putAll(map);

        return list;  
    }
    
	/**
	 * sets the current test interactor
	 * @param testInteractor a test interactor
	 */
	public void setTestInteractor(TestInteractor testInteractor) {
		
		this.currentTestInteractor=testInteractor;
	}
	
	/**
	 * reinitializes the test rtdp
	 */
	public void reinitialize(){
		
		testSimulationValuesSpecifiersMap.clear();
		dataNames.clear();
		currentTestInteractor=null;
	}
}
