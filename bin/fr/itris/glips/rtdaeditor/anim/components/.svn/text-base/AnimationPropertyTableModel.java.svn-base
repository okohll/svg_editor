package fr.itris.glips.rtdaeditor.anim.components;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.anim.*;



import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the model of the table used for modifying the properties of an animation
 * @author ITRIS, Jordi SUC
 */
public class AnimationPropertyTableModel extends AnimationTableModel{

	/**
	 * the name and the value labels
	 */
	private String nameLabel="", valueLabel="";
	
	/**
	 * the constructor of the class
	 * @param animationTable the animations table
	 */
	public AnimationPropertyTableModel(AnimationTable animationTable){
		
		super(animationTable);
		
		//getting the labels
		try{
			nameLabel=ResourcesManager.bundle.getString("rtdaanim_propertyTableName");
			valueLabel=ResourcesManager.bundle.getString("rtdaanim_propertyTableValue");
		}catch (Exception ex){}
	}
	
	@Override
	public String getColumnName(int col) {
		
		String columnName="";
		
		switch (col){
		
			case 0 :
				columnName=nameLabel;
				break;
				
			case 1 :
				columnName=valueLabel;
				break;
		}
		
		return columnName;
	}

	/**
	 * @return the number of rows in the model
	 */
	public int getRowCount() {

		if(animationObject!=null){
			
			return animationObject.getAttributesList().size();
		}
		
		return 0;
	}

	/**
	 * @return the number of columns in the model
	 */
	public int getColumnCount() {

		return 2;
	}

	/**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 * @param rowIndex - the row whose value is to be queried
     * @param columnIndex - the column whose value is to be queried 
     * @return the value Object at the specified cell
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {

		if(animationObject!=null && animationObject.getAttributesList().size()>rowIndex){

			return animationObject.getAttributesList().get(rowIndex);
		}
		
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		if(aValue!=null && aValue instanceof EditableItem){
			
			EditableItem item=(EditableItem)aValue;
			item.validateChanges();
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		if(columnIndex==1){
			
			return true;
		}
		
		return true;
	}

	@Override
	public void setAnimationObject(AnimationObject animObject) {

		//getting the current handle
		SVGHandle handle=animObject.getSVGHandle();

		//removing the former dom listener
		if(animationObject!=null) {
			
			handle.getSvgDOMListenerManager().
				removeDOMListener(domListener);
		}
		
		this.animationObject=animObject;

		//creating the new dom listener
		domListener=new SVGDOMListener(animationObject.getAnimationElement()) {

			@Override
			public void nodeChanged() {

				fireTableDataChanged();
			}

			@Override
			public void nodeInserted(Node insertedNode) {}
			
			@Override
			public void nodeRemoved(Node removedNode) {}

			@Override
			public void structureChanged(Node lastModifiedNode) {

				fireTableStructureChanged();
			}
		};
		
		//registering the listener
		handle.getSvgDOMListenerManager().
			addDOMListener(domListener);
		fireTableDataChanged();
	}
}
