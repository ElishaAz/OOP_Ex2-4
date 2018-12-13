package Algorithms.TSP;

import Geom.LLA;
import Geom.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elisha
 */
public class Salesman
{
	protected LLA position;
	protected double speed;
	protected double currentTime;
	protected List<MoveRecord> records;

	public Salesman(LLA position, double speed, double currentTime)
	{
		this.position = position;
		this.speed = speed;
		records = new ArrayList<>();
	}

	public Salesman(LLA position, double speed)
	{
		this(position, speed, 0);
	}

	public Salesman(Salesman other)
	{
		this(other.position, other.speed, other.currentTime);
	}

	/**
	 * Travel to {@code city} and visit it.
	 *
	 * @param city city to travel to.
	 * @return false if {@code city} was already visited,
	 */
	public boolean travelTo(City city)
	{
		if (city.visited())
			return false;

		MoveRecord record = new MoveRecord(position.clone(), currentTime);

		currentTime += this.position.distance3D(city.position) / speed;
		this.position = city.position;

		record.to = position.clone();
		record.endTime = currentTime;
		records.add(record);

		return city.visit();
	}

	/**
	 * Calculates the position at the time {@code time}.
	 *
	 * @return the record of the move this salesman was doing at the time {@code time}.
	 * Null if this salesman wasn't moving at that time.
	 */
	public MoveRecord getMoveAtTime(double time)
	{
		for (int i = 0; i < records.size(); i++)
		{
			MoveRecord move = records.get(i);
			if (move.startTime <= time && time <= move.endTime)
			{
				return move.clone();
			}
		}
		return null;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Salesman clone()
	{
		return new Salesman(this);
	}

	public LLA getPosition()
	{
		return position;
	}

	public double getSpeed()
	{
		return speed;
	}

	public double getCurrentTime()
	{
		return currentTime;
	}

	/**
	 * @return a copy of the move records
	 */
	public List<MoveRecord> getRecords()
	{
		ArrayList<MoveRecord> records = new ArrayList<>(this.records.size());
		for (MoveRecord record : this.records)
		{
			records.add(record.clone());
		}
		return records;
	}
}
