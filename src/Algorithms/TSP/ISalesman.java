package Algorithms.TSP;

import java.util.List;

/**
 * @author Elisha
 */
public interface ISalesman
{
	/**
	 * Travel to {@code city} and visit it.
	 *
	 * @param city city to travel to.
	 * @return false if {@code city} was already visited,
	 */
	boolean travelTo(City city);

	/**
	 * Calculates the position at the time {@code time}.
	 *
	 * @return the record of the move this salesman was doing at the time {@code time}.
	 * Null if this salesman wasn't moving at that time.
	 */
	MoveRecord getMoveAtTime(double time);

	Salesman clone();

	/**
	 * @return a copy of the move records
	 */
	List<MoveRecord> getRecords();
}
