package fr.itris.glips.svgeditor.actions.dom;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.shape.*;

/**
 * the class used to execute rotate actions on svg elements
 * @author Jordi SUC
 */
public class CanvasSizeModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int RESIZE_CANVAS=0, 
		FIT_CANVAS_SIZE_TO_CONTENT=1;
	
	/**
	 * the canvas size dialog
	 */
	private CanvasSizeDialog canvasSizeDialog;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public CanvasSizeModule(Editor editor) {
		
		//setting the id
		moduleId="CanvasSize";

		//filling the arrays of the types
		int[] types={RESIZE_CANVAS, FIT_CANVAS_SIZE_TO_CONTENT};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"ResizeCanvas", "FitCanvasSizeToContent"};
		actionsIds=ids;
		
		//creating the canvas modifier dialog
		if(Editor.getParent() instanceof Frame){

			canvasSizeDialog=
				new CanvasSizeDialog(this, (Frame)Editor.getParent());
			
		}else if(Editor.getParent() instanceof JDialog){
			
			canvasSizeDialog=
				new CanvasSizeDialog(this, (JDialog)Editor.getParent());
		}
		
		//creating the items
		createItems();
	}
	
	@Override
	protected void doAction(
		SVGHandle handle, Set<Element> elements, 
			int index, ActionEvent evt) {
		
 		//getting the type of the action
		int type=actionsTypes[index];

		switch(type){
			
			case RESIZE_CANVAS :
				
				//getting the current size of the canvas
				Point2D currentSize=handle.getCanvas().getGeometryCanvasSize();
				
				//showing the dialog
				canvasSizeDialog.showDialog((JComponent)evt.getSource(), currentSize);
				break;
				
			case FIT_CANVAS_SIZE_TO_CONTENT :

				resizeToContent(handle);
				break;
		}
	}
	
	/**
	 * changes the canvas size
	 * @param newSize the new canvas size
	 */
	protected void changeCanvasSize(final Point2D newSize){
		
		if(newSize!=null){
			
			//getting the current svg handle
			final SVGHandle currentHandle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			if(currentHandle!=null){
				
				//getting the current size of the canvas
				final Point2D currentSize=currentHandle.getCanvas().getGeometryCanvasSize();
				
				//the execute and undo runnables
				final Runnable executeRunnable=new Runnable() {

					public void run() {

						currentHandle.getCanvas().setCanvasSize(newSize);
					}
				};
				
				//the undo runnable
				final Runnable undoRunnable=new Runnable() {

					public void run() {

						currentHandle.getCanvas().setCanvasSize(currentSize);
					}
				};

				//creating the undo/redo action
				UndoRedoAction undoRedoAction=new UndoRedoAction(
					undoRedoActionsLabels[0], executeRunnable, 
						undoRunnable, executeRunnable, new HashSet<Element>());
				
				//adding the undo/redo action to the undo stack
				UndoRedoActionList actionlist=
					new UndoRedoActionList(undoRedoAction.getName(), false);
				actionlist.add(undoRedoAction);
				currentHandle.getUndoRedo().addActionList(actionlist, true);
			}
		}
	}
	
	/**
	 * resizes the canvas denoted by the provided handle so that all 
	 * whitespaces around the content shape are removed
	 * @param handle a svg handle
	 */
	protected void resizeToContent(final SVGHandle handle){
		
		//checking if shape nodes exists in the canvas
		NodeList childNodes=handle.getCanvas().getDocument().
			getDocumentElement().getChildNodes();
		Node node=null;
		Element element=null;
		Rectangle2D wholeBounds=null;
		Rectangle2D bounds=null;
		Set<Element> modifiedElements=new HashSet<Element>();
		
		for(int i=0; i<childNodes.getLength(); i++){
			
			node=childNodes.item(i);
			
			if(node instanceof Element && 
				EditorToolkit.isElementAShape((Element)node)){
				
				element=(Element)node;
				
				//getting the sensitive bounds of the shape
				bounds=handle.getSvgElementsManager().
					getSensitiveBounds(element);
				modifiedElements.add(element);
				
				if(bounds!=null){
					
					//merging the bounds
					if(wholeBounds==null){
						
						wholeBounds=bounds;
						
					}else{
						
						wholeBounds.add(bounds);
					}
				}
			}
		}
		
		if(wholeBounds!=null){
			
			wholeBounds.setRect(wholeBounds.getX(), wholeBounds.getY(), 
					wholeBounds.getWidth(), wholeBounds.getHeight());
			
			//translating each shape element so that the upper left corner of the 
			//bounds matches the origin of the svg file
			AbstractShape shapeModule=null;
			Set<UndoRedoAction> undoRedoActions=
				new HashSet<UndoRedoAction>();
			UndoRedoAction action=null;
			Set<Element> elts=null;
			Point2D translationFactors=new Point2D.Double(
				-wholeBounds.getX(), -wholeBounds.getY());
			
			for(Element el : modifiedElements){
				
				//getting the shape module for this element
				shapeModule=ShapeToolkit.getShapeModule(el);
				
				if(shapeModule!=null){
					
					//executing the action
					elts=new HashSet<Element>();
					elts.add(el);
					action=shapeModule.translate(handle, elts, translationFactors);
					
					if(action!=null){
						
						undoRedoActions.add(action);
					}
				}
			}
			
			//resizing the canvas
			final Point2D currentSize=handle.getCanvas().getGeometryCanvasSize();
			final Point2D newCanvasSize=new Point2D.Double(
				wholeBounds.getWidth(), wholeBounds.getHeight());
			
			//the execute runnable
			final Runnable executeRunnable=new Runnable() {

				public void run() {

					handle.getCanvas().setCanvasSize(newCanvasSize);
				}
			};
			
			//the undo runnable
			final Runnable undoRunnable=new Runnable() {

				public void run() {

					handle.getCanvas().setCanvasSize(currentSize);
				}
			};

			//creating the undo/redo action
			action=new UndoRedoAction(
				"", executeRunnable, undoRunnable, executeRunnable, 
					new HashSet<Element>());
			
			//creating the undo/redo action list
			UndoRedoActionList actionList=new UndoRedoActionList(
					undoRedoActionsLabels[1], false);
			actionList.addAll(undoRedoActions);
			actionList.add(action);
			
			//adding the action list to the undo/redo stack
			handle.getUndoRedo().addActionList(actionList, true);
		}
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		boolean selectionCorrect=false;
		
		if(index==1){
			
			//getting the current handle
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			if(handle!=null){
				
				//checking if shape nodes exists in the canvas
				NodeList childNodes=handle.getCanvas().getDocument().
					getDocumentElement().getChildNodes();
				Node node=null;
				
				for(int i=0; i<childNodes.getLength(); i++){
					
					node=childNodes.item(i);
					
					if(node instanceof Element && 
						EditorToolkit.isElementAShape((Element)node)){
						
						selectionCorrect=true;
						break;
					}
				}
			}

		}else{
			
			selectionCorrect=true;
		}
		
		return selectionCorrect;
	}
}

