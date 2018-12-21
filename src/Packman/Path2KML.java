package Packman;

import Algorithms.TSP.City;
import Algorithms.TSP.MoveRecord;
import File_format.ReadWrite;
import GIS.LLAElement;
import GIS.Layer;
import GIS.Project;
import Geom.LLA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for writing a path to a KML file.
 *
 * @author Elisha
 */
public class Path2KML
{
	/**
	 * Writes a path to a KML file.
	 * @param file the file to write to.
	 * @param packmen an array of all the packmen (after the algorithm was computed).
	 * @param fruits an array of all the fruits (after the algorithm was computed).
	 * @param eps time in second between the frames.
	 * @param startTime time in UTC to mark as the start time of the path.
	 */
	public static void path2KML(File file, Packman[] packmen, Fruit[] fruits, double eps,
								long startTime)
	{

		double durationSeconds = 0;
		for (Fruit fruit : fruits)
		{
			if (fruit.timeVisited() > durationSeconds)
				durationSeconds = fruit.timeVisited();
		}
		long durationUTC = toUTC(durationSeconds);


		List<Layer<LLAElement>> packmenElements = new ArrayList<>(packmen.length);

		for (int p = 0; p < packmen.length; p++)
		{
			packmenElements.add(new Layer<>(startTime, durationUTC, "Packman " + p));
		}
		boolean lastLoop = false;
		double currentTime = 0;
		while (currentTime <= durationSeconds)
		{
			for (int p = 0; p < packmen.length; p++)
			{

				LLA point = packmen[p].getMoveAtTime(currentTime).getPositionAtTime(currentTime);

				LLAElement element = new LLAElement(point, secondsFromUTC(startTime, currentTime),
						toUTC(eps), "Packman " + p + " {0}");

				packmenElements.get(p).add(element);
			}

			currentTime += eps;
			if (!lastLoop && currentTime > durationSeconds)
			{
				currentTime = durationSeconds;
				lastLoop = true;
			}
		}


		/* create GIS project */
		Project<Layer<LLAElement>> project = new Project<>(startTime, durationUTC, "Game {0}");

		project.addAll(packmenElements);

		for (int f = 0; f < fruits.length; f++)
		{
			Layer<LLAElement> layer = new Layer<>(startTime, toUTC(fruits[f].timeVisited()),
					"Fruit " + f);
			layer.add(new LLAElement(fruits[f].position, startTime, toUTC(fruits[f].timeVisited()), "Fruit " + f));
			project.add(layer);
		}

		/* Write it to file */
		ReadWrite.writeKML(file, project);
	}


	/**
	 * @return time in UTC of {@code seconds} seconds after {@code startTime}.
	 */
	private static long secondsFromUTC(long startTime, double seconds)
	{
		return startTime + toUTC(seconds);
	}

	private static long toUTC(double seconds)
	{
		return (long) (seconds * 1000);
	}

	private static double toSeconds(long utc)
	{
		return utc / 1000.0;
	}
}
