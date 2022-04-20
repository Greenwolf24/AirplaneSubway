package io.github.greenwolf24.AirplaneSubway.AirportData;

import io.github.greenwolf24.AirplaneSubway.CSVFile;

import java.io.File;
//import java.sql.Timestamp;
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
	
	public static ArrayList<String> smartGrabDistance(String airportCode, double distance)
	{
		// the distance is in nautical miles
		// divide this by 60 to get the number of degrees
		
		Airport airport = getAirport(airportCode);
		if(airport != null)
		{
			//Timestamp now = new Timestamp(System.currentTimeMillis());
			//System.out.println("I am here, Line 53 AirportGrabber");
			ArrayList<String> airportCodes = new ArrayList<>();
			int distDegrees = (int)Math.ceil(distance / 60.0);
			int originLat = (int)Math.round(airport.latitude);
			int originLon = (int)Math.round(airport.longitude);
			
			for(int lat = originLat - distDegrees; lat <= originLat + distDegrees; lat++)
			{
				for(int lon = originLon - distDegrees; lon <= originLon + distDegrees; lon++)
				{
					try
					{
						File folder = new File("data/AirportDataReStore/LocationBasedKey/" + lat + "/" + lon+"/");
						File[] listOfFiles = folder.listFiles();
						for(int i = 0; i < listOfFiles.length; i++)
						{
							if(listOfFiles[i].isFile())
							{
								String fileName = listOfFiles[i].getName();
								// remove the .point extension
								fileName = fileName.substring(0, fileName.length() - 6);
								airportCodes.add(fileName);
							}
						}
					}
					catch(Exception e)
					{
						// ignore
					}
				}
			}
			//Timestamp now2 = new Timestamp(System.currentTimeMillis());
			//System.out.println("Time to grab: " + (now2.getTime() - now.getTime()) + "ms");
			return airportCodes;
		}
		
		return null;
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
		airportDataFile = cleanAirportRunwaysTypes(airportDataFile);
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
		
		String[] dontWant = {"heliport","closed","seaplane_base","baloonport","small_airport"};
		
		ArrayList<String[]> cleanAirports = new ArrayList<>();
		for(String[] airport : airports)
		{
			boolean want = true;
			for(String dontWantThis : dontWant)
			{
				if(airport[2].equals(dontWantThis))
				{
					ignoredAirports.add(airport[1]);
					want = false;
				}
			}
			if(want)
			{
				cleanAirports.add(airport);
			}
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
	
	private static String[][] cleanAirportRunwaysTypes(String[][] airportdatafiile)
	{
		ArrayList<String[]> retAirports = new ArrayList<>();
		
		String[] hardSurfaceRunways = {"CONC","ASPH","ASP","CON","Concrete","Asphalt","Concrete - Grooved","ASPH-G","PEM",
				"ASPH-G","ASPH-TRTD","ASPH-F","ASPH-E","ASPH-CONC","BIT","ASPH-L","PEM","ASP/CONC","Bituminous"};
		
		
		for(String[] airportString : airportdatafiile)
		{
			Airport airport = getAirport(airportString[1]);
			boolean want = false;
			for(Runway runway : airport.runways)
			{
				for(String surf : hardSurfaceRunways)
				{
					if(surf.equals(runway.surface))
					{
						want = true;
					}
				}
			}
			if(want)
			{
				retAirports.add(airportString);
			}
		}
		
		String[][] cleanAirportsArray = new String[retAirports.size()][];
		for(int i = 0; i < retAirports.size(); i++)
		{
			cleanAirportsArray[i] = retAirports.get(i);
		}
		return cleanAirportsArray;
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
		
		// TODO
		// this should be a binary search
		// I can't get it to work right now
		// I will need to figure out how to do a binary search
		
		
		//*
		for(int i = 0; i < airportFile.length; i++)
		{
			if(airportFile[i][1].equals(airportCode))
			{
				return airportFile[i];
			}
		}
		// */
		return recurseBinarySearch(airportFile, airportCode, 0, airportFile.length - 1);
		//return null;
	}
	
	private static String[] recurseBinarySearch(String[][] file, String airportCode, int start, int end)
	{
		if(start > end)
		{
			return null;
		}
		int mid = (start + end) / 2;
		if(file[mid][1].equals(airportCode))
		{
			return file[mid];
		}
		if(StringGreaterThan(file[mid][1], airportCode))
		{
			return recurseBinarySearch(file, airportCode, start, mid - 1);
		}
		else
		{
			return recurseBinarySearch(file, airportCode, mid + 1, end);
		}
	}
	
	private static boolean StringGreaterThan(String s1, String s2)
	{
		// the strings will be compared based on alphabetical order
		// if s1 is greater than s2, return true
		// if s1 is less than s2, return false
		// if s1 is equal to s2, return false
		
		// if s1 is null, return false
		// if s2 is null, return true
		
		
		
		if(s1 == null)
		{
			return false;
		}
		if(s2 == null)
		{
			return true;
		}
		
		// the strings may have dashes in them
		// remove the dashes
		s1 = s1.replace("-", "");
		s2 = s2.replace("-", "");
		
		
		int i = 0;
		while(i < s1.length() && i < s2.length())
		{
			if(s1.charAt(i) > s2.charAt(i))
			{
				return true;
			}
			if(s1.charAt(i) < s2.charAt(i))
			{
				return false;
			}
			i++;
		}
		if(i == s1.length() && i == s2.length())
		{
			return false;
		}
		if(i == s1.length())
		{
			return false;
		}
		return true;
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
