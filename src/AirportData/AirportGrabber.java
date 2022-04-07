package AirportData;

import java.io.File;
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
		System.out.print("Enter the airport ICAO code: ");
		String airportCode = new Scanner(System.in).nextLine();
		Airport airport = getAirport(airportCode);
		if(airport != null)
		{
			System.out.println(airport);
		}
		else
		{
			System.out.println("Airport not found");
		}
	}
	
	public static Airport getAirport(String airportCode)
	{
		// ask user for airport ICAO code
		//System.out.print("Enter the airport ICAO code: ");
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
			// add the runway object to an array list
			ArrayList<Runway> airportRunways = new ArrayList<>();
			for(String[] runway : runways)
			{
				Runway r1 = new Runway();
				r1.name = runway[8];
				r1.length = Integer.parseInt(runway[3]);
				r1.surface = runway[5];
				r1.heading = headingFromName(r1.name) * 10;
				Runway r2 = new Runway();
				r2.name = runway[14];
				r2.length = Integer.parseInt(runway[3]);
				r2.surface = runway[5];
				r2.heading = headingFromName(r2.name) * 10;
				airportRunways.add(r1);
				airportRunways.add(r2);
			}
			// create a new airport object
			AirportData.Airport airport = new AirportData.Airport(airportInfo,airportRunways);
			//System.out.println(airport);
			return airport;
		}
		return null;
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
		String[][] airportFile = CSVFile.getFile("data/AirportDataDatabase/airports.csv");
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
		String[][] runwaysFile = CSVFile.getFile("data/AirportDataDatabase/runways.csv");
		ArrayList<String[]> runways = new ArrayList<>();
		for(int i = 0; i < runwaysFile.length; i++)
		{
			if(runwaysFile[i][2].equals(airportCode))
			{
				runways.add(runwaysFile[i]);
			}
		}
		return runways;
	}
	
	public static class CSVFile
	{
		public static String[][] getFile(String fileName)
		{
			File file = new File(fileName);
			ArrayList<String> lines = readFileLines(file);
			return prepareLines(lines);
		}
		
		private static String[][] prepareLines(ArrayList<String> lines)
		{
			String[][] linesArray = new String[lines.size()][];
			for(int i = 0; i < lines.size(); i++)
			{
				String[] lineParts = lines.get(i).split(",");
				// remove the character """ from the line
				for(int j = 0; j < lineParts.length; j++)
				{
					lineParts[j] = lineParts[j].replaceAll("\"", "");
				}
				linesArray[i] = lineParts;
			}
			return linesArray;
		}
		
		private static ArrayList<String> readFileLines(File file)
		{
			ArrayList<String> lines = new ArrayList<String>();
			try
			{
				
				Scanner scanner = new Scanner(file);
				while(scanner.hasNextLine())
				{
					lines.add(scanner.nextLine());
				}
			}
			catch(Exception e){}
			return lines;
		}
	}
}
