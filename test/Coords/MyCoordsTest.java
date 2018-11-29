package Coords;

import Geom.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Elisha
 */
class MyCoordsTest
{
	public static final MyCoords coords = new MyCoords();

	@Test
	void add()
	{
		Point3D a = new Point3D(32.103315,35.209039,670);
		Point3D vec = new Point3D(0,0,0);
		Point3D b = coords.add(a,vec);
		assertEquals(a.toString(),b.toString());
	}

	@Test
	void distance3d()
	{
		Point3D a = new Point3D(32.103315,35.209039,670);
		Point3D b = new Point3D(32.106352,35.205225,650);
		double dist = coords.distance3d(a,b);
		assertEquals((int) 493.0523318,(int) dist);
	}

	@Test
	void vector3D()
	{
		Point3D a = new Point3D(32.103315,35.209039,670);
		Point3D b = new Point3D(32.106352,35.205225,650);
		Point3D vec = coords.vector3D(a,b);
		Point3D expect = new Point3D((int)337.6989921, (int)-359.2492069, -20);
		vec = new Point3D((int)vec.x(),(int) vec.y(),(int) vec.z());
		assertEquals(expect.toString(),vec.toString());

		Point3D c = new Point3D(32.103315,35.209039,670);
		vec = coords.vector3D(a,c);
		assertEquals(new Point3D(0,0,0).toString(),vec.toString());
	}

	@Test
	void azimuth_elevation_dist()
	{
		Point3D a = new Point3D(32.103315,35.209039,670);
		Point3D b = new Point3D(32.103315,35.209039,670);
		double[] ans = coords.azimuth_elevation_dist(a,b);
		assertEquals(3,ans.length);
		for (int i = 0; i < 3; i++)
		{
			assertEquals(0,ans[i]);
		}
	}

	@Test
	void isValid_GPS_Point()
	{
		assertTrue(coords.isValid_GPS_Point(new Point3D(0,34,1045)));
		assertTrue(coords.isValid_GPS_Point(new Point3D(-20,-89,-30)));

		assertFalse(coords.isValid_GPS_Point(new Point3D(-370,-89,-30)));
		assertFalse(coords.isValid_GPS_Point(new Point3D(-350,-180,-30)));
		assertFalse(coords.isValid_GPS_Point(new Point3D(50,1000,20)));
	}
}