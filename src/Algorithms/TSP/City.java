package Algorithms.TSP;

import Geom.LLA;
import Geom.Point3D;

import java.util.List;

/**
 * @author Elisha
 */
public class City implements ICity
{
	public final LLA position;
	protected boolean _visited;
	protected double _timeVisited;

	public City(LLA position)
	{
		this.position = position.clone();
		_visited = false;
		_timeVisited = Double.NEGATIVE_INFINITY;
	}

	public City(City other)
	{
		position = other.position.clone();
		_visited = other._visited;
		_timeVisited = other._timeVisited;

	}

	@Override
	public boolean wasVisited(double time)
	{
		return time >= _timeVisited;
	}

	/**
	 * Visit this city.
	 *
	 * @return false if this city was already visited.
	 */
	@Override
	public boolean visit(double time)
	{
		if (_visited)
			return false;
		_visited = true;
		_timeVisited = time;
		return true;
	}

	/**
	 * Check if this city was visited already.
	 *
	 * @return false if {@link #visit(double)} was already called.
	 */
	@Override
	public boolean visited()
	{
		return _visited;
	}

	/**
	 * @return the time this city was visited.
	 */
	@Override
	public double timeVisited()
	{
		return _timeVisited;
	}


	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public City clone()
	{
		return new City(this);
	}

	@Override
	public String toString()
	{
		return "City{" +
				"position=" + position +
				", _visited=" + _visited +
				", _timeVisited=" + _timeVisited +
				'}';
	}
}
