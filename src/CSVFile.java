import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVFile
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
			linesArray[i] = lines.get(i).split(",");
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
