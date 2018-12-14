package GIS;

import Geom.Point3D;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Elisha
 */
public class Data implements Meta_data
{

	/**
	 * Creates a new Data.
	 *
	 * @param string String for the toString method.
	 *               "{0}" will be replaced with {@code currentTime} in the format "MM/dd/yyyy HH:mm:ss".
	 *               "{1}" will be replaced with {@code orientation.toString()}.
	 */
	public Data(long time,long duration, Point3D orientation, String string)
	{
		this.time = time;
		this.duration = duration;
		this.orientation = orientation;
		if (string != null)
		{
			this.string = string;
		}
	}

	private long time;
	private long duration;
	private Point3D orientation;
	public String string = "Data({0}, {1})";

	/**
	 * returns the Universal Time Clock associated with this data;
	 */
	@Override
	public long getUTC()
	{
		return time;
	}

	/** returns the Universal Time Clock duration associated with this data; */
	@Override
	public long getDuration()
	{
		return duration;
	}

	/**
	 * @return the orientation: yaw, pitch and roll associated with this data;
	 */
	@Override
	public Point3D get_Orientation()
	{
		return orientation;
	}

	@Override
	public String toString()
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return MessageFormat.format(string, dateFormat.format(time), orientation.toString());
	}
}
