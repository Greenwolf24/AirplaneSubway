package io.github.greenwolf24.AirplaneSubway.AirportData;

import io.github.greenwolf24.AirplaneSubway.CSVFile;

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
	
	private static String[][] airportDataFile = null;
	private static String[][] runwayDataFile = null;
	
	private static ArrayList<String> ignoredAirports = new ArrayList<>();
	
	public AirportGrabber()
	{
		loadFileToMemory();
	}
	
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
				try
				{
					r1.name = runway[8];
				}
				catch(Exception e)
				{
					r1.name = "";
				}
				try
				{
					r1.length = Integer.parseInt(runway[3]);
				}
				catch(Exception e)
				{
					r1.length = 0;
				}
				try
				{
					r1.surface = runway[5];
				}
				catch(Exception e)
				{
					r1.surface = "";
				}
				try
				{
					r1.heading = headingFromName(r1.name) * 10;
				}
				catch(Exception e)
				{
					r1.heading = -1;
				}
				airportRunways.add(r1);
				try
				{
					Runway r2 = new Runway();
					r2.name = runway[14];
					r2.length = Integer.parseInt(runway[3]);
					r2.surface = runway[5];
					r2.heading = headingFromName(r2.name) * 10;
					
					airportRunways.add(r2);
				}
				catch(Exception e){}
			}
			// create a new airport object
			io.github.greenwolf24.AirplaneSubway.AirportData.Airport airport = new io.github.greenwolf24.AirplaneSubway.AirportData.Airport(airportInfo,airportRunways);
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
	
	public static void loadFileToMemory()
	{
		airportDataFile = CSVFile.getFile("data/AirportDataDatabase/airports.csv");
		runwayDataFile = CSVFile.getFile("data/AirportDataDatabase/runways.csv");
		
		// each file has a header row, so we need to remove it
		// do this by removing the first row of each file
		airportDataFile = removeFirstItem(airportDataFile);
		runwayDataFile = removeFirstItem(runwayDataFile);
		
		// now we need to remove any airports that are not in the airport data file
		airportDataFile = cleanUpAirports(airportDataFile);
		
	}
	
	private static String[][] cleanUpAirports(String[][] airports)
	{
		// The airport data file has a lot of extra data that we don't need
		// an example of this would be any heliports
		// another example are closed airports
		// another are seaplane bases
		// these are designated in the index 2 column of the file
		// we will remove these rows from the file
		// this method will return a new file with the rows removed
		
		ArrayList<String[]> cleanAirports = new ArrayList<>();
		for(String[] airport : airports)
		{
			if(airport[2].equals("heliport") || airport[2].equals("closed") || airport[2].equals("seaplane_base"))
			{
				ignoredAirports.add(airport[1]);
				continue;
			}
			cleanAirports.add(airport);
		}
		
		String[][] cleanAirportsArray = new String[cleanAirports.size()][];
		for(int i = 0; i < cleanAirports.size(); i++)
		{
			cleanAirportsArray[i] = cleanAirports.get(i);
		}
		
		// it is possible that an airport name string has a comma in it
		// this causes problems with the CSV file and reading data from it
		// in order to detect the error, we will need to check each line
		// if index 3 and 4 are strings, then we can assume that a comma is in the airport name
		// and we need to reformat the line
		// this is not a perfect solution, but it is a good one
		
		cleanAirportsArray = cleanAirportCommaName(cleanAirportsArray);
		
		return cleanAirportsArray;
	}
	
	private static String[][] cleanAirportCommaName(String[][] cleanAirportsArray)
	{
		boolean recurse = false;
		for(int i = 0; i < cleanAirportsArray.length; i++)
		{
			try
			{
				double d1 = Double.parseDouble(cleanAirportsArray[i][4]);
			}
			catch(Exception e)
			{
				recurse = true;
				String[] newLine = new String[cleanAirportsArray[i].length - 1];
				for(int j = 0; j < newLine.length; j++)
				{
					if(j < 3)
					{
						newLine[j] = cleanAirportsArray[i][j];
					}
					if(j > 4)
					{
						newLine[j - 1] = cleanAirportsArray[i][j];
					}
					if (j == 3)
					{
						newLine[j] = cleanAirportsArray[i][j] + ", " + cleanAirportsArray[i][j + 1];
					}
				}
				cleanAirportsArray[i] = newLine;
			}
		}
		
		if(recurse)
		{
			cleanAirportCommaName(cleanAirportsArray);
		}
		return cleanAirportsArray;
	}
	
	private static String[][] cleanUpRunways(String[][] runways)
	{
		// because of cleaning up the airports, we need to clean up the runways
		// the runways we need
		return null;
	}
	
	private static String[][] removeFirstItem(String[][] file)
	{
		String[][] newFile = new String[file.length-1][];
		for(int i = 0; i < file.length-1; i++)
		{
			newFile[i] = file[i+1];
		}
		return newFile;
	}
	
	public static ArrayList<String> allAirportCodes()
	{
		ArrayList<String> airportCodes = new ArrayList<>();
		for(int i = 0; i < airportDataFile.length; i++)
		{
			airportCodes.add(airportDataFile[i][1]);
		}
		return airportCodes;
	}
	
	private static String[] getAirportFromCSV(String airportCode)
	{
		// check the CSV file for the airport
		// if the airport is not found, return null
		
		// if the airportDataFile is null, grab the file for airportFile
		// if the airportDataFile is not null, use it for airportFile
		String[][] airportFile;
		if(airportDataFile == null)
		{
			airportFile = CSVFile.getFile("data/AirportDataDatabase/airports.csv");
		}
		else
		{
			airportFile = airportDataFile;
		}
		
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
		
		String[][] runwaysFile;
		
		if(runwayDataFile == null)
		{
			runwaysFile = CSVFile.getFile("data/AirportDataDatabase/runways.csv");
		}
		else
		{
			runwaysFile = runwayDataFile;
		}
		
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
}
