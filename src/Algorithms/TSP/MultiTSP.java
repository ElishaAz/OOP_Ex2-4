package Algorithms.TSP;

import Geom.LLA;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a class is a solution to the Multiple Traveling Salesmen Problem (mTSP) with some differences:
 * <lbk/> it doesn't bring the salesmen back to their starting city.
 * <lbk/> the salesmen have speed.
 * Note: this algorithm that is used here is not optimal.
 *
 * @author Elisha
 */
public class MultiTSP implements mTSP
{

	private City[] cities;
	private Salesman[] salesmen;

	/**
	 * This is a simple class containing to indexes for use in {@link MultiTSP}, one of salesmen, and one of cities.
	 */
	public static class SalesmanToCity
	{
		public final int salesmanIndex;
		public final int cityIndex;

		SalesmanToCity(int salesmanIndex, int cityIndex)
		{
			this.salesmanIndex = salesmanIndex;
			this.cityIndex = cityIndex;
		}
	}

	/**
	 * Initiate MutiTSP with given cities and salesmen.
	 *
	 * @param salesmen the salesmen for the problem.
	 * @param cities   the cities for the problem.
	 */
	public MultiTSP(Salesman[] salesmen, City[] cities)
	{
		this.salesmen = salesmen;
		this.cities = cities;
	}

	/**
	 * Computes the algorithm.
	 */
	@Override
	public void compute()
	{
		boolean finished = false;
		while (!finished)
		{
			SalesmanToCity nextMove = next();

			if (nextMove == null)
				finished = true;
			else
				visit(nextMove);
		}
	}

	/**
	 * Calculates the best next visit, using a greedy algorithm (no optimal!).
	 *
	 * @return a {@link SalesmanToCity} object with the index of a salesman
	 * and an index of the city he should travel to.
	 * @see SalesmanToCity
	 */
	private SalesmanToCity next()
	{
		if (salesmen.length == 0 || cities.length == 0)
			return null;

		updateDistances();


		/* Finds the salesman that is closest to a city
		*
		{*/

		double minDistance = Double.POSITIVE_INFINITY;
		int salesmanIndex = -1;
		int cityIndex = -1;
		for (int s = 0; s < salesmen.length; s++)
		{
			cityIndex = getClosestCity(s);
			double currentDistance = distances[s][cityIndex];
			if (currentDistance <= minDistance)
			{
				minDistance = currentDistance;
				salesmanIndex = s;
			}
		}

		if (salesmanIndex == -1 || cityIndex == -1)
			return null;
		/*
		}*/

		SalesmanToCity ans = new SalesmanToCity(salesmanIndex, cityIndex);
		return ans;
	}

	private void updateDistances()
	{
		if (distances == null || distances.length != salesmen.length ||
				(salesmen.length >= 1 && distances[0].length != cities.length))
			// null, length isn't salesmen.length, and if salesmen.length is bigger than 0, length isn't cities.length
			distances = new double[salesmen.length][cities.length];

		for (int s = 0; s < salesmen.length; s++)
		{
			for (int c = 0; c < cities.length; c++)
			{
				distances[s][c] = salesmen[s].position.distance3D(cities[c].position) / salesmen[s].speed;
			}
		}
	}

	/**
	 * Finds the closest city to the salesman at the index {@code salesmanIndex}.
	 *
	 * @param salesmanIndex index of the salesman.
	 * @return the index of the city that is the closest to {@code salesmanIndex}.
	 */
	private int getClosestCity(int salesmanIndex)
	{
		double distance = Double.POSITIVE_INFINITY;
		int index = -1;
		double[] citiesDistances = distances[salesmanIndex];
		for (int i = 0; i < citiesDistances.length; i++)
		{
			if (citiesDistances[i] <= distance)
			{
				distance = citiesDistances[i];
				index = i;
			}
		}
		return index;
	}

	private double[][] distances;

	/**
	 * Tells {@code salesmanToCity}'s salesman to travel to {@code salesmanToCity}'s city.
	 *
	 * @param salesmanToCity index of salesman that will travel and the index of the city it will travel to.
	 * @return false if {@code salesmanToCity}'s city was visited already.
	 * @see SalesmanToCity
	 */
	public boolean visit(SalesmanToCity salesmanToCity)
	{
		return visit(salesmanToCity.salesmanIndex, salesmanToCity.cityIndex);
	}

	/**
	 * Tells {@code salesmanIndex} to travel to {@code cityIndex}.
	 *
	 * @param salesmanIndex the index of the salesman that will travel.
	 * @param cityIndex     the index of the city that the salesman will travel to.
	 * @return false if {@code cityIndex} was visited already.
	 */
	@Override
	public boolean visit(int salesmanIndex, int cityIndex)
	{
		if (0 <= salesmanIndex && salesmanIndex < salesmen.length &&
				0 <= cityIndex && cityIndex < cities.length)
		{
			return salesmen[salesmanIndex].travelTo(cities[cityIndex]);
		} else
		{
			throw new IndexOutOfBoundsException("index is out of bounds");
		}
	}

	/**
	 * Checks if all the cities were visited.
	 *
	 * @return true if all the cities were visited.
	 */
	@Override
	public boolean allCitiesVisited()
	{
		for (City city : cities)
		{
			if (!city.visited())
				return false;
		}
		return true;
	}

	/**
	 * Calculates the position and direction of all the salesmen at the time {@code time}.
	 *
	 * @param time time from the beginning of the Algorithm.
	 * @return an array of the positions of all the salesmen at the time {@code time}.
	 */
	@Override
	public PositionAndDirection[] getPositionsAtTime(double time)
	{
		PositionAndDirection[] times = new PositionAndDirection[salesmen.length];
		for (int i = 0; i < salesmen.length; i++)
		{
			times[i] = salesmen[i].getMoveAtTime(time).getPositionAndDirectionAtTime(time);
		}

		return times;
	}

	/**
	 * @return a list of move records for each of the salesmen.
	 */
	@Override
	public List<List<MoveRecord>> getAllRecords()
	{
		List<List<MoveRecord>> recordLists = new ArrayList<>(salesmen.length);
		for (Salesman salesman : salesmen)
		{
			recordLists.add(salesman.getRecords());
		}

		return recordLists;
	}

	/**
	 * @return the array of cities.
	 */
	public City[] getCities()
	{
		return cities;
	}

	/**
	 * @return the array of salesmen.
	 */
	public Salesman[] getSalesmen()
	{
		return salesmen;
	}
}
