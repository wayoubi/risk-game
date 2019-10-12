package ca.concordia.app.risk.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;


//@example:full:begin
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxCellEditor;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxStylesheet;

import ca.concordia.app.risk.model.cache.RunningGame;
import ca.concordia.app.risk.model.dao.ContinentDaoImpl;
import ca.concordia.app.risk.model.dao.CountryDaoImpl;
import ca.concordia.app.risk.model.dao.PlayerDaoImpl;
import ca.concordia.app.risk.model.xmlbeans.CountryModel;
import ca.concordia.app.risk.model.xmlbeans.GameModel;
import ca.concordia.app.risk.services.GameService;

/**
 * A demo applet that shows how to use JGraphX to visualize JGraphT graphs.
 * Applet based on JGraphAdapterDemo.
 *
 */
public class JGraphXAdapterView extends JApplet {
	private static final long serialVersionUID = 2202072534703043194L;

	private static final Dimension DEFAULT_SIZE = new Dimension(550, 420);

	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;

	/**
	 * An alternative starting point for this demo, to also allow running this
	 * applet as an application.
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
		frame.setLocationByPlatform(true);
		
		//Size of the frame
		frame.setPreferredSize(DEFAULT_SIZE);
		
		//Shows window in the middle of the screen
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	@Override
	public void init() {
		ListenableGraph<String, DefaultEdge> g = new DefaultListenableGraph<>(RunningGame.getInstance().getGraph());
		
		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<>(g);

		jgxAdapter.selectAll();
        Object[] cells = jgxAdapter.getSelectionCells();


        jgxAdapter.clearSelection(); 
        
		jgxAdapter.setLabelsVisible(true);
		
		jgxAdapter.setAutoSizeCells(true);
		jgxAdapter.alignCells(mxConstants.ALIGN_CENTER);
		
		mxStylesheet stylesheet = jgxAdapter.getStylesheet();

        
		//Make vertices style
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_ROUNDED, true);
		
		//Coloring the Vertices
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_FILLCOLOR, Color.YELLOW.getRGB());
		
		//Let's do add shadow
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_GRADIENTCOLOR, "#A9C4EB");
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SHADOW, true);
		
		
		//Add styles to the adapter
		jgxAdapter.setStylesheet(stylesheet);
		jgxAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_DASHED, "1");
		
		
		mxGraphComponent component = new mxGraphComponent(jgxAdapter);

		//Prevents drawing edges from user side
		component.getGraph().setAllowDanglingEdges(false);
		component.getGraph().alignCells("center");
		getContentPane().add(component);

		
	    // Iterate graph cells
        for (Object c : cells) {
            mxCell cell = (mxCell) c;
            mxGeometry geometry = cell.getGeometry();

            if (cell.isVertex()) {
                // Here I can change vertex dimensions 
                geometry.setWidth(120);
                geometry.setHeight(65);
                String countryName = (String)cell.getValue();
                
                //Show Vertice label
                CountryDaoImpl countryDaoImpl = new CountryDaoImpl();
                CountryModel countryModel = countryDaoImpl.findByName(RunningGame.getInstance(), countryName);
                ContinentDaoImpl continentDaoImpl = new ContinentDaoImpl();
                String continentName = continentDaoImpl.findById(RunningGame.getInstance(), countryModel.getContinentId()).getName(); 
                String verticeLabel = String.format("Country: %s \n Continent: %s \n Number of Armies: %s \n",
                		countryModel.getName(), continentName,countryModel.getNumberOfArmies());
                
                
                cell.setValue(verticeLabel);
            }
        } 
		
		//Setting for Vertices
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_AUTOSIZE, 1);
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		
		//Make graph indercted
		stylesheet.getDefaultEdgeStyle().put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
		stylesheet.getDefaultEdgeStyle().put(mxConstants.STYLE_STARTARROW, mxConstants.NONE);
		
		//Setting alignment of text for Vertices
		//stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SPACING_BOTTOM, 5);
		//stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SPACING_LEFT, 5);
		//stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SPACING_RIGHT, 5);
		//stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SPACING_TOP, 5);

		//Positioning
		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
	
		layout.execute(jgxAdapter.getDefaultParent());
		// that's all there is to it!...
	}
	
}
