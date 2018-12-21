package Packman;

import Algorithms.TSP.City;
import Algorithms.TSP.MoveRecord;
import Algorithms.TSP.Salesman;
import File_format.MyLogger;
import Geom.LLA;
import Geom.Point3D;

/**
 * @author Elisha
 */
public class Packman extends Salesman
{

	protected double radius;
	protected Point3D direction;

	public Packman(LLA position, double speed, double radius)
	{
		super(position, speed);
		this.radius = radius;
	}

	public Packman(Packman packman)
	{
		super(packman);
		this.radius = packman.radius;
		this.direction = packman.direction;
	}

	/**
	 * Travel to {@code fruit} and eat (visit) it.
	 *
	 * @param city fruit to travel to.
	 * @return false if {@code fruit} was already eaten (visited).
	 */
	@Override
	public boolean travelTo(City city)
	{
		if (!(city instanceof Fruit))
		{
			throw new IllegalArgumentException("Packman can only eat fruit, not visit other types of cities!");
		}

		Fruit fruit = (Fruit) city;

		MyLogger.logln("Packman " + this + " is traveling to Fruit " + fruit);

		MoveRecord record = new MoveRecord(position.clone(), currentTime);

		if (fruit.eaten())
			return false;
		direction = position.distance3DVector(fruit.position); // face the fruit.
		currentTime += position.distance3D(fruit.position) / speed; // update time.

		position = position.transform(position.distance3DVector(fruit.position).add(direction.normalized().multiply(-1)));
		// move to the point where the fruit is at our radius's edge.

		record.to = position.clone();
		record.endTime = currentTime;
		records.add(record);

		return fruit.eat(currentTime); // eat the fruit.
	}


	public double getRadius()
	{
		return radius;
	}

	public Point3D getDirection()
	{
		return direction;
	}

	@Override
	public Packman clone()
	{
		return new Packman(this);
	}

	@Override
	public String toString()
	{
		return "Packman{" +
				"r " + radius +
				", d " + direction +
				", p " + position +
				", s " + speed +
				", t " + currentTime +
				'}';
	}
}
