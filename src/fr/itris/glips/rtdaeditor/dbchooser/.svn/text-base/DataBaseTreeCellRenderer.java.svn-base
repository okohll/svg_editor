/*
 * Created on 24 juin 2005
 */
package fr.itris.glips.rtdaeditor.dbchooser;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class DataBaseTreeCellRenderer extends DefaultTreeCellRenderer{

    /**
     * the colors
     */
    private Color  textSelectionColor, textNonSelectionColor, 
                            backgroundSelectionColor, backgroundNonSelectionColor,
                            borderSelectionColor;
    
    /**
     * the label that will be used for the rendering
     */
    private JLabel labelCmp=null;
    
    /**
     * whether the cell is selected or not
     */
    protected boolean selected=false;
    
    /**
     * whether the cell has focus or not
     */
    protected boolean hasFocus=false;
    
    /**
     * the constructor of the class
     * @param tree the tree that will use this renderer
     */
    public DataBaseTreeCellRenderer(JTree tree){
        
        //getting the colors
        textSelectionColor=UIManager.getColor("Tree.selectionForeground");
        textNonSelectionColor=UIManager.getColor("Tree.textForeground");
        backgroundSelectionColor=UIManager.getColor("Tree.selectionBackground");
        backgroundNonSelectionColor=UIManager.getColor("Tree.textBackground");
        borderSelectionColor=UIManager.getColor("Tree.selectionBorderColor");
        
        //creating the label that will be used for rendering
        labelCmp=new JLabel() {

       	 	@Override
            protected void paintComponent(Graphics g) {

                int paintedX=0;
                
                if(getIcon()!=null) {
                    
                    paintedX=getIcon().getIconWidth()+getIconTextGap();
                }
                
                //setting the background
                if(selected) {
                    
                    g.setColor(backgroundSelectionColor);

                }else {
                    
                    g.setColor(backgroundNonSelectionColor);
                }
                
                g.fillRect(paintedX, 0, getWidth()-paintedX-1, getHeight()-1);
                
                super.paintComponent(g);
  
                //painting the focus
                if(hasFocus) {
                    
                    g.setColor(borderSelectionColor);
                    g.drawRect(paintedX, 0, getWidth()-paintedX-1, getHeight()-1);
                }
            }
        };
        
        labelCmp.setBorder(new EmptyBorder(2, 2, 2, 2));
        labelCmp.setComponentOrientation(tree.getComponentOrientation());
    }
    
	 @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFcs){

        if(value!=null && value instanceof DataBaseTreeNode){

            this.selected=sel;
            this.hasFocus=hasFcs;
            
            DataBaseTreeNode node=(DataBaseTreeNode)value;
            
            //setting the icon
            ImageIcon currentIcon=getIcon(node);
            labelCmp.setIcon(currentIcon);

            //setting the text
            labelCmp.setText(value.toString());
            labelCmp.setToolTipText(node.getToolTipText());
        }
        
        return labelCmp;
    }
    
    /**
     * returning the icon corresponding to the given node
     * @param node a node
     * @return the icon corresponding to the given node
     */
    protected ImageIcon getIcon(DataBaseTreeNode node){
        
        ImageIcon icon=null;
        
        if(node!=null){
            
            icon=ResourcesManager.getIcon(node.getIconName(), false);
        }
        
        return icon;
    }

	 @Override
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

	 @Override
    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

	 @Override
    public Color getBorderSelectionColor() {
        return borderSelectionColor;
    }

	 @Override
    public Color getTextNonSelectionColor() {
        return textNonSelectionColor;
    }

	 @Override
    public Color getTextSelectionColor() {
        return textSelectionColor;
    }

}
