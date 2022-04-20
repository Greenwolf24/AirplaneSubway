package io.github.greenwolf24.AirplaneSubway.AirportData;

import java.util.ArrayList;

public class AirReStorer
{
	// this is going to re-store the airport data
	// this will be done to optimize the access to the data on a location based basis
	
	public static void main(String[] args)
	{
		AirportGrabber grabber = new AirportGrabber();
		grabber.loadFileToMemory();
		ArrayList<String> airportICAO = grabber.allAirportCodes();
		
		for(String code : airportICAO)
		{
			Airport airport = grabber.getAirport(code);
			// what we are going to do is grab the airport's coordinate data
			// we will then use that data as the directory to store the airport code
			// we will then store the airport data in the directory
			// the directory will be the airport's latitude and longitude, both rounded to the nearest integer
			
			int lat = (int) Math.round(airport.latitude);
			int lon = (int) Math.round(airport.longitude);
			
			String dir = "data/AirportDataReStore/LocationBasedKey/" + lat + "/" + lon + "/";
			// if the directory does not exist, create it
			if(!(new java.io.File(dir).exists()))
			{
				new java.io.File(dir).mkdirs();
			}
			// we will then create an empty file with the airport code as the name
			String fileName = dir + code + ".point";
			// now we will create the file
			java.io.File file = new java.io.File(fileName);
			// if the file does not exist, create it
			if(!file.exists())
			{
				try
				{
					file.createNewFile();
				}
				catch(Exception e){}
			}
		}
	}
	
	/*
	
	//these should in theory already by handled and parsed out upon file load
	
	private static boolean doWeWantAirport(Airport airport)
	{
		// this will determine if we want to store this airport
		// this will look at many factors that will determine if we want this airport
		
		if(!haveHardRunway(airport))
		{
			return false;
		}
		
		
		
		return true;
	}
	
	private static boolean haveHardRunway(Airport airport)
	{
		// first we determine if the airport has a hard-surface runway
		// if it does not, return false
		String[] hardSurfaceRunways = {"CONC","ASPH","ASP","CON","Concrete","Asphalt","Concrete - Grooved","ASPH-G","PEM",
		                               "ASPH-G","ASPH-TRTD","ASPH-F","ASPH-E","ASPH-CONC","BIT","ASPH-L","PEM","ASP/CONC","Bituminous"};
		for(Runway runway : airport.runways)
		{
			for(String hardSurface : hardSurfaceRunways)
			{
				if(runway.surface.equals(hardSurface))
				{
					return true;
				}
			}
		}
		return false;
	}
	//*/
}
