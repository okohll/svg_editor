package fr.itris.glips.rtdaeditor.dbchooser;

import fr.itris.glips.library.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * the class allowing to filter the nodes, i.e., 
 * 
 * @author ITRIS, Jordi SUC
 */
public class DataBaseNodeFilter {

    /**
     * the split tag name string
     */
    private String[] splitTagName=null;
    
    /**
     * the tag name
     */
    private String tagName=null;
    
    /**
     * the type of the tag
     */
    private int tagType=TagToolkit.NOT_A_TAG;

    /**
     * the level at which the nodes that could be selected should be found
     */
    private int level=0;
    
    /**
     * whether two tag types should be comparent with a strict equality or not
     */
    private boolean strictEquality=false;
 
    /**
     * whether or not the only nodes that can be displayed are those who are at the last level
     */
    private boolean showOnlyThisLevel=false;
    
    /**
     * the path of the view that should be excluded from the view chooser
     */
    private String excludedViewPath;
	
    /**
     * the constructor of the class
     * @param tagName the split tag name string
     * @param level the level at which the nodes that could be selected should be found
     * @param tagType the type of node to be chosen
     * @param strictEquality whether two tag types should be compared with a strict equality or not
     * @param showOnlyThisLevel 	whether or not the only nodes that can be 
     * 	displayed are those who are at the last level
     * @param excludedViewPath the path of the view that should be excluded from the view chooser
     */
	public DataBaseNodeFilter(String tagName, int level, int tagType, boolean strictEquality, 
																boolean showOnlyThisLevel, String excludedViewPath) {
		
        this.level=level-1;
		this.tagType=tagType;
		this.strictEquality=strictEquality;
		this.showOnlyThisLevel=showOnlyThisLevel;
		this.excludedViewPath=excludedViewPath;
		setTagName(tagName);
	}
	
	/**
	 * sets the new tag name for the filter
	 * @param tagName a tag name
	 */
	public void setTagName(String tagName){
		
		this.tagName=tagName;
		
		//splitting the tag name
		this.splitTagName=Toolkit.getSplitPath(tagName);
        
        if(splitTagName!=null && splitTagName.length==0) {
            
            splitTagName=null;
        }
	}

	/**
	 * @return the depth
	 */
	public int getLevel(){
		
		if(this.splitTagName!=null) {
			
			return this.splitTagName.length-1;
		}
		
		return level;
	}
	
	/**
	 * @return the tag name
	 */
	public String getTagName() {
		return this.tagName;
	}

	/**
	 * @return the split tag name
	 */
	public String[] getSplitTagName() {
		return this.splitTagName;
	}

	/**
	 * @return whether or not the only nodes that can be 
	 * displayed are those who are at the last level
	 */
	public boolean isShowOnlyThisLevel() {
		return this.showOnlyThisLevel;
	}

	/**
	 * @return the tag type
	 */
	public int getTagType() {
		return this.tagType;
	}

	/**
	 * @return whether two tag types should be compared
	 * with a strict equality or not
	 */
	public boolean isStrictEquality() {
		return this.strictEquality;
	}

	/**
	 * @return the excluded view path.
	 */
	public String getExcludedViewPath() {
		return excludedViewPath;
	}
	
}
