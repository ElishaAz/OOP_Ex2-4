package GIS;

import Geom.Point3D;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * @author Elisha
 */
public class Layer<E extends GIS_element> extends HashSet<E> implements GIS_layer<E>
{
	public static final long defaultDuration = 1000;

	public Layer(long time,long duration, Point3D orientation, String name)
	{
		this.time = time;
		this.duration = duration;
		this.orientation = orientation;
		this.name = name;
	}

	public Layer(long time,long duration, String name)
	{
		this(time,duration, new Point3D(0, 0, 0), name);
	}

	public Layer(String name)
	{
		this((new Date()).getTime(),defaultDuration, name);
	}

	public Layer()
	{
		this("Layer {0}");
	}

	private long time, duration;
	private Point3D orientation;
	private String name;

	@Override
	public Data get_Meta_data()
	{
		return new Data(time,duration, orientation, name);
	}
}
