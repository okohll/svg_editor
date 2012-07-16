/*
 * Created on 1 juin 2005
 */
package fr.itris.glips.compiler.rtdb.file;

import java.io.*;
import org.w3c.dom.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class CompiledFile {
	
	/**
	 * the name of the attribute allowing to specify the name of the server IP address
	 */
	public static String serverAddressName="serverIPAddress";

    /**
     * the file manager
     */
    protected FileManager fileManager=null;
	
    /**
     * the constant that should be used to handle a compiled xml file
     */
    public static int XML_FILE=0;
    
    /**
     * the constant that should be used to handle a compiled svg file
     */
    public static int SVG_FILE=1;
    
    /**
     * the constant that should be used to handle a view file
     */
    public static int VIEWS_FILE=2;
    
    /**
     * the file corresponding to this compiled file
     */
    protected File file=null;
    
    /**
     * the document of this compiled file
     */
    protected Document doc=null;
    
    /**
     * the type of the file (XML_FILE or SVG_FILE)
     */
    protected int type=XML_FILE;
    
    /**
     * the constructor of the class
     * @param fileManager the file manager
     * @param path the path of the file
     * @param doc the document of this compiled file
     * @param type the type of the file (XML_FILE or SVG_FILE)
     */
    public CompiledFile(FileManager fileManager, String path, Document doc, int type){
        
    	this.fileManager=fileManager;
      	this.doc=doc;
        this.type=type;
        
        try{
        	file=FileManager.createFile(new URI(path).toASCIIString());
        }catch (Exception ex){file=null;ex.printStackTrace();}
    }
    
    /**
     * prints the document into the file
     */
    protected void writeFile(){

        if(doc!=null && file!=null){

            StringBuffer buffer=new StringBuffer("");
            
            try {
            	for (Node node=doc.getFirstChild(); node!=null; node=node.getNextSibling()) {
            		
                	writeNode(node, buffer, type!=SVG_FILE || true);//TODO
                }
            }catch (Exception ex) {ex.printStackTrace();}

            //writing to the writer
            try{

        		ByteBuffer byteBuffer=ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
                FileOutputStream out=new FileOutputStream(file);
                
                /*if(type==SVG_FILE && false){//TODO
                	
                	//compressing the svg content
                	GZIPOutputStream zout=new GZIPOutputStream(out);
                	
                	zout.write(byteBuffer.array());
                	zout.flush();
                	zout.close();
                	
                }else{*/
                	
                    FileChannel channel=out.getChannel();
                    channel.write(byteBuffer);
                    channel.close();
                //}

            }catch (Exception ex){ex.printStackTrace();}
        }
    }

    /**
     * writes the string representation of the given node in the given string buffer
     * @param node a node
     * @param buffer the string buffer
     * @param returnToLine whether each written node should be on a single line
     */
    public static void writeNode(Node node, StringBuffer buffer, boolean returnToLine){
    	
    	if(node!=null){
        	
            switch (node.getNodeType()) {
            
            case Node.ELEMENT_NODE:
            	
                buffer.append("<");
                buffer.append(node.getNodeName());

                if (node.hasAttributes()) {
                	
                    NamedNodeMap attr = node.getAttributes();
                    int len = attr.getLength();
                    
                    for (int i = 0; i < len; i++) {
                    	
                        Attr a = (Attr)attr.item(i);
                        buffer.append(" ");
                        buffer.append(a.getNodeName());
                        buffer.append("=\"");
                        buffer.append(contentToString(a.getNodeValue()));
                        buffer.append("\"");
                    }
                }

                Node c = node.getFirstChild();
                
                if (c != null) {
                	
                    buffer.append(">");
                    
                    if(returnToLine){
                    	
                    	buffer.append("\n");
                    }
                    
                    for (; c != null; c = c.getNextSibling()) {
                    	
                        writeNode(c, buffer, returnToLine);
                    }
                    
                    buffer.append("</");
                    buffer.append(node.getNodeName());
                    buffer.append(">");

                } else {
                	
                    buffer.append("/>");
                }
                
                if(returnToLine){
                  
                	buffer.append("\n");
                }

                break;
                
            case Node.TEXT_NODE:
            	
                buffer.append(contentToString(node.getNodeValue()));
                break;
                
            case Node.CDATA_SECTION_NODE:
            	
                buffer.append("<![CDATA[");
                buffer.append(node.getNodeValue());
                buffer.append("]]>");
                break;
                
            case Node.ENTITY_REFERENCE_NODE:
            	
                buffer.append("&");
                buffer.append(node.getNodeName());
                buffer.append(";");
                break;
                
            case Node.PROCESSING_INSTRUCTION_NODE:
            	
                buffer.append("<?");
                buffer.append(node.getNodeName());
                buffer.append(" ");
                buffer.append(node.getNodeValue());
                buffer.append("?>");
                break;
                
            case Node.COMMENT_NODE:
            	
                buffer.append("<!--");
                buffer.append(node.getNodeValue());
                buffer.append("-->");
                break;
                
            case Node.DOCUMENT_TYPE_NODE: 
            	
                DocumentType dt = (DocumentType)node;
                buffer.append ("<!DOCTYPE "); 
                buffer.append (node.getOwnerDocument().getDocumentElement().getNodeName());
                String pubID = dt.getPublicId();
                
                if (pubID != null) {
                	
                    buffer.append (" PUBLIC \"" + dt.getNodeName() + "\" \"" +
                               pubID + "\">");
                    
                } else {
                	
                    String sysID = dt.getSystemId();
                    
                    if (sysID != null){
                    	
                        buffer.append (" SYSTEM \"" + sysID + "\">");
                    }
                }

                break;
            }
    	}
    }

    /**
     * @param s the string to be modified
     * @return the given content value transformed to replace invalid
     * characters with entities.
     */
    public static String contentToString(String s) {
    	
        StringBuffer result = new StringBuffer();
        
        if(s!=null) {
        	
            s=s.replaceAll("\\n+", "");
            s=s.replaceAll("\\r+", "");
            s=s.replaceAll("\\t+", "");

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                switch (c) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&apos;");
                    break;
                default:
                    result.append(c);
                }
            }
        }
        
        return result.toString();
    }
 
}
