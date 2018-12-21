package Algorithms.TSP;

/**
 * @author Elisha
 */
public interface ICity
{
	boolean wasVisited(double time);

	/**
	 * Visit this city.
	 *
	 * @return false if this city was already visited.
	 */
	boolean visit(double time);

	/**
	 * Check if this city was visited already.
	 *
	 * @return false if {@link #visit(double)} was already called.
	 */
	boolean visited();

	/**
	 * @return the time this city was visited.
	 */
	double timeVisited();

	City clone();
}
