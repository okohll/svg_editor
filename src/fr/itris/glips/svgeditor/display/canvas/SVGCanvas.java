/*
 * Created on 23 mars 2004
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 Contact : jordi.suc@itris.fr; philippe.gil@itris.fr
 
 =============================================
 */
package fr.itris.glips.svgeditor.display.canvas;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.*;
import org.w3c.dom.svg.*;
import org.apache.batik.bridge.*;
import org.apache.batik.dom.svg.*;
import org.apache.batik.ext.awt.image.*;
import org.apache.batik.gvt.*;
import org.apache.batik.util.XMLResourceDescriptor;

import fr.itris.glips.library.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.grid.*;
import fr.itris.glips.svgeditor.display.canvas.zoom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.RepaintManager;
import java.awt.geom.*;
import java.awt.image.*;
import libraries.*;

/**
 * @author ITRIS, Jordi SUC
 * the class of the canvas of a SVG file
 */
public class SVGCanvas extends JPanel {
	
	/**
	 * the constant for the grid layer
	 */
	public static final int GRID_LAYER=0;
	
	/**
	 * the constant for the bottom layer
	 */
	public static final int BOTTOM_LAYER=1;
	
	/**
	 * the constant for the selection layer
	 */
	public static final int SELECTION_LAYER=2;
	
	/**
	 * the constant for the draw layer
	 */
	public static final int DRAW_LAYER=3;
	
	/**
	 * the constant for the top layer
	 */
	public static final int TOP_LAYER=4;
	
	/**
	 * the array of the layers
	 */
	protected static int[] layers={GRID_LAYER, BOTTOM_LAYER, 
		SELECTION_LAYER, DRAW_LAYER, TOP_LAYER};
	
	/**
	 * the color used for drawing the area that is outside the current parent bounds
	 */
	protected static final Color parentVeilColor=new Color(255, 255, 255, 150);
	
	/**
	 * the canvas uri
	 */
	private String uri="";
	
	/**
	 * the canvas document
	 */
	private Document document;
	
	/**
	 * the builder
	 */
    private GVTBuilder builder;
    
    /**
     * the bridge context
     */
    private BridgeContext ctx;
    
    /**
     * the root graphics node
     */
    private RootGraphicsNode gvtRoot;
    
	/**
	 * the offscreen image of the canvas
	 */
	private BufferedImage canvasOffscreenImage;
	
	/**
	 * the rendered rectangle
	 */
	private Rectangle renderedRectangle=new Rectangle(0, 0, 1, 1),
									tmpRectangle=new Rectangle(0, 0, 0, 0);
	
	/**
	 * the scrollpane that contains the canvas
	 */
	private SVGScrollPane scrollpane;
	
	/**
	 * the canvas zoom manager
	 */
	private Zoom zoomManager;
	
	/**
	 * the grid manager
	 */
	protected Grid gridManager;
	
	/**
	 * the file of the project this canvas is associated with
	 */
	private File projectFile;
	
	/**
	 * the map associating an id integer to the list of the paint listeners for a layer
	 */
	private Map<Integer, Set<CanvasPainter>> paintListeners=
			new ConcurrentHashMap<Integer, Set<CanvasPainter>>();
	
	/**
	 * the zone that should be repainted with the paint listeners when the rectangle is not null
	 */
	private Rectangle2D repaintZone;
	
	/**
	 * the svg handle this canvas is associated to
	 */
	private SVGHandle svgHandle;
	
	/**
	 * the repaint manager
	 */
	protected RepaintManager repaintManager=
		RepaintManager.currentManager(this);
	
	/**
	 * the constructor of the class
	 * @param scrollpane the scrollpane into which the canvas will be inserted
	 */
	public SVGCanvas(SVGScrollPane scrollpane) {

		this.scrollpane=scrollpane;
		this.svgHandle=scrollpane.getSVGHandle();
		this.zoomManager=new Zoom(this);
		setDoubleBuffered(true);
		setBackground(Color.white);
		
		//creating the paint listeners map structure
		paintListeners.put(GRID_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(BOTTOM_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(SELECTION_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(DRAW_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(TOP_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
	}
	
	/**
	 * creates a new svg document
	 * @param width the width of the new document
	 * @param height the height of the new document
	 */
	public void newDocument(String width, String height){

		DOMImplementation impl=SVGDOMImplementation.getDOMImplementation();
		String svgNS=SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc=(SVGDocument)impl.createDocument(svgNS, "svg", null);
		
		//gets the root element (the svg element)
		Element svgRoot=doc.getDocumentElement();
		
		//set the width and height attribute on the root svg element
		svgRoot.setAttributeNS(null, "width", width);
		svgRoot.setAttributeNS(null, "height", height);
		svgRoot.setAttribute("viewBox","0 0 "+
			EditorToolkit.getPixelledNumber(width)+" "+
				EditorToolkit.getPixelledNumber(height));
		
		//creating a defs element
		Element defsElement=doc.createElementNS(doc.getNamespaceURI(), "defs");
		svgRoot.appendChild(defsElement);
		
		//initializing the canvas
		Toolkit.checkRtdaXmlns(doc);
		this.document=doc;
		initializeCanvas(null);
	}
	
	/**
	 * sets the uri for the canvas (should not be invoked in the AWT thread)
	 * @param uri the uri of the svg file to be loaded
	 * @param monitor the object monitoring the loading of the svg file
	 */
	public void setURI(final String uri, Monitor monitor){
		
		synchronized(this){this.uri=uri;}
		
		if(Editor.isRtdaAnimationsVersion) {
			
			projectFile=Editor.getColorChooser().getProjectFile(uri);
		}

		try{
			if(monitor!=null){
				
				monitor.start();
				monitor.setProgress(0);
			}
			
			//creating the svg document corresponding to this uri
			String parser = XMLResourceDescriptor.getXMLParserClassName();
			System.out.println("Using parser " + parser + " for URI " + uri);
			SAXSVGDocumentFactory factory=new SAXSVGDocumentFactory("org.apache.xerces.parsers.SAXParser"); // Oliver: replace ""
			SVGDocument doc=factory.createSVGDocument(uri);

			if(doc!=null){
				// Debug
				NodeList childNodes = doc.getChildNodes();
				int numNodes = childNodes.getLength();
				for (int i=0; i < numNodes; i++) {
					Node item = childNodes.item(i);
					System.out.println("There are " + item.getChildNodes().getLength() + " child nodes");
				}
				//normalizing the document
				Dimension scaledCanvasSize=
					getScaledCanvasSize(doc.getDocumentElement());
				svgHandle.getSvgDOMNormalizer().normalize(doc, scaledCanvasSize);
				Toolkit.checkRtdaXmlns(doc);
				
				synchronized(this){this.document=doc;}

				if(monitor!=null && monitor.isCancelled()){
					
					monitor.stop();
					getSVGHandle().dispose();
					return;
				}
				
				initializeCanvas(monitor);
			}
			
		}catch(IOException ex){
			
			ex.printStackTrace();
			getSVGHandle().dispose();
			
			if(monitor!=null){
				
				String messageLabel=ResourcesManager.bundle.getString(
					"canvasLoadingFailedMessage");
				messageLabel=URIEncoderDecoder.HTMLEntityEncode(messageLabel);

				String returnedErrorMessage=
					URIEncoderDecoder.HTMLEntityEncode(ex.getMessage());
				
				messageLabel="<html><body>"+messageLabel+"<br><i>"+
					returnedErrorMessage+"</i></body></html>";
				monitor.setErrorMessage(messageLabel);
			}
		}
	}
	
	/**
	 * initializes the canvas
	 * @param monitor the object monitoring the loading of the svg file
	 */
	protected void initializeCanvas(final Monitor monitor){
		
		//creating the selection handler
		svgHandle.createSelection();
		
		if(monitor!=null){

			monitor.setProgress(40);
		}
		
		//creating the graphics node
        //try {
        	UserAgentAdapter userAgent=new UserAgentAdapter();
    		ctx=new BridgeContext(
    				userAgent, null, new DocumentLoader(userAgent));
            builder=new DynamicGVTBuilder();
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GraphicsNode gvt=builder.build(ctx, document);
            
            if(gvt!=null) {
            	
            	gvtRoot=gvt.getRoot();
            }
        //}catch (Exception ex) {ex.printStackTrace();}
            
		if(monitor!=null){

			monitor.setProgress(80);
		}

        if(monitor==null || ! monitor.isCancelled()) {
        	
        	SwingUtilities.invokeLater(new Runnable() {
        		
            	public void run() {

                    //setting the size of the canvas
                    Dimension scaledCanvasSize=getScaledCanvasSize();
                    setCanvasPreferredSize(scaledCanvasSize);
            		svgHandle.getSVGFrame().displayFrame(scaledCanvasSize);
            		
            		//notifies the handle manager that the current handle has changed
            		Editor.getEditor().getHandlesManager().handleChanged();
            		
            		if(monitor!=null){
            			
            			monitor.stop();
            		}
            		
            		//creating the grid manager
            		gridManager=new Grid(SVGCanvas.this);
            	}
            });
        	
        }else{
        	
        	monitor.stop();
        	getSVGHandle().dispose();
        }
	}
	
	/**
	 * disposes this canvas
	 */
	public void dispose(){//TODO
		
		//disposing all the elements of the canvas
		removeAllPaintListeners();
		paintListeners.clear();
		
		if(ctx!=null) {
			
			ctx.dispose();
		}
		
		repaintManager.removeInvalidComponent(this);
		removeAll();
		
		uri=null;
		document=null;
		builder=null;
		ctx=null;
		gvtRoot=null;
		canvasOffscreenImage=null;
		renderedRectangle=null;
		tmpRectangle=null;
		scrollpane=null;
		zoomManager=null;
		gridManager=null;
		projectFile=null;
		paintListeners=null;
		repaintZone=null;
		svgHandle=null;
		repaintManager=null;
	}
	
	/**
	 * sets the new uri for the canvas
	 * @param uri a uri
	 */
	public void setNewURI(String uri){
		
		this.uri=uri;
		
		//getting the uri object
		URI theURI=null;
		
		try{
			theURI=new URI(uri);
		}catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
		
		if(theURI!=null){
			
			try{
				((SVGOMDocument)getDocument()).setURLObject(theURI.toURL());
			}catch (MalformedURLException ex) {
				ex.printStackTrace();
			}
		}
		
		Editor.getEditor().getHandlesManager().handleChanged();
	}
	
	/**
	 * @return the uri of the canvas
	 */
	public String getURI(){
		
		return uri;
	}
	
	/**
	 * @return the viewing transform
	 */
	public AffineTransform getViewingTransform(){
		
		CanvasGraphicsNode canvasGraphicsNode=getCanvasGraphicsNode();
		
		if(canvasGraphicsNode!=null) {
			
			return canvasGraphicsNode.getViewingTransform();
		}
		
		return null;
	}

	/**
	 * @return the rendering transform
	 */
	public AffineTransform getRenderingTransform(){
		
		double scale=zoomManager.getCurrentScale();
		return AffineTransform.getScaleInstance(scale, scale);
	}
	
	/**
	 * @return the offscreen image
	 */
	public BufferedImage getOffscreen(){
		
		return canvasOffscreenImage;
	}
	
	/**
	 * @return the document of the canvas
	 */
	public Document getDocument(){
		
		return document;
	}
	
	/**
	 * @return the grid manager
	 */
	public Grid getGridManager() {
		return gridManager;
	}

	/**
	 * @return the zoom manager
	 */
	public Zoom getZoomManager() {
		return zoomManager;
	}

	/**
	 * setting the preferred size for this canvas
	 * @param size the preferred size
	 */
	public void setCanvasPreferredSize(Dimension size) {

		setPreferredSize(size);
		setSize(size);
	}

	/**
	 * @return the bridge context
	 */
	public BridgeContext getBridgeContext(){
		
		return ctx;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(canvasOffscreenImage!=null) {
			
			//getting the sub image that should be painted
			Rectangle clip=g.getClipBounds();
			
			Rectangle rendRect=new Rectangle(renderedRectangle.x, renderedRectangle.y, 
					canvasOffscreenImage.getWidth(), canvasOffscreenImage.getHeight());

			if(clip.width!=0 && clip.height!=0) {

				if(clip.contains(rendRect)) {
					
					((Graphics2D)g).drawRenderedImage(canvasOffscreenImage, 
							AffineTransform.getTranslateInstance(rendRect.x, rendRect.y));

				}else if(clip.intersects(rendRect)){
					
					//getting the sub image to draw 
					Rectangle rZone=clip.intersection(rendRect);
					
					if(rZone.width!=0 && rZone.height!=0) {

						BufferedImage subImage=canvasOffscreenImage.getSubimage(
								rZone.x-rendRect.x, rZone.y-rendRect.y, rZone.width, rZone.height);

						((Graphics2D)g).drawRenderedImage(subImage, 
								AffineTransform.getTranslateInstance(rZone.x, rZone.y));
					}
				}
			}
		}
		
		drawPainters((Graphics2D)g);
	}
	
	/**
	 * refreshing the svg canvas content
	 * @param repaintSVGContent whether the whole svg image should be redrawn
	 * @param updateSVGContent whether one a part of the svg image should be redrawn
	 * @param dirtyAreas the areas to redraw
	 */
	protected void refreshCanvasContent(
			boolean repaintSVGContent, boolean updateSVGContent, Set<Area> dirtyAreas){

		//drawing the offscreen image and painting it
		if(repaintSVGContent){
			
			BufferedImage tempCanvasOffscreenImage=canvasOffscreenImage;
			
			//getting the gvt root
			GraphicsNode root=gvtRoot;

			if(root!=null){
				
				Rectangle usedRectangle=null;
				boolean isScrollAction=false;
				int scrollX=0, scrollY=0;
				
				if(tmpRectangle!=null){
					
					usedRectangle=new Rectangle(tmpRectangle);
					scrollX=usedRectangle.x-renderedRectangle.x;
					scrollY=usedRectangle.y-renderedRectangle.y;
				}
				
				//checking if the rendered rectangle has been changed owing to a scroll action		
				if(usedRectangle!=null && usedRectangle.width==renderedRectangle.width && 
						usedRectangle.height==renderedRectangle.height && 
							Math.abs(scrollX)<usedRectangle.width && Math.abs(scrollY)<usedRectangle.height){
					
					renderedRectangle.x=usedRectangle.x;
					renderedRectangle.y=usedRectangle.y;
					
					BufferedImage image=new BufferedImage(renderedRectangle.width, 
							renderedRectangle.height, BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2=GraphicsUtil.createGraphics(image);
					g2.setColor(Color.white);
					BufferedImage tmpImage=null;
					
					if(scrollY>0){
						
						tmpImage=tempCanvasOffscreenImage.getSubimage(0, scrollY, 
								renderedRectangle.width, renderedRectangle.height-scrollY);
						g2.drawImage(tmpImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
						
					}else if(scrollY<0){
						
						tmpImage=tempCanvasOffscreenImage.getSubimage(
								0, 0, renderedRectangle.width, renderedRectangle.height+scrollY);
						g2.drawImage(tmpImage, 0, -scrollY, tmpImage.getWidth(), tmpImage.getHeight(), null);
					}
					
					if(scrollX>0){
						
						tmpImage=tempCanvasOffscreenImage.getSubimage(
								scrollX, 0, renderedRectangle.width-scrollX, renderedRectangle.height);
						g2.drawImage(tmpImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
						
					}else if(scrollX<0){
						
						tmpImage=tempCanvasOffscreenImage.getSubimage(
								0, 0, renderedRectangle.width+scrollX, renderedRectangle.height);
						g2.drawImage(tmpImage, -scrollX, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
					}
					
					g2.dispose();
					tempCanvasOffscreenImage=image;
					isScrollAction=true;
					
				}else if( ! updateSVGContent){
					
					if(usedRectangle!=null){
						
						renderedRectangle.x=usedRectangle.x;
						renderedRectangle.y=usedRectangle.y;
						renderedRectangle.width=usedRectangle.width;
						renderedRectangle.height=usedRectangle.height;
					}
		
					//creating the new offscreen image
					tempCanvasOffscreenImage=new BufferedImage(
							renderedRectangle.width, renderedRectangle.height, BufferedImage.TYPE_INT_ARGB);
				}
				
				Graphics2D g2=null;
				
				if(isScrollAction){
					
					//getting the canvas scale
					double scale=zoomManager.getCurrentScale();
					
					//computing the transform
					AffineTransform af=AffineTransform.getScaleInstance(scale, scale);
					af.preConcatenate(AffineTransform.getTranslateInstance(-renderedRectangle.x, -renderedRectangle.y));
					
					//computing the rendered rectangle in the base coordinates
					Rectangle2D baseRectangle=
						getSVGHandle().getTransformsManager().getScaledRectangle(renderedRectangle, true);

					//computing the image dimensions in the base coordinates
					Rectangle2D rect=getSVGHandle().getTransformsManager().getScaledRectangle(
							new Rectangle2D.Double(0, 0, tempCanvasOffscreenImage.getWidth(), 
									tempCanvasOffscreenImage.getHeight()), true);
					Point2D basedImageSize=new Point2D.Double(rect.getWidth(), rect.getHeight());
					
					//computing the scrolling values in the base coordinates
					Point2D baseScrollPoint=getSVGHandle().getTransformsManager().getScaledPoint(
						new Point2D.Double(scrollX, scrollY), true);
					double baseScrollX=baseScrollPoint.getX();
					double baseScrollY=baseScrollPoint.getY();
					
					g2=GraphicsUtil.createGraphics(tempCanvasOffscreenImage);
					g2.setTransform(af);
					
					//clearing the image
					g2.setColor(getBackground());
					
					Rectangle2D svgRectangle=null;
					
					if(baseScrollY>0){
						
						svgRectangle=new Rectangle2D.Double(baseRectangle.getX(), 
								baseRectangle.getY()+basedImageSize.getY()-baseScrollY, 
									basedImageSize.getX(), baseScrollY);
						
					}else if(baseScrollY<0){
				
						svgRectangle=new Rectangle2D.Double(
								baseRectangle.getX(), baseRectangle.getY(), basedImageSize.getX(), -baseScrollY);
					}
					
					if(baseScrollX>0){
					
						svgRectangle=new Rectangle2D.Double(
							baseRectangle.getX()+basedImageSize.getX()-baseScrollX, baseRectangle.getY(), 
								baseScrollX, basedImageSize.getY());
						
					}else if(baseScrollX<0){
						
						svgRectangle=new Rectangle2D.Double(
								baseRectangle.getX(), baseRectangle.getY(), -baseScrollX, basedImageSize.getY());
					}
					
					if(svgRectangle!=null){
						
						g2.clip(svgRectangle);
						
						//painting the background
				        g2.setColor(Color.white);
				        g2.fill(svgRectangle);

						setRenderingHints(g2); 
					}
						
				}else{
					
					//getting the canvas scale
					double scale=zoomManager.getCurrentScale();
					
					//computing the transform
					AffineTransform af=AffineTransform.getScaleInstance(scale, scale);
					af.preConcatenate(AffineTransform.getTranslateInstance(-renderedRectangle.x, -renderedRectangle.y));
					
					//root.setTransform(af);
					g2=GraphicsUtil.createGraphics(tempCanvasOffscreenImage);
					g2.setTransform(af);
					setRenderingHints(g2);
			        
			        if(updateSVGContent) {

			        	if(dirtyAreas!=null) {
			        		
			        		//computing the clip rectangle
			        		Area clip=null;
			        		
			        		for(Area area : dirtyAreas) {
			        			
			        			if(area!=null) {
			        				
			        				if(clip==null) {
			        					
			        					clip=area;
			        					
			        				}else {
			        					
			        					clip.add(area);
			        				}
			        			}
			        		}
			        		
			        		if(clip!=null) {

			        			g2.clip(clip);
			        			
						        //painting the background
						        g2.setColor(Color.white);
						        g2.fill(clip);
		
			        		}else {
			        			
			        			//clearing the background
			        			g2.setColor(Color.white);
			        			g2.fillRect(0, 0, tempCanvasOffscreenImage.getWidth(), 
			        					tempCanvasOffscreenImage.getHeight());
			        		}
			        	}
			        }
				}
				
				//painting the image
				root.paint(g2);
				
				Rectangle2D clipBounds=g2.getClipBounds();

				if(g2.getClip()!=null) {
					
					clipBounds=svgHandle.getTransformsManager().
						getScaledRectangle(clipBounds, false);
				}

				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

						if(renderedRectangle!=null) {
							
							repaintManager.addDirtyRegion(
								SVGCanvas.this, renderedRectangle.x, renderedRectangle.y, 
									renderedRectangle.width, renderedRectangle.height);
							
						}else {
							
							repaintManager.markCompletelyDirty(SVGCanvas.this);
						}						
					}
				});

				g2.dispose();
			}
			
			canvasOffscreenImage=tempCanvasOffscreenImage;
			tmpRectangle=null;
		}
	}
	
	/**
	 * sets the rendering hints
	 * @param g a graphics object
	 */
	protected void setRenderingHints(Graphics2D g) {
		
		//setting the rendering hints
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	}
	
	/**
	 * setting the new canvas size
	 * @param newSize the new size
	 */
	public void setCanvasSize(Point2D newSize){

		CanvasGraphicsNode canvasGraphicsNode=getCanvasGraphicsNode();
		
		if(canvasGraphicsNode!=null && 
				canvasGraphicsNode.getPositionTransform()==null){
			
			canvasGraphicsNode.setPositionTransform(new AffineTransform());
		}
		
		Element root=document.getDocumentElement();
		String width=FormatStore.format(newSize.getX());
		String height=FormatStore.format(newSize.getY());
		
		root.setAttribute("width", width);
		root.setAttribute("height", height);
		root.setAttribute("viewBox", "0 0 "+width+" "+height);
		
		setCanvasPreferredSize(getScaledCanvasSize());
		
		scrollpane.revalidate();
		scrollpane.refreshRulers();
		setRenderedRectangle(new Rectangle(
				scrollpane.getViewport().getViewRect()), true, true);
	}
	
	/** 
	 * @return SVGScrollPane the scrollpane that contains the canvas
	 */
	public SVGScrollPane getScrollPane(){
		return scrollpane;
	}

	/**
	 * @return the svg handle linked to this canvas
	 */
	public SVGHandle getSVGHandle() {
		return svgHandle;
	}
	
	/**
	 * @return the rendering rectangle
	 */
	public Rectangle getRenderedRectangle() {
		return renderedRectangle;
	}
	
	/**
	 * sets the new rendered rectangle
	 * @param rect a rendered rectangle
	 * @param reinitialize whether the stored information about 
	 * the rendered picture should be erased or not
	 * @param forceReinitialize whether the stored information about the rendered 
	 * picture should be erased or not, forcing it if necessary
	 */
	public void setRenderedRectangle(
			Rectangle rect, boolean reinitialize, boolean forceReinitialize){

		if(forceReinitialize){
		
			renderedRectangle=new Rectangle(0, 0, 1, 1);
			tmpRectangle=rect;
			refreshCanvasContent(true, false, null);
			
		}else if(reinitialize) {
			
			if(rect!=null && ! renderedRectangle.equals(rect) && reinitialize){
				
				renderedRectangle=new Rectangle(0, 0, 1, 1);
				tmpRectangle=rect;
				refreshCanvasContent(true, false, null);
			}
			
		}else if(rect!=null && ! renderedRectangle.equals(rect) && 
				renderedRectangle.width>0 && renderedRectangle.height>0){
			
			tmpRectangle=rect;
			refreshCanvasContent(true, false, null);
		}
	}
	
	/**
	 * requests that the svg content should be repainted
	 */
	public void requestRepaintContent(){

		refreshCanvasContent(true, false, null);
	}
	
	/**
	 * requests that the svg content should be updated
	 * @param repaintArea the area to repaint
	 */
	public void requestUpdateContent(Area repaintArea){

		if(repaintArea!=null) {

			Set<Area> dirtyAreas=new HashSet<Area>();
			dirtyAreas.add(repaintArea);
			
			refreshCanvasContent(true, true, dirtyAreas);
		}
	}

	/**
	 * @return the project file linked to the canvas
	 */
	public File getProjectFile() {
		return projectFile;
	}
	
	/**
	 * sets the current cursor
	 * @param cursor the current cursor
	 */
	public void setSVGCursor(Cursor cursor){

		if(cursor!=null){

			setCursor(cursor);
		}
	}
	
	/**
	 * notifies that the parent element has changed
	 */
	public void notifyParentElementChanged(){

		repaintManager.markCompletelyDirty(this);
	}
	
	/**
	 * @return the canvas' size
	 */
	public Point2D getGeometryCanvasSize(){
		
		return getGeometryCanvasSize(document);
	}
	
	/**
	 * returns the size of the svg denoted by the provided document
	 * @param aDocument a svg document
	 * @return the size of the svg denoted by the provided document
	 */
	public static Point2D getGeometryCanvasSize(Document aDocument){
		
		if(aDocument!=null){
			
			//getting the root element	
			Element root=aDocument.getDocumentElement();
			
			if(root!=null){
				
				double w=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "width"));
				double h=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "height"));
				
				return new Point2D.Double(w, h);
			}
		}
		
		return new Point2D.Double(0,0);
	}
	
	/**
	 * @param root the root element
	 * @return the scaled canvas' size
	 */
	public Dimension getScaledCanvasSize(Element root){
		
		Dimension scaledSize=new Dimension(0, 0);
		
		if(root!=null){
			
			double w=0, h=0;
			
			try{
				//getting the scale factor of the canvas
				double scale=zoomManager.getCurrentScale();
				
				w=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "width"))*scale;
				h=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "height"))*scale;
				scaledSize.width=(int)w;
				scaledSize.height=(int)h;
			}catch (DOMException ex) {
				ex.printStackTrace();
			}
		}
		
		return scaledSize;
	}
	
	/**
	 * @return the scaled canvas' size
	 */
	public Dimension getScaledCanvasSize(){
		
		Dimension scaledSize=new Dimension(0, 0);
		
		//gets the root element
		if(document!=null){
			
			Element root=document.getDocumentElement();
			
			if(root!=null){
				
				double w=0, h=0;
				
				try{
					//getting the scale factor of the canvas
					double scale=zoomManager.getCurrentScale();
					
					w=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "width"))*scale;
					h=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "height"))*scale;
					scaledSize.width=(int)w;
					scaledSize.height=(int)h;
				}catch (DOMException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		return scaledSize;
	}
	
	/**
	 * handles the repaint of the canvas
	 * @param clipZone the clip zone or null to repaint the whole canvas
	 */
	protected void handleRepaint(Rectangle2D clipZone) {
		
		//getting the repaint zone
		if(clipZone!=null) {
			
			clipZone=new Rectangle2D.Double(clipZone.getX(), clipZone.getY(), 
					clipZone.getWidth(), clipZone.getHeight());
		}
		
		Rectangle2D clip=null;
		
		if(repaintZone!=null) {
			
			Rectangle2D rZone=new Rectangle2D.Double(repaintZone.getX(), repaintZone.getY(), 
					repaintZone.getWidth(), repaintZone.getHeight());
			
			if(clipZone!=null) {
				
				rZone=rZone.createUnion(clipZone);
			}
			
			rZone=rZone.createIntersection(renderedRectangle);
			
	        double x0=rZone.getX();
	        double y0=rZone.getY();
	        double x1=rZone.getX()+rZone.getWidth();
	        double y1=rZone.getY()+rZone.getHeight();

			clip=new Rectangle2D.Double(x0-2, y0-2, x1-x0+5, y1-y0+5);
			repaintZone=null;
			
		}else if(clipZone!=null) {
			
			clipZone=clipZone.createIntersection(renderedRectangle);
			
	        double x0=clipZone.getX();
	        double y0=clipZone.getY();
	        double x1=clipZone.getX()+clipZone.getWidth();
	        double y1=clipZone.getY()+clipZone.getHeight();
	        
			clip=new Rectangle2D.Double(x0-2, y0-2, x1-x0+5, y1-y0+5);
		}

		//repainting
		if(clip==null) {
			
			//repaint();
			repaintManager.markCompletelyDirty(this);
			
		}else {
			
			repaintManager.addDirtyRegion(this, 
				clip.getBounds().x, clip.getBounds().y, 
					clip.getBounds().width, clip.getBounds().height);
			//repaint(clip.getBounds());
		}
	}
	
	/**
	 * adds a grid paint listener
	 * @param type the integer representing the layer at which the painting should be done
	 * @param l the grid paint listener to be added
	 * @param makeRepaint whether to make a repaint after the paint listener was added or not
	 */
	public void addLayerPaintListener(int type, CanvasPainter l, boolean makeRepaint) {
		
		if(l!=null){

			Set<CanvasPainter> set=paintListeners.get(type);

			if(set!=null){
				
				set.add(l);
			}

			//handle the clips of the painter to compute the new clipping zone
			handleClips(l.getClip(), makeRepaint);
		}
	}

	/**
	 * removes a paint listener
	 * @param l the paint listener to be removed
	 * @param makeRepaint whether to make a repaint after the paint listener was removed or not
	 */
	public void removePaintListener(CanvasPainter l, boolean makeRepaint){
		
		//try{
			paintListeners.get(GRID_LAYER).remove(l);
			paintListeners.get(BOTTOM_LAYER).remove(l);
			paintListeners.get(SELECTION_LAYER).remove(l);
			paintListeners.get(DRAW_LAYER).remove(l);
			paintListeners.get(TOP_LAYER).remove(l);
		//} catch (Exception ex) {}

		if(l!=null){
			
			//handle the clips of the painter to compute the new clipping zone
			handleClips(l.getClip(), makeRepaint);
		}
	}
	
	/**
	 * handles the clipping given the set of clips
	 * @param clips a set of clip rectangles
	 * @param makeRepaint whether the repaint action should be done
	 */
	protected void handleClips(Set<Rectangle2D> clips, boolean makeRepaint) {

		if(clips!=null && clips.size()>0) {
			
			if(makeRepaint) {
				
				for(Rectangle2D clip : clips) {
					
					handleRepaint(clip);
				}
				
			}else {
				
				for(Rectangle2D clip : clips) {
					
					if(repaintZone!=null) {
						
						repaintZone.add(clip);
						
					}else {
						
						repaintZone=new Rectangle2D.Double();
						repaintZone.setRect(clip);
					}
				}
			}

		}else if(makeRepaint) {
				
			handleRepaint(null);
		}
	}
	
	/**
	 * notifies the paint listeners when a paint action is done
	 *@param g2 the graphics
	 */
	protected void drawPainters(Graphics2D g2){
		
		g2=(Graphics2D)g2.create();
		
		//setting the rendering hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
        		RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, 
        		RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, 
        		RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, 
        		RenderingHints.VALUE_DITHER_DISABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, 
        		RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
        		RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
        		RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 
        		RenderingHints.VALUE_STROKE_NORMALIZE);
        
        Set<CanvasPainter> set=null;
        
        for(int layer : layers){
        	
        	set=paintListeners.get(layer);
        	
    		for(CanvasPainter listener : set){
    			
    			listener.paintToBeDone(g2);
    		}
        }
		
		//painting the current parent bounds
		Element parentElement=getSVGHandle().getSelection().getParentElement();
		
		if(parentElement!=null && 
				! parentElement.equals(document.getDocumentElement())){
			
			Rectangle2D parentElementBounds=
				getSVGHandle().getSvgElementsManager().getNodeBounds(parentElement);
			
			//getting the shape that should be drawn
			Area area=new Area(renderedRectangle);
			area.subtract(new Area(parentElementBounds));
			
			//painting the area
			g2.setColor(parentVeilColor);
			g2.fill(area);
			g2.setColor(Color.lightGray);
			g2.draw(parentElementBounds);
		}
		
		g2.dispose();
	}
	
	/**
	 * removes all paint listeners
	 */
	public void removeAllPaintListeners(){
		
		paintListeners.get(GRID_LAYER).clear();
		paintListeners.get(BOTTOM_LAYER).clear();
		paintListeners.get(SELECTION_LAYER).clear();
		paintListeners.get(DRAW_LAYER).clear();
		paintListeners.get(TOP_LAYER).clear();
	}
	
	/**
	 * handles the repaint of the canvas
	 * @param clipZone the clip zone or null the repaint all the canvas
	 */
	public void doRepaint(Rectangle clipZone){
		
		handleRepaint(clipZone);
	}
	
	/**
	 * @return the gvt root
	 */
	public GraphicsNode getGraphicsNode(){
		return getCanvasGraphicsNode();
	}
	
	/**
	 * @return the canvas graphics node for this canvas
	 */
	private CanvasGraphicsNode getCanvasGraphicsNode() {
		
        java.util.List<?> children=gvtRoot.getChildren();
        
        if(children.size()==0) {
        	
            return null;
        }
        
        GraphicsNode gn=(GraphicsNode)children.get(0);
        
        if(! (gn instanceof CanvasGraphicsNode)) {
        	
        	 return null;
        }
           
        return (CanvasGraphicsNode)gn;
    }
	
	/**
	 * clears the cache
	 */
	public void clearCache(){
		
		if(ctx!=null){
			
			ctx.getDocumentLoader().dispose();
		}
	}
}
