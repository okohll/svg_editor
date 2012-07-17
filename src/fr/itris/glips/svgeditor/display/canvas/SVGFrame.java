package fr.itris.glips.svgeditor.display.canvas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import libraries.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the internal frame containing the svg canvas
 * @author ITRIS, Jordi SUC
 */
public class SVGFrame extends JInternalFrame{

	/**
	 * the svg handle associated to this frame
	 */
	private SVGHandle svgHandle;
	
	/**
	 * the menuitem associated with the frame in the menu bar
	 */
	private JRadioButtonMenuItem menuitem=new JRadioButtonMenuItem();
	
	/**
	 * the panel containing the svg scrollpane and other widgets
	 */
	private JPanel contentPane=new JPanel();
	
	/**
	 * the svg scrollpane
	 */
	private SVGScrollPane scrollpane;
	
	/**
	 * the state bar
	 */
	private CanvasStateBar stateBar;
	
	/**
	 * the list of the runnables enabling to dispose the frame
	 */
	private LinkedList<Runnable> disposeRunnables=new LinkedList<Runnable>();
	
	/**
	 * 
	 * the constructor of the class
	 * @param svgHandle the svg handle associated to this frame
	 */
	public SVGFrame(SVGHandle svgHandle){
		
		super("", true, true, true, true);
		this.svgHandle=svgHandle;
		build();
	}
	
	/**
	 * builds the internal frame content pane
	 */
	protected void build(){
		
		//handlling the properties of the internal frame//
		
		//setting the icon
		final ImageIcon editorIcon=ResourcesManager.getIcon("EditorInner", false);
		setFrameIcon(editorIcon);
		
		//setting the location of the frame
		Rectangle bounds=Editor.getEditor().getPreferredWidgetBounds("frame");
		int nb=Editor.getEditor().getHandlesManager().getHandlesNumber();
		int offset=30;
		int beginX=75;
		int beginY=0;
		
		if(bounds!=null){
			
			beginX=bounds.x;
			beginY=bounds.y;
		}
		
		setLocation(beginX+nb*offset,beginY+nb*offset);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		//creating and adding an internal frame listener
		final InternalFrameListener internalFrameListener=new InternalFrameAdapter(){
			
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				
				svgHandle.close();
			}
		};
		
		addInternalFrameListener(internalFrameListener);
		
		//creating and adding the listener to the focus changes
		final VetoableChangeListener vetoableChangeListener=new VetoableChangeListener(){
			
			public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
				
				if(evt.getPropertyName()!=null && evt.getPropertyName().equals("selected") && 
					Editor.getEditor().getHandlesManager().getCurrentHandle()!=svgHandle && 
						((Boolean)evt.getNewValue()).booleanValue()){
					
					Editor.getEditor().getHandlesManager().setCurrentHandle(svgHandle.getName());
				}
			}
		};
		
		addVetoableChangeListener(vetoableChangeListener);
		
		//creating and handling the properties of the inner widgets of the internal frame//
		
		//handling the properties of the content pane
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(3, 3, 3, 3));
		
		//creating and adding the state bar
		stateBar=new CanvasStateBar();
		contentPane.add(stateBar, BorderLayout.SOUTH);
		
		//creating and adding the scrollpane
		scrollpane=new SVGScrollPane(svgHandle);
		contentPane.add(scrollpane, BorderLayout.CENTER);

		//adding a dispose runnable
		disposeRunnables.add(new Runnable(){
			
			public void run() {

				removeInternalFrameListener(internalFrameListener);
				removeVetoableChangeListener(vetoableChangeListener);
			}
		});
	}
	
	/**
	 * displays this frame
	 * @param canvasSize the canvas size
	 */
	public void displayFrame(Dimension canvasSize){
		
		if(canvasSize!=null){
			
			//adds the internal frame to the desktop panel contained in the main frame
			Editor.getEditor().getDesktop().add(this);
			getContentPane().add(contentPane);
			
			//packing
			pack();
			
			//computing the available space for the frame
			final Dimension availableSpace=new Dimension(
					Editor.getEditor().getDesktop().getWidth()-getX(), 
						Editor.getEditor().getDesktop().getHeight()-getY());	

			//computing the size of the frame so that it does 
			//not extend outside the desktop pane
			Dimension currentSize=getSize();
			Dimension newSize=new Dimension(currentSize);

			if(currentSize.width>availableSpace.width){
				
				newSize.width=availableSpace.width;
			}
			
			if(currentSize.height>availableSpace.height){
				
				newSize.height=availableSpace.height;
			}

			//setting the new size and repacking
			setPreferredSize(newSize);
			pack();
			
			setVisible(true);
		}
	}
	
	/**
	 * disposes this frame
	 */
	public void disposeFrame(){//TODO
		
		//executing the dispose runnables
		for(Runnable runnable : disposeRunnables){
			
			runnable.run();
		}
		
		contentPane.removeAll();
		stateBar.removeAll();
		scrollpane.dispose();

		removeAll();
		dispose();
		
		//removing all the listeners registered on the menuitem
		ActionListener[] listeners=menuitem.getActionListeners();
		
		if(listeners!=null){
			
			for(ActionListener listener : listeners){

				menuitem.removeActionListener(listener);
			}
		}
		
		svgHandle=null;
		menuitem=null;
		contentPane=null;
		scrollpane=null;
		stateBar=null;
		disposeRunnables=null;
	}
	
	/**
	 * sets the label of the internal frame
	 * @param name the string associated with the internal frame
	 */
	public void setSVGFrameLabel(String name){
		
		String label="";
		
	    try{
			File file=new File(name);
			label=file.toURI().toASCIIString();
			
			int pos=label.lastIndexOf("/");
			
			if(pos!=-1){
				
				label=label.substring(pos+1, label.length());
			}
			
			label=URIEncoderDecoder.decode(label);
			label=new String(label.getBytes(Charset.defaultCharset().name()));
		}catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		
		if(label.equals("") && name!=null && 
				! name.equals("")){
			
			label=name;	
		}
		
		if(svgHandle.isModified()){
			
			label=label.concat("*");
		}
		
		menuitem.setText(label);
		setTitle(label);
	}
	
	/**
	 * deselects this internal frame
	 */
	public void moveFrameToBack(){
		
		try{setSelected(false);}catch(PropertyVetoException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * shows this frame in the main frame
	 */
	public void moveFrameToFront(){
		
		try{setSelected(true);}catch(PropertyVetoException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * removes this internal from the desktop
	 */
	public void removeFromDesktop(){
		
		Editor.getEditor().getDesktop().remove(this);
		Editor.getEditor().getDesktop().repaint();
	}
	
	/**
	 * @return the menu item that will be displayed in
	 * the menu bar to switch from one SVG picture to another 
	 */
	public JMenuItem getFrameMenuItem(){
		return menuitem;
	}
	
	/**
	 * @return the svg scrollpane that is displayed in this internal frame
	 */
	public SVGScrollPane getScrollpane() {
		return scrollpane;
	}
	
	/**
	 * @return the svg canvas that is displayed in this internal frame
	 */
	public SVGCanvas getCanvas(){

		return scrollpane.getSVGCanvas();
	}

	/**
	 * @return the state bar
	 */
	public CanvasStateBar getStateBar() {
		return stateBar;
	}
}
