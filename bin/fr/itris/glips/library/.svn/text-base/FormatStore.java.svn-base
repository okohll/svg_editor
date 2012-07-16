package fr.itris.glips.library;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * the decimal format store
 * @author ITRIS, Jordi SUC
 */
public class FormatStore {

    /**
     * the decimal format used to print numbers
     */
	public static DecimalFormat format;
	
	/**
	 * the decimal format used to display numbers
	 */
	public static DecimalFormat displayFormat;
	
	static{
		
		//sets the format object that will be used to convert a double value into a string
		DecimalFormatSymbols symbols=new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format=new DecimalFormat("############.#####################################################################################", 
				symbols);
		format.setDecimalSeparatorAlwaysShown(false);
		
		symbols=new DecimalFormatSymbols();
		displayFormat=new DecimalFormat("##########.#", symbols);
		displayFormat.setDecimalSeparatorAlwaysShown(false);
	}
	
	/**
	 * returns the formatted string corresponding to the provided value
	 * @param value a double value
	 * @return the string representation of the provided double value
	 */
	public static String format(double value){
		
		if(Double.isNaN(value)){
			
			value=1;
		}
		
		if(Double.isInfinite(value)){
			
			value=Double.MAX_VALUE;
		}
		
		return format.format(value);
	}
}
