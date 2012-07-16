package fr.itris.glips.extension.jwidget.trends;

/**
 * the class of the toolkit for the trends
 * @author ITRIS, Jordi SUC
 */
public class TrendsToolkit {

	/**
	 * the string separating a tag name from its type
	 */
	public static final String tagNameSeparator="/**/";
	
	/**
	 * returns the displayable tag name corresponding to the given tag name
	 * @param tagName a tag name
	 * @return the displayable tag name corresponding to the given tag name
	 */
	public static String getDisplayableTagName(String tagName){
		
		String value="";
		
		//getting the position of the separator
		int pos=tagName.indexOf(TrendsToolkit.tagNameSeparator);
		
		if(pos!=-1){
			
			//getting the displayable tag name string
			value=tagName.substring(0, pos);
		}

		return value;
	}
	
	/**
	 * returns the tag type corresponding to the given tag name
	 * @param tagName a tag name
	 * @return the tag type corresponding to the given tag name
	 */
	public static String getTagType(String tagName){
		
		String tagType="";
		
		//getting the position of the separator
		int pos=tagName.indexOf(TrendsToolkit.tagNameSeparator);
		
		if(pos!=-1){
			
			//getting the string defining the tag type
			tagType=tagName.substring(
					pos+TrendsToolkit.tagNameSeparator.length(), tagName.length());
		}

		return tagType;
	}
}
