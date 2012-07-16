package fr.itris.glips.svgeditor.shape;

import java.awt.geom.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling svg rectangle elements
 * @author ITRIS, Jordi SUC
 */
public class RectangleShape extends RectangularShape {

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RectangleShape(Editor editor) {
		
		super(editor);
		
		shapeModuleId="RectangleShape";
		handledElementTagName="rect";
		retrieveLabels();
		createMenuAndToolItems();
	}
	
	@Override
	protected void retrieveLabels() {

		super.retrieveLabels();
		
		radiusModificationUndoRedoLabel=
			ResourcesManager.bundle.getString(shapeModuleId+"RadiusModificationUndoRedoLabel");
	}
	
	@Override
	public int getLevelCount() {

		return 2;
	}

	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds){
		    
		//the edited document
		Document doc=handle.getCanvas().getDocument();
		
		//normalizing the bounds of the element
		if(bounds.getWidth()<1){
		    
		    bounds.setRect(bounds.getX(), bounds.getY(), 1, bounds.getHeight());
		}
		
		if(bounds.getHeight()<1){
		    
			bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), 1);
		}
		
		//creating the rectangle
		Element element=doc.createElementNS(
				doc.getDocumentElement().getNamespaceURI(), handledElementTagName);
		
		EditorToolkit.setAttributeValue(element, xAtt, bounds.getX());
		EditorToolkit.setAttributeValue(element, yAtt, bounds.getY());
		EditorToolkit.setAttributeValue(element, wAtt, bounds.getWidth());
		EditorToolkit.setAttributeValue(element, hAtt, bounds.getHeight());
		
		//getting the last color that has been used by the user
		String colorString=Editor.getColorChooser().getColorString(ColorManager.getCurrentColor());
		element.setAttributeNS(null, "style", "fill:"+colorString+";stroke:#000000;");
		
		//inserting the element in the document and handling the undo/redo support
		insertShapeElement(handle, element);
		
		return element;
	}
}
