package Packman;

import Algorithms.TSP.MoveRecord;
import Algorithms.TSP.MultiTSP;
import Algorithms.TSP.mTSP;

import java.util.List;

/**
 * @author Elisha
 */
public class ShortestPathAlgo
{
	public final List records;
	public final Packman[] packmen;
	public final Fruit[] fruits;

	public ShortestPathAlgo(Game game)
	{
		mTSP problem = new MultiTSP(game.getPackmen(),game.getFruits());
		problem.compute();
		records = problem.getAllMoveRecords();
		packmen = (Packman[]) problem.getSalesmen();
		fruits = (Fruit[]) problem.getCities();
	}


	public double getDurationSeconds()
	{
		double durationSeconds = -1;
		for (Fruit fruit : fruits)
		{
			if (fruit.timeVisited() > durationSeconds)
				durationSeconds = fruit.timeVisited();
		}
		return durationSeconds;
	}
}
