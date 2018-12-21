package Geom;

/**
 * Latitude, Longitude, Altitude
 *
 * @author Elisha
 */
public class LLA implements Geom_element
{
	public static final double minLatitude = -180;
	public static final double maxLatitude = +180;
	public static final double minLongitude = -90;
	public static final double maxLongitude = +90;
	public static final double minAltitude = -450;

	public static final double earth_radius = 6371 * 1000;

	private double latitude = 0, longitude = 0, altitude = 0;

	/**
	 * Creates a new LLA with the values (0,0,0).
	 */
	public LLA()
	{
		setLatitude(0);
		setLongitude(0);
		setAltitude(0);
	}

	/**
	 * Creates a new LLA with the given values.
	 *
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 */
	public LLA(double latitude, double longitude, double altitude)
	{
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
	}

	public LLA(LLA other)
	{
		this(other.getLatitude(), other.getLongitude(), other.getAltitude());
	}

	/**
	 * Creates a new LLA with the values of the {@code Point3D} (i.e. (gps.x, gps.y, gps.z)).
	 *
	 * @param gps the point to copy the values from.
	 */
	public LLA(Point3D gps)
	{
		setLatitude(gps.x());
		setLongitude(gps.y());
		setAltitude(gps.z());
	}


	/**
	 * Measures the distance in meters from this LLA point to {@code point}.
	 *
	 * @return a vector in meters of the distance from this LLA point to {@code point}.
	 */
	public Point3D distance3DVector(LLA point)
	{
		double lon_norm = Math.cos(getLatitude() * 2 * Math.PI / 360);
		LLA diff = point.subtract(this);
		Point3D dist = new Point3D(Math.sin(diff.getLatitude() * 2 * Math.PI / 360) * earth_radius,
				Math.sin(diff.getLongitude() * 2 * Math.PI / 360) * earth_radius * lon_norm,
				diff.getAltitude());

		return dist;
	}

	/**
	 * computes the 3D distance (in meters) between the two gps like points
	 *
	 * @return distance in meters from this point to {@code point}.
	 */
	public double distance3D(LLA point)
	{
		Point3D vector = this.distance3DVector(point);
		return Math.sqrt(Math.pow(vector.x(), 2) + Math.pow(vector.y(), 2) + Math.pow(vector.z(), 2));
	}

	public double distance3D(Point3D p)
	{
		Point3D dist = distance3DVector(new LLA(p));
		return Math.sqrt(Math.pow(dist.x(), 2) + Math.pow(dist.y(), 2) + Math.pow(dist.z(), 2));
	}


	@Override
	public double distance2D(Point3D p)
	{
		Point3D point2d = new Point3D(p.x(), p.y());
		return distance3D(point2d);
	}

	/**
	 * This function is the inverse of the calculations in {@code distance3DVector}.
	 *
	 * @param vector the distance in meters.
	 * @return the distance in Polar representation.
	 */
	private LLA diffMetersToPolar(Point3D vector)
	{
		double lon_norm = Math.cos(this.getLatitude() * 2 * Math.PI / 360);
		LLA ans = new LLA();
		ans.setLatitude((360 / 2 * Math.PI) * Math.asin(vector.x() / earth_radius));
		ans.setLongitude((360 / 2 * Math.PI) * Math.asin(vector.x() / (earth_radius * lon_norm)));
		ans.setAltitude(vector.z());
		return ans;
	}

	/**
	 * Calculates an LLA point which is this point transformed by {@code vector}.
	 * code from
	 * <a href = https://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of-meters>
	 * https://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of
	 * -meters</a>
	 *
	 * @param vector the vector to transform this point by.
	 * @return an LLA point which is this point transformed by {@code vector}.
	 */
	public LLA transform(Point3D vector)
	{
		//Coordinate offsets in radians
		double dLat = vector.x() / earth_radius;
		double dLon = vector.y() / (earth_radius * Math.cos(Math.PI * latitude / 180));

		//OffsetPosition, decimal degrees
		double latO = latitude + dLat * 180 / Math.PI;
		double lonO = longitude + dLon * 180 / Math.PI;

		return new LLA(latO,lonO,altitude + vector.z());

		//LLA diff = diffMetersToPolar(vector);
		//return this.add(diff);
	}

	/**
	 * Subtracts {@code point} from this point.
	 *
	 * @param point the LLA point to subtract.
	 * @return a new LLA point that is this point subtracted by {@code point}.
	 */
	public LLA subtract(LLA point)
	{
		return new LLA(getLatitude() - point.getLatitude(),
				getLongitude() - point.getLongitude(),
				getAltitude() - point.getAltitude());
	}

	/**
	 * Adds {@code point} from this point.
	 *
	 * @param point the LLA point to subtract.
	 * @return a new LLA point that is this point added by {@code point}.
	 */
	public LLA add(LLA point)
	{
		return new LLA(getLatitude() + point.getLatitude(),
				getLongitude() + point.getLongitude(),
				getAltitude() + point.getAltitude());
	}

	/**
	 * @return a new {@code Point3D} with the values of this LLA point (i.e. {@code new Point3D(lat, long, alt)}).
	 */
	public Point3D toGPSPoint()
	{
		return new Point3D(getLatitude(), getLongitude(), getAltitude());
	}

	/**
	 * @return true iff  {@code point} is a valid lat, lon , alt coordinate: [-180,+180],[-90,+90],[-450, +inf]
	 */
	public static boolean isValid_LLA_Point(Point3D point)
	{
		return isValid_LLA_Point(point.x(), point.y(), point.z());
	}

	/**
	 * @return true iff  {@code (latitude, longitude, altitude)} is a valid lat, lon , alt coordinate: [-180,+180],
	 * [-90,+90],[-450, +inf]
	 */
	public static boolean isValid_LLA_Point(double latitude, double longitude, double altitude)
	{
		return (minLatitude <= latitude && latitude <= maxLatitude)
				&& (minLongitude <= longitude && longitude <= maxLongitude)
				&& (minAltitude <= altitude);
	}

	public double getLatitude()
	{
		return latitude;
	}

	public boolean setLatitude(double latitude)
	{
		if (minLatitude <= latitude && latitude <= maxLatitude)
		{
			this.latitude = latitude;
			return true;
		}
		return false;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public boolean setLongitude(double longitude)
	{
		if (minLongitude <= longitude && longitude <= maxLongitude)
		{
			this.longitude = longitude;
			return true;
		}
		return false;
	}

	public double getAltitude()
	{
		return altitude;
	}

	public boolean setAltitude(double altitude)
	{
		if (minAltitude <= altitude)
		{
			this.altitude = altitude;
			return true;
		}
		return false;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public LLA clone()
	{
		return new LLA(this);
	}

	@Override
	public String toString()
	{
		return "LLA{" + latitude + ", " + longitude + ", " + altitude + '}';
	}
}
