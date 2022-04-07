package Graph;

public class Edge
{
	// an edge is basically a pointer to another airnode
	// the edge has a distance to the other node
	// the edge has the airport code of the other node
	// the edge has a callsign and a flight number
	private String airportCode;
	private double distance;
	private String callsign;
	private String flightNumber;
	
	public Edge(String airportCode, double distance, String callsign, String flightNumber)
	{
		this.airportCode = airportCode;
		this.distance = distance;
		this.callsign = callsign;
		this.flightNumber = flightNumber;
	}
	
	public Edge(Object[] data)
	{
		this.airportCode = (String) data[0];
		this.distance = Double.parseDouble((String) data[1]);
		this.callsign = (String) data[2];
		this.flightNumber = (String) data[3];
	}
	
	public String getAirportCode()
	{
		return airportCode;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public String getCallsign()
	{
		return callsign;
	}
	
	public String getFlightNumber()
	{
		return flightNumber;
	}
	
	public String toString()
	{
		return callsign + " " + flightNumber + " " + airportCode + " " + distance;
	}
}
