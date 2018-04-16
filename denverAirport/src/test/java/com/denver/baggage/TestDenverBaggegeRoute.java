package com.denver.baggage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.denver.baggage.model.Edge;
import com.denver.baggage.model.Graph;
import com.denver.baggage.model.Vertex;

public class TestDenverBaggegeRoute {

	private List<Vertex> nodes;
	private List<Edge> edges;
	private Graph graph;
	private DenverBaggegeRoute autoRoute;
	private Map<String, Vertex[]> bags;
	private Map<String, Vertex> departures;

	@Before
	public void setUp() {

		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();

		// Add Vertexes
		for (int i = 0; i <= 11; i++) {
			if (i == 0) {
				Vertex location = new Vertex("Concourse_A_Ticketing", "Concourse_A_Ticketing");
				nodes.add(location);
			} else {
				if (i == 11) {
					Vertex location = new Vertex("BaggageClaim", "BaggageClaim");
					nodes.add(location);
				} else {
					Vertex location = new Vertex("A" + i, "A" + i);
					nodes.add(location);
				}
			}
		}

		// Add Edges
		addLane("Edge0", 0, 5, 5);
		addLane("Edge1", 5, 11, 5);
		addLane("Edge2", 5, 10, 4);
		addLane("Edge3", 5, 1, 6);
		addLane("Edge4", 1, 2, 1);
		addLane("Edge5", 2, 3, 1);
		addLane("Edge6", 3, 4, 1);
		addLane("Edge7", 10, 9, 1);
		addLane("Edge8", 9, 8, 1);
		addLane("Edge9", 8, 7, 1);
		addLane("Edge10", 7, 6, 1);

		// Lets check from location Loc_1 to Loc_10
		graph = new Graph(nodes, edges);
		autoRoute = new DenverBaggegeRoute(graph);

		departures = new HashMap<>();
		departures.put("UA11", nodes.get(1));
		departures.put("UA12", nodes.get(1));
		departures.put("UA13", nodes.get(2));
		departures.put("UA14", nodes.get(2));
		departures.put("UA10", nodes.get(1));
		departures.put("UA15", nodes.get(2));
		departures.put("UA16", nodes.get(3));
		departures.put("UA17", nodes.get(4));
		departures.put("UA18", nodes.get(5));
		departures.put("ARRIVAL", nodes.get(11));

		bags = new LinkedHashMap<>();
		bags.put("0001", new Vertex[] { nodes.get(0), departures.get("UA12") });
		bags.put("0002", new Vertex[] { nodes.get(5), departures.get("UA17") });
		bags.put("0003", new Vertex[] { nodes.get(2), departures.get("UA10") });
		bags.put("0004", new Vertex[] { nodes.get(8), departures.get("UA18") });
		bags.put("0005", new Vertex[] { nodes.get(7), departures.get("ARRIVAL") });

	}

	@Test
	public void getPathDistance() {

		for (Map.Entry<String, Vertex[]> bag : bags.entrySet()) {
			String bagName = bag.getKey();
			Vertex[] vertexes = bag.getValue();
			autoRoute.execute(vertexes[0]);
			Map<Integer, LinkedList<Vertex>> path = autoRoute.getPath(vertexes[1]);

			assertNotNull(path);

			assertTrue(path.size() > 0);

			for (Map.Entry<Integer, LinkedList<Vertex>> pathDistance : path.entrySet()) {
				System.out.println(bagName + "\t" + pathDistance.getValue().toString() + "\t" + pathDistance.getKey());
			}
		}

	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {

		// Bidirectional Edges
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);

		lane = new Edge(laneId, nodes.get(destLocNo), nodes.get(sourceLocNo), duration);
		edges.add(lane);
	}

}
