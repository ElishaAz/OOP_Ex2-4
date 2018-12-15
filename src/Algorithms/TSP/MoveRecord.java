package Algorithms.TSP;

import Geom.LLA;
import Geom.Point3D;

/**
 * A record of a move from one point to another with start and end times.
 *
 * @author Elisha
 */
public class MoveRecord
{
	/** Start position */
	public LLA from;

	/** End position */
	public LLA to;
	/** The time the move started */
	public double startTime;
	/** The time the move ended */
	public double endTime;

	public MoveRecord(LLA from, double startTime)
	{
		this.from = from;
		this.startTime = startTime;
	}

	public MoveRecord(LLA from, LLA to, double startTime, double endTime)
	{
		this.from = from;
		this.to = to;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public MoveRecord(MoveRecord other)
	{
		this.from = other.from;
		this.to = other.to;
		this.startTime = other.startTime;
		this.endTime = other.endTime;
	}

	/**
	 * Calculates the position at the time {@code time}, assuming the speed was the same for the whole move,
	 * and that the move was directly from {@link #from} to {@link #to}.
	 *
	 * @return the position at time {@code time}.
	 * If {@code time} is not between {@link #startTime} and {@link #endTime}, will return null.
	 */
	public LLA getPositionAtTime(double time)
	{
		if (time < startTime)
		{
			return from.clone();
		} else if (startTime <= time && time <= endTime)
		{
			double ratio = (time - startTime) / (endTime - startTime);
			LLA point = from.clone();
			Point3D moveVector = from.distance3DVector(to);
			moveVector.multiply(ratio);
			point.transform(moveVector);
			return point;
		}else
		{
			return to.clone();
		}
	}//PositionAndDirection

	/**
	 * Calculates the position at the time {@code time}, assuming the speed was the same for the whole move,
	 * and that the move was directly from {@link #from} to {@link #to}.
	 *
	 * @return the position at time {@code time}.
	 * If {@code time} is not between {@link #startTime} and {@link #endTime}, will return null.
	 */
	public PositionAndDirection getPositionAndDirectionAtTime(double time)
	{
		LLA point = getPositionAtTime(time);
		return new PositionAndDirection(point, point.distance3DVector(to));
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public MoveRecord clone()
	{
		return new MoveRecord(this);
	}
}
