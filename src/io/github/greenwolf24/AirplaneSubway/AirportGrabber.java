package io.github.greenwolf24.AirplaneSubway;

import io.github.greenwolf24.AirplaneSubway.AirportData.Runway;

import java.util.ArrayList;
import java.util.Scanner;

public class AirportGrabber
{
	//The program will ask the user for the airport ICAO code
	// The program will then check the CSV file for the airport
	// The program will then create a new airport object
	// The program will also check a second CSV file for the airport's runways
	// The program will then create a new runway object for each runway
	
	public static void main(String[] args)
	{
		// ask user for airport ICAO code
		String airportCode = new Scanner(System.in).nextLine();
		String[] airportInfo = getAirportFromCSV(airportCode);
		if(airportInfo == null)
		{
			System.out.println("Airport not found");
		}
		else
		{
			// grab runways
			ArrayList<String[]> runways = getRunwaysFromCSV(airportCode);
			// for each runway, create a new runway object
			for(String[] runway : runways)
			{
				Runway r1 = new Runway();
				r1.name = runway[8];
				r1.length = Integer.parseInt(runway[3]);
				r1.surface = runway[5];
				r1.heading = headingFromName(r1.name);
				Runway r2 = new Runway();
				
			}
		}
	}
	
	private static int headingFromName(String name)
	{
		// a runway name contains a heading, but sometimes also contains a letter
		// this method will return the heading by simply removing non-numeric characters
		// if the runway name is not in the correct format, return 0
		String heading = "";
		for(int i = 0; i < name.length(); i++)
		{
			if(Character.isDigit(name.charAt(i)))
			{
				heading += name.charAt(i);
			}
		}
		if(heading.length() == 0)
		{
			return 0;
		}
		return Integer.parseInt(heading);
	}
	
	private static String[] getAirportFromCSV(String airportCode)
	{
		// check the CSV file for the airport
		// if the airport is not found, return null
		String[][] airportFile = CSVFile.getFile("data/airports.csv");
		for(int i = 0; i < airportFile.length; i++)
		{
			if(airportFile[i][1].equals(airportCode))
			{
				return airportFile[i];
			}
		}
		return null;
	}
	
	public static ArrayList<String[]> getRunwaysFromCSV(String airportCode)
	{
		// check the CSV file for the airport's runways
		// if the airport has runways, return the runways
		String[][] runwaysFile = CSVFile.getFile("data/runways.csv");
		ArrayList<String[]> runways = new ArrayList<>();
		for(int i = 0; i < runwaysFile.length; i++)
		{
			if(runwaysFile[i][0].equals(airportCode))
			{
				runways.add(runwaysFile[i]);
			}
		}
		return runways;
	}
}
