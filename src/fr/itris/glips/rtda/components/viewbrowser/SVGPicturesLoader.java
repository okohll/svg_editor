package fr.itris.glips.rtda.components.viewbrowser;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.util.*;
import org.w3c.dom.*;
import org.w3c.dom.svg.*;
import fr.itris.glips.compiler.rtdb.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.rtda.widget.*;

/**
 * the class used to load pictures 
 * @author Jordi SUC
 */
public class SVGPicturesLoader {

	/**
	 * the view browser
	 */
	private ViewBrowser viewBrowser;
	
    /**
     * the history of the displayed pictures
     */
    protected java.util.List<String> history=new CopyOnWriteArrayList<String>();
    
    /**
     * the current history position
     */
    protected int currentHistoryPosition=-1;
	
	/**
	 * the constructor of the class
	 * @param viewBrowser the picture loader linked to the view browser 
	 */
	public SVGPicturesLoader(ViewBrowser viewBrowser) {

		this.viewBrowser=viewBrowser;
	}
	
	/**
	 * clears the picture loader
	 */
	public void clear(){
		
		history.clear();
	}
	
	/**
     * sets the new picture to be displayed given the uri of a canvas
     * @param uri the uri of a canvas
     * @param disposeAllPreviousPictures whether the 
     * previously loaded pictures should be disposed or not
     */
    public void setCurrentPicture(
    	String uri, boolean disposeAllPreviousPictures){

        disposeUnusedPictures();
    	
        if(disposeAllPreviousPictures){
            
        	viewBrowser.disposeAll();
        }

        if(uri!=null && ! uri.equals("")){
            
            //getting the picture object corresponding to this uri
            SVGPicture picture=viewBrowser.mainDisplay.getPictureManager().getPicture(uri);

            if(picture==null){
            	
            	//creating the document for the picture
        		SVGDocument doc=null;
        		
        		try {
        			SAXSVGDocumentFactory factory=new SAXSVGDocumentFactory("");
        			factory.setValidating(false);
        			doc=factory.createSVGDocument(uri);
        		} catch (Exception ex) {ex.printStackTrace();}
            	
                //creating a new picture
               if(doc!=null){
            	   
            	   setCurrentPictureSimple(doc, uri);
               }
            
               return; 
               
            }else if(! picture.equals(viewBrowser.canvasContainer.getCurrentPicture()) && 
            		! picture.isDisplayed() && picture.getAnimActionsHandler().isEntitled()){
            	
                addInHistory(picture);
                viewBrowser.canvasContainer.setPicture(picture);
                String projectName="";
                
                try{
                	projectName=picture.getCanvas().getProjectName();
                }catch(Exception ex){ex.printStackTrace();}

                viewBrowser.mainDisplay.getViewBrowsersManager().fireViewLoaded(
                		projectName, viewBrowser.viewBrowserId, uri);
            }
        }
        
        disposeUnusedPictures();
    }
    
    /**
     * disposes all the unused pictures
     */
    protected void disposeUnusedPictures(){
    	
    	//getting the list of the pictures
    	Set<SVGPicture> allPictures=
    		viewBrowser.mainDisplay.getPictureManager().getPictures();
    	
    	for(SVGPicture picture : allPictures){
    		
    		if(picture!=null && ! picture.isDisplayed()){
    			
    			viewBrowser.mainDisplay.getPictureManager().
    				removePicture(picture);
    			picture.dispose();
    		}
    	}
    }
    
    /**
     * sets the new picture to be displayed given the uri of a canvas
     * @param doc the document of the canvas
     * @param uri the uri of a canvas
     * @param disposeAllPreviousPictures whether the previously 
     * loaded pictures should be disposed or not
     */
    public void setCurrentPicture(final SVGDocument doc, 
    		String uri, boolean disposeAllPreviousPictures){
    	
        if(disposeAllPreviousPictures){
        	
        	viewBrowser.disposeAll();
        }
        
        if(doc!=null && uri!=null){
        	
        	if(! fr.itris.glips.library.Toolkit.isDocumentAView(doc)) {
        		
                final String furi=uri;
                
                //executing the action in another thread
                Thread thread=new Thread() {
                	
                	@Override
                	public void run() {

                		 //cloning the document
                        final SVGDocument svgDoc=(SVGDocument)doc.cloneNode(true);
 
                    	//replacing the jwidget images by a rectangle
                        NodeList childNodes=null;
                    	NodeList jwidgetElements=svgDoc.getElementsByTagName("rtda:jwidget");
                    	Element imageNode=null, jwidgetElement=null;

                    	//creating the list of the image nodes
                    	LinkedList<Element> imagesList=new LinkedList<Element>();
                    	
                    	for(int i=0; i<jwidgetElements.getLength(); i++) {
                    		
                    		jwidgetElement=(Element)jwidgetElements.item(i);
                    		imageNode=(Element)jwidgetElement.getParentNode();
                    		
                    		if(imageNode.getNodeName().equals("image")) {
                    		
                    			imagesList.add(imageNode);
                    		}
                    	}
                    	
                    	//replacing each image node by a rect node
                    	Element rectNode=null, parentNode=null;
                    	LinkedList<Node> childNodesList=null;
                    	
                    	for(Element imageElement : imagesList) {

                			parentNode=(Element)imageElement.getParentNode();
                			
                			//getting all the children of the image node
                			childNodes=imageElement.getChildNodes();
                			
                			//creating the set of the child nodes of the image element
                			childNodesList=new LinkedList<Node>();
                			
                			for(int j=0; j<childNodes.getLength(); j++) {
                				
                				childNodesList.add(childNodes.item(j));
                			}
                			
                			//creating a new rect node
                			rectNode=svgDoc.createElementNS(svgDoc.getDocumentElement().getNamespaceURI(), "rect");
                			rectNode.setAttributeNS(null, "x", imageElement.getAttribute("x"));
                			rectNode.setAttributeNS(null, "y", imageElement.getAttribute("y"));
                			rectNode.setAttributeNS(null, "width", imageElement.getAttribute("width"));
                			rectNode.setAttributeNS(null, "height", imageElement.getAttribute("height"));
                			rectNode.setAttributeNS(null, "transform", imageElement.getAttribute("transform"));
                			rectNode.setAttributeNS(null, "style", "fill:#ffffff;stroke:none;opacity:0;");
                			
                			//adding the children of the image node to the rect node
                			for(Node child : childNodesList) {
                				
                				if(child!=null && child instanceof Element) {
                					
                					imageElement.removeChild(child);
                					rectNode.appendChild(child);
                				}
                			}
                			
                			//adding all the image child nodes to the rect nodes
                			parentNode.insertBefore(rectNode, imageElement);
                			parentNode.removeChild(imageElement);
                    	}
                        
                        //normalizing each node in the document
                        Node node=null;
                        
                        for(fr.itris.glips.rtda.NodeIterator it=
                        	new fr.itris.glips.rtda.NodeIterator(svgDoc.getDocumentElement()); 
                        		it.hasNext();) {
                        	
                        	node=it.next();
                        	
                        	if(node!=null && node instanceof Element) {
                        		
                        		fr.itris.glips.library.Toolkit.transformFromStyleToAttribute((Element)node);
                        	}
                        }

                        //sets the new picture
						SwingUtilities.invokeLater(new Runnable() {
							
							public void run() {

								//showing the new picture
								setCurrentPictureSimple(svgDoc,  furi);
							}
						});
                	}
                };
                
                thread.start();
        		
        	}else if(viewBrowser.mainDisplay.isTestVersion()){
        		
               	final String furi=uri;
            	
             	//getting the label of the task
            	String taskLabel="";
            	
            	try {
            		taskLabel=ViewBrowser.bundle.getString("compilingLabel");
            	}catch (Exception ex) {}
            	
            	//creating the progress bar that will be used to give information on the process to the user
            	ProgressBarDialog pbd=null;
            	Container container=viewBrowser.canvasContainer.getTopLevelAncestor();

            	if(container!=null){
            		
                	if(container instanceof Frame){
                		
                		pbd=new ProgressBarDialog((Frame)container, taskLabel);
                		
                	}else if(container instanceof JApplet){
                    		
                    	pbd=new ProgressBarDialog(viewBrowser.mainDisplay.getTopLevelFrame(), taskLabel);
                		
                	}else{
                		
                		pbd=new ProgressBarDialog((JDialog)container, taskLabel);
                	}
                	
                	final ProgressBarDialog progressBarDialog=pbd;

    				//showing the progress bar
                	progressBarDialog.setVisible(true);
                	
                    //executing the action in an other thread
                    Thread thread=new Thread() {
                    	
                    	@Override
                    	public void run() {

                    		 //getting the canvas file
                            File canvasFile=null;
                            
                            try{
                            	canvasFile=new File(new URI(furi));
                            }catch (Exception ex){}

                            if(canvasFile!=null){
                            	
                                //getting the needed piece of information for compiling the application
                                String xmlFilePath=doc.getDocumentElement().getAttribute("referenceFile");
                                String viewPath=doc.getDocumentElement().getAttribute("viewPath");
                                String projectPath=DataBaseToolkit.getProjectPath(furi, xmlFilePath, true);
                                String startViewPath=doc.getDocumentElement().getAttribute("referenceNode");
                                
                                if(startViewPath.endsWith("/")){
                                	
                                	startViewPath=startViewPath.substring(0, startViewPath.length()-1);
                                }
                                
                                if(startViewPath.startsWith("/")){
                                	
                                	startViewPath=startViewPath.substring(1, startViewPath.length());
                                }
                                
                                viewPath="/"+startViewPath+"/"+viewPath;
                                
                                //computing the workspace path and the project name
                                String workspacePath="", projectName="";
                                int pos=projectPath.lastIndexOf("/");
                                
                                if(pos!=-1){
                                	
                                	workspacePath=projectPath.substring(0, pos);
                                	projectName=projectPath.substring(pos+1, projectPath.length());
                                	
                                }else{
                                	
                                	projectName=projectPath;
                                }

                                //creating the temporary folder that will contain the compiled files
                                File outputFolder=null;
                                
                                try{
                                	//creating a temporary file and getting the folder of the temporary files
                                	File tempFile=File.createTempFile("test", null);
                                	tempFile.deleteOnExit();
                                	
                                	File tempFolder=tempFile.getParentFile();
                                	
                                	//creating a temporary folder
                                	outputFolder=new File(tempFolder, "GGTemp"+Math.random());
                                	outputFolder.deleteOnExit();
                                }catch (Exception ex){ex.printStackTrace();}

                                if(outputFolder!=null){
                                	
                                	//creating the compiler
                                	RTDBCompiler compiler=new RTDBCompiler(
                                		workspacePath, projectName, xmlFilePath, 
                                			outputFolder.toURI().toASCIIString(), viewPath, true);
                                	compiler.compile();
                                	
                                	//getting the svg file corresponding to the view
                                	File compiledSVGFile=null;
                                	
                                	try {
                                		File parentOutputFolder=new File(new URI(outputFolder.toURI().toASCIIString()));
                                		
                                		//getting the output project file
                                		File outputProject=null;
                                		File[] children=parentOutputFolder.listFiles();
                                		String theUri="", name="";
                                		
                                		for(int i=0; i<children.length; i++) {
                                			
                                			if(children[i]!=null && children[i].isDirectory()){
                                				
                                				theUri=children[i].toURI().toASCIIString();
                                				
                                				if(theUri.endsWith("/")){
                                					
                                					theUri=theUri.substring(0, theUri.length()-1);
                                				}
                                				
                                				pos=theUri.lastIndexOf("/");

                                				if(pos!=-1){
                                					
                                					name=theUri.substring(pos+1, theUri.length());

                                        			if(name.equals(projectName)) {
                                        				
                                        				outputProject=children[i];
                                        				break;
                                        			}
                                				}
                                			}
                                		}

                                		compiledSVGFile=new File(outputProject, canvasFile.getName());
                                	}catch (Exception ex) {ex.printStackTrace();}
                                	
                                	//getting the svg document corresponding to this file
                                	SVGDocument svgDocument=null;

                                	if(compiledSVGFile!=null && compiledSVGFile.exists()){
                                		
                                		//getting the dom implementation
                                		SAXSVGDocumentFactory documentFactory=new SAXSVGDocumentFactory(
                                				XMLResourceDescriptor.getXMLParserClassName(), true);
                                		documentFactory.setValidating(false);
                                		
                                		try{
                                			svgDocument=documentFactory.createSVGDocument(
                                					compiledSVGFile.toURI().toASCIIString());
                                		}catch (Exception ex){ex.printStackTrace();}
                                		
                                		if(svgDocument!=null){

                                			final SVGDocument fsvgDocument=svgDocument;
                                			final String path=compiledSVGFile.toURI().toASCIIString();

                                            //sets the new picture
                                			try {
                                                SwingUtilities.invokeAndWait(new Runnable() {
                                                	
                                                	public void run() {
                                                		
                                            			//setting the document of the current picture
                                            			setCurrentPictureSimple(fsvgDocument,  path);
                                                	}
                                                });
                                			}catch (Exception ex) {ex.printStackTrace();}
                                		}
                                	}
                                }
                            }
                            
                            try {
    							SwingUtilities.invokeAndWait(new Runnable() {
    								
    								public void run() {
    									
    									//hiding the progress bar
    									progressBarDialog.setVisible(false);
    								}
    							});
    						} catch (Exception e) {e.printStackTrace();}
                    	}
                    };
                    
                    thread.start();
            	}

        	}else{
        		
        		setCurrentPictureSimple(doc, uri);
        	}
        }
    }
    
    /**
     * sets the new picture to be displayed given the uri of a canvas
     * @param doc the document of the canvas
     * @param uri the uri of a canvas
     */
    private void setCurrentPictureSimple(SVGDocument doc, String uri){

        //getting the picture object corresponding to this uri
        SVGPicture picture=viewBrowser.mainDisplay.getPictureManager().getPicture(uri);

        if(picture==null){

            //creating a new picture
            picture=new SVGPicture(viewBrowser.mainDisplay, doc, uri);
        }

        if(! picture.isDisplayed() && picture.getAnimActionsHandler().isEntitled()){
        	
            addInHistory(picture);
            viewBrowser.canvasContainer.setPicture(picture);
            String projectName="";
            
            try{
            	projectName=picture.getCanvas().getProjectName();
            }catch (Exception ex){}
            
            viewBrowser.mainDisplay.getViewBrowsersManager().
            	fireViewLoaded(projectName, viewBrowser.viewBrowserId, uri);
        }
        
        disposeUnusedPictures();
    }
    
    /**
     * sets the new picture to be displayed
     * @param picture a picture
     */
    public void setCurrentPicture(SVGPicture picture){

        if(picture!=null){
            
        	//getting the last picture uri that can be found in the history list
        	String lastUri=null;
        	
        	if(history.size()>0){
        		
        		lastUri=history.get(history.size()-1);
        	}
        	
        	if(! picture.isDisplayed() && picture.getAnimActionsHandler().isEntitled()){
        		
                if(! picture.getUri().equals(lastUri)){
                	
                	addInHistory(picture);
                }
            	
                viewBrowser.canvasContainer.setPicture(picture);
                String projectName=picture.getCanvas().getProjectName();
                viewBrowser.mainDisplay.getViewBrowsersManager().fireViewLoaded(
                	projectName, viewBrowser.viewBrowserId, picture.getUri());
        	}
        }
        
        disposeUnusedPictures();
    }
    
    /**
     * adds the given picture in the history
     * @param picture a picture
     */
    protected void addInHistory(SVGPicture picture){
        
        if(picture!=null){
        	
            int maxSize=100;
            
            //removes the first occurence in the history list if its size is superior to maxSize
            if(history.size()>=maxSize){
                
                history.remove(history.get(0));
            }
            
            if(history.size()==0) {
            	
            	currentHistoryPosition=-1;
            	
            }else if(currentHistoryPosition<history.size()-1) {
            	
                //removing all the pictures that lie after the current picture position
            	int historySize=history.size();
            	
            	for(int i=currentHistoryPosition+1;i<historySize; i++) {
            		
            		history.remove(history.size()-1);
            	}
            	
            }else if(currentHistoryPosition>=history.size()){
            	
            	currentHistoryPosition=history.size()-1;
            }

            //adding the given picture
            history.add(picture.getUri());
            currentHistoryPosition++;
            
            //handles the state of the buttons
            viewBrowser.handleButtonsState();
        }
    }
    
    /**
     * displays the previous picture
     */
    public void returnToPreviousPicture() {
    	
    	SVGPicture previousPicture=null;
    	
        if(history.size()>1){
            
        	currentHistoryPosition--;

        	if(currentHistoryPosition>=history.size()-1){
        		
        		currentHistoryPosition=history.size()-2;
        	}
        	
        	if(currentHistoryPosition<0){
        		
        		currentHistoryPosition=0;
        	}

        	//getting the previous uri
        	String previousUri="";
        	
        	try{
        		previousUri=history.get(currentHistoryPosition);
        	}catch (Exception ex){}
        	
        	//getting the previous picture
        	previousPicture=viewBrowser.mainDisplay.
        		getPictureManager().getPicture(previousUri);
        	
        	if(previousPicture==null){
        		
        		setCurrentPicture(previousUri, false);
        	}
        }
    	
    	if(previousPicture!=null && ! previousPicture.isDisplayed()) {
        	
            viewBrowser.canvasContainer.setPicture(previousPicture);
            String projectName=previousPicture.getCanvas().getProjectName();
            viewBrowser.mainDisplay.getViewBrowsersManager().fireViewLoaded(
            	projectName, viewBrowser.viewBrowserId, previousPicture.getUri());
    	}
    	
    	viewBrowser.handleButtonsState();
    }
    
    /**
     * displays the next picture
     */
    public void goToNextPicture() {
    	
    	SVGPicture nextPicture=null;
    	
        if(history.size()>0){

        	currentHistoryPosition++;
        	
        	if(currentHistoryPosition>=history.size()){
        		
        		currentHistoryPosition=history.size()-1;
        	}
        	
        	if(currentHistoryPosition<0){
        		
        		currentHistoryPosition=0;
        	}
        	
        	//getting the next uri
        	String nextUri=history.get(currentHistoryPosition);
        	
        	//getting the previous picture
        	nextPicture=viewBrowser.mainDisplay.
        		getPictureManager().getPicture(nextUri);
        	
        	if(nextPicture==null){
        		
        		setCurrentPicture(nextUri, false);
        	}
        }
    	
    	if(nextPicture!=null && ! nextPicture.isDisplayed()) {
        	
    		viewBrowser.canvasContainer.setPicture(nextPicture);
            String projectName=nextPicture.getCanvas().getProjectName();
            viewBrowser.mainDisplay.getViewBrowsersManager().fireViewLoaded(
            		projectName, viewBrowser.viewBrowserId, nextPicture.getUri());
    	}
    	
    	viewBrowser.handleButtonsState();
    }

	/**
	 * @return the current history position
	 */
	public int getCurrentHistoryPosition() {
		return currentHistoryPosition;
	}

	/**
	 * @return the current history list
	 */
	public java.util.List<String> getHistory() {
		
		return history;
	}
}
