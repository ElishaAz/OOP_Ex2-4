package GIS;

import Geom.Point3D;

import java.util.Date;
import java.util.HashSet;

/**
 * @author Elisha
 */
public class Project<P extends GIS_layer> extends HashSet<P> implements GIS_project<P>
{
	public static final long defaultDuration = 1000;

	public Project(long time, long duration, Point3D orientation, String name)
	{
		this.time = time;
		this.duration = duration;
		this.orientation = orientation;
		this.name = name;
	}

	public Project(long time, long duration, String name)
	{
		this(time, duration, new Point3D(0, 0, 0), name);
	}

	public Project(String name)
	{
		this((new Date()).getTime(), defaultDuration, name);
	}

	public Project()
	{
		this("Project {0}");
	}

	private long time, duration;
	private Point3D orientation;
	private String name;

	@Override
	public Data get_Meta_data()
	{
		return new Data(time, duration, orientation, name);
	}
}
