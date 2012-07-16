package fr.itris.glips.rtdaeditor.dbchooser;

import javax.swing.tree.*;
import org.w3c.dom.*;
import java.net.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.*;
import java.util.*;

/**
 * the class handling the data base tree nodes
 * @author ITRIS, Jordi SUC
 */
public class DataBaseTreeNode extends DefaultMutableTreeNode{
    
    /**
     * the root element of the data base
     */
    private Element rootDataBase=null;
    
    /**
     * an element of the rtda data base associated with this tree node
     */
    private Element element=null;
    
    /**
     * the id of the node
     */
    private String id="";
    
    /**
     * the name of the tag denoted by this node
     */
    private String tagName="";
    
    /**
     * the type of the node
     */
    private int nodeType=TagToolkit.NOT_A_TAG;
    
    /**
     * the node filter
     */
    private DataBaseNodeFilter filter=null;
    
    /**
     * whether this node can be displayed in the tree or not
     */
    private boolean canBeDisplayed=false;
    
    /**
     * whether this node can be selected
     */
    private boolean canBeSelected=false;
    
    /**
     * the value of the location attribute of the node if this node has the type "VIEW"
     */
    private String location="";
    
    /**
     * the list of the enumerated values
     */
    private LinkedList<String> enumeratedValues=new LinkedList<String>();
    
    /**
     * the name of the icon to be displayed for this node
     */
    private String iconName="";
    
    /**
     * the tool tip text to be displayed for this node
     */
    private String toolTipText="";
    
    /**
     * the constructor of the class
     * @param rootDataBase the root database
     * @param filter the node filter
     * @param element an element of the rtda data base associated with this tree node
     */
    public DataBaseTreeNode(Element rootDataBase, 
    		DataBaseNodeFilter filter, Element element) {
        
        this.rootDataBase=rootDataBase;
        this.filter=filter;
        this.element=element;
        this.id=this.element.getAttribute("id");
        
        //computes the type of this node
        this.nodeType=TagToolkit.getNodeType(this.element.getNodeName());
        
        //getting the icon name for this node
        this.iconName=TagToolkit.getIconName(this.nodeType);
        
        //getting the tool tip text for this node
        this.toolTipText=TagToolkit.getTagLabel(this.nodeType);
        
        //getting the level of this node
        int elementLevel=getElementLevel()-1;

        //whether this tag type is equivalent with the filter's tag type
        boolean isNodeTypeEquivalent=TagToolkit.areTypesEquivalent(	
        		this.nodeType, this.filter.getTagType(),  this.filter.isStrictEquality());
        
        //computing whether the node can be selected
        this.canBeSelected=isNodeTypeEquivalent;

        //whether the element is the root node of the data base or not
        boolean isRootNode=this.element.equals(this.rootDataBase);

        String absoluteLocation="";
        
        if(! (filter.getExcludedViewPath()!=null && ! filter.getExcludedViewPath().equals("") && 
        	filter.getExcludedViewPath().equals(getTagName()))) {
        	
            if(this.nodeType==TagToolkit.VIEW  && 
            		filter.getTagType()==TagToolkit.VIEW){
            	
                //computing the absolute location of the view if this node corresponds to a view
                try{
                	absoluteLocation=DataBaseToolkit.getAbsoluteLocation(
                		new URI(this.element.getAttribute("location")), 
                				new URI(filter.getExcludedViewPath()));
                }catch (Exception ex){absoluteLocation=""; ex.printStackTrace();}

                //computes whether this node can be displayed or not
                if((filter.getExcludedViewPath()!=null && ! filter.getExcludedViewPath().equals("") && 
                	! filter.getExcludedViewPath().equals(absoluteLocation)) || 
                	(filter.getExcludedViewPath()==null || (filter.getExcludedViewPath()!=null && 
                			filter.getExcludedViewPath().equals("")))){
                	
                    //the node can be displayed
                    this.canBeDisplayed=true;
                }
                
            }else if((this.nodeType==TagToolkit.ANALOGIC || 
            		this.nodeType==TagToolkit.ANALOGIC_FLOAT || 
            		this.nodeType==TagToolkit.ANALOGIC_INTEGER ||
            		this.nodeType==TagToolkit.ENUMERATED ||
            		this.nodeType==TagToolkit.STRING) && 
            		filter.getTagType()==TagToolkit.ANY_TAG){

                //the node can be displayed
                this.canBeDisplayed=true;
            	
            }else{
            	
                //computes whether this node can be displayed or not
                if(((isRootNode || (
                    this.filter.isShowOnlyThisLevel() && (           
                    (elementLevel<this.filter.getLevel() && 
                    this.nodeType==TagToolkit.NOT_A_TAG) ||
                    (this.filter.getLevel()==elementLevel && 
                    isNodeTypeEquivalent))) || (
                    ! this.filter.isShowOnlyThisLevel() && (
                    this.nodeType==TagToolkit.NOT_A_TAG || isNodeTypeEquivalent)))) &&
                    this.nodeType!=TagToolkit.ENUMERATED_CHILD) {
                    
                    //the node can be displayed
                    this.canBeDisplayed=true;
                }
            }
        }

        //if this node can have children, and can be displayed, the tree nodes are created
        if(this.canBeDisplayed && 
                (this.nodeType==TagToolkit.NOT_A_TAG || 
                this.nodeType==TagToolkit.ENUMERATED)) {
            
            //creating the tree nodes
            DataBaseTreeNode childTreeNode=null;
            Node node=null;
            String valueId="";
            
            //the number of the children of the node having the type : NOT_A_TAG
            int notATagChildrenCount=0;
            
            for(node=element.getFirstChild(); node!=null; node=node.getNextSibling()) {
                
                if(node instanceof Element) {
                    
                    if(this.nodeType==TagToolkit.ENUMERATED) {
                        
                        //getting the enumerated child id
                        valueId=((Element)node).getAttribute("id");
                        
                        if(valueId!=null && ! valueId.equals("")) {
                            
                            this.enumeratedValues.add(valueId);
                        }
                        
                    }else {
                        
                        //creating the new tree node
                        childTreeNode=new DataBaseTreeNode(rootDataBase, this.filter, (Element)node);
                        
                        if(childTreeNode.canBeDisplayed()) {
                            
                            //adding the child tree node to its parent node
                            add(childTreeNode);
                            notATagChildrenCount++;
                        }
                    }
                }
            }
            
            if(this.nodeType==TagToolkit.NOT_A_TAG) {
                
                //checking if the tag that has the type : NOT_A_TAG has displayed children, 
                //if not, it is not displayed
                if(notATagChildrenCount<=0) {

                    this.canBeDisplayed=false;
                }
            }
        }
    }
    
    /**
     * @return whether the tree node can be displayed or not
     */
    protected boolean canBeDisplayed() {
        
        return this.canBeDisplayed;
    }
    
    /**
     * @return  whether this node can be selected
     */
    public boolean canBeSelected() {
        return this.canBeSelected;
    }
    
    /**
     * @return Returns the nodeType.
     */
    public int getNodeType() {
        return this.nodeType;
    }
    
    /**
     * @return the tag name corresponding to this node
     */
    public String getTagName() {
        
        if(this.nodeType!=TagToolkit.NOT_A_TAG) {
            
            //computing the tag name
            this.tagName=EditorAnimationsToolkit.normalizePath(
                    DataBaseNodeToolkit.getPath(this.rootDataBase, this.element));
        }
        
        return this.tagName;
    }
    
    /**
     * @return the list of the values of the enumerated tag
     */
    public LinkedList<String> getEnumeratedValues(){
        
        return this.enumeratedValues;
    }
    
    /**
     * @return Returns the location.
     */
    public String getLocation() {
        
        //if this node represents a view node, the location attribute value is retrieved
        if(this.canBeDisplayed && this.nodeType==TagToolkit.VIEW){
            
            this.location=this.element.getAttribute("location");
        }
        
        return this.location;
    }
    
    /**
     * @return 	the level of the element, i.e.: the number of levels above this node 
     * 					(the distance from the root to this node)
     */
    protected int getElementLevel() {
        
        int elementLevel=-1;
        
        if(this.element!=null) {
            
            Node parentNode=this.element;
            
            while(parentNode!=null && parentNode instanceof Element) {
                
                parentNode=parentNode.getParentNode();
                elementLevel++;
            }
        }
        
        return elementLevel;
    }
    
    /**
     * retrieves and returns the tree node under this tree node corresponding to the given element
     * @param el an element
     * @return the tree node corresponding to the given element
     */
    public DataBaseTreeNode getTreeNodeForElement(Element el) {
        
        DataBaseTreeNode treeNode=null;
        
        if(el!=null) {
            
            if(el.equals(this.element)) {
                
                //the searched element is this tree node
                treeNode=this;
                
            }else if(this.nodeType==TagToolkit.NOT_A_TAG){
                
                //searching the element into the children of the the tree node
                DataBaseTreeNode childTreeNode=null;
                Enumeration<?> childrenTreeNodes=children();
                
                while(childrenTreeNodes.hasMoreElements()) {
                    
                    childTreeNode=(DataBaseTreeNode)childrenTreeNodes.nextElement();
                    
                    if(childTreeNode!=null) {
                        
                        treeNode=childTreeNode.getTreeNodeForElement(el);
                        
                        if(treeNode!=null){
                            
                            break;
                        }
                    }
                }
            }
        }
        
        return treeNode;
    }
    
	 @Override
    public String toString() {
        
        return this.id;
    }
    
    /**
     * @return the name of the icon that will be displayed for this node
     */
    public String getIconName(){
        
        return iconName;
    }
    
    /**
     * @return the tool tip text associated with this node
     */
    public String getToolTipText() {
        
        return this.toolTipText;
    }
}
