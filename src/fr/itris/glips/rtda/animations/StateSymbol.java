/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.test.*;
import java.util.*;

/**
 * the listener to the changes of data for the "label" animation node
 * 
 * @author ITRIS, Jordi SUC
 */
public class StateSymbol extends DataChangedListener{
	
	/**
	 * attribute names
	 */
	private final static String tagAtt="tag", idAtt="id", 
		invalidSymbolAtt="invalidSymbol", defaultSymbolAtt="defaultSymbol", 
		symbolAtt="symbol", usedAtt="used", valueAtt="value", opacityAtt="opacity";

    /**
     * the set of the children of the parent element
     */
    private Set<Element> childrenSet=new HashSet<Element>();
    
    /**
     * the map associating the id of a child to this child
     */
    private Map<String, Element> idToChild=new HashMap<String, Element>();
    
    /**
     * the symbols for the invalid and defaultvalues
     */
    private String invalidSymbol="", defaultSymbol="";
    
    /**
     * the last symbol
     */
    private String lastSymbol;

    /**
     * the map of the ids associating a possible value of a 
     * tag to the id of a child of the parent element
     */
    private Map<String, String> idsMap=new HashMap<String, String>();

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public StateSymbol(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        //getting the name specified in the "tag" attribute and adding 
    	//it to the set of the data names
        addData(animationElement.getAttribute(tagAtt));

        //storing the children of the parent element that will be modified
        String theId="";
        Element childEl=null;
        
        for(Node child=parentElement.getFirstChild(); child!=null; child=child.getNextSibling()){
            
            if(child instanceof Element && ! child.getNodeName().startsWith(Toolkit.rtdaPrefix)){

            	childEl=(Element)child;
            	
                childrenSet.add(childEl);
                
                //getting the id of the child
                theId=Toolkit.toUnURLValue(childEl.getAttribute(idAtt));
                
                if(theId!=null && ! theId.equals("")){
                    
                    idToChild.put(theId, childEl);
                }
            }
        }

        //storing the values of the attributes of the animation node
        invalidSymbol=Toolkit.toUnURLValue(
        		animationElement.getAttribute(invalidSymbolAtt));
        defaultSymbol=Toolkit.toUnURLValue(
        		animationElement.getAttribute(defaultSymbolAtt));
        
        //fills the maps with the fill and stroke values
        String value="", symbol="";
        Element el=null;
        boolean isUsed=false;
        LinkedList<String> realValuesNames=new LinkedList<String>();

        for(Node cur=animationElement.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
                
            	el=(Element)cur;

                //whether the child should be used or not
        		isUsed=Boolean.parseBoolean(el.getAttribute(usedAtt));

                if(isUsed){
                    
                    //getting one of the possible values of the tag
                    value=el.getAttribute(valueAtt);
                    
                    if(! value.equals("")){
                        
                        //getting the symbol for the given tag value
                        symbol=Toolkit.toUnURLValue(el.getAttribute(symbolAtt));
                        
                        realValuesNames.add(value);
                        value=AnimationsToolkit.normalizeEnumeratedValue(value);
                        idsMap.put(value, symbol);
                    }
                }
            }
        }
        
        //if we are in the test version, we store information on the tag
        if(picture.getMainDisplay().isTestVersion()){
            
            TestTagInformation info=new TestTagInformation(
            	picture, animationElement.getAttribute(tagAtt), realValuesNames);
            
            dataNamesToInformation.put(animationElement.getAttribute(tagAtt), info);
        }
        
        //hiding all the children of the parent element
        for(Element child : childrenSet){

            if(child!=null){
                
            	child.setAttribute(opacityAtt, "0");
            }
        }
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {

        //whether the current state of the listener is invalid or not
        boolean isInvalidState=false;
        Runnable runnable=null;

        //getting the value of the tag
        String value=(String)getData(
        		animationElement.getAttribute(tagAtt));
        
        //the new symbol
        String newSymbol="";
        
        if(value==null){
        	
            //the state of the tag is invalid//
            newSymbol=invalidSymbol;
            isInvalidState=true;
            
        }else if(idsMap.containsKey(value)){

            newSymbol=idsMap.get(value);
            
        }else{
            
            newSymbol=defaultSymbol;
        }

        if(newSymbol!=null && ! newSymbol.equals(lastSymbol)){
        	
            final String fnewSymbol=newSymbol;

            //the runnable that will be returned
            runnable=new Runnable(){

	            public void run() {

	                Element lastChild=null, child=null;
	                
	                //getting the last and new children corresponding to the ids
	                try{
	                    lastChild=idToChild.get(lastSymbol);
	                }catch (Exception ex){}
	                
	                try{
	                    child=idToChild.get(fnewSymbol);
	                }catch (Exception ex){}

	                //hiding the last displayed child
	                if(lastChild!=null){
	                    
	                	lastChild.setAttribute(opacityAtt, "0");
	                }
	                
	                if(child!=null){
	                    
	                	child.setAttribute(opacityAtt, "1");
	                }
	                
	                lastSymbol=fnewSymbol;
	            } 
            };
        }
        
        //stores that the state of the listener is invalid as some of its tags are invalid
        setInvalidTag(isInvalidState);
        
        return runnable;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {}
}
