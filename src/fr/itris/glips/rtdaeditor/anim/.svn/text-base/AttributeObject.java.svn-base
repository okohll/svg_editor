package fr.itris.glips.rtdaeditor.anim;

import org.w3c.dom.*;
import fr.itris.glips.library.FormatStore;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.colorchooser.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.awt.geom.*;
import java.util.*;

/**
 * the object for an attribute of an animation
 * @author ITRIS, Jordi SUC
 */
public class AttributeObject extends EditableItem{
	
	/**
	 * the rtda animations label prefix
	 */
	private final static String rtdaAnimLabelPrefix="rtdaanim_", tagMinAtt="tagMin", tagMaxAtt="tagMax";
	
	/**
	 * the spec element
	 */
	private Element specElement;
	
	/**
	 * the parent element
	 */
	private Element parentElement;
	
	/**
	 * this list is only used if this attribute is a group one
	 * it contains the list of the attribute objects contained in the group attribute
	 */
	private LinkedList<AttributeObject> groupAttributes=new LinkedList<AttributeObject>();
	
	/**
	 * whether this attribute is a group attribute
	 */
	private boolean isGroupAttribute=false;
	
	/**
	 * the animation object
	 */
	private AnimationObject animationObject=null;
	
	/**
	 * the name of the group if this attribute represents a group
	 */
	private String groupName="";
	
	/**
	 * the currently modified value for this attribute object
	 */
	private String currentValue=null;

	/**
	 * the svg handle
	 */
	private SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
	
	/**
	 * the constructor of the class
	 * @param animationObject the animation object
	 * @param parentElement the parent element
	 * @param specElements the list of the spec elements for this object
	 */
	public AttributeObject(AnimationObject animationObject, 
			Element parentElement, LinkedList<Element> specElements) {

		this.animationObject=animationObject;
		this.parentElement=parentElement;
		
		if(specElements!=null && specElements.size()>1){
			
			isGroupAttribute=true;
			
			//creating the attributes associated with each spec element
			for(Element attSpecElement : specElements){
				
				if(groupName.equals("")){
					
					groupName=attSpecElement.getAttribute("group");
				}
				
				groupAttributes.add(new AttributeObject(animationObject, parentElement, attSpecElement));
			}
			
		}else if(specElements!=null){
			
			specElement=specElements.getFirst();
		}
	}
	
	/**
	 * the constructor of the class
	 * @param animationObject the animation object
	 * @param parentElement the parent element
	 * @param specElement the spec element for this object
	 */
	public AttributeObject(AnimationObject animationObject, Element parentElement, Element specElement) {
		
		this.animationObject=animationObject;
		this.specElement=specElement;
		this.parentElement=parentElement;
	}
	
	@Override
	public String getColor() {
		return getSpecAttribute("color");
	}
	
	@Override
	public String getConstraint() {
		return getSpecAttribute("constraint");
	}
	
	@Override
	public String getDefaultValue() {
		return getSpecAttribute("defaultValue");
	}
	
	@Override
	public String getName() {
		return getSpecAttribute("name");
	}
	
	@Override
	public String getLabel() {
		
		String label="";
		
		if(isGroupAttribute){
			
			String lb="", nm="";
			
			for(AttributeObject obj : groupAttributes){
				
				nm=obj.getSpecElement().getAttribute("name");
				
				try{
					lb=animationObject.getLabel(rtdaAnimLabelPrefix+nm);
				}catch(Exception ex){lb=nm;}
				
				label+=lb+separator;
			}
			
		}else{
			
			String nm=getName();
			
			try{
				label=animationObject.getLabel(rtdaAnimLabelPrefix+nm);
			}catch(Exception ex){label=nm;}
		}
		
		return label;
	}
	
	@Override
	public String getGroupName() {
		
		return groupName;
	}
	
	@Override
	public String getGroupLabel() {
		
		String label="";
		
		if(isGroupAttribute){
			
			String nm=getGroupName();
			
			try{
				label=animationObject.getLabel(rtdaAnimLabelPrefix+nm);
			}catch(Exception ex){label=nm;}
		}
		
		return label;
	}
	
	@Override
	public HashMap<String, String> getPossibleValues(String name) {
		
		HashMap<String, String> map=null;
		
		for(AttributeObject obj : groupAttributes){
			
			if(obj.getName().equals(name)){
				
				map=obj.getPossibleValues();
				break;
			}
		}
		
		return map;
	}
	
	@Override
	public LinkedHashMap<String, String> getPossibleValues() {
		
		LinkedHashMap<String, String> map=new LinkedHashMap<String, String>();
		
		if(getType().equals(EditableItem.TAG_VALUES_CHOOSER) || 
				getType().equals(EditableItem.TAG_VALUES_MULTI_CHOOSER)){
			
			//getting the tag values corresponding to the referenced tag//
			
			//getting the tag value corresponding to the referenced attribute
			String tag="", referencedAttributeName=getRef();
			
			for(AttributeObject attributeObject : animationObject.getAttributesList()){
				
				if(attributeObject.getName().equals(referencedAttributeName)){
					
					tag=attributeObject.getValue();
					break;
				}
			}
			
			if(tag!=null && ! tag.equals("")){
				
				//filling the map with the tag values
				LinkedList<String> tagValues=DataBaseNodeToolkit.
				getEnumeratedTagValues(getParentElement().getOwnerDocument(), tag);
				
				for(String value : tagValues){
					
					map.put(value, value);
				}
			}
			
		}else if(getType().equals(EditableItem.BLINKING_CHOOSER)){
			
			//getting the blinking values corresponding to the current handle
			Map<String, Object> blinkings=((RtdaColorChooserModule)Editor.getColorChooser()).getColorsAndBlinkingsToolkit().
			getBlinkingsMap(Editor.getEditor().getHandlesManager().getCurrentHandle().
					getScrollPane().getSVGCanvas().getProjectFile());
			
			if(blinkings!=null){
				
				for(String blinkingId : blinkings.keySet()){
					
					map.put(blinkingId, blinkingId);
				}
			}
			
		}else if(getType().equals(EditableItem.SYMBOL_CHOOSER)){
			
			//getting the symbols that can be found under the animated node
			String id="";
			
			for(Node cur=parentElement.getFirstChild(); cur!=null;  cur=cur.getNextSibling()){
				
				if(cur instanceof Element && ! cur.getNodeName().startsWith("rtda:")){
					
					id=((Element)cur).getAttribute("id");
					
					if(id!=null && ! id.equals("")){
						
						map.put(id, id);
					}
				}
			}
			
		}else{
			
			//getting the child items of the spec element
			NodeList childNodes=specElement.getElementsByTagName("item");
			
			if(childNodes!=null){
				
				Element cur=null;
				
				for(int i=0; i<childNodes.getLength(); i++){
					
					cur=(Element)childNodes.item(i);
					map.put(cur.getAttribute("name"), cur.getAttribute("value"));
				}
			}
		}
		
		return map;
	}
	
	@Override
	public String getRef() {
		return getSpecAttribute("ref");
	}
	
	@Override
	public int getTagType() {
		return EditableItem.getType(getSpecAttribute("tagtype"));
	}
	
	@Override
	public String getType() {
		
		String value="";
		
		if(isGroupAttribute){
			
			if(groupAttributes.size()>0){
				
				value=groupAttributes.getFirst().getSpecElement().getAttribute("type");
			}

		}else{
			
			value=specElement.getAttribute("type");
		}
		
		return value;
	}
	
	@Override
	public String getValue() {
		
		String value="";
		
		if(isGroupAttribute){
			
			for(AttributeObject obj : groupAttributes){
				
				value+=obj.getParentElement().getAttribute(obj.getName())+separator;
			}
			
		}else{
			
			value=getParentElement().getAttribute(getName());
		}
		
		return value;
	}
	
	@Override
	public void setValue(String value) {
		
		this.currentValue=value;
	}
	
	@Override
	public void validateChanges() {

		if(currentValue!=null){
			
			//getting the currently selected element
			Set<Element> selectedElements=
				handle.getSelection().getSelectedElements();
		
			Element selectedElement=null;
			
			if(selectedElements.size()>0){
				
				selectedElement=selectedElements.iterator().next();
			}
			
			String value=currentValue;
			
			if(isGroupAttribute){
				
				value=value.trim();
				String cur="";
				int pos=-1;
				final HashMap<String, String> attributeNameToValue=
					new HashMap<String, String>();
				final HashMap<String, String>	lastAttributeNameToValue=
					new HashMap<String, String>();

				for(AttributeObject obj : groupAttributes){
					
					pos=value.indexOf(separator);

					if(pos!=-1){
						
						cur=value.substring(0, pos);
						value=value.substring(pos+separator.length(), value.length());
						lastAttributeNameToValue.put(obj.getName(), obj.getValue());
						attributeNameToValue.put(obj.getName(), cur);
						
					}else{

						break;
					}
				}

				//executing the changes
				final Element parent=parentElement;
				final SVGHandle hnd=handle;
				final Element fselectedElement=selectedElement;
				
				Runnable executeRunnable=new Runnable() {
					
					public void run() {
						
						String val="";
						
						for(String name : attributeNameToValue.keySet()) {
							
							val=attributeNameToValue.get(name);
							parent.setAttribute(name, val);
						}
						
						hnd.getSvgDOMListenerManager().fireNodeChanged(parent);
						
						if(fselectedElement!=null){
							
							hnd.getSelection().handleSelection(fselectedElement, false, true);
						}
					}
				};
				
				//the undo runnable
				Runnable undoRunnable=new Runnable() {
					
					public void run() {
						
						String val="";
						
						for(String name : attributeNameToValue.keySet()) {
							
							val=attributeNameToValue.get(name);
							parent.setAttribute(name, val);
						}
						
						hnd.getSvgDOMListenerManager().fireNodeChanged(parent);
						
						if(fselectedElement!=null){
							
							hnd.getSelection().handleSelection(fselectedElement, false, true);
						}
					}
				};
				
				//the set of the modified elements
				animationObject.addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);

			}else{
				
				final String name=getName(), lastValue=getValue(), newValue=value;
				final Element directParentElement=parentElement;
				final Element animationElement=animationObject.getAnimationElement();
				final Element childSpecElement=animationObject.getSpecChildElement();
				final SVGHandle hnd=handle;

				//handling the change of this attribute value
				if(getType().equals(EditableItem.TAG_CHOOSER) && 
						getTagType()==TagToolkit.ENUMERATED && 
							! directParentElement.getAttribute(name).equals(newValue)){//TODO
					
					//storing the list of the children of the animation element
					final LinkedList<Element> oldChildren=new LinkedList<Element>();
					
					for(Node node=animationElement.getFirstChild(); node!=null; node=node.getNextSibling()) {
						
						if(node instanceof Element) {
							
							oldChildren.add((Element)node);
						}
					}
					
					//getting the list of the values of the tag
					final LinkedList<String> tagValues=
						DataBaseNodeToolkit.getEnumeratedTagValues(getDocument(), value);
					final String tagValueAttribute=animationObject.getChildTagValueAttribute();
					final LinkedList<Element> newChildren=new LinkedList<Element>();
					
					//creating the new children
					if(! newValue.equals("")) {
						
						Element newChild=null, att=null;
						
						for(String tagValue : tagValues) {
							
					        newChild=animationElement.getOwnerDocument().createElementNS(	
					        		animationElement.getNamespaceURI(), childSpecElement.getAttribute("name"));
					        
					        //creating the attributes of the new child
					        for(Node node=animationObject.getSpecChildElement().getFirstChild(); node!=null; 
					        		node=node.getNextSibling()) {
					        	
					        	if(node instanceof Element) {
					        		
					        		att=(Element)node;
					        		newChild.setAttribute(att.getAttribute(ItemObject.nameAttribute), 
																		att.getAttribute(ItemObject.defaultValueAttribute));
					        	}
					        }
					        
					        //setting the tag value attribute
					        newChild.setAttribute(tagValueAttribute, tagValue);
					        newChildren.add(newChild);
						}
					}
					
					final Element fselectedElement=selectedElement;

					//the runnable used for executing the action and redoing it
					Runnable executeRunnable=new Runnable() {

						public void run() {

							directParentElement.setAttribute(name, newValue);
							
							//removing all the previous children
							for(Element child : oldChildren) {
								
								animationElement.removeChild(child);
							}
							
							//adding all the new children
							for(Element child : newChildren) {
								
								animationElement.appendChild(child);
							}
							
							hnd.getSvgDOMListenerManager().
								fireStructureChanged(animationElement, animationElement);
							
							if(fselectedElement!=null){
								
								hnd.getSelection().handleSelection(fselectedElement, false, true);
							}
						}
					};
					
					Runnable undoRunnable=new Runnable() {

						public void run() {

							directParentElement.setAttribute(name, lastValue);
							
							//removing all the new children
							for(Element child : newChildren) {
								
								animationElement.removeChild(child);
							}
							
							//adding all the new children
							for(Element child : oldChildren) {
								
								animationElement.appendChild(child);
							}
							
							hnd.getSvgDOMListenerManager().
								fireStructureChanged(animationElement, animationElement);
							
							if(fselectedElement!=null){
								
								hnd.getSelection().handleSelection(fselectedElement, false, true);
							}
						}
					};

					animationObject.addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
					
				}else if(getConstraint().equals("tag") && 
						getTagType()==TagToolkit.ANALOGIC) {//TODO

					//getting the min and max of this tag
					Point2D point=DataBaseNodeToolkit.getMinAndMax(getDocument(), newValue);
					String minVal="", maxVal="";
					
					//getting the value of the tagMin and tagMax attributes
					if(point!=null){
						
						if(! Double.isNaN(point.getX())) {
							
							minVal=FormatStore.format(point.getX());
						}
						
						if(! Double.isNaN(point.getY())) {
							
							maxVal=FormatStore.format(point.getY());
						}
					}

					final String fminVal=minVal;
					final String fmaxVal=maxVal;
					final String oldMinVal=directParentElement.getAttribute(tagMinAtt);
					final String oldMaxVal=directParentElement.getAttribute(tagMaxAtt);
					
					//modifying the attribute value
					Runnable executeRunnable=new Runnable() {

						public void run() {
							
							if(! fminVal.equals("")){
								
								directParentElement.setAttribute(tagMinAtt, fminVal);
							}
							
							if(! fmaxVal.equals("")){
								
								directParentElement.setAttribute(tagMaxAtt, fmaxVal);
							}

							directParentElement.setAttribute(name, newValue);
							hnd.getSvgDOMListenerManager().
								fireNodeChanged(directParentElement);
						}
					};
					
					Runnable undoRunnable=new Runnable() {

						public void run() {
							
							directParentElement.setAttribute(tagMinAtt, oldMinVal);
							directParentElement.setAttribute(tagMaxAtt, oldMaxVal);

							directParentElement.setAttribute(name, lastValue);
							hnd.getSvgDOMListenerManager().
								fireNodeChanged(directParentElement);
						}
					};

					animationObject.addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
					
				}else {
				
					//modifying the attribute value
					Runnable executeRunnable=new Runnable() {

						public void run() {

							directParentElement.setAttribute(name, newValue);
							hnd.getSvgDOMListenerManager().
								fireNodeChanged(directParentElement);
						}
					};
					
					Runnable undoRunnable=new Runnable() {

						public void run() {

							directParentElement.setAttribute(name, lastValue);
							hnd.getSvgDOMListenerManager().
								fireNodeChanged(directParentElement);
						}
					};

					animationObject.addUndoRedoAction(executeRunnable, undoRunnable, executeRunnable);
				}
			}
		}
	}
	
	/**
	 * @return Returns the specElement.
	 */
	public Element getSpecElement() {
		return specElement;
	}
	
	@Override
	public Element getAnimationElement() {
		return animationObject.getAnimationElement();
	}
	
	@Override
	public AnimationObject getAnimationObject() {

		return animationObject;
	}

	@Override
	public Element getSVGElement() {

		return (Element)getAnimationElement().getParentNode();
	}
	
	/**
	 * @return the parent element
	 */
	public Element getParentElement() {
		return parentElement;
	}
	
	@Override
	public boolean isGroup() {
		return isGroupAttribute;
	}
	
	@Override
	public Document getDocument() {
		
		return getParentElement().getOwnerDocument();
	}
	
	/**
	 * returns the attribute that has the given name
	 * @param attributeName the name of an attribute
	 * @return the value of the attribute denoted by the given name
	 */
	protected String getSpecAttribute(String attributeName){
		
		String value="";
		
		if(isGroupAttribute){
			
			for(AttributeObject obj : groupAttributes){
				
				value+=obj.getSpecElement().getAttribute(attributeName)+separator;
			}
			
		}else{
			
			value=specElement.getAttribute(attributeName);
		}
		
		return value;
	}
	
	/**
	 * @see fr.itris.glips.rtdaeditor.anim.EditableItem#isEditable()
	 */
	@Override
	public boolean isEditable() {

		if(getType().equals(LABEL)) {
			
			return false;
		}
		
		return true;
	}
}
