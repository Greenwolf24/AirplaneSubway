package io.github.greenwolf24.AirplaneSubway.AirRoute;

import java.util.LinkedHashMap;

public class Airport2
{
	// An airport is simple.
	// It contains it's ICAO name, and a list of airports it can reach.
	public String ICAO;
	public LinkedHashMap<String,Double> reachable;
	public LinkedHashMap<String,String> callsigns;
	public LinkedHashMap<String,String> flightNumbers;
	
	// Constructor
	public Airport2(String ICAO)
	{
		this.ICAO = ICAO;
		this.reachable = new LinkedHashMap<>();
		this.callsigns = new LinkedHashMap<>();
		this.flightNumbers = new LinkedHashMap<>();
	}
	
	// Add an airport to the list of reachable airports
	public void addReachable(String ICAO, double distance,String callsign,String flightNumber)
	{
		this.reachable.put(ICAO, distance);
		this.callsigns.put(ICAO, callsign);
		this.flightNumbers.put(ICAO, flightNumber);
	}
	
	// Returns true if the given airport is reachable from this airport
	public boolean isReachable(String ICAO)
	{
		return this.reachable.containsKey(ICAO);
	}
}
