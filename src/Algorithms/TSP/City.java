package Algorithms.TSP;

import Geom.LLA;
import Geom.Point3D;

import java.util.List;

/**
 * @author Elisha
 */
public class City
{
	public final LLA position;
	private boolean _visited;

	public City(LLA position)
	{
		this.position = position;
		_visited = false;
	}

	public City(City other)
	{
		position = other.position;
		_visited = other._visited;
	}

	/**
	 * Visit this city.
	 * @return false if this city was already visited.
	 */
	public boolean visit()
	{
		if (_visited)
			return false;
		_visited = true;
		return true;
	}

	/**
	 * Check if this city was visited already.
	 * @return false if {@link #visit()} was already called.
	 */
	public boolean visited()
	{
		return _visited;
	}



	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public City clone()
	{
		return new City(this);
	}
}
