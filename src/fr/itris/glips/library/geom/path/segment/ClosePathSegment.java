package fr.itris.glips.library.geom.path.segment;

import java.awt.geom.*;

import org.apache.batik.ext.awt.geom.*;

/**
 * the class that handles close path instructions
 * @author ITRIS, Jordi SUC
 */
public class ClosePathSegment extends Segment{

	/**
	 * a constructor of the class
	 * @param previousSegment the segment that lies just before this segment
	 */
	public ClosePathSegment(Segment previousSegment){
		
		super(previousSegment);
		initialize();
	}
	
	@Override
	public void initialize() {
		
		this.absoluteCmdName="Z";
		this.relativeCmdName="z";
	}
	
	@Override
	public void storeValues() {}
	
	@Override
	public void fillPath(ExtendedGeneralPath path) {
		
		path.closePath();
		super.fillPath(path);
	}
	
	@Override
	public void applyTransform(AffineTransform transform) {}
	
@Override
	public void modifyPoint(Point2D point, int index) {}
	
	@Override
	public String toString() {

		return absoluteCmdName;
	}
}
