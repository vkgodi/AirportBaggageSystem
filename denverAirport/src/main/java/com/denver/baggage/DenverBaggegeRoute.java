package com.denver.baggage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.denver.baggage.exception.BaggageRouteException;
import com.denver.baggage.model.Edge;
import com.denver.baggage.model.Graph;
import com.denver.baggage.model.Vertex;

/**
 * @author vinod
 *
 */
public class DenverBaggegeRoute {

	final static Logger logger = Logger.getLogger(DenverBaggegeRoute.class);

	private final List<Edge> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Vertex> predecessors;
	private Map<Vertex, Integer> distance;

	public DenverBaggegeRoute(Graph graph) {

		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(Vertex source) {

		logger.info("DenverBaggegeRoute::execute() started to excute the Automate Baggage route :" + source);

		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}

		logger.info(
				"DenverBaggegeRoute::execute() successfully executed and found the shortest distance between the nodes.");
	}

	private void findMinimalDistances(Vertex node) {

		List<Vertex> adjacentNodes = getNeighbors(node);

		try {

			if (adjacentNodes != null) {

				for (Vertex target : adjacentNodes) {

					if (getShortestDistance(target) > getShortestDistance(node) + getBaggageTravelTime(node, target)) {

						distance.put(target, getShortestDistance(node) + getBaggageTravelTime(node, target));

						predecessors.put(target, node);

						unSettledNodes.add(target);
					}
				}

			}
		} catch (Exception ex) {

			logger.error("Unable to traverse and routes doesn't exists between source and destination gate."
					+ ex.getMessage());

			throw new BaggageRouteException(ex.getMessage());
		}

	}

	private int getBaggageTravelTime(Vertex node, Vertex target) {

		for (Edge edge : edges) {

			if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {

				return edge.getTravelTime();
			}

		}

		throw new BaggageRouteException(
				"Unable to traverse and routes doesn't exists between source and destination gate.");
	}

	private List<Vertex> getNeighbors(Vertex node) {

		List<Vertex> neighbors = new ArrayList<Vertex>();

		for (Edge edge : edges) {

			if (edge.getSource().equals(node) && !isSettled(edge.getDestination())) {

				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertex getMinimum(Set<Vertex> vertexes) {

		logger.info("DenverBaggegeRoute::getMinimum() started getMinimum method to find the minimum path :" + vertexes);

		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Vertex destination) {

		Integer shortestDistance = distance.get(destination);
		if (shortestDistance == null) {
			return Integer.MAX_VALUE;
		} else {
			return shortestDistance;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public Map<Integer, LinkedList<Vertex>> getPath(Vertex target) {

		logger.info("DenverBaggegeRoute::getPath () started to get the path from the source to the selected target :"
				+ target);

		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		int totalSum = 0;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			totalSum += getBaggageTravelTime(step, predecessors.get(step));
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		Map<Integer, LinkedList<Vertex>> map = new HashMap<>();
		map.put(Integer.valueOf(totalSum), path);

		logger.info("This method returned the path from the source to the selected target :" + map);

		return map;
	}
}
