package ca.concordia.app.risk.model.cache;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import ca.concordia.app.risk.model.xmlbeans.GameModel;

/**
 * 
 * @author i857625
 *
 */
public class RunningGame extends GameModel {
	
	/**
	 * 
	 */
	private static RunningGame runningGame;

	/**
	 * 
	 */
	private Graph<String, DefaultEdge> graph;


	/**
	 * 
	 */
	private RunningGame() { 
		super();
		graph = GraphTypeBuilder
	            .<String, DefaultEdge> undirected().allowingMultipleEdges(false)
	            .allowingSelfLoops(false).edgeClass(DefaultEdge.class).weighted(false).buildGraph();
	}
	
	/**
	 * 
	 * @return
	 */
	public static RunningGame getInstance() {
		if(runningGame == null) {
			runningGame = new RunningGame();
		}
		return runningGame;
	}
	
	/**
	 * 
	 */
	public static void reset() {
		runningGame = new RunningGame();
	}
	
	/**
	 * 
	 * @return
	 */
	public Graph<String, DefaultEdge> getGraph() {
		return this.graph;
	}

}
