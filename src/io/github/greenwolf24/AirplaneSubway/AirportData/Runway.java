package io.github.greenwolf24.AirplaneSubway.AirportData;

public class Runway
{
	public String name;
	public int heading;
	public int length; // feet
	public String surface;
	
	public String toString()
	{
		return name + " " + heading + " " + length + " " + surface;
	}
}
