package AirRoute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class AirRouteMaker
{
	// The program will repeatedly ask the user for an airport code, and then for destination airport codes
	// until the user enters the word "done".
	public static void main(String[] args)
	{
		System.out.print("Enter an airport code: ");
		String airportCode = new Scanner(System.in).nextLine();
		while (!airportCode.equals("done"))
		{
			AirRoute.Airport airport = new AirRoute.Airport(airportCode);
			System.out.print("Enter destination airport code: ");
			String destAirportCode = new Scanner(System.in).nextLine();
			boolean done = false;
			while (!done)
			{
				if (destAirportCode.equals("done"))
				{
					done = true;
				}
				else
				{
					airport.addReachable(destAirportCode);
					System.out.print("Enter destination airport code: ");
					destAirportCode = new Scanner(System.in).nextLine();
				}
			}
			try
			{
				saveAirport(airport);
			}catch(Exception e){}
			System.out.print("Enter an airport code: ");
			airportCode = new Scanner(System.in).nextLine();
		}
	}
	
	private static void saveAirport(AirRoute.Airport airport) throws FileNotFoundException
	{
		// this will take in the airport and save it to a file
		// the file name will be the airport code followed by ".airnode"
		// the file will contain the airports that are reachable from the airport
		// each reachable airport will be on a separate line
		// the file will be saved in the directory "AirRouteAirportData"
		// if the file already exists, it will be overwritten
		File file = new File("data/AirRouteAirportData/V1/" + airport.ICAO + ".airnode");
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
		
		for (String reachableAirport : airport.reachable)
		{
			// if the reachable airport is not already in the file, add it
			if (!content.contains(reachableAirport))
			{
				content += reachableAirport + "\n";
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
}
