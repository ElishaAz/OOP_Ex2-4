package Packman;

import Geom.LLA;
import Geom.Point3D;

import java.awt.*;
import java.util.Dictionary;

/**
 * @author Elisha
 */
public class Map
{
	public final Image map;
	public final LLA top_left, bottom_right;

	private int screenWidth;
	private int screenHeight;

	public Map(Image map, LLA top_left, LLA bottom_right)
	{
		this.map = map;
		this.top_left = top_left;
		this.bottom_right = bottom_right;
	}

	public void updateScreenSize(int screenWidth, int screenHeight)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	public Point worldToPixel(LLA point) // (point pixels) = (point meters)/(screen meters) * (screen pixels)
	{
		final Point3D screenMeters = top_left.distance3DVector(bottom_right);
		final Point3D pointMeters = top_left.distance3DVector(point);

		double x = (pointMeters.x() / screenMeters.x()) * screenWidth;
		double y = (pointMeters.y() / screenMeters.y()) * screenHeight;

		return new Point((int) (x), (int) (y));
	}

	public Point worldToPixel(Point3D meters)
	{
		final Point3D screenMeters = top_left.distance3DVector(bottom_right);

		double x = (meters.x() / screenMeters.x()) * screenWidth;
		double y = (meters.y() / screenMeters.y()) * screenHeight;

		return new Point((int) (x), (int) (y));
	}


	public LLA pixelToWorld(Point point) // (point meters) = (point pixels)/(screen pixels) * (screen meters)
	{
		final Point3D screenMeters = top_left.distance3DVector(bottom_right);

		double x = (point.x / (double) screenWidth) * screenMeters.x();
		double y = (point.y / (double) screenHeight) * screenMeters.y();

		return top_left.transform(new Point3D(x,y));
	}


	public int getScreenWidth()
	{
		return screenWidth;
	}

	public int getScreenHeight()
	{
		return screenHeight;
	}
}