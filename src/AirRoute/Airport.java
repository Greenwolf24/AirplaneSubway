package AirRoute;

import java.util.ArrayList;

public class Airport
{
	// An airport is simple.
	// It contains it's ICAO name, and a list of airports it can reach.
	public String ICAO;
	public ArrayList<String> reachable;
	
	// Constructor
	public Airport(String ICAO)
	{
		this.ICAO = ICAO;
		this.reachable = new ArrayList<String>();
	}
	
	// Add an airport to the list of reachable airports
	public void addReachable(String ICAO)
	{
		this.reachable.add(ICAO);
	}
	
	// Returns true if the given airport is reachable from this airport
	public boolean isReachable(String ICAO)
	{
		return this.reachable.contains(ICAO);
	}
}
