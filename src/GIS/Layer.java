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
	public Layer(long time, Point3D orientation, String name)
	{
		this.time = time;
		this.orientation = orientation;
		this.name = name;
	}

	public Layer(long time, String name)
	{
		this(time, new Point3D(0, 0, 0), name);
	}

	public Layer(String name)
	{
		this((new Date()).getTime(), name);
	}

	public Layer()
	{
		this("Layer {0}");
	}

	long time;
	Point3D orientation;
	String name;

	@Override
	public Data get_Meta_data()
	{
		return new Data(time, orientation, name);
	}
}
