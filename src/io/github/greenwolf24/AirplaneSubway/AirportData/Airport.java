package io.github.greenwolf24.AirplaneSubway.AirportData;

import java.util.ArrayList;

public class Airport
{
	public String ICAO;
	public String name;
	public String city;
	public String country;
	public double latitude;
	public double longitude;
	public int altitude;
	public ArrayList<Runway> runways;
	
	public Airport(String[] data, ArrayList<Runway> runways)
	{
		ICAO = data[1];
		name = data[3];
		city = data[10];
		country = data[8];
		latitude = Double.parseDouble(data[4]);
		longitude = Double.parseDouble(data[5]);
		try
		{
			altitude = Integer.parseInt(data[6]);
		}
		catch(Exception e){altitude = -54321;}
		this.runways = runways;
	}
	
	public String toString()
	{
		String ret = ICAO + " " + name + " " + city + " " + country + " " + latitude + " " + longitude + " " + altitude + "\n";
		for(Runway r : runways)
		{
			ret = ret + r.toString() + "\n";
		}
		return ret;
	}
}
