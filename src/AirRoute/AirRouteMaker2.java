package AirRoute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Scanner;

public class AirRouteMaker2
{
	static String reusableSourceCountry;
	
	// The program will repeatedly ask the user for an airport code, and then for destination airport codes
	// until the user enters the word "done".
	public static void main(String[] args)
	{
		System.out.println("Welcome to the AirRoute Maker!");
		System.out.println("The program wants to make a route for you.");
		System.out.print("Please give a Callsign prefix that will be used later: ");
		String callsignPrefix = new Scanner(System.in).nextLine();
		System.out.print("Please give a Flight Number prefix that will be used later: ");
		String flightNumberPrefix = new Scanner(System.in).nextLine();
		System.out.print("Are the callsigns and flight numbers going to be the same? (y/n): ");
		String sameCallsignsAndFlightNumbers = new Scanner(System.in).nextLine().toLowerCase();
		System.out.print("Are the callsigns and flightnumbers going to be incrementing by 1? (y/n): ");
		String incrementCallsignsAndFlightNumbersInput = new Scanner(System.in).nextLine().toLowerCase();
		boolean incCSandFN = false;
		if(incrementCallsignsAndFlightNumbersInput.equals("y"))
		{
			incCSandFN = true;
		}
		System.out.print("what is the first flight number?: ");
		int firstFlightNumber = new Scanner(System.in).nextInt();
		int firstCallsignNumber;
		if(!sameCallsignsAndFlightNumbers.equals("y"))
		{
			System.out.print("what is the first callsign number?: ");
			firstCallsignNumber = new Scanner(System.in).nextInt();
		}
		else
		{
			firstCallsignNumber = firstFlightNumber;
		}
		
		boolean sameCallsignsAndFlightNumbersBool = false;
		if (sameCallsignsAndFlightNumbers.equals("y"))
		{
			sameCallsignsAndFlightNumbersBool = true;
		}
		
		System.out.print("Enter an airport code: ");
		String airportCode = new Scanner(System.in).nextLine();
		while (!airportCode.toLowerCase().equals("done"))
		{
			Airport2 airport = new Airport2(airportCode);
			System.out.print("Enter destination airport code: ");
			String destAirportCode = new Scanner(System.in).nextLine();
			//
			// if the destination airport code is "done", then the program will stop
			if (destAirportCode.toLowerCase().equals("done"))
			{
				break;
			}
			// ask the user what the flight number is
			String input = "";
			if(incCSandFN)
			{
				input = Integer.toString(firstFlightNumber);
				firstFlightNumber++;
			}
			else
			{
				System.out.print("Finish flight number: " + flightNumberPrefix);
				input = new Scanner(System.in).nextLine();
			}
			String flightNumber = flightNumberPrefix + input;
			// ask the user what the callsign is (if the user wants to use the same callsigns and flight numbers)
			if(incCSandFN)
			{
				input = Integer.toString(firstCallsignNumber);
				firstCallsignNumber++;
			}
			else if (!sameCallsignsAndFlightNumbersBool)
			{
				System.out.print("Finish callsign: " + callsignPrefix);
				input = new Scanner(System.in).nextLine();
			}
			String callsign = callsignPrefix + input;
			double distance = getDistance(airportCode, destAirportCode);
			airport.addReachable(destAirportCode, distance, callsign, flightNumber);
			try
			{
				saveAirport(airport,reusableSourceCountry);
			}catch(Exception e){}
			airportCode = destAirportCode;
			System.out.println("Next airport code: " + airportCode);
		}
	}
	
	private static void saveAirport(Airport2 airport,String country) throws FileNotFoundException
	{
		// this will take in the airport and save it to a file
		// the file name will be the airport code followed by ".airnode"
		// the file will contain the airports that are reachable from the airport
		// each reachable airport will be on a separate line
		// the file will be saved in the directory "AirRouteAirportData"
		// if the file already exists, it will be overwritten
		File file = new File("data/AirRouteAirportData/Snowpiercer/" + country + "/" + airport.ICAO + ".airnode");
		// if the directory does not exist, create it
		if (!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}
		// if the file already exists, read it into String content
		String content = "";
		if (file.exists())
		{
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
				content += scanner.nextLine() + "\n";
			}
			scanner.close();
		}
		
		for (String reachableAirport : airport.reachable.keySet())
		{
			// if the reachable airport is not already in the file, add it
			if (!content.contains(reachableAirport))
			{
				content += reachableAirport + ","
						+ airport.reachable.get(reachableAirport) + ","
						+ airport.callsigns.get(reachableAirport) + ","
						+ airport.flightNumbers.get(reachableAirport) + "\n";
			}
			//content += reachableAirport + "\n";
		}
		// write the content to the file
		try
		{
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.close();
		}
		catch (Exception e){
			System.err.println("Error writing to file");
			e.printStackTrace();
		}
		//*/
	}
	
	private static double getDistance(String airportCode1, String airportCode2)
	{
		AirportData.Airport airport1 = AirportData.AirportGrabber.getAirport(airportCode1);
		AirportData.Airport airport2 = AirportData.AirportGrabber.getAirport(airportCode2);
		
		// set the reusable source country to the first airport's country
		reusableSourceCountry = airport1.country;
		
		
		// get the distance between the two airports
		// the distance is the distance between the two airports in miles
		// the distance is calculated using the Haversine formula
		// the formula is:
		// d = 2 * asin(sqrt((sin((lat1 - lat2) / 2))^2 + cos(lat1) * cos(lat2) * (sin((lon1 - lon2) / 2))^2))
		double lat1 = Math.toRadians(airport1.latitude);
		double lat2 = Math.toRadians(airport2.latitude);
		double lon1 = Math.toRadians(airport1.longitude);
		double lon2 = Math.toRadians(airport2.longitude);
		double d = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1 - lat2) / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon1 - lon2) / 2), 2)));
		d = d * 3958.8;
		// convert the distance from miles to nautical miles
		d = d * 0.8684;
		return d;
	}
}
