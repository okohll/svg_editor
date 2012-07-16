package fr.itris.glips.rtdaeditor.anim.components;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class of the child table model
 * @author ITRIS, Jordi SUC
 */
public class AnimationChildTableModel extends AnimationTableModel{
	
	/**
	 * the animation child table model
	 * @param animationTable the animation table
	 */
	public AnimationChildTableModel(AnimationTable animationTable){
		
		super(animationTable);
	}
	
	@Override
	public String getColumnName(int col) {
		
		String columnName="";
		
		if(animationObject!=null){
			
			columnName=animationObject.getChildAttributeNamesList().get(col);
			
			try{
				columnName=animationObject.getLabel("rtdaanim_"+columnName);
			}catch (Exception ex){}
		}
		
		return columnName;
	}

	/**
	 * @return the number of rows in the model
	 */
	public int getRowCount() {

		if(animationObject!=null){
			
			return animationObject.getChildrenList().size();
		}
		
		return 0;
	}

	/**
	 * @return the number of columns in the model
	 */
	public int getColumnCount() {

		if(animationObject!=null){
			
			return animationObject.getChildAttributeNamesList().size();
		}
		
		return 0;
	}

	/**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 * @param rowIndex - the row whose value is to be queried
     * @param columnIndex - the column whose value is to be queried 
     * @return the value Object at the specified cell
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {

		if(animationObject!=null && animationObject.getChildrenList().size()>rowIndex){

			ChildObject childObject=animationObject.getChildrenList().get(rowIndex);
			
			if(childObject!=null && childObject.getAttributesList().size()>columnIndex){
				
				return childObject.getAttributesList().get(columnIndex);
			}
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
		
		//getting the editable item corresponding to this row
		if(animationObject!=null && animationObject.getChildrenList().size()>rowIndex){

			ChildObject childObject=animationObject.getChildrenList().get(rowIndex);
			
			if(childObject!=null && childObject.getAttributesList().size()>columnIndex){
				
				return childObject.getAttributesList().get(columnIndex).isEditable();
			}
		}
		
		return false;
	}

	@Override
	public void setAnimationObject(AnimationObject animObject) {

		//getting the current handle
		SVGHandle handle=animObject.getSVGHandle();

		//removing the former dom listener
		if(domListener!=null) {
			
			domListener.removeListener();
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
				
				if(animationObject!=null) {
					
					removeChildrenListeners(animationObject.getSVGHandle());
					animationObject.handleChildren();
					createChildrenListeners();
				}
				
				fireTableStructureChanged();
			}
		};
		
		//registering the listener
		handle.getSvgDOMListenerManager().
			addDOMListener(domListener);
		createChildrenListeners();
		fireTableDataChanged();
	}
	
}
