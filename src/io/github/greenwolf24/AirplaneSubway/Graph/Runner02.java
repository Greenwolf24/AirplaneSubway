package io.github.greenwolf24.AirplaneSubway.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner02
{
	
	public static void main(String[] args)
	{
		// test the getAirNode method
		// ask the user for a node name
		// print the node
		System.out.print("Enter a node name: ");
		String name1 = new Scanner(System.in).nextLine();
		AirNode node1 = getAirNode(name1);
		if (node1 == null)
		{
			System.out.println("Node not found");
		}
		else
		{
			System.out.println(node1);
		}
		
		System.out.print("Enter a node name: ");
		String name2 = new Scanner(System.in).nextLine();
		AirNode node2 = getAirNode(name2);
		if (node2 == null)
		{
			System.out.println("Node not found");
		}
		else
		{
			System.out.println(node2);
		}
		
		// test the findAPath method
		ArrayList<AirNode> startwork = new ArrayList<>();
		startwork.add(node1);
		ArrayList<AirNode> path = findAPath(node1, node2, startwork, new ArrayList<>());
		// for some reason, the path has 2 additional nodes at the end
		//
		if (path == null)
		{
			System.out.println("No path found");
		}
		else
		{
			System.out.println("Path found");
			for (AirNode node : path)
			{
				System.out.println(node);
			}
		}
		
	}
	
	// find a path from start to end
	// if a path is found, return an ArrayList of the nodes
	// if no path is found, return null
	// it doesn't need to be the shortest path, just one that works
	// use recursion
	public static ArrayList<AirNode> findAPath(AirNode start, AirNode end, ArrayList<AirNode> workingPath,ArrayList<AirNode> visted)
	{
		// if the start node equals the end node, add the end node to the working path and return
		if(start.equals(end))
		{
			//workingPath.add(end);
			return workingPath;
		}
		if(start.getEdges().length == 0)
		{
			return null;
		}
		visted.add(start);
		ArrayList<AirNode> newPath = (ArrayList<AirNode>) workingPath.clone();
		newPath.add(start);
		for(Edge edge : start.getEdges())
		{
			// grab the node from the edge
			AirNode next = getAirNode(edge.getAirportCode());
			if(alreadyVisited(visted,next))
			{
				continue;
			}
			//ArrayList<AirNode> nextPath = (ArrayList<AirNode>) workingPath.clone();
			//nextPath.add(next);
			return findAPath(next,end,newPath,visted);
		}
		
		return null;
	}
	
	private static boolean alreadyVisited(ArrayList<AirNode> visited,AirNode checking)
	{
		for(AirNode inlist : visited)
		{
			if(inlist.equals(checking))
			{
				return true;
			}
		}
		return false;
	}
	
	private static AirNode getAirNode(String name)
	{
		// get the file for the node
		// create a new AirNode object
		// read the file
		
		File file = getAirNodeFile(name);
		if (file == null)
		{
			return null;
		}
		AirNode node = new AirNode(name);
		
		// read the file
		// each line is a new edge
		// the sections are separated by a comma
		// the secions are: Destination, Distance, Callsign, Flight Number
		try
		{
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine())
			{
				String line = scan.nextLine();
				// the line may be an empty line at the end of the file
				// if so, skip it
				if (line.equals(""))
				{
					continue;
				}
				String[] sections = line.split(",");
				Edge edge = new Edge(sections);
				node.addEdge(edge);
			}
		}catch(FileNotFoundException e){
			System.err.println("Something went fucky wucky on line 31");
			System.exit(47);
		}
		return node;
	}
	
	private static File getAirNodeFile(String name)
	{
		// search for the node with the given name
		// if the node exists, it will be in a subfolder
		// if the node is found, return the file
		// if the node is not found, return null
		
		for (File folder : new File("data/AirRouteAirportData/Snowpiercer").listFiles())
		{
			for (File file : folder.listFiles())
			{
				if (stripExtension(file.getName()).equals(name))
				{
					return file;
				}
			}
		}
		
		return null;
	}
	
	private static String stripExtension(String name)
	{
		// remove the extension from the file name
		// if the file has no extension, return the name
		
		if (name.contains("."))
		{
			return name.substring(0, name.lastIndexOf("."));
		}
		return name;
	}
}
