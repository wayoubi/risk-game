package ca.concordia.app.risk.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.PlayerModel;

/**
 * Show map graph as applet
 *
 */
public class JGraphXAdapterView extends JApplet {

	private static final long serialVersionUID = 2202072534703043194L;
	private static final Dimension DEFAULT_SIZE = new Dimension(550, 420);
	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;

	/**
	 *
	 * Main method, can be used to start stand alone applet in case of testing
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		JGraphXAdapterView applet = new JGraphXAdapterView();
		applet.init();

		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle(".:: RiskGame - Show Current Game Map");
		frame.pack();
		frame.setVisible(true);
		frame.setPreferredSize(DEFAULT_SIZE);
		frame.setLocationRelativeTo(null);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Overrides the JApplet init method.
	 */
	@Override
	public void init() {
		ListenableGraph<String, DefaultEdge> graph = new DefaultListenableGraph<>(RunningGame.getInstance().getGraph());
		jgxAdapter = new JGraphXAdapter<>(graph);

		jgxAdapter.selectAll();
		Object[] cells = jgxAdapter.getSelectionCells();
		jgxAdapter.clearSelection();
		jgxAdapter.setLabelsVisible(true);
		jgxAdapter.setAutoSizeCells(true);
		jgxAdapter.alignCells(mxConstants.ALIGN_CENTER);

		mxStylesheet stylesheet = setGraphStyles(jgxAdapter.getStylesheet());
		jgxAdapter.setStylesheet(stylesheet);

		mxGraphComponent component = new mxGraphComponent(jgxAdapter);
		component.getGraph().setAllowDanglingEdges(false);
		component.getGraph().alignCells("center");
		getContentPane().add(component);

		for (Object c : cells) {
			mxCell cell = (mxCell) c;
			mxGeometry geometry = cell.getGeometry();

			if (cell.isVertex()) {
				// Vertex dimensions
				geometry.setWidth(120);
				geometry.setHeight(65);
				String countryName = (String) cell.getValue();

				// Show vertice label
				CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
				CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
				ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();

				String continentName = continentDaoImpl
						.findById(RunningGame.getInstance(), countryModel.getContinentId()).getName();
				String verticeLabel = String.format("Country: %s \n Continent: %s \n No of Armies: %s \n",
						countryModel.getName(), continentName, countryModel.getNumberOfArmies());

				if (!RunningGame.getInstance().equals(null)) {
					PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();

					if (countryModel.getPlayerId() > 0) {
						PlayerModel player = playerDaoImpl.findById(RunningGame.getInstance(),
								countryModel.getPlayerId());
						String color = setCustomizedColor(player.getColor().toLowerCase());
						component.getGraph().setCellStyle("fillColor=" + color, new Object[] { cell });
						// component.getGraph().setCellStyle("fontColor=#FFFFFF", obj);
						// component.getGraph().setCellStyles("fontColor", "#FFFFFF");
						// component.refresh();
						verticeLabel += String.format(" Player: %s", player.getName());
					}
				}
				// cell.setStyle("fontColor=#FFFFFF");
				cell.setValue(verticeLabel);
			}
		}

		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());

	}

	/**
	 * This method customize color of the vertices
	 *
	 * @param color color of the player
	 * @return customized color
	 */
	private String setCustomizedColor(String color) {
		Color mycolor = Color.getColor("red");
		switch (color) {
		case "red":
			mycolor = Color.pink;
			break;
		case "green":
			mycolor = Color.cyan;
			break;
		case "blue":
			mycolor = Color.orange;
			break;
		case "gray":
			mycolor = Color.gray;
			break;
		case "yellow":
			mycolor = Color.yellow;
			break;
		default:
			mycolor = Color.LIGHT_GRAY;
			break;
		}
		String result = "#" + Integer.toHexString(mycolor.getRGB() & 0xffffff);

		return result;
	}

	/**
	 * This method adds some style to the graph
	 *
	 * @param styleSheet style sheet
	 * @return styleSheet
	 */
	private mxStylesheet setGraphStyles(mxStylesheet styleSheet) {
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_ROUNDED, true);
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_FILLCOLOR, Color.YELLOW.getRGB());
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_GRADIENTCOLOR, "#A9C4EB");
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_SHADOW, true);
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_AUTOSIZE, 1);
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		styleSheet.getDefaultVertexStyle().put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		styleSheet.getDefaultEdgeStyle().put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
		styleSheet.getDefaultEdgeStyle().put(mxConstants.STYLE_STARTARROW, mxConstants.NONE);
		styleSheet.getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
		styleSheet.getDefaultEdgeStyle().put(mxConstants.STYLE_DASHED, "1");
		styleSheet.getDefaultEdgeStyle().put(mxConstants.STYLE_SWIMLANE_FILLCOLOR, Color.white);

		return styleSheet;
	}

}
