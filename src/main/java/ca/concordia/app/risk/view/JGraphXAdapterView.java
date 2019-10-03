package ca.concordia.app.risk.view;

import java.awt.Color;
import java.awt.Dimension;

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
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;

import ca.concordia.app.risk.model.cache.RunningGame;

/**
 * A demo applet that shows how to use JGraphX to visualize JGraphT graphs.
 * Applet based on JGraphAdapterDemo.
 *
 */
public class JGraphXAdapterView extends JApplet {
	private static final long serialVersionUID = 2202072534703043194L;

	private static final Dimension DEFAULT_SIZE = new Dimension(530, 420);

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
        Object[] cells = jgxAdapter.getSelectionCells(); //here you have all cells


        jgxAdapter.clearSelection(); 
        
		jgxAdapter.setLabelsVisible(true);
		
		jgxAdapter.setAutoSizeCells(true);
		jgxAdapter.alignCells(mxConstants.ALIGN_CENTER);
		
		mxStylesheet stylesheet = jgxAdapter.getStylesheet();
		
		
		//Make vertices style -> rounded instead of hard rectangle
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_ROUNDED, true);
		//stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		//stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_FONTCOLOR, Color.WHITE);
		
		//Coloring the Vertices
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_FILLCOLOR, Color.YELLOW.getRGB());
		//Let's do add shadow
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_GRADIENTCOLOR, "#A9C4EB");
		stylesheet.getDefaultVertexStyle().put(mxConstants.STYLE_SHADOW, true);
		


		
		jgxAdapter.setStylesheet(stylesheet);
		jgxAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_DASHED, "1");
		

		//setPreferredSize(DEFAULT_SIZE);
		mxGraphComponent component = new mxGraphComponent(jgxAdapter);
	 
		
		//Perevents drawing edges from user side
		component.getGraph().setAllowDanglingEdges(false);
		component.getGraph().alignCells("center");
		
		//component.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR,"red");
		
		//component.getGraph().setStylesheet(stylesheet);
		
		//set vertices color
		//component.getGraph().setCellStyles(mxConstants.STYLE_FILLCOLOR, "red");
		
		getContentPane().add(component);
		resize(DEFAULT_SIZE);

		// positioning via jgraphx layouts
		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);

		// center the circle
		int radius = 170;
		layout.setX0((DEFAULT_SIZE.width / 1.5) - radius);
		layout.setY0((DEFAULT_SIZE.height / 1.5) - radius);
		layout.setRadius(radius);
		layout.setMoveCircle(true);

		
		layout.execute(jgxAdapter.getDefaultParent());
		// that's all there is to it!...
	}
	
}
