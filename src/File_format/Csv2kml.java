package File_format;

import GIS.LLAElement;
import GIS.Layer;
import GIS.Project;

import java.io.File;

/**
 * @author Elisha
 */
public class Csv2kml
{
	public static boolean convert(String inFileName, String outFileName, CSV_MenuSettings settings, long duration)
	{
		File in = new File(inFileName);
		File out = new File(outFileName);
		if (!in.exists())
			return false;
		String[][] file = ReadWrite.readCSV(in);
		Layer<LLAElement> layer = ReadWrite.stringsToLayer(file, in.lastModified(),duration, in.getName(), settings);
		Project<Layer<LLAElement>> project = new Project<>();
		project.add(layer);
		ReadWrite.writeKML(out, project);
		return true;
	}
}
