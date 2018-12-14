package File_format;

import GIS.LLAElement;
import GIS.Layer;
import GIS.Project;
import Geom.LLA;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 *
 * @author Elisha
 */
public class ReadWrite
{
	public static final String csvSplitRegex = ",";

	public static String[][] readCSV(File file)
	{
		List<String[]> csvFile = new LinkedList<>();
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			while ((line = br.readLine()) != null)
			{
				csvFile.add(line.split(csvSplitRegex));
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return csvFile.toArray(new String[csvFile.size()][]);
	}

	public static void writeCSV(File file, String[][] csvFile)
	{
		StringBuilder line;

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
		{
			for (int i = 0; i < csvFile.length; i++)
			{
				if (csvFile[i].length > 0)
				{
					line = new StringBuilder(csvFile[i][0]);
					for (int j = 1; j < csvFile[i].length; j++)
					{
						line.append(csvSplitRegex).append(csvFile[i][j]);
					}
					bw.write(line.toString());
				}
				bw.newLine();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Converts a file in the format of {@code String[][]}
	 * (i.e the output of {@link #readCSV(File)}) to a {@link GIS.Layer} object.
	 *
	 * @param file           a csv-like file int the form {@code String[][]}.
	 * @param time           the start of the timespan for {@code Layer}'s Data.
	 * @param duration       the duration of the timespan.
	 * @param name           the name for {@code Layer}'s Data.
	 * @param fileStartIndex ignores lines before this index.
	 * @param latIndex       the line index of the latitude values.
	 * @param lonIndex       the line index of the longitude values.
	 * @param altIndex       the line index of the altitude values (set to -1 if there isn't one. the altitude will be
	 *                       set to 0).
	 * @param nameIndex      the line index of the new for LLAElement's Data (set to -1 if there isn't one. name will
	 *                       be set to "").
	 * @return a {@link GIS.Layer} object representing {@code file}.
	 */
	public static Layer<LLAElement> stringsToLayer(String[][] file, long time, long duration, String name,
												   int fileStartIndex,
												   int latIndex, int lonIndex, int altIndex, int nameIndex,
												   int timeIndex, String timePattern)
	{
		Layer<LLAElement> layer = new Layer<>(time, duration, name);

		for (int i = fileStartIndex; i < file.length; i++)
		{
			String[] line = file[i];
			double lat, lon, alt;
			String elementName;
			long utc;
			if (0 <= latIndex && latIndex < line.length)
			{
				lat = Double.parseDouble(line[latIndex]);
			} else
			{
				throw new ArrayIndexOutOfBoundsException("latIndex is out of bounds");
			}
			if (0 <= lonIndex && lonIndex < line.length)
			{
				lon = Double.parseDouble(line[lonIndex]);
			} else
			{
				throw new ArrayIndexOutOfBoundsException("lonIndex is out of bounds");
			}
			if (0 <= altIndex && altIndex < line.length)
			{
				alt = Double.parseDouble(line[altIndex]);
			} else if (altIndex == -1)
			{
				alt = 0;
			} else
			{
				throw new ArrayIndexOutOfBoundsException("altIndex is out of bounds. Set to -1 if you don't want to " +
						"use it");
			}

			if (0 <= nameIndex && nameIndex < line.length)
			{
				elementName = line[nameIndex];
			} else if (nameIndex == -1)
			{
				elementName = null;
			} else
			{
				throw new ArrayIndexOutOfBoundsException("nameIndex is out of bounds. Set to -1 if you don't want to" +
						" " +
						"use it");
			}

			if (0 <= timeIndex && timeIndex < line.length)
			{
				DateFormat df = new SimpleDateFormat(timePattern);
				try
				{
					utc = df.parse(line[timeIndex]).getTime();
				} catch (ParseException e)
				{
					e.printStackTrace();
				}
			} else if (timeIndex == -1)
			{
				time = (new Date()).getTime();
			} else
			{
				throw new ArrayIndexOutOfBoundsException("nameIndex is out of bounds. Set to -1 if you don't want to" +
						" " +
						"use it");
			}
			layer.add(new LLAElement(new LLA(lat, lon, alt), time, duration, (elementName == null) ? "" :
					elementName));
		}

		return layer;
	}

	/**
	 * Converts a file in the format of {@code String[][]} (i.e the output of {@link #readCSV(File)}) to a
	 * {@link GIS.Layer}
	 * object.
	 *
	 * @param file          a csv-like file int the form {@code String[][]}.
	 * @param time          the start of the timespan for {@code Layer}'s Data.
	 * @param duration      the duration of the timespan.
	 * @param name          the name for {@code Layer}'s Data.
	 * @param menuIndex     the index of the menu line (usually 0). Lines before this one will be ignored.
	 * @param latMenuValue  Latitude's value in the menu.
	 * @param lonMenuValue  Longitude's value in the menu.
	 * @param altMenuValue  Altitude's value in the menu.
	 * @param nameMenuValue the line's name's value in the menu.
	 * @param timeMenuValue the lin's currentTime's value in the menu.
	 * @param timePattern   the format the currentTime is in (i.e. "yyyy-MM-dd HH:mm:ss").
	 * @return a {@link GIS.Layer} object representing {@code file}.
	 */
	public static Layer<LLAElement> stringsToLayer(String[][] file, long time, long duration, String name,
												   int menuIndex,
												   String latMenuValue, String lonMenuValue, String altMenuValue,
												   String nameMenuValue,
												   String timeMenuValue, String timePattern)
	{
		String[] menuLine = file[menuIndex];
		int latIndex = -1, lonIndex = -1, altIndex = -1, nameIndex = -1, timeIndex = -1;
		for (int i = 0; i < menuLine.length; i++)
		{
			String currentValue = menuLine[i].trim();

			if (latMenuValue.equals(currentValue))
				latIndex = i;

			if (lonMenuValue.equals(currentValue))
				lonIndex = i;

			if (altMenuValue != null && altMenuValue.equals(currentValue))
				altIndex = i;

			if (nameMenuValue != null && nameMenuValue.equals(currentValue))
				nameIndex = i;

			if (timeMenuValue != null && timeMenuValue.equals(currentValue))
				timeIndex = i;

		}
		if (latIndex == -1)
		{
			throw new IllegalArgumentException("Latitude menu value \"" + latMenuValue + "\" was not found");
		}

		if (lonIndex == -1)
		{
			throw new IllegalArgumentException("Longitude menu value \"" + latMenuValue + "\" was not found");
		}
		if (altIndex == -1 && altMenuValue != null && !altMenuValue.isEmpty())
		{
			throw new IllegalArgumentException("Altitude menu value \"" + latMenuValue + "\" was not found");
		}
		if (nameIndex == -1 && nameMenuValue != null && !nameMenuValue.isEmpty())
		{
			throw new IllegalArgumentException("Name menu value \"" + nameMenuValue + "\" was not found");
		}
		if (timeIndex == -1 && timeMenuValue != null && !timeMenuValue.isEmpty())
		{
			throw new IllegalArgumentException("Time menu value \"" + timeMenuValue + "\" was not found");
		}
		return stringsToLayer(file, time, duration, name, menuIndex + 1, latIndex, lonIndex, altIndex, nameIndex,
				timeIndex,
				timePattern);
	}


	/**
	 * Converts a file in the format of {@code String[][]} (i.e the output of {@code readCSV}) to a {@code GIS.Layer}
	 * object.
	 *
	 * @param file     a csv-like file int the form {@code String[][]}.
	 * @param time     the start of the timespan for {@code Layer}'s Data.
	 * @param duration the duration of the timespan.
	 * @param name     the name for {@code Layer}'s Data.
	 * @param settings a {@code MenuSettings} object with the settings.
	 * @return a {@code GIS.Layer} object representing {@code file}.
	 */
	public static Layer<LLAElement> stringsToLayer(String[][] file, long time, long duration, String name,
												   CSV_MenuSettings settings)
	{
		return stringsToLayer(file, time, duration, name, settings.menuIndex, settings.latMenuValue,
				settings.lonMenuValue,
				settings.altMenuValue,
				settings.nameMenuValue, settings.timeMenuValue, settings.timePattern);
	}

	public static void writeKML(File file, Project<Layer<LLAElement>> project)
	{
		final Kml kml = new Kml();
		Folder folder = kml.createAndSetFolder().withName(project.get_Meta_data().toString());
		for (Layer<LLAElement> layer : project)
		{
			Document document = folder.createAndAddDocument().withName(layer.get_Meta_data().toString());
			for (LLAElement element : layer)
			{
				Placemark placemark =
						document.createAndAddPlacemark().withName(element.getData().toString()).withOpen(Boolean.TRUE);
				placemark.createAndSetPoint()
						.addToCoordinates(element.getGeom().getLongitude(), element.getGeom().getLatitude(),
								element.getGeom().getAltitude());
				placemark.createAndSetTimeSpan().withBegin(String.valueOf(element.getData().getUTC()))
						.withEnd(String.valueOf(element.getData().getUTC() + element.getData().getDuration()));
			}
		}
		try
		{
			kml.marshal(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
