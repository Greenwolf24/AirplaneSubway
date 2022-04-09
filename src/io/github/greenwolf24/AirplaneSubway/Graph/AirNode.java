package io.github.greenwolf24.AirplaneSubway.Graph;

public class AirNode
{
	// A node is an object with an airport code and edges to other nodes
	// each edge has a distance to the other node
	// each edge also has a callsign for the flight
	// each edge also has a flight number
	private String airportCode;
	private Edge[] edges;
	
	public AirNode(String airportCode)
	{
		this.airportCode = airportCode;
		edges = new Edge[0];
	}
	
	public String getAirportCode()
	{
		return airportCode;
	}
	
	public void addEdge(Edge e)
	{
		Edge[] temp = new Edge[edges.length + 1];
		for (int i = 0; i < edges.length; i++)
		{
			temp[i] = edges[i];
		}
		temp[edges.length] = e;
		edges = temp;
	}
	
	public Edge[] getEdges()
	{
		return edges;
	}
	
	public Edge getEdge(String callsign)
	{
		for (int i = 0; i < edges.length; i++)
		{
			if (edges[i].getCallsign().equals(callsign))
			{
				return edges[i];
			}
		}
		return null;
	}
	
	public String toString()
	{
		String s = "";
		for (int i = 0; i < edges.length; i++)
		{
			s += edges[i].toString() + "\n";
		}
		return s;
	}
	
	public boolean equals(AirNode other)
	{
		return airportCode.equals(other.getAirportCode());
	}
}
