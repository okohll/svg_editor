/*
 * Created on 26 janv. 2005
 */
package fr.itris.glips.view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * the main class for the view renderer
 * @author ITRIS, Jordi SUC
 */
public class ViewRuntime {

	/**
	 * the svg view
	 */
	private View svgView;
	
    /**
     * the regular frame
     */
    private JFrame regularFrame=new JFrame();
    
    /**
     * whether we are in the debug mode
     */
    private boolean isDebugMode;
    
    /**
     * whether we start in fullscreen mode
     */
    private boolean isFullScreenMode;
    
    /**
     * the constructor of the class
     * @param path the path of the file to open
     * @param isDebugMode whether we are in the debug mode
     * @param isFullScreenMode whether the application should start in full screen mode
     */
    public ViewRuntime(String path, boolean isDebugMode, boolean isFullScreenMode){

    	this.isDebugMode=isDebugMode;
    	this.isFullScreenMode=isFullScreenMode;
		buildMainFrame();

		//setting the path of the file to be displayed
		URI uri=null;
		
        try {
        	uri=new URI(path);
        	File file=new File(uri);
        	path=file.toURI().toASCIIString();
        }catch (Exception ex){ex.printStackTrace();}

        if(path.startsWith("/")){
        	
        	path="file://"+uri;
        }
        
        if(! path.equals("")) {

            svgView.getMainDisplay().getMainViewBrowser().getPictureLoader().
            	setCurrentPicture(path, true);
        }
        
		regularFrame.pack();
    }

    /**
     * builds the main frame
     */
    protected void buildMainFrame(){
        
    	svgView=new View(regularFrame, 
    			regularFrame, true, true, isDebugMode);

        regularFrame.getContentPane().setLayout(new BoxLayout(
        		regularFrame.getContentPane(), BoxLayout.Y_AXIS));
        
        regularFrame.getContentPane().add(svgView.getCanvasContainer());
        regularFrame.getContentPane().doLayout();

		//handles the behavior of the close button of the main frame
        regularFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		//sets a window listener to the main frame
        regularFrame.addWindowListener(new WindowAdapter(){
			
        	@Override
			public void windowClosing(WindowEvent e) {
				
			    svgView.getMainDisplay().getAnimationsHandler().dispose();
				System.exit(0);
			}
		});
		
		//getting the title
		String mainFrameTitle="";
		
        if(svgView.getBundle()!=null){
            
            try{
                mainFrameTitle=svgView.getBundle().getString("mainFrameTitle");
            }catch (Exception ex){}
        }
		
        regularFrame.setTitle(mainFrameTitle);
		regularFrame.setJMenuBar(svgView.buildMenubar());
        
        if(isFullScreenMode){
        	
        	regularFrame.setVisible(false);
        	regularFrame.getJMenuBar().setVisible(false);
        	svgView.setInFullScreenState(true);
        	regularFrame.dispose();
        	regularFrame.setUndecorated(true);
        	regularFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        	
        }else{
        	
        	regularFrame.setVisible(false);
    		regularFrame.pack();
    		
    		//computing the bounds of the frame
    		Dimension preferredDimension=new Dimension(300, 
    				regularFrame.getHeight()-regularFrame.getContentPane().getHeight());
    		
    		//computing the location
    		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    		Point location=new Point((screenSize.width-preferredDimension.width)/2, 
    				(screenSize.height-preferredDimension.height)/4);
    		
    		regularFrame.setLocation(location);
    		regularFrame.setPreferredSize(preferredDimension);
        }

		regularFrame.pack();
		
		//displays the main frame
		regularFrame.setVisible(true);
    }

	/**
	 * the main method
	 * @param args the array of arguments
	 */
	public static void main(String[] args) {
		
		boolean isDebugMode=false;
		boolean isFullScreenMode=false;
		String filePath="";
		
		//creating the set of the arguments
		Set<String> argsSet=new HashSet<String>();
		
		for(int i=0; i<args.length; i++){
			
			if(args[i]!=null && ! args[i].equals("")){
				
				argsSet.add(args[i]);
				
				if(! args[i].equals("-v")){
					
					filePath=args[i];
				}
			}
		}
		
		if(argsSet.contains("-v")) {
			
			isDebugMode=true;
		}
		
		if(argsSet.contains("-f")) {
			
			isFullScreenMode=true;
		}

	    new ViewRuntime(filePath, isDebugMode, isFullScreenMode);
	}
}
