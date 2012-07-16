package fr.itris.glips.svgeditor.io.managers.export.image;

import org.apache.batik.bridge.*;
import org.apache.batik.gvt.*;
import org.w3c.dom.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.*;
import java.util.*;

/**
 * the class used to generate the image of a svg file
 * @author ITRIS, Jordi SUC
 */
public class SVGDocumentImageCreator {
	
	/**
	 * the monitor
	 */
	private Monitor monitor;
	
	/**
	 * the document of the svg file
	 */
	private Document document;
	
	/**
	 * the user agent
	 */
	private UserAgentAdapter userAgent;
	
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
     * the image
     */
    private BufferedImage image=null;
    
    /**
     * the listeners the this image creator
     */
    private Set<SVGDocumentImageCreatorListener> listeners=
    		Collections.synchronizedSet(new HashSet<SVGDocumentImageCreatorListener> ());
	
	/**
	 * the constructor of the class
	 * @param doc the document of a svg file
	 * @param monitor the monitor
	 */
	public SVGDocumentImageCreator(Document doc, Monitor monitor) {
		
		this.document=doc;
		this.monitor=monitor;
	}
	
	/**
	 * creates the image at the given dimension
	 * @param size the dimension of the created image
	 * @param encodeAlpha whether the image should use a alpha channel
	 */
	public void createImage(final Dimension size, final boolean encodeAlpha) {

		Thread thread=new Thread() {
			
			@Override
			public void run() {
				
				image=new BufferedImage(size.width, size.height, 
						encodeAlpha?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);

		        try {
					//creating the graphics node
		    		userAgent=new UserAgentAdapter();
		    		ctx=new BridgeContext(userAgent, null, new DocumentLoader(userAgent));
		            builder=new GVTBuilder();
		            ctx.setDynamicState(BridgeContext.STATIC);
		            
		            monitor.setProgress(5);
		            
		            if(monitor.isCancelled()) {

		        		monitor.stop();
		            	ctx.dispose();
		            	return;
		            }
		            
		            GraphicsNode gvt=builder.build(ctx, document);
		            monitor.setProgress(40);
		            
		            if(monitor.isCancelled()) {

		        		monitor.stop();
		            	ctx.dispose();
		            	return;
		            }
		            
		            if(gvt!=null) {
		            	
		            	gvtRoot=gvt.getRoot();
		            }
		            
		            //getting the size of the canvas
		            Point2D canvasSize=getGeometryCanvasSize(document);
		            
		            //computing the scale transformation for the svg doc content to fit the created image size
		            AffineTransform af=AffineTransform.getScaleInstance(
		            		size.width/canvasSize.getX(), size.height/canvasSize.getY());
		            
		            //getting the graphics object of the image
		            Graphics2D g2=image.createGraphics();
		            
		            if(! encodeAlpha) {
		            	
		            	//painting a white background
		            	g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		            }
		            
		            handleGraphicsConfiguration(g2);
		            g2.setTransform(af);
		   
		            //painting the image
		            gvtRoot.paint(g2);
		            g2.dispose();
		   
		            monitor.setProgress(50);
		            
		            //notifies the listeners that the image has been created
		            for(SVGDocumentImageCreatorListener listener : 
		            		new HashSet<SVGDocumentImageCreatorListener>(listeners)) {
		            	
		            	listener.imageCreated(image);
		            }
		            
		            listeners.clear();
		            ctx.dispose();
		            
		        }catch (Exception ex) {
		        	
		        	ex.printStackTrace();
		    		monitor.stop();
		        	listeners.clear();
		        	ctx.dispose();
		        }
			}
		};
		
		thread.start();
	}
	
	/**
	 * computes and returns the canvas' size of the given svg document
	 * @param document a svg document
	 * @return the canvas' size of the given svg document
	 */
	public static Point2D getGeometryCanvasSize(Document document){
		
		//gets the root element
		if(document!=null){
			
			Element root=document.getDocumentElement();
			
			if(root!=null){
				
				double w=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "width"));
				double h=EditorToolkit.getPixelledNumber(root.getAttributeNS(null, "height"));
				
				return new Point2D.Double(w, h);
			}
		}
		
		return new Point2D.Double(0,0);
	}
	
	/**
	 * adds a new svg document image creator listener to the list of the listeners
	 * @param listener a listener
	 */
	public void addImageCreatorListener(
			SVGDocumentImageCreatorListener listener) {
		
		listeners.add(listener);
	}

	/**
	 * @return the created image
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * sets the parameters of the given graphics object
	 * @param g2 a graphics object
	 */
	public static void handleGraphicsConfiguration(Graphics2D g2) {
		
		//setting the rendering hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	}
}
