package fr.itris.glips.svgeditor.io.managers.export.handler.dialog;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.io.managers.export.image.*;
import fr.itris.glips.svgeditor.resources.*;
import org.w3c.dom.*;

/**
 * the class of the dialogs used to choose the parameters of an export action 
 * 
 * @author ITRIS, Jordi SUC
 */
public abstract class ExportDialog extends JDialog{
    
    /**
     * the constant describing a ok action
     */
    public static final int OK_ACTION=0;
    
    /**
     * the constant describing a cancel action
     */
    public static final int CANCEL_ACTION=1;

    /**
     * the title of the export dialog
     */
    protected String exportDialogTitle="";
    
    /**
     * the buttons panel
     */
    protected JPanel buttonsPanel=new JPanel();
    
    /**
     * the current action
     */
    protected int currentActionState=CANCEL_ACTION;
    
    /**
     * the panel of the content of the dialog
     */
    protected JPanel contentPanel=new JPanel(); 

    /**
     * the panel containing the widget used for specifying other parameters
     */
    protected JPanel parametersPanel=new JPanel();
    
    /**
     * the dimension of the exported image
     */
    protected Point2D.Double exportSize=new Point2D.Double(0, 0);
    
    /**
     * the set of the runnables that will be used to initialize the dialog each time it is shown
     */
    protected HashSet<Runnable> initializers=new HashSet<Runnable>();
    
    /**
     * the bundle
     */
    protected ResourceBundle bundle=null;
    
    /**
     * the constructor of the class
     * @param parent the parent frame
     */
    public ExportDialog(Frame parent) {
        
        super(parent, "", true);
    }
    
    /**
     * the constructor of the class
     * @param parent the parent dialog
     */
    public ExportDialog(JDialog parent) {
    	
        super(parent, "", true);
    }
    
    /**
     * initializes the dialog
     */
    protected void initialize(){
    	
        this.bundle=ResourcesManager.bundle;

        //getting the labels
        String okLabel="", cancelLabel="";
        
        if(bundle!=null){
            
            try{
                okLabel=bundle.getString("labelok");
                cancelLabel=bundle.getString("labelcancel");
            }catch (Exception ex){}
        }

        //the buttons
        final JButton okButton=new JButton(okLabel), cancelButton=new JButton(cancelLabel);
        
        //the buttons panel
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        //the listener to the ok and cancel buttons
        final ActionListener buttonListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent evt) {

                if(evt.getSource().equals(okButton)){
                    
                    currentActionState=OK_ACTION;
                    
                }else{
                    
                    currentActionState=CANCEL_ACTION;
                }
                
                setVisible(false);
            } 
        };
        
        okButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        //handling the panels
        contentPanel.setLayout(new BorderLayout(0, 5));
        contentPanel.add(parametersPanel, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * @return the size chooser panel
     */
    protected JPanel getSizeChooserPanel() {
        
        //getting the labels
        String heightLabel="", widthLabel="", exportSizeLabel="";
        
        if(bundle!=null){
            
            try{
                exportSizeLabel=bundle.getString("labelexportsize");
                widthLabel=bundle.getString("labelwidth");
                heightLabel=bundle.getString("labelheight");
            }catch (Exception ex){}
        }
        
        //the label widgets
        JLabel  lbw, lbh, pxw, pxh;
        
        JPanel panel=new JPanel();
        
        //creating the spinners and the spinner models
        final JSpinner widthSpinner=new JSpinner(), heightSpinner=new JSpinner();
        
        SpinnerNumberModel widthSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1);
        SpinnerNumberModel heightSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1);
        
        widthSpinner.setModel(widthSpinnerModel);
        heightSpinner.setModel(heightSpinnerModel);
        
        ((JSpinner.DefaultEditor)widthSpinner.getEditor()).getTextField().setColumns(5);
        ((JSpinner.DefaultEditor)heightSpinner.getEditor()).getTextField().setColumns(5);
        
        //setting the dimension of the spinner
        
        //setting the listeners to the spinners
        widthSpinner.addChangeListener(new ChangeListener(){

            public void stateChanged(ChangeEvent evt) {

                exportSize.x=((Double)widthSpinner.getValue()).doubleValue();
            }
        });
        
        heightSpinner.addChangeListener(new ChangeListener(){

            public void stateChanged(ChangeEvent evt) {

                exportSize.y=(int)((Double)heightSpinner.getValue()).doubleValue();
            }
        });
        
        //creating the labels
        lbw=new JLabel(widthLabel.concat(" :"));
        lbh=new JLabel(heightLabel.concat(" :"));
        pxw=new JLabel("px");
        pxh=new JLabel("px");
        
        JPanel spinnerWidthPanel=new JPanel();
        spinnerWidthPanel.setLayout(new BorderLayout(5, 0));
        spinnerWidthPanel.add(widthSpinner, BorderLayout.CENTER);
        spinnerWidthPanel.add(pxw, BorderLayout.EAST);

        JPanel spinnerHeightPanel=new JPanel();
        spinnerHeightPanel.setLayout(new BorderLayout(5, 0));
        spinnerHeightPanel.add(heightSpinner, BorderLayout.CENTER);
        spinnerHeightPanel.add(pxh, BorderLayout.EAST);
        
        //setting the layout
        GridBagLayout gridBag=new GridBagLayout();
        GridBagConstraints c=new GridBagConstraints();
        panel.setLayout(gridBag);
        c.fill=GridBagConstraints.NONE;
        c.insets=new Insets(3, 3, 3, 3);

        //adding the widgets to the panel
        c.anchor=GridBagConstraints.EAST;
        c.gridwidth=1;
        gridBag.setConstraints(lbw, c);
        panel.add(lbw);

        c.anchor=GridBagConstraints.WEST;
        c.gridwidth=GridBagConstraints.REMAINDER;
        gridBag.setConstraints(spinnerWidthPanel, c);
        panel.add(spinnerWidthPanel);
        
        c.anchor=GridBagConstraints.EAST;
        c.gridwidth=1;
        gridBag.setConstraints(lbh, c);
        panel.add(lbh);

        c.anchor=GridBagConstraints.WEST;
        c.gridwidth=GridBagConstraints.REMAINDER;
        gridBag.setConstraints(spinnerHeightPanel, c);
        panel.add(spinnerHeightPanel);
        
        //setting the border
        TitledBorder border=new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), exportSizeLabel);
        panel.setBorder(border);
        
        //adding the runnable to initialize this panel
        initializers.add(new Runnable() {
            
            public void run() {
                
                widthSpinner.setValue(exportSize.x);
                heightSpinner.setValue(exportSize.y);
            }
        });
        
        return panel;
    }

    /**
     * shows the dialog box to configure the export action
     * @param document a svg document
     * @return the state of the action : OK_ACTION or CANCEL_ACTION
     */
    public int showExportDialog(Document document){
        
        currentActionState=CANCEL_ACTION;
        
        if(document!=null ){
            
            //setting the default size for the image
            Point2D imageSize=SVGDocumentImageCreator.getGeometryCanvasSize(document);
            
            if(imageSize!=null) {
                
                exportSize=new Point2D.Double(imageSize.getX(), imageSize.getY());
                
                //initializing the dialog
                for(Runnable runnable : initializers) {
                    
                    runnable.run();
                }
            }

            pack();
            
            //sets the location of the dialog box
            int   x=(int)(Editor.getParent().getLocationOnScreen().getX()+Editor.getParent().getWidth()/2-getWidth()/2), 
                    y=(int)(Editor.getParent().getLocationOnScreen().getY()+Editor.getParent().getHeight()/2-getHeight()/2);
            
            setLocation(x,y);

            //displays the dialog box
            setVisible(true);

            //waits until the parameters are chosen and one of the buttons is clicked
            while(isVisible()){
                
                try{
                    wait((long)500.0);
                }catch (Exception ex){}
            }
        }

        return currentActionState;
    }

    /**
     * @return Returns the exportHeight.
     */
    public Point2D getExportSize() {
        return exportSize;
    }

    /**
     * @return Returns the exportDialogTitle.
     */
    public String getExportDialogTitle() {
        return exportDialogTitle;
    }

}
