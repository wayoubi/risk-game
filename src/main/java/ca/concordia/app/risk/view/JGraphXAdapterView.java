package ca.concordia.app.risk.view;

import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;

//@example:full:begin
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;

import ca.concordia.app.risk.model.cache.RunningGame;

/**
 * A demo applet that shows how to use JGraphX to visualize JGraphT graphs.
 * Applet based on JGraphAdapterDemo.
 *
 */
public class JGraphXAdapterView extends JApplet {
	private static final long serialVersionUID = 2202072534703043194L;

	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

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
	}

	@Override
	public void init() {
		ListenableGraph<String, DefaultEdge> g = new DefaultListenableGraph<>(RunningGame.getInstance().getGraph());

		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<>(g);
		jgxAdapter.setLabelsVisible(true);
		
		setPreferredSize(DEFAULT_SIZE);
		mxGraphComponent component = new mxGraphComponent(jgxAdapter);
		component.setConnectable(false);
		component.getGraph().setAllowDanglingEdges(false);
		
		getContentPane().add(component);
		resize(DEFAULT_SIZE);

		// positioning via jgraphx layouts
		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);

		// center the circle
		int radius = 100;
		layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
		layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
		layout.setRadius(radius);
		layout.setMoveCircle(true);

		layout.execute(jgxAdapter.getDefaultParent());
		// that's all there is to it!...
	}
}
