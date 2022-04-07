package Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner01
{
	private static ArrayList<AirNode> nodes;
	private static String dir = "data/AirRouteAirportData/Snowpiercer";
	
	public static void main(String[] args)
	{
		nodes = new ArrayList<AirNode>();
		
		// Load up the nodes and flights from the files
		// grab a list of the files in the nodes directory
		// for each file, load the node
		
		// grab a list of the names of all the files in all subdirectories
		ArrayList<String> files = new ArrayList<String>();
		for (File file : new File(dir).listFiles())
		{
			if (file.isFile())
			{
				files.add(file.getName());
			}
			else if (file.isDirectory())
			{
				for (File subFile : file.listFiles())
				{
					if (subFile.isFile())
					{
						files.add(file.getName() + "/" + subFile.getName());
					}
				}
			}
		}
		
		// for each file string, remove the extension
		// load the node with the file string
		for (String file : files)
		{
			nodes.add(loadNode(fileNameNoExt(file)));
		}
		
		/*
		
		File[] files = new File("data/AirRouteAirportData").listFiles();
		
		for(File file : files)
		{
			// load the node
			String code = fileNameNoExt(file.getName());
			AirNode node = loadNode(code);
			
			// add the node to the array
			if(node != null)
				nodes.add(node);
		}
		
		//*/
		
		System.out.print("Enter the origin airport code: ");
		Scanner scanner = new Scanner(System.in);
		String origin = scanner.nextLine();
		
		// print out the origin node
		for (AirNode node : nodes)
		{
			if(node.getAirportCode().equals(origin))
			{
				System.out.println(node);
			}
		}
		
		System.out.print("Enter the destination airport code: ");
		String destination = scanner.nextLine();
		
		// print out the destination node
		for (AirNode node : nodes)
		{
			if(node.getAirportCode().equals(destination))
			{
				System.out.println(node);
			}
		}
		
		// show the result of the first found path
		ArrayList<AirNode> path = firstFoundPath(loadNode(origin), loadNode(destination), new ArrayList<AirNode>());
		
		if(path != null)
		{
			System.out.println("The shortest path is: ");
			for(AirNode node : path)
			{
				System.out.println(node);
			}
		}
	}
	
	// this method will determine the shortest path between two nodes
	// it will return an array of the nodes in the path
	// this will be recursion based
	private static ArrayList<AirNode> firstFoundPath(AirNode start, AirNode end, ArrayList<AirNode> visited)
	{
		// create a new array list to hold the path
		ArrayList<AirNode> path = new ArrayList<AirNode>();
		
		// add the start node to the path
		path.add(start);
		
		// if the start node is already in the visited list, return null
		if(visited.contains(start))
			return null;
		
		// add the start node to the visited list
		visited.add(start);
		
		// if the start node is the end node, return the path
		if(start.equals(end))
			return path;
		
		// if the node is a dead end, return null
		if(isDeadEnd(start))
			return null;
		
		// for each edge in the start node
		for(Edge edge : start.getEdges())
		{
			// get the destination node
			AirNode dest = loadNode(edge.getAirportCode());
			ArrayList<AirNode> newPath = firstFoundPath(dest, end, visited);
			
			// if the new path is not null, add the new path to the path
			if(newPath != null)
			{
				path.addAll(newPath);
				return path;
			}
			else
			{
				continue;
			}
		}
		return null;
	}
	
	
	/*
	public static ArrayList<String> shortestPath(AirNode origin, AirNode dest)
	{
		// create a queue
		// add the origin node to the queue
		// while the queue is not empty
		//		pop a node from the queue
		//		for each edge in the node
		//			if the edge is not visited
		//				add the edge to the queue
		//				mark the edge as visited
		//				add the edge to the path
		//				add the destination node to the path
		// return the path
		
		// create a list of nodes
		// add the origin node to the list
		// create a list of visited edges
		ArrayList<AirNode> queue = new ArrayList<AirNode>();
		queue.add(origin);
		ArrayList<Edge> visited = new ArrayList<Edge>();
		
		ArrayList<String> path = new ArrayList<String>();
		
		while(!queue.isEmpty())
		{
			AirNode node = queue.remove(0);
			if (node.equals(dest))
			{
				path.add(node.getAirportCode());
				return path;
			}
			if (isDeadEnd(node))
			{
				continue;
			}
			for (Edge edge : node.getEdges())
			{
				if (!visited.contains(edge))
				{
					queue.add(loadNode(edge.getAirportCode()));
					visited.add(edge);
					
				}
			}
		}
	}
	//*/
	
	private static boolean isDeadEnd(AirNode node)
	{
		// return true if the node has no edges
		// return false otherwise
		
		return node.getEdges().length == 0;
	}
	
	public static AirNode loadNode(String airportCode)
	{
		AirNode node = new AirNode(airportCode);
		
		// Load the nodes from the file
		File file = findFile(airportCode);
		
		// for each line in the file, a line is a flight
		try
		{
			Scanner scanner;
			try
			{
				scanner = new Scanner(file);
			}
			catch (FileNotFoundException e) {return null;}
			catch(NullPointerException e) {return null;}
			catch(Exception e) {return null;}
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				// it is possible that the line is empty
				if(line.equals(""))
					continue;
				node.addEdge(loadFlight(line));
			}
			scanner.close();
			return node;
		}
		catch(NullPointerException e){return null;}
		catch(Exception e){e.printStackTrace();return null;}
	}
	
	private static File findFile(String airportCode)
	{
		// sometimes the airport code is passed in with the subfolder name already included
		// this will remove the subfolder name, keeping only the airport code
		airportCode = airportCode.substring(airportCode.lastIndexOf("/") + 1);
		
		// find the file with the airport code
		// search subdirectories
		// return the file
		File root = new File(dir);
		File[] folders = root.listFiles();
		for(File folder : folders)
		{
			File[] files = folder.listFiles();
			for(File file : files)
			{
				if(file.getName().equals(airportCode + ".airnode"))
					return file;
			}
		}
		return null;
	}
	
	public static Edge loadFlight(String line)
	{
		// Load the flights from the file
		// a flight is a line in the file
		// the line is split by commas
		// the first part is the destination airport code
		// the second part is the distance
		// the third part is the callsign
		// the fourth part is the flight number
		
		String[] parts = line.split(",");
		
		String destCode = parts[0];
		double distance = Double.parseDouble(parts[1]);
		String callsign = parts[2];
		String flightNumber = parts[3];
		
		return new Edge(destCode, distance, callsign, flightNumber);
	}
	
	public static String fileNameNoExt(String fileName)
	{
		// returns the file name without the extension
		// for example, "file.txt" returns "file"
		
		int index = fileName.lastIndexOf(".");
		if(index == -1)
			return fileName;
		else
			return fileName.substring(0, index);
	}
}
