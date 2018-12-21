package Packman;

import File_format.ReadWrite;
import Geom.LLA;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Elisha
 */
public class Game
{
	public static final int typeIndex = 0, idIndex = 1, latIndex = 2, lonIndex = 3,
			altIndex = 4, speedIndex = 5, weightIndex = 5, radiusIndex = 6,
			packmanCountIndex = 7, fruitCountIndex = 8;
	public static final String packmanString = "P", fruitString = "F";
	public static final double altDefault = 0, speedDefault = 1, weightDefault = 1, radiusDefault = 1;
	public static final String menuString = "Type,id,Lat,Lon,Alt,Speed/Weight,Radius,{0},{1}";

	public List<Packman> packmen;
	public List<Fruit> fruits;

	public Game(List<Packman> pacemen, List<Fruit> fruits)
	{
		this.packmen = pacemen;
		this.fruits = fruits;
	}

	public Game(File csvFile)
	{
		String[][] file = ReadWrite.readCSV(csvFile);

		int packmanCount = Integer.parseInt(file[0][packmanCountIndex]);
		int fruitCount = Integer.parseInt(file[0][fruitCountIndex]);

		packmen = new ArrayList<Packman>(packmanCount);
		fruits = new ArrayList<Fruit>(fruitCount);

		for (int i = 1; i < file.length; i++) // start from 1 to skip menu
		{
			String[] line = file[i];

			/* position */
			double latitude, longitude, altitude;

			latitude = Double.parseDouble(line[latIndex]);

			longitude = Double.parseDouble(line[lonIndex]);

			if (line[altIndex].trim().isEmpty())
			{
				altitude = altDefault;
			} else
			{
				altitude = Double.parseDouble(line[altIndex]);
			}

			LLA position = new LLA(latitude, longitude, altitude);

			/* speed */
			double speed;
			if (line[speedIndex].trim().isEmpty())
			{
				speed = speedDefault;
			} else
			{
				speed = Double.parseDouble(line[speedIndex]);
			}

			/* weight */
			double weight;
			if (line[weightIndex].trim().isEmpty())
			{
				weight = weightDefault;
			} else
			{
				weight = Double.parseDouble(line[weightIndex]);
			}

			/* radius */
			double radius;
			if (line[radiusIndex].trim().isEmpty())
			{
				radius = radiusDefault;
			} else
			{
				radius = Double.parseDouble(line[radiusIndex]);
			}

			/* type */
			switch (line[typeIndex])
			{
				case packmanString:
					Packman p = new Packman(position, speed, radius);
					packmen.add(p);
					break;
				case fruitString:
					Fruit f = new Fruit(position, weight);
					fruits.add(f);
					break;
			}
		}
	}

	public Game()
	{
		this.packmen = new ArrayList<Packman>();
		this.fruits = new ArrayList<Fruit>();
	}

	private static String[] newStringArray(int length)
	{
		String[] array = new String[length];
		for (int i = 0; i < length; i++)
		{
			array[i] = "";
		}
		return array;
	}

	private int max(int... args)
	{
		int ans = args[0];

		for (int i : args)
		{
			ans = Integer.max(ans, i);
		}
		return ans;
	}

	public void saveToCSV(File file)
	{
		String[][] csvFile = new String[1 + packmen.size() + fruits.size()][];


		String[] menu = menuString.split(MessageFormat.format(menuString, packmen.size(), fruits.size()), -1);
		int menuLength = max(typeIndex, idIndex, latIndex, lonIndex,
				altIndex, speedIndex, weightIndex, radiusIndex,
				packmanCountIndex, fruitCountIndex);
		csvFile[0] = menu;

		boolean setPackman = true;
		int p = 0, f = 0;
		for (int i = 1; i < csvFile.length; i++)
		{
			String[] line = newStringArray(menuLength);
			LLA position = null;
			int id = -1;

			if (setPackman)
			{
				id = p;
				position = packmen.get(p).getPosition();

				line[typeIndex] = packmanString;
				line[speedIndex] = String.valueOf(packmen.get(p).getSpeed());
				line[radiusIndex] = String.valueOf(packmen.get(p).getRadius());

				p++;
				if (p >= packmen.size())
					setPackman = false;
			} else
			{

				id = f;
				position = fruits.get(p).position;
				line[typeIndex] = fruitString;
				line[weightIndex] = String.valueOf(fruits.get(p).weight);

				f++;
			}

			/* Latitude, Longitude, Altitude */
			if (position != null)
			{
				line[latIndex] = String.valueOf(position.getLatitude());
				line[lonIndex] = String.valueOf(position.getLongitude());
				line[altIndex] = String.valueOf(position.getAltitude());
			}

			/* ID */
			line[idIndex] = String.valueOf(id);

			csvFile[i] = line;
		}

		ReadWrite.writeCSV(file, csvFile);
	}

	public void addPackman(Packman p)
	{
		packmen.add(p.clone());
	}

	public void addFruit(Fruit f)
	{
		fruits.add(f.clone());
	}

	/**
	 * @return a copy of all the fruits.
	 */
	public Packman[] getPackmen()
	{
		Packman[] pms = new Packman[packmen.size()];

		for (int i = 0; i < packmen.size(); i++)
		{
			pms[i] = packmen.get(i).clone();
		}
		return pms;
	}

	public Fruit[] getFruits()
	{
		Fruit[] fs = new Fruit[fruits.size()];

		for (int i = 0; i < fruits.size(); i++)
		{
			fs[i] = fruits.get(i).clone();
		}
		return fs;
	}
}
