package GIS;

import Geom.Point3D;

import java.util.Date;
import java.util.HashSet;

/**
 * @author Elisha
 */
public class Project<P extends GIS_layer> extends HashSet<P> implements GIS_project<P>
{
	public Project(long time, Point3D orientation, String name)
	{
		this.time = time;
		this.orientation = orientation;
		this.name = name;
	}

	public Project(long time, String name)
	{
		this(time, new Point3D(0, 0, 0), name);
	}

	public Project(String name)
	{
		this((new Date()).getTime(), name);
	}

	public Project()
	{
		this("Project {0}");
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
