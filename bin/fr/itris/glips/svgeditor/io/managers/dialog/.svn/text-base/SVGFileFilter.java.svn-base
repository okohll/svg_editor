/*
 * Created on 24 mars 2004
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
package fr.itris.glips.svgeditor.io.managers.dialog;

import java.io.*;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class for filtering files in a JFileChooser
 * @author ITRIS, Jordi SUC
 */
public class SVGFileFilter extends AbstractFileFilter {

	/**
	 * the description of the file filter
	 */
	private String description="";
	
	/**
	 * the constructor of the class
	 */
	public SVGFileFilter() {
	    
		//getting the labels from the resources
		description=ResourcesManager.bundle.
			getString("SVGMessageFileFilter");
	}

	@Override
	public boolean accept(File f) {
	    
	    String name=f.getName().toLowerCase();
	    
		if(f.isDirectory() || 
				name.endsWith(EditorToolkit.SVG_FILE_EXTENSION) ||
					name.endsWith(EditorToolkit.SVGZ_FILE_EXTENSION)){
		    
		    return true;
		    
		}else{
		    
		    return false;
		}
	}

	@Override
	public String getDescription() {
		
		return description;
	}
	
	@Override
	public boolean acceptFile(File f) {
	    
	    String name=f.getName().toLowerCase();
	    
		if(name.endsWith(EditorToolkit.SVG_FILE_EXTENSION) || 
			name.endsWith(EditorToolkit.SVGZ_FILE_EXTENSION)){
		    
		    return true;
		}
		
		return false;
	}

	@Override
	public String getDefaultExtension() {
		
		return EditorToolkit.SVG_FILE_EXTENSION;
	}
}
