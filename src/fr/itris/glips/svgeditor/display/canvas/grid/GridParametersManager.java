package fr.itris.glips.svgeditor.display.canvas.grid;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class handling the grid parameters
 * 
 * @author Jordi SUC
 */
public class GridParametersManager {

	/**
	 * the id for the grid enablement preference
	 */
	private static final String GRID_ENABLED_PREF_ID = "GridEnabled";

	/**
	 * the id for the horizontal distance preference
	 */
	private static final String HORIZONTAL_DISTANCE_PREF_ID = "GridHorizontalDistance";

	/**
	 * the id for the vertical distance preference
	 */
	private static final String VERTICAL_DISTANCE_PREF_ID = "GridVerticalDistance";

	/**
	 * the id for the color preference
	 */
	private static final String COLOR_PREF_ID = "GridColor";

	/**
	 * the id for the stroke preference
	 */
	private static final String STROKE_PREF_ID = "GridStroke";

	/**
	 * the grid parameters dialog
	 */
	private GridParametersDialog gridParametersDialog;

	/**
	 * the default value for the grid enabled boolean
	 */
	protected static final boolean defaultGridEnabled = false;

	/**
	 * the default distances for the grid
	 */
	protected static final double defaultHorizontalDistance = 50, defaultVerticalDistance = 50;

	/**
	 * the default color of the grid
	 */
	protected static final Color defaultGridColor = new Color(200, 200, 200);

	/**
	 * the default stroke
	 */
	protected static final BasicStroke defaultGridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL, 0, new float[] { 2, 3 }, 0);

	/**
	 * the default string representation of the dashes for the stroke
	 */
	protected static final String defaultStrokeDashesValues = "2 3";

	/**
	 * the handles manager
	 */
	private HandlesManager handlesManager;

	/**
	 * whether the grid is enabled or not
	 */
	private boolean gridEnabled = true;

	/**
	 * the horizontal distance for the grid
	 */
	private double horizontalDistance = defaultHorizontalDistance;

	/**
	 * the vertical distance for the grid
	 */
	private double verticalDistance = defaultVerticalDistance;

	/**
	 * the color of the grid
	 */
	private Color gridColor = defaultGridColor;

	/**
	 * the stroke of the grid
	 */
	private BasicStroke gridStroke = defaultGridStroke;

	/**
	 * the string representation of the dashes for the stroke
	 */
	private String strokeDashesValues = "2 3";

	/**
	 * the constructor of the class
	 * 
	 * @param handlesManager
	 *          the handles manager
	 */
	public GridParametersManager(HandlesManager handlesManager) {

		this.handlesManager = handlesManager;

		initializeParameters();

		// creating the dialog used for setting the parameters
		if (Editor.getParent() instanceof Frame) {

			gridParametersDialog = new GridParametersDialog(this, (Frame) Editor.getParent());

		} else if (Editor.getParent() instanceof JDialog) {

			gridParametersDialog = new GridParametersDialog(this, (JDialog) Editor.getParent());
		}
	}

	/**
	 * initializes the grid parameters
	 */
	protected void initializeParameters() {

		// getting the parameters from the preference store
		gridEnabled = Boolean.parseBoolean(PreferencesStore.getPreference(null, GRID_ENABLED_PREF_ID));

		try {
			String horizontalDistanceString = PreferencesStore.getPreference(null,
					HORIZONTAL_DISTANCE_PREF_ID);
			if (horizontalDistanceString != null) {
				horizontalDistance = Double.parseDouble(horizontalDistanceString);
			} else {
				horizontalDistance = defaultHorizontalDistance;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			horizontalDistance = defaultHorizontalDistance;
		}

		try {
			String verticalDistanceString = PreferencesStore.getPreference(null,
					VERTICAL_DISTANCE_PREF_ID);
			if (verticalDistanceString != null) {
				verticalDistance = Double.parseDouble(verticalDistanceString);
			} else {
				verticalDistance = defaultVerticalDistance;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			verticalDistance = defaultVerticalDistance;
		}

		gridColor = Editor.getColorChooser().getColor(null,
				PreferencesStore.getPreference(null, COLOR_PREF_ID));

		if (gridColor == null) {

			gridColor = defaultGridColor;
		}

		// computing the grid stroke
		String dashesStr = PreferencesStore.getPreference(null, STROKE_PREF_ID);
		handleDashes(dashesStr);
	}

	/**
	 * creates the stroke corresponding to the provided dashes string
	 * 
	 * @param dashesString
	 *          the string representation of dashes
	 */
	protected void handleDashes(String dashesString) {

		if (dashesString != null) {

			// getting the array of the dash factors
			float[] dashes = DashChooserWidget.getDashes(dashesString);

			if (dashes.length > 0) {

				strokeDashesValues = dashesString;
				gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashes, 0);

			} else {

				gridStroke = null;
				strokeDashesValues = "";
			}

		} else {

			gridStroke = defaultGridStroke;
			strokeDashesValues = defaultStrokeDashesValues;
		}
	}

	/**
	 * launches the dialog used to modify the parameters
	 * 
	 * @param relativeComponent
	 *          the component relatively to which the dialog should be shown
	 */
	public void launchDialog(JComponent relativeComponent) {

		gridParametersDialog.showDialog(relativeComponent);

		if (gridParametersDialog.isCorrectValues()) {

			// updating the parameters of the grid
			double hDist = gridParametersDialog.getHorizontalDistance();
			double vDist = gridParametersDialog.getVerticalDistance();
			Color color = gridParametersDialog.getColor();
			String dashesStr = gridParametersDialog.getDashes();

			if (hDist > 0 && vDist > 0 && color != null) {

				horizontalDistance = hDist;
				verticalDistance = vDist;
				gridColor = color;

				if (dashesStr == null) {

					dashesStr = "";
				}

				handleDashes(dashesStr);
				updateGrids();
			}
		}
	}

	/**
	 * provides all the grids with their new parameters
	 */
	protected void updateGrids() {

		for (SVGHandle handle : handlesManager.getHandles()) {

			handle.getCanvas().getGridManager().refresh();
		}

		updatePreferences();
	}

	/**
	 * updates the preferences values
	 */
	protected void updatePreferences() {

		PreferencesStore.setPreference(null, GRID_ENABLED_PREF_ID, Boolean.toString(gridEnabled));

		PreferencesStore.setPreference(null, HORIZONTAL_DISTANCE_PREF_ID,
				FormatStore.format(horizontalDistance));

		PreferencesStore.setPreference(null, VERTICAL_DISTANCE_PREF_ID,
				FormatStore.format(verticalDistance));

		PreferencesStore.setPreference(null, COLOR_PREF_ID,
				Editor.getColorChooser().getColorString(gridColor));

		PreferencesStore.setPreference(null, STROKE_PREF_ID, strokeDashesValues);
	}

	/**
	 * @return whether the grid should be displayed
	 */
	public boolean isGridEnabled() {

		return gridEnabled;
	}

	/**
	 * sets whether the grid should be displayed
	 * 
	 * @param gridEnabled
	 *          whether the grid should be displayed
	 */
	public void setGridEnabled(boolean gridEnabled) {

		this.gridEnabled = gridEnabled;
		updateGrids();
	}

	/**
	 * @return the grid color
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * @return the grid stroke
	 */
	public BasicStroke getGridStroke() {
		return gridStroke;
	}

	/**
	 * @return the string representation of the dashes for the stroke
	 */
	public String getStrokeDashesValues() {
		return strokeDashesValues;
	}

	/**
	 * @return the horizontal distance
	 */
	public double getHorizontalDistance() {
		return horizontalDistance;
	}

	/**
	 * @return the vertical distance
	 */
	public double getVerticalDistance() {
		return verticalDistance;
	}
}
