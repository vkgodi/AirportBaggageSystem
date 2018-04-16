package com.denver.baggage.model;

public class Edge {
	private final String id;
	private final Vertex source;
	private final Vertex destination;
	private final int travelTime;

	/**
	 * @param id 
	 * @param source
	 * @param destination
	 * @param travelTime
	 */
	public Edge(String id, Vertex source, Vertex destination, int travelTime) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.travelTime = travelTime;
	}

	public String getId() {
		return id;
	}

	public Vertex getDestination() {
		return destination;
	}

	public Vertex getSource() {
		return source;
	}

	public int getTravelTime() {
		return travelTime;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}

}
