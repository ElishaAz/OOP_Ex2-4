package GIS;

import Geom.LLA;
import Geom.Point3D;

import java.util.Date;

/**
 * @author Elisha
 */
public class LLAElement implements GIS_element
{
	private LLA point;
	private long time;
	private Point3D orientation;
	private String name;

	public LLAElement(LLA point, long time, Point3D orientation, String name)
	{
		this.point = point;
		this.time = time;
		this.orientation = orientation;
		this.name = name;
	}
	public LLAElement(LLA point, long time, String name)
	{
		this(point,time,new Point3D(0,0,0),name);
	}
	public LLAElement(LLA point, String name)
	{
		this(point, (new Date()).getTime(), name);
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
		return new Data(time, orientation, name);
	}

	@Override
	public void translate(Point3D vec)
	{
		point.transform(vec);
	}
}
