package Algorithms.TSP;

import java.util.List;

/**
 * <p>
 * This is an interface representing the Multiple Traveling Salesmen Problem (mTSP),
 * which is an extension of the Traveling Salesman Problem (TSP), where there are more than one salesman.
 * </p>
 * see:
 * <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">
 * https://en.wikipedia.org/wiki/Travelling_salesman_problem
 * </a>
 *
 * @author Elisha
 */
public interface mTSP
{
	/** Computes the algorithm. */
	void compute();

	/**
	 * Tells {@code salesmanIndex} to travel to {@code cityIndex}.
	 *
	 * @param salesmanIndex the index of the salesman that will travel.
	 * @param cityIndex     the index of the city that the salesman will travel to.
	 * @return false if {@code cityIndex} was visited already.
	 */
	boolean visit(int salesmanIndex, int cityIndex);

	/**
	 * Checks if all the cities were visited.
	 *
	 * @return true if all the cities were visited.
	 */
	boolean allCitiesVisited();

	/**
	 * Calculates the position and direction of all the salesmen at the time {@code time}.
	 *
	 * @param time time from the beginning of the Algorithm.
	 * @return an array of the positions of all the salesmen at the time {@code time}.
	 */
	PositionAndDirection[] getPositionsAtTime(double time);

	/**
	 * @return a list of move records for each of the salesmen.
	 */
	List<List<MoveRecord>> getAllRecords();
}
