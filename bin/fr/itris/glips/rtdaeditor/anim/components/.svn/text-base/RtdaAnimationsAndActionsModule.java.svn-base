package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtdaeditor.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.disposer.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.test.*;
import fr.itris.glips.rtdaeditor.test.display.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.toolbar.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.resources.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.rtda.*;

/**
 * the class used to create the rtda animations
 * @author ITRIS, Jordi SUC
 */
public class RtdaAnimationsAndActionsModule extends ModuleAdapter{

	/**
	 * setting whether the rtda animations are handled or not
	 */
	static {
		
		Editor.isRtdaAnimationsVersion=true;
		HandlesManager.handleDisposer=new RtdaFrameDisposer();
	}
	
	/**
	 * the ids of the module
	 */
	private final String idRtdaAnimationsAndActions="RtdaAnimationsAndActions", 
									idrtdaTestLauncher="RtdaAnimationsAndActionsTestLauncher";
	
	/**
	 * the labels
	 */
	private String saveDialogTitle="", saveDialogMessage="";
	
	/**
	 * the menu item allowing to switch to the test mode
	 */
	private JMenuItem launchTestMenuItem=new JMenuItem();
	
	/**
	 * the button
	 */
	private JButton launchTestButton=new JButton();
	
	/**
	 * the nodes that are currently selected
	 */
	private LinkedList<Element> selectedNodes=new LinkedList<Element>();
	
	/**
	 * the header panel
	 */
	private HeaderPanel headerPanel;
	
	/**
	 * the panel in which the widget panel will be inserted
	 */
	private JPanel rtdaAnimationsAndActionsPanel=new JPanel();
	
	/**
	 * the tabbed pane
	 */
	private JTabbedPane tabbedPane=new JTabbedPane();
	
	/**
	 * the animation panel and the action panel
	 */
	private AnimationPanel animationsPanel, actionsPanel;
	
	/**
	 * the jwidgets panel
	 */
	private JWidgetPropertiesPanel jwidgetsPanel;
	
	/**
	 * the animations and actions main display toolkit test
	 */
	private MainDisplayToolkitTest mainDisplayToolkitTest;
	
	/**
	 * the test dialog
	 */
	private DialogTest dialogTest;
	
	/**
	 * the frame into which the rtda animations and actions panel will be inserted
	 */
	private ToolsFrame rtdaAnimationsAndActionsFrame;
	
	/**
	 * the current dom listener
	 */
	private SVGDOMListener domListener;
	
	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwidgetManager=null;
	
	/**
	 * whether the graphic components of the dialog have already been reinitialized
	 */
	private boolean hasBeenReinitialized=false;
	
	/**
	 * the object used to record the state of this frame
	 */
	private static StateRecord stateRecord=new StateRecord();
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RtdaAnimationsAndActionsModule(final Editor editor){
		
		//getting the jwidget manager module
		AbstractShape shape=Editor.getEditor().getShapeModule("JWidgetManager");
		
		if(shape!=null) {
			
			jwidgetManager=(JWidgetManager)shape;
		}
		
		jwidgetsPanel=new JWidgetPropertiesPanel(jwidgetManager);
		
		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandleListener=new HandlesListener(){
			
			/**
			 * a listener on the selection changes
			 */
			private SelectionChangedListener selectionListener;
			
			/**
			 * the last handle
			 */
			private SVGHandle lastHandle;
			
			@Override
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
				
				if(lastHandle!=null && selectionListener!=null && lastHandle.getSelection()!=null){
					
					//if a selection listener is already registered on a selection module, it is removed	
					lastHandle.getSelection().removeSelectionChangedListener(selectionListener);
				}
				
				//removing the dom listener
				if(domListener!=null) {
					
					domListener.removeListener();
					domListener=null;
				}
				
				//clearing the state record element
				stateRecord.setSelectedElement(null);

				//gets the current selection module	
				if(currentHandle!=null){

					launchTestMenuItem.setEnabled(true);
					launchTestButton.setEnabled(true);
					manageSelection(new LinkedList<Element>(
							currentHandle.getSelection().getSelectedElements()), true);
					
				}else{
					
					launchTestMenuItem.setEnabled(false);
					launchTestButton.setEnabled(false);
					manageSelection(new LinkedList<Element>(), true);
				}

				if(currentHandle!=null){
					
					//the listener of the selection changes
					selectionListener=new SelectionChangedListener(){
						
						@Override
						public void selectionChanged(Set<Element> selectedElements) {
							
							manageSelection(new LinkedList<Element>(selectedElements), false);
						}
					};
					
					//adds the selection listener
					if(selectionListener!=null){
						
						currentHandle.getSelection().addSelectionChangedListener(selectionListener);
					}
					
				}else{

					//clears the list of the selected items
					selectedNodes.clear();
					refreshAnimationsAndActions(null, null);
					selectionListener=null;
				}
				
				lastHandle=currentHandle;
			}	
			
			/**
			 * updates the selected items and the state of the menu items
			 * @param selectedElements the list of the selected elements
			 * @param frameChanged whether the frame has changed
			 */
			protected void manageSelection(LinkedList<Element> selectedElements, boolean frameChanged){
				
				boolean selectionNotChanged=false;
				
				if(! frameChanged) {
					
					try {
						//comparing the old and new selected nodes to check if the selection has changed,
						//in order to know whether the frame should be refreshed or not
						HashSet<Element> oldSel=new HashSet<Element>(selectedNodes);
						HashSet<Element> newSel=new HashSet<Element>(selectedElements);
						
						if(oldSel.equals(newSel) && newSel.equals(oldSel)) {
							
							selectionNotChanged=true;
						}
					}catch (Exception ex) {}
				}

				if(frameChanged || ! selectionNotChanged) {
					
					selectedNodes.clear();
					
					//refresh the selected nodes list
					if(selectedElements!=null){
						
						selectedNodes.addAll(selectedElements);
					}
					
					if(selectedNodes.size()>=1 && rtdaAnimationsAndActionsFrame.isVisible()){

						refreshAnimationsAndActions(selectedNodes, null);
						
					}else if(rtdaAnimationsAndActionsFrame.isVisible()){
						
						refreshAnimationsAndActions(null, null);		
					}
				}
			}
		};
		
		//adds the svg handle change listener
		editor.getHandlesManager().addHandlesListener(svgHandleListener);

		//creating the main display toolkit
		dialogTest=new DialogTest();
		mainDisplayToolkitTest=new MainDisplayToolkitTest(dialogTest);
		
		//creating the main display
		MainDisplay mainDisplay=new MainDisplay(mainDisplayToolkitTest, true, false);
		dialogTest.initialize(mainDisplay);
		
		//creating the header panel
		headerPanel=new HeaderPanel(this, launchTestButton);
		
		//creating the animations and actions panel
		animationsPanel=new AnimationPanel(jwidgetManager, ItemObject.ANIMATION);
		actionsPanel=new AnimationPanel(jwidgetManager, ItemObject.ACTION);
		
		//handling the tabbed pane
		tabbedPane=new JTabbedPane();

		//filling the tabbed pane
		String frameLabel="", animationsPanelLabel="", actionsPanelLabel="", jwidgetPanelLabel="", testLabel="";
		
		try{
			saveDialogTitle=ResourcesManager.bundle.getString("label_rtdaanimationsandactionsSaveDialogTitle");
			saveDialogMessage=ResourcesManager.bundle.getString("label_rtdaanimationsandactionsSaveDialogMessage");
			frameLabel=ResourcesManager.bundle.getString("label_rtdaanimationsandactions");
			animationsPanelLabel=ResourcesManager.bundle.getString("label_rtdaanimations");
			actionsPanelLabel=ResourcesManager.bundle.getString("label_rtdaactions");
			jwidgetPanelLabel=ResourcesManager.bundle.getString("label_jwidgets");
			testLabel=ResourcesManager.bundle.getString("label_rtdaAnimationsAndActionTest");
		}catch (Exception ex){}
		
		tabbedPane.addTab(animationsPanelLabel, animationsPanel);
		tabbedPane.addTab(actionsPanelLabel, actionsPanel);
		tabbedPane.addTab(jwidgetPanelLabel, jwidgetsPanel);

		//creating the internal frame containing the animations panel
		rtdaAnimationsAndActionsFrame=new ToolsFrame(
			editor, idRtdaAnimationsAndActions, frameLabel, rtdaAnimationsAndActionsPanel);
		
		//setting the visibility changes handler
		Runnable visibilityRunnable=new Runnable(){
			
			public void run() {
				
				if(! rtdaAnimationsAndActionsFrame.isVisible() || selectedNodes.size()==0) {

					refreshAnimationsAndActions(null, null);
					
				}else {
					
					refreshAnimationsAndActions(selectedNodes, null);
				}
			}
		};
		
		rtdaAnimationsAndActionsFrame.setVisibilityChangedRunnable(visibilityRunnable);
		
		//adding the listener to the tabbed pane
		tabbedPane.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent evt) {

				stateRecord.setSourceName("");
				
				//getting the selected tab
				if(animationsPanel!=null && animationsPanel.getPointsPainter()!=null) {
					
					animationsPanel.getPointsPainter().setEnabled(tabbedPane.getSelectedIndex()==0);
				}
			}
		});

		//handling the test menu item
		ImageIcon launchTestIcon=ResourcesManager.getIcon("TestLauncher", false);
		ImageIcon launchTestDisabledIcon=ResourcesManager.getIcon("TestLauncher", true);
		launchTestMenuItem.setText(testLabel);
		launchTestMenuItem.setIcon(launchTestIcon);
		launchTestMenuItem.setDisabledIcon(launchTestDisabledIcon);
		launchTestMenuItem.setEnabled(false);
		
		//handling the button used to launch the test dialog
		launchTestButton.setToolTipText(testLabel);
		launchTestButton.setEnabled(false);
		
		launchTestButton.setMargin(new Insets(1, 1, 1, 1));
		launchTestButton.setIcon(launchTestIcon);
		launchTestButton.setDisabledIcon(launchTestDisabledIcon);

		//the listener
		ActionListener testLauncherActionListener=new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				launchTestDialog((JComponent)evt.getSource());
			}
		};
		
		launchTestMenuItem.addActionListener(testLauncherActionListener);
		launchTestButton.addActionListener(testLauncherActionListener);
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

        HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
        map.put("ToolFrame_"+this.idRtdaAnimationsAndActions, 
        		rtdaAnimationsAndActionsFrame.getMenuItem());
		map.put(idrtdaTestLauncher, launchTestMenuItem);
		map.putAll(jwidgetManager.getMenuItems());
        return map;
	}
	
	@Override
	public HashMap<String, AbstractButton> getToolItems() {

        HashMap<String, AbstractButton> map=
        	new HashMap<String, AbstractButton>();
        map.put(idRtdaAnimationsAndActions, 
        		rtdaAnimationsAndActionsFrame.getToolBarButton());
        map.putAll(jwidgetManager.getToolItems());
        return map;
	}
	
	/**
	 * refreshes the animations and actions widgets
	 * @param nodes the list of nodes
	 * @param animationOrActionToSelect the animation or action 
	 * element that should be selected in the animations or actions chooser
	 */
	protected void refreshAnimationsAndActions(
			LinkedList<Element> nodes, Element animationOrActionToSelect){

		//getting the current svg handle
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();

		//if the frame has already been reinitialized and is not visible, nothing is done
		if(hasBeenReinitialized && ((handle==null && nodes==null) || 
				! rtdaAnimationsAndActionsFrame.isVisible())) {

			return;
		}
		
		//if the frame is not visible and has not been reinitialized, the reinitalization is processed
		if(! rtdaAnimationsAndActionsFrame.isVisible()) {
			
			nodes=null;
		}
		
		//if the list of nodes is null, the frame is reinitialized
		hasBeenReinitialized=(handle==null && nodes==null);
		
		//removing the previous dom listener
		if(domListener!=null && handle!=null) {
			
			handle.getSvgDOMListenerManager().
				removeDOMListener(domListener);
		}

		if(handle!=null && (nodes==null || nodes.size()==0)){

			//adding the root element to the list of the nodes that should be animated
			nodes=new LinkedList<Element>();
			nodes.add(handle.getCanvas().getDocument().getDocumentElement());
		}
		
		//the list of the animations and actions objects
		LinkedList<AnimationObject> objects=null;
		
		//creating the list of the animations and the list of the actions
		LinkedList<AnimationObject> animationObjects=null, actionObjects=null;
		
		if(nodes!=null && nodes.size()==1){
			
			objects=new LinkedList<AnimationObject>();
			Element selectedNode=nodes.getFirst();
			Element lastElement=stateRecord.getSelectedElement();
			
			if(lastElement!=null && ! lastElement.equals(selectedNode)) {
				
				stateRecord.reinitialize();
			}

			stateRecord.setSelectedElement(selectedNode);
			
			//getting the animation elements that can be found under the selected node
			//and creating the associated animation objects
			for(Node cur=selectedNode.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
				
				if(cur instanceof Element && 
						cur.getNodeName().startsWith(fr.itris.glips.library.Toolkit.rtdaPrefix) &&
						! cur.getNodeName().equals(fr.itris.glips.library.Toolkit.rtdaPrefix+
								EditorAnimationsToolkit.widgetDatabaseRootTagName) &&
						! Toolkit.isJWidgetElement((Element)cur)){
					
					objects.add(new AnimationObject(jwidgetManager, (Element)cur));
				}
			}
			
			//creating the new dom listener
			domListener=new SVGDOMListener(selectedNode) {

				@Override
				public void nodeChanged() {}

				@Override
				public void nodeInserted(Node insertedNode) {

					refreshAnimationsAndActions(selectedNodes, (Element)insertedNode);
				}

				@Override
				public void nodeRemoved(Node removedNode) {

					refreshAnimationsAndActions(selectedNodes, (Element)removedNode);
				}

				@Override
				public void structureChanged(Node lastModifiedNode) {}
			};
			
			//adding the dom listener
			Editor.getEditor().getHandlesManager().getCurrentHandle().
				getSvgDOMListenerManager().addDOMListener(domListener);
			
			//creating the list of the animations and the list of the actions
			animationObjects=new LinkedList<AnimationObject>();
			actionObjects=new LinkedList<AnimationObject>();
			
		}else {
			
			stateRecord.reinitialize();
		}

		if(objects!=null && animationObjects!=null && actionObjects!=null) {
			
			//sorting the animation and action objects
			for(AnimationObject object : objects){
				
				if(object.getObjectType()==ItemObject.ANIMATION){
					
					animationObjects.add(object);
					
				}else{
					
					actionObjects.add(object);
				}
			}
		
			//computing the index of the animation and action to select in the animation chooser
			int animationIndex=0, actionIndex=0;
			
			if(animationOrActionToSelect!=null) {
				
				stateRecord.setAnimationIndex(0);
				
				for(AnimationObject animObj : animationObjects) {
					
					if(animObj.getAnimationElement().equals(animationOrActionToSelect)) {
						
						stateRecord.setAnimationIndex(animationIndex);
						break;
					}
					
					animationIndex++;
				}
				
				stateRecord.setActionIndex(0);
				
				for(AnimationObject actionObj : actionObjects) {
					
					if(actionObj.getAnimationElement().equals(animationOrActionToSelect)) {
						
						stateRecord.setActionIndex(actionIndex);
						break;
					}
					
					actionIndex++;
				}
			}
		}
		
		//cleaning the jwidget edition objects
		for(JWidgetEdition jwidgetEdition : jwidgetManager.getJWidgets()) {
			
			if(jwidgetEdition!=null) {
				
				jwidgetEdition.clean();
			}
		}
		
		//whether the tabs should be enabled
		boolean enableAnimations=false, enableActions=false, enablesJWidgetsPanel=false;
		
		//getting the selected element
		Element element=null;
		
		if(nodes!=null && nodes.size()==1){

			element=nodes.getFirst();
		}

		//setting the element for the header panel
		headerPanel.setCurrentElement(element);
		
		//removing the tabbed pane
		rtdaAnimationsAndActionsPanel.removeAll();

		if(element!=null) {
			
			//handling the jwidgets panel
			jwidgetsPanel.setElement(element);
			JWidgetEdition jwidgetEdition=null;
			
			//checks if the animations, action and jwidget tabs should be enabled or not
			if(Toolkit.hasJWidgetChildElement(element)) {
				
				Element jwidgetElement=JWidgetManager.getJWidgetElement(element);
				jwidgetEdition=jwidgetManager.getJWidgetEdition(
					jwidgetElement.getAttribute(fr.itris.glips.library.Toolkit.nameAttribute));
				
				if(jwidgetEdition!=null) {
					
					enableAnimations=jwidgetEdition.defineAnimations();
					enableActions=jwidgetEdition.defineActions();
					enablesJWidgetsPanel=true;
					
				}else {
					
					enableAnimations=true;
					enableActions=true;
				}
				
			}else if(! Toolkit.hasJWidgetChildElement(element)) {
				
				if(element.getOwnerDocument().getDocumentElement().equals(element)){
					
					enableAnimations=false;
					enableActions=true;
					
				}else{
					
					enableAnimations=true;
					enableActions=true;
				}
			}

			//setting the new state of the tabs
			tabbedPane.setEnabledAt(0, enableAnimations);
			tabbedPane.setEnabledAt(1, enableActions);
			tabbedPane.setEnabledAt(2, enablesJWidgetsPanel);
			
			//getting the selected tab
			int selectedTab=tabbedPane.getSelectedIndex();
			LinkedList<Integer> enabledTabIndex=new LinkedList<Integer>();
			
			if(enableAnimations) {
				
				enabledTabIndex.add(0);
			}
			
			if(enableActions) {
				
				enabledTabIndex.add(1);
			}
			
			if(enablesJWidgetsPanel) {
				
				enabledTabIndex.add(2);
			}
			
			//getting the tab index that should be selected and selects the corresponding tab
			if(enabledTabIndex.size()>0 && ! enabledTabIndex.contains(selectedTab)) {
				
				tabbedPane.setSelectedIndex(enabledTabIndex.getFirst());
				stateRecord.reinitialize();
			}

			//adding the components in the main panel
			rtdaAnimationsAndActionsPanel.setLayout(new BorderLayout());
			rtdaAnimationsAndActionsPanel.add(headerPanel, BorderLayout.NORTH);
			rtdaAnimationsAndActionsPanel.add(tabbedPane, BorderLayout.CENTER);

			//displaying the widgets associated with the animations and actions
			animationsPanel.setAnimations(enableAnimations?animationObjects:null);
			actionsPanel.setAnimations(enableActions?actionObjects:null);
			
			rtdaAnimationsAndActionsFrame.revalidate();
			
		}else {

			//adding the header panel in the main panel
			rtdaAnimationsAndActionsPanel.setLayout(
					new BoxLayout(rtdaAnimationsAndActionsPanel, BoxLayout.X_AXIS));
			rtdaAnimationsAndActionsPanel.add(headerPanel);
			animationsPanel.setAnimations(null);
			actionsPanel.setAnimations(null);
			jwidgetsPanel.setElement(null);
			stateRecord.reinitialize();
			rtdaAnimationsAndActionsFrame.revalidate();
		}
	}
	
	/**
	 * @return the jwidget manager
	 */
	public JWidgetManager getJwidgetManager() {
		return jwidgetManager;
	}

	/**
	 * launches the test dialog
	 * @param relativeComponent the component relatively 
	 * to which the dialog should be displayed
	 */
	protected void launchTestDialog(JComponent relativeComponent) {
		
		boolean showTestDialog=false;
		
		//getting the current handle
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		if(handle!=null && handle.isModified()) {
			
			//showing the dialog 
			int result=JOptionPane.showConfirmDialog(
					Editor.getParent(), saveDialogMessage, saveDialogTitle, 
							JOptionPane.YES_NO_CANCEL_OPTION);
			
			switch (result) {
			
				case JOptionPane.YES_OPTION :
					
					//saving the file
					Editor.getEditor().getIOManager().getFileSaveManager().
						saveHandleDocument(handle, false, relativeComponent);
					showTestDialog=true;
					break;
					
				case JOptionPane.NO_OPTION :
					
					showTestDialog=true;
					break;
			}
			
		}else if(handle!=null && ! handle.isModified()) {
			
			showTestDialog=true;
		}

		if(showTestDialog) {

			Runnable runnable=new Runnable() {
				
				public void run() {
				
					SwingUtilities.invokeLater(new Runnable() {

						public void run() {

							//showing the test dialog
							mainDisplayToolkitTest.getDialogTest().refreshDialogState(true);
						}
					});
				}
			};
			
			Editor.getEditor().getIOManager().requestExecution(runnable);
		}
	}

	/**
	 * @return the stateRecord
	 */
	public static StateRecord getStateRecord() {
		return stateRecord;
	}

}
