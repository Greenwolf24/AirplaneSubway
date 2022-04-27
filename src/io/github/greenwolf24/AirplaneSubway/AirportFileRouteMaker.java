package io.github.greenwolf24.AirplaneSubway;

import io.github.greenwolf24.AirplaneSubway.AirportData.Airport;
import io.github.greenwolf24.AirplaneSubway.AirportData.AirportGrabber;
import io.github.greenwolf24.KMLmanage.Maker.PathMaker;
import io.github.greenwolf24.KMLmanage.Util.Position;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class AirportFileRouteMaker
{
	// the purpose of this class is to take a file that is a list of airports and create a route between them
	// the file should be a list of airports, one per line, with the first airport listed first, and the last airport listed last
	public static void main(String[] args)
	{
		try
		{
			File file = new File("data/FlightNums.csv");
			ArrayList<String> airports = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new java.io.FileReader(file));
			String line;
			while ((line = br.readLine()) != null)
			{
				airports.add(line);
			}
			br.close();
			
			// now we have a list of airports
			// now we want to make a kml path between them
			// we start by creating an AirportGrabber object
			AirportGrabber grabber = new AirportGrabber(false);
			//grabber.loadFileToMemory();
			
			
			/*
			// ask the user what the base callsign is
			System.out.print("What is the base callsign?: ");
			String baseCallsign = new Scanner(System.in).nextLine();
			// ask the user what the base flightNumber is
			System.out.print("What is the base flightNumber?: ");
			String baseFlightNumber = new Scanner(System.in).nextLine();
			// ask the user how many digits to extend the number portions to be
			System.out.print("How many digits to extend the number portions to be?: ");
			int numDigits = new Scanner(System.in).nextInt();
			// ask the user what the first number will be
			// tell them the first and second airports
			System.out.println("The first airport is " + airports.get(0));
			System.out.println("The second airport is " + airports.get(1));
			System.out.print("What is the first number?: ");
			int firstNum = new Scanner(System.in).nextInt();
			//*/
			
			// now we have access to all the information we need to make the route
			ArrayList<io.github.greenwolf24.KMLmanage.Util.Position> positions = new ArrayList<>();
			for(int i = 0; i < airports.size(); i++)
			{
				String airportName = airports.get(i);
				Airport airport = grabber.getAirport(airportName);
				System.out.println("Adding " + airport.ICAO + " to the route");
				// the airport altitude is in feet, so we need to convert it to meters
				int altitude = (int)(airport.altitude * 0.3048);
				Position position = new Position(airport.latitude, airport.longitude, altitude);
				positions.add(position);
			}
			
			// now we have a list of positions
			// we need to make a kml route
			PathMaker pathMaker = new PathMaker("data/KMLout/");
			pathMaker.makePathLine(positions,"OutputPath",false);
			
			// we are done
			System.out.println("Done!");
			
			/*
			for(int i = 0; i < airports.size(); i++)
			{
				// if we are out of airports, we are done
				// we are out of airports if we are on the second to last airport
				if(i == airports.size() - 2)
				{
					break;
				}
				
			}
			//*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String extendNum(int num, int numDigits)
	{
		// this function will extend a number to the number of digits specified
		// for example, if num = 5 and numDigits = 3, it will return "005"
		// if num = 5 and numDigits = 2, it will return "05"
		// if num = 5 and numDigits = 1, it will return "5"
		// if num = 5 and numDigits = 0, it will return "5"
		
		String numStr = "" + num;
		int numLength = numStr.length();
		
		// if the number is less than the number of digits, we need to add zeroes to the beginning
		if (numLength < numDigits)
		{
			int numZeroes = numDigits - numLength;
			for (int i = 0; i < numZeroes; i++)
			{
				numStr = "0" + numStr;
			}
		}
		
		return numStr;
	}
}
