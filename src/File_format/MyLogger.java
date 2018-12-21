package File_format;

import java.io.*;

/**
 * @author Elisha
 */
public class MyLogger
{
	public static MyLogger logger;

	private static StringBuilder sb = new StringBuilder();

	public static void log(Object message)
	{
		sb.append(message.toString());
	}

	public static void logln(Object message)
	{
		log(message.toString() + "\n");
	}

	public static void save(File file)
	{
		System.out.println("Printing log");
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file));)
		{
			bw.write(sb.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
