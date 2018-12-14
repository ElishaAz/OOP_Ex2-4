package File_format;

import GIS.LLAElement;
import GIS.Layer;
import GIS.Project;

import java.io.File;
import java.util.Objects;

/**
 * @author Elisha
 */
public class MultiCSV
{
	public static Project<Layer<LLAElement>> readFolder(String folderName, CSV_MenuSettings settings)
	{
		File file = new File(folderName);

		Project<Layer<LLAElement>> projct = new Project<>();
		if (file.isDirectory())
		return readFolderRe(file, projct, settings);
		else
			return projct;
	}

	private static Project<Layer<LLAElement>> readFolderRe(final File folder, Project<Layer<LLAElement>> project,
														   CSV_MenuSettings settings)
	{
		for (final File file : Objects.requireNonNull(folder.listFiles()))
		{
			if (file.isDirectory())
			{
				project = readFolderRe(file, project, settings);
			} else
			{
				String name = file.getName();
				int lastIndex = name.lastIndexOf('.');
				if (lastIndex >= 0)
				{
					String ending = name.substring(lastIndex);
					String nameNoEnding = name.substring(0, lastIndex);
					if (ending.equalsIgnoreCase(".csv"))
					{
						String[][] stringFile = ReadWrite.readCSV(file);
						Layer<LLAElement> layer = ReadWrite.stringsToLayer(stringFile, file.lastModified(),
								nameNoEnding, settings);
						project.add(layer);
					}
				}
			}
		}
		return project;
	}
}
