package io.github.greenwolf24.AirplaneSubway.AirRoute;
import io.github.greenwolf24.AirplaneSubway.AirportData.*;
import io.github.greenwolf24.AirplaneSubway.AirportData.Airport;

import java.util.ArrayList;
import java.util.Scanner;

public class AirRouteFinderMaker
{
	private static AirportGrabber airportGrabber = new AirportGrabber();
	
	public static void main(String[] args)
	{
		// This program will create an air route by asking the user for the starting and ending airports
		// the program will then find an optimal route between the two airports
		// the user will be asked for the optimal distance between airports
		// the user will be asked for the minimum and maximum distance between airports
		
		airportGrabber.loadSortedFileToMemory();
		
		String startAirport;
		String endAirport;
		double optimalDistance;
		double minDistance;
		double maxDistance;
		
		System.out.println("Welcome to the Air Route Finder!");
		System.out.print("Please enter the starting airport: ");
		startAirport = new Scanner(System.in).nextLine();
		System.out.print("Please enter the ending airport: ");
		endAirport = new Scanner(System.in).nextLine();
		System.out.print("Please enter the optimal distance between airports: ");
		optimalDistance = new Scanner(System.in).nextDouble();
		System.out.print("Please enter the minimum distance between airports: ");
		minDistance = new Scanner(System.in).nextDouble();
		System.out.print("Please enter the maximum distance between airports: ");
		maxDistance = new Scanner(System.in).nextDouble();
		System.out.print("Please enter the minimum ratio between travel/progress: ");
		double optimalRatio = new Scanner(System.in).nextDouble();
		
		
		Airport start = airportGrabber.getAirport(startAirport);
		Airport end = airportGrabber.getAirport(endAirport);
		
		ArrayList<AirRouteStep> airportsInRange = allAirportsInRange(start,minDistance,maxDistance,end);
		
		// find closest match routes
		ArrayList<AirRouteStep> optimalRoutes = findBestRoutes(airportsInRange,optimalDistance,optimalRatio);
		
		// print out the routes
		System.out.println("The optimal routes are:");
		for(AirRouteStep route : optimalRoutes)
		{
			System.out.println(route.airport.ICAO);
		}
		
		System.out.println("The program has finished running.");
	}
	
	private static ArrayList<AirRouteStep> findBestRoutes(ArrayList<AirRouteStep> airportsInRange, double optimalDistance, double minimumRatio)
	{
		// find routes that are close to the optimal distance and ratio
		// the range of acceptable distances is +/- 15% of the optimal distance
		// the possible range of ratios is -1.0 to 1.0
		// we only want ratios above the minimum ratio
		
		ArrayList<AirRouteStep> bestRoutes = new ArrayList<>();
		
		for(AirRouteStep airport : airportsInRange)
		{
			if(airport.distanceTraveled >= optimalDistance * 0.85
			&& airport.distanceTraveled <= optimalDistance * 1.15
			&& airport.travelCloserRatio >= minimumRatio)
			{
				bestRoutes.add(airport);
			}
		}
		
		return bestRoutes;
	}
	
	private static ArrayList<AirRouteStep> allAirportsInRange(Airport start,double min,double max,Airport end)
	{
		ArrayList<String> allAirports = airportGrabber.allAirportCodes();
		ArrayList<AirRouteStep> airportsInRange = new ArrayList<>();
		for(String airportCode : allAirports)
		{
			Airport airport = airportGrabber.getAirport(airportCode);
			double distance = airportDistance(start,airport);
			if(distance >= min && distance <= max)
			{
				AirRouteStep step = new AirRouteStep(airport,distance,distanceRatio(start,airport,end));
				airportsInRange.add(step);
			}
		}
		return airportsInRange;
	}
	
	private static double airportDistance(Airport start, Airport end)
	{
		// get the distance between the two airports
		// the distance is the distance between the two airports in miles
		// the distance is calculated using the Haversine formula
		// the formula is:
		// d = 2 * asin(sqrt((sin((lat1 - lat2) / 2))^2 + cos(lat1) * cos(lat2) * (sin((lon1 - lon2) / 2))^2))
		double lat1 = Math.toRadians(start.latitude);
		double lat2 = Math.toRadians(end.latitude);
		double lon1 = Math.toRadians(start.longitude);
		double lon2 = Math.toRadians(end.longitude);
		double d = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1 - lat2) / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon1 - lon2) / 2), 2)));
		d = d * 3958.8;
		// convert the distance from miles to nautical miles
		d = d * 0.8684;
		return d;
	}
	
	private static double distanceCloser(Airport start, Airport mid, Airport end)
	{
		// get the distance between the two airports start and end
		// this is distance A
		// get the distance between the two airports mid and end
		// this is distance B
		// return the difference between the two distances
		double distanceA = airportDistance(start,end);
		double distanceB = airportDistance(mid,end);
		return distanceA - distanceB;
	}
	
	private static double distanceRatio(Airport start, Airport mid, Airport end)
	{
		// get the distance between the two airports start and mid
		// this is distance A
		// get the distanceCloser between the airports start mid and end
		// this is distance B
		// return the ratio of the two distances B/A
		double distanceA = airportDistance(start,mid);
		double distanceB = distanceCloser(start,mid,end);
		return distanceB/distanceA;
	}
	
	
	
	
	
	
	public static class AirRouteStep
	{
		public Airport airport;
		public double distanceTraveled;
		public double travelCloserRatio;
		
		public AirRouteStep(Airport airport, double distanceTraveled, double travelCloserRatio)
		{
			this.airport = airport;
			this.distanceTraveled = distanceTraveled;
			this.travelCloserRatio = travelCloserRatio;
		}
	}
}