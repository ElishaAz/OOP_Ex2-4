package Coords;

import Geom.LLA;
import Geom.Point3D;

/**
 * @author Elisha
 */
public class MyCoords implements coords_converter
{
	/**
	 * computes a new point which is the gps point transformed by a 3D vector (in meters)
	 *
	 * @param gps
	 * @param local_vector_in_meter
	 */
	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter)
	{
		return (new LLA(gps)).transform(local_vector_in_meter).toGPSPoint();
	}

	/**
	 * computes the 3D distance (in meters) between the two gps like points
	 *
	 * @param gps0
	 * @param gps1
	 */
	@Override
	public double distance3d(Point3D gps0, Point3D gps1)
	{
		Point3D vector = vector3D(gps0, gps1);
		return Math.sqrt(Math.pow(vector.x(), 2) + Math.pow(vector.y(), 2) + Math.pow(vector.z(), 2));
	}

	/**
	 * computes the 3D vector (in meters) between two gps like points
	 *
	 * @param gps0
	 * @param gps1
	 */
	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1)
	{
		return (new LLA(gps0)).distance3DVector(new LLA(gps1));
	}

	/**
	 * computes the polar representation of the 3D vector be gps0-->gps1
	 * <p>
	 * Note: this method should return an azimuth (aka yaw), elevation (pitch), and distance
	 * yaw / pitch function taken from
	 * <a href="https://stackoverflow.com/questions/24026418/how-to-convert-latitude-and-longitude-to-orientation-yaw-pitch-roll">
	 * https://stackoverflow.com/questions/24026418/how-to-convert-latitude-and-longitude-to-orientation-yaw-pitch
	 * -roll</a>
	 *
	 * @param gps0
	 * @param gps1
	 */
	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1)
	{
		double distance = distance3d(gps0, gps1);
		Point3D vec = vector3D(gps0, gps1);
		double yaw = (vec.x() == 0 || vec.z() == 0) ? 0 : -Math.atan2(vec.x(), -vec.z());
		double pitch = (vec.x() == 0 || vec.y() == 0) ? 0 : Math.atan2(vec.y(),
				Math.sqrt(Math.pow(vec.x(), 2) + Math.pow(vec.z(), 2)));
		return new double[]{yaw, pitch, distance};
	}

	/**
	 * return true iff this point is a valid lat, lon , alt coordinate: [-180,+180],[-90,+90],[-450, +inf]
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isValid_GPS_Point(Point3D p)
	{
		return LLA.isValid_LLA_Point(p);
	}
}
