package Algorithms.TSP;

import Geom.LLA;
import Geom.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elisha
 */
public class Salesman implements ISalesman
{
	protected LLA position;
	protected double speed;
	protected double currentTime;
	protected List<MoveRecord> records;

	public Salesman(LLA position, double speed, double currentTime)
	{
		this.position = position.clone();
		this.speed = speed;
		this.currentTime = currentTime;
		records = new ArrayList<>();
	}

	public Salesman(LLA position, double speed)
	{
		this(position, speed, 0);
	}

	public Salesman(Salesman other)
	{
		this(other.position.clone(), other.speed, other.currentTime);

		for (MoveRecord mr : other.records)
		{
			records.add(mr.clone());
		}
	}

	/**
	 * Travel to {@code city} and visit it.
	 *
	 * @param city city to travel to.
	 * @return false if {@code city} was already visited,
	 */
	@Override
	public boolean travelTo(City city)
	{
		if (city.visited())
			return false;

		MoveRecord record = new MoveRecord(position.clone(), currentTime);

		currentTime += this.position.distance3D(city.position) / speed;
		this.position = city.position.clone();

		record.to = position.clone();
		record.endTime = currentTime;
		records.add(record);

		return city.visit(currentTime);
	}

	/**
	 * Calculates the position at the time {@code time}.
	 *
	 * @return the record of the move this salesman was doing at the time {@code time}.
	 * Null if this salesman wasn't moving at that time.
	 */
	@Override
	public MoveRecord getMoveAtTime(double time)
	{
		if (records.size() == 0)
			return null;

		for (int i = 0; i < records.size(); i++)
		{
			MoveRecord move = records.get(i);
			if (move.startTime <= time && time <= move.endTime)
			{
				System.out.println("Got move " + move);
				return move.clone();
			}
		}

		if (records.get(0).startTime > time)
			return records.get(0).clone();
		else
			return records.get(records.size() - 1).clone();
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
	@Override
	public List<MoveRecord> getRecords()
	{
		ArrayList<MoveRecord> records = new ArrayList<>(this.records.size());
		for (MoveRecord record : this.records)
		{
			records.add(record.clone());
		}
		return records;
	}

	@Override
	public String toString()
	{
		return "Salesman{" +
				"position=" + position +
				", speed=" + speed +
				", currentTime=" + currentTime +
				", records=" + records +
				'}';
	}
}
