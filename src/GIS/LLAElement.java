package GIS;

import Geom.LLA;
import Geom.Point3D;

import java.util.Date;

/**
 * @author Elisha
 */
public class LLAElement implements GIS_element
{
	public static final long defaultDuration = 1000;
	private LLA point;
	private long time, duration;
	private Point3D orientation;
	private String name;

	public LLAElement(LLA point, long time, long duration, Point3D orientation, String name)
	{
		this.point = point;
		this.time = time;
		this.duration = duration;
		this.orientation = orientation;
		this.name = name;
	}
	public LLAElement(LLA point, long time, long duration, String name)
	{
		this(point,time,duration,new Point3D(0,0,0),name);
	}
	public LLAElement(LLA point, String name)
	{
		this(point, (new Date()).getTime(),defaultDuration, name);
	}
	public LLAElement()
	{
		this(new LLA(),"Element {0}");
	}

	@Override
	public LLA getGeom()
	{
		return point;
	}

	@Override
	public Data getData()
	{
		return new Data(time,duration, orientation, name);
	}

	@Override
	public void translate(Point3D vec)
	{
		point.transform(vec);
	}
}
