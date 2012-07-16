/*
 * Created on 26 mai 2004
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
package fr.itris.glips.rtda;

import java.text.*;
import java.awt.geom.*;

	/**
	 * @author ITRIS, Jordi SUC
	 *
	 * the class representing the transformation matrix applied to a node
	 */
	public class SVGTransformMatrix{
		
		private AffineTransform transform=null;
		
		private DecimalFormat format;
		
		/**
		 * the constructor of the class
		 * @param a
		 * @param b
		 * @param c
		 * @param d
		 * @param e
		 * @param f
		 */
		public SVGTransformMatrix(double a, double b, double c, double d, double e, double f){
			
			transform=new AffineTransform(a, b, c, d, e, f);
			
			//sets the format obkect that will be used to convert a double value into a string
			DecimalFormatSymbols symbols=new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			format=new DecimalFormat("############.################", symbols);
		}
		
		/**
		 * tells whether this matrix is the identity matrix or not
		 * @return true if the matrix is the identity matrix
		 */
		public boolean isIdentity(){
			return transform.isIdentity();
		}
		
		/**
		 * concatenates a translation transform to the matrix
		 * @param e
		 * @param f
		 */
		public void concatenateTranslate(double e, double f){
			transform.preConcatenate(AffineTransform.getTranslateInstance(e, f));
		}
		
		/**
		 * concatenates a scale transform to the matrix
		 * @param a
		 * @param d
		 */
		public void concatenateScale(double a, double d){
			transform.preConcatenate(AffineTransform.getScaleInstance(a, d));
		}
		
		/**
		 * concatenates a rotate transform to the matrix
		 * @param angle
		 */
		public void concatenateRotate(double angle){
			transform.preConcatenate(AffineTransform.getRotateInstance(angle));
		}
		
		/**
		 * concatenates a skewx transform to the matrix
		 * @param angle
		 */
		public void concatenateSkewX(double angle){
			transform.preConcatenate(AffineTransform.getShearInstance(Math.tan(angle), 0));
		}

		/**
		 * concatenates a skewy transform to the matrix
		 * @param angle		 
		 */		
		public void concatenateSkewY(double angle){
			transform.preConcatenate(AffineTransform.getShearInstance(0, Math.tan(angle)));
		}
		
		/**
		 * concatenates a horizontal flip transform to the matrix
		 */		
		public void concatenateHorizontalFlip(){
			transform.preConcatenate(AffineTransform.getScaleInstance(-1, 1));
		}
		
		/**
		 * concatenates a transform matrix to the matrix
		 * @param matrix the transform matrix to concatenate
		 */		
		public void concatenateMatrix(SVGTransformMatrix matrix){
			
			AffineTransform newTransform=new AffineTransform(	matrix.getA(), matrix.getB(), matrix.getC(), 
																								matrix.getD(), matrix.getE(), matrix.getF());
			transform.preConcatenate(newTransform);
			
		}
		
		/**
		 * concatenates a vertical flip transform to the matrix		 
		 */		
		public void concatenateVerticalFlip(){
		    
			transform.preConcatenate(AffineTransform.getScaleInstance(1, -1));
		}
		
		/**
		 * concatenates an affine transform to the matrix
		 * @param af an affine transform
		 */
		public void concatenateTransform(AffineTransform af){
		    
		    if(af!=null){
		        
		        transform.preConcatenate(af);
		    }
		}

		@Override
		public String toString(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			String display="";
			int i;
			
			for(i=0;i<6;i++){
			    
				display=display.concat(matrix[i]+" ");
			}	
				
			return display;
		}
		
		/**
		 * clones the current matrix object
		 * @return a cloned matrix
		 */
		public SVGTransformMatrix cloneMatrix(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return new SVGTransformMatrix(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
		}
		
		/**
		 * tells whether the matrix is correct or not
		 * @return true if te matrix is correct
		 */
		public boolean isMatrixCorrect(){
		    
		    boolean isNotCorrect=false;

		    isNotCorrect=isNotCorrect || Double.isNaN(getA()) || Double.isInfinite(getA());
		    isNotCorrect=isNotCorrect || Double.isNaN(getB()) || Double.isInfinite(getB());		    
		    isNotCorrect=isNotCorrect || Double.isNaN(getC()) || Double.isInfinite(getC());
		    isNotCorrect=isNotCorrect || Double.isNaN(getD()) || Double.isInfinite(getD());
		    isNotCorrect=isNotCorrect || Double.isNaN(getE()) || Double.isInfinite(getE());
		    isNotCorrect=isNotCorrect || Double.isNaN(getF()) || Double.isInfinite(getF());
		    
		    return ! isNotCorrect;
		}
		
		/**
		 * @return a double representing the a element in the matrix
		 */
		public double getA(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return matrix[0];
		}

		/**
		 * @return a double representing the b element in the matrix
		 */
		public double getB(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return matrix[1];
		}
		
		/**
		 * @return a double representing the c element in the matrix
		 */
		public double getC(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return matrix[2];
		}
		
		/**
		 * @return a double representing the d element in the matrix
		 */
		public double getD(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return matrix[3];
		}
		
		/**
		 * @return a double representing the e element in the matrix
		 */
		public double getE(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return matrix[4];
		}
		
		/**
		 * @return a double representing the f element in the matrix
		 *
		 */
		public double getF(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			
			return matrix[5];
		}
		
		/**
		 * @return the affine transform corresponding to the matrix
		 */
		public AffineTransform getTransform(){
			return transform;
		}
		
		/**
		 * gets the string that will be added in the dom as the value of the transform attribute
		 * @return the string that will be added in the dom as the value of the transform attribute
		 */
		public String getMatrixRepresentation(){
			
			double[] matrix=new double[6];
			transform.getMatrix(matrix);
			String rep="matrix(";
			
			rep=rep.concat(" "+format.format(matrix[0])+",");
			rep=rep.concat(" "+format.format(matrix[1])+",");			
			rep=rep.concat(" "+format.format(matrix[2])+",");
			rep=rep.concat(" "+format.format(matrix[3])+",");			
			rep=rep.concat(" "+format.format(matrix[4])+",");		
			rep=rep.concat(" "+format.format(matrix[5]));			
			rep=rep.concat(")");
			
			return rep;
		}
	}
	
