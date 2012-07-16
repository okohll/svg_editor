package fr.itris.glips.rtdaeditor.anim;

import java.util.*;
import fr.itris.glips.rtda.toolkit.*;
import org.w3c.dom.*;

/**
 * @author ITRIS, Jordi SUC
 */
public abstract class EditableItem {

	/**
	 * the separator for the group attributes
	 */
	public final static String separator="/*item*/";
	
	/**
	 * the separator regex for the group attributes
	 */
	public final static String separatorRegex="/[*]item[*]/";
	
	/**
	 * the constant UPDATE_CELL
	 */
	public static final int UPDATE_CELL=0;
	
	/**
	 * the constant UPDATE_ROW
	 */
	public static final int UPDATE_ROW=1;
	
	/**
	 * the constant UPDATE_ALL
	 */
	public static final int UPDATE_ALL=2;
	
	/**
	 * the constant UPDATE_NONE
	 */
	public static final int UPDATE_NONE=3;
	
	/**
	 * the constant TAG_CHOOSER
	 */
	public static final String TAG_CHOOSER="tagchooser";
	
	/**
	 * the constant LOW_TAG_CHOOSER
	 */
	public static final String LOW_TAG_CHOOSER="lowtagchooser";
	
	/**
	 * the constant LOW_REAL_TAG_CHOOSER
	 */
	public static final String LOW_REAL_TAG_CHOOSER="lowrealtagchooser";
	
	/**
	 * the constant COLOR_CHOOSER
	 */
	public static final String COLOR_CHOOSER="colorchooser";
	
	/**
	 * the constant VIEW_CHOOSER
	 */
	public static final String VIEW_CHOOSER="viewchooser";
	
	/**
	 * the constant EXTENDED_VIEW_CHOOSER
	 */
	public static final String EXTENDED_VIEW_CHOOSER="extendedviewchooser";
	
	/**
	 * the constant ACTION_CHOOSER
	 */
	public static final String ACTION_CHOOSER="actionchooser";
	
	/**
	 * the constant ENTRY
	 */
	public static final String ENTRY="entry";
	
	/**
	 * the constant LABEL
	 */
	public static final String LABEL="label";
	
	/**
	 * the constant LIMIT
	 */
	public static final String LIMIT="limit";
	
	/**
	 * the constant CHILD_LIMIT
	 */
	public static final String CHILD_LIMIT="childlimit";
	
	/**
	 * the constant RET_INIT_VAL_CHOOSER
	 */
	public static final String RET_INIT_VAL_CHOOSER="returntoinitialvaluechooser";
	
	/**
	 * the constant SIMPLE_RET_INIT_VAL_CHOOSER
	 */
	public static final String SIMPLE_RET_INIT_VAL_CHOOSER="simplereturntoinitialvaluechooser";
	
	/**
	 * the constant CHECK_BOX
	 */
	public static final String CHECK_BOX="checkbox";
	
	/**
	 * the constant COMBO
	 */
	public static final String COMBO="combo";
	
	/**
	 * the constant NUMBER_CHOOSER
	 */
	public static final String NUMBER_CHOOSER="numberchooser";
	
	/**
	 * the constant INTEGER_CHOOSER
	 */
	public static final String INTEGER_CHOOSER="integerchooser";
	
	/**
	 * the constant DASH_CHOOSER
	 */
	public static final String DASH_CHOOSER="dashchooser";
	
	/**
	 * the constant POINT_STYLE_CHOOSER
	 */
	public static final String POINT_STYLE_CHOOSER="pointstylechooser";
	
	/**
	 * the constant INTERPOLATION_CHOOSER
	 */
	public static final String INTERPOLATION_CHOOSER="interpolationchooser";
	
	/**
	 * the constant TAG_VALUES_CHOOSER
	 */
	public static final String TAG_VALUES_CHOOSER="tagvalueschooser";
	
	/**
	 * the constant TAG_VALUES_MULTI_CHOOSER
	 */
	public static final String TAG_VALUES_MULTI_CHOOSER="tagvaluesmultichooser";
	
	/**
	 * the constant BLINKING_CHOOSER
	 */
	public static final String BLINKING_CHOOSER="blinkingchooser";
	
	/**
	 * the constant POINT_CHOOSER
	 */
	public static final String POINT_CHOOSER="pointchooser";
	
	/**
	 * the constant DB_TABLE_CHOOSER
	 */
	public static final String DB_TABLE_CHOOSER="dbtablechooser";
	
	/**
	 * the constant REQUEST_CHOOSER
	 */
	public static final String REQUEST_CHOOSER="requestchooser";
	
	/**
	 * the constant DIRECTORY_CHOOSER
	 */
	public static final String DIRECTORY_CHOOSER="directorychooser";
	
	/**
	 * the constant SYMBOL_CHOOSER
	 */
	public static final String SYMBOL_CHOOSER="symbolchooser";
	
	/**
	 * the constant TARGET_CHOOSER
	 */
	public static final String TARGET_CHOOSER="targetchooser";
	
	/**
	 * the constant COMPLEX_TARGET_CHOOSER
	 */
	public static final String COMPLEX_TARGET_CHOOSER="complextargetchooser";
	
	/**
	 * the constant EVENT_CHOOSER
	 */
	public static final String EVENT_CHOOSER="eventchooser";
	
	/**
	 * the constant TAG_EVENT_CHOOSER
	 */
	public static final String TAG_EVENT_CHOOSER="TagEventChooser";
	
	/**
	 * the constant EQUAL_CHOOSER
	 */
	public static final String EQUAL_CHOOSER="equalChooser";
	
	/**
	 * @return the svg parent element
	 */
	public abstract Element getSVGElement();
	
	/**
	 * @return the animation or action element
	 */
	public abstract Element getAnimationElement();
	
	/**
	 * @return the animation objectrelated to this item
	 */
	public abstract AnimationObject getAnimationObject();
	
	/**
	 * @return whether this item can be edited
	 */
	public abstract boolean isEditable();
	
	   /**
     * @return the color
     */
    public abstract String getColor();

    /**
     * @return the constraint
     */
    public abstract String getConstraint();

    /**
     * @return the defaultValue
     */
    public abstract String getDefaultValue();

    /**
     * @return the name
     */
    public abstract String getName();
    
    /**
     * @return the label
     */
    public abstract String getLabel();
    
    /**
     * @return the group name
     */
    public abstract String getGroupName();
    
    /**
     * @return the group label
     */
    public abstract String getGroupLabel();

    /**
     * returns the map of the possible values for the given attribute of the group attribute
     * @param name the name of an attribute of the group
     * @return the map of the possible values for the given attribute of the group attribute
     */
    public abstract HashMap<String, String> getPossibleValues(String name);
    
    /**
     * @return the map of the possible values
     */
    public abstract LinkedHashMap<String, String> getPossibleValues();

    /**
     * @return the ref
     */
    public abstract String getRef();

    /**
     * @return the tagType
     */
    public abstract int getTagType();

    /**
     * @return the type
     */
    public abstract String getType();

    /**
     * @return the value
     */
    public abstract String getValue();

    /**
     * @param value the new value
     */
    public abstract void setValue(String value);

	/**
	 * @return the isGroupAttribute
	 */
	public abstract boolean isGroup();
	
	/**
	 * @return the document linked with this item
	 */
	public abstract Document getDocument();
	
	/**
	 * validates the changes made on the item
	 */
	public abstract void validateChanges();
	
    /**
     * computes the type of the attribute given a string representation of it
     * @param typeStr a string
     * @return the type of the attribute given a string representation of it
     */
    public static int getType(String typeStr){

    	int attributeType=TagToolkit.NONE;
    	
    	if(typeStr!=null && ! typeStr.equals("")){
    		
        	//getting the type of the animation
        	if(typeStr.equals("analogic")){
        		
        		attributeType=TagToolkit.ANALOGIC;
        		
        	}else if(typeStr.equals("enumerated")){
        		
        		attributeType=TagToolkit.ENUMERATED;
        		
        	}else if(typeStr.equals("view")){
        		
        		attributeType=TagToolkit.VIEW;
        		
        	}else if(typeStr.equals("text") || typeStr.equals("string")){
        		
        		attributeType=TagToolkit.STRING;
        		
        	}else if(typeStr.equals("needChildren")){
        		
        		attributeType=TagToolkit.NEED_CHILDREN;
        		
        	}else if(typeStr.equals("anyTag")){
        		
        		attributeType=TagToolkit.ANY_TAG;
        	}
    	}

    	return attributeType;
    }
    
    /**
     * returns the list of all the ids of the svg shapes and jwidgets that can be found in the given document
     * @param root the root element
     * @return the list of all the ids of the svg shapes and jwidgets that can be found in the given document
     */
    public static LinkedList<String> getSVGShapeOrJWidgetIds(Element root){
    	
    	LinkedList<String> ids=new LinkedList<String>();
    	
    	if(root!=null) {
    		
    		String id="";
    		Element el=null;
    		
    		for(Node node=root.getFirstChild(); node!=null; node=node.getNextSibling()) {
    			
    			if(		node instanceof Element &&
    					! node.getNodeName().startsWith(fr.itris.glips.library.Toolkit.rtdaPrefix) && 
    					! node.getNodeName().equals("defs")) {
    				
    				el=(Element)node;
    				id=el.getAttribute("id");
    				
    				if(! id.equals("")) {
    					
        				ids.add(id);
    				}

    				//adding the ids of the children of the node
    				ids.addAll(getSVGShapeOrJWidgetIds(el));
    			}
    		}
    	}
    	
    	return ids;
    }
    
    /**
     * returns the map associating the id of a view browser to its label
     * @param root the root element from which the id of the view browsers should be retrieved
     * @return the map associating the id of a view browser to its label
     */
    public static LinkedHashMap<String, String> getViewBrowsersIds(Element root){
    	
    	 LinkedHashMap<String, String> idsToLabel=new  LinkedHashMap<String, String>();
    	 
    	 //getting the list of thejwidget elements
    	 NodeList jwidgetElements=root.getElementsByTagName(
    			 fr.itris.glips.library.Toolkit.jwidgetElementName);
    	 
    	 if(jwidgetElements!=null) {
    		 
    		 //retrieves the id and label of all the view browser elements
        	 String id="", label="";
        	 Element element=null;
        	 
        	 for(int i=0; i<jwidgetElements.getLength(); i++) {
        		 
        		 element=(Element)jwidgetElements.item(i);
        		 
        		 if(element!=null && element.getAttribute(
        				fr.itris.glips.library.Toolkit.nameAttribute).equals("ViewBrowserWidget")) {
        			 
        			 id=element.getAttribute(fr.itris.glips.library.Toolkit.idAttribute);
        			 label=element.getAttribute(fr.itris.glips.library.Toolkit.labelAttribute);
        			 
        			 if(id!=null && ! id.equals("")) {
        				 
        				 if(label==null) {
        					 
        					 label="";
        				 }
        				 
        				 idsToLabel.put(id, label);
        			 }
        		 }
        	 }
    	 }

    	 return idsToLabel;
    }
 }
