package Algorithms.TSP;

import Geom.LLA;
import Geom.Point3D;

/**
 * @author Elisha
 */
public class PositionAndDirection
{
	public final LLA position;
	public final Point3D direction;

	public PositionAndDirection(LLA position, Point3D direction)
	{
		this.position = position;
		this.direction = direction;
	}
}
