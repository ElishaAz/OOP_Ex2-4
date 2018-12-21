package Packman;

import Algorithms.TSP.City;
import File_format.MyLogger;
import Geom.LLA;

/**
 * @author Elisha
 */
public class Fruit extends City
{
	public final double weight;
	public Fruit(LLA position, double weight)
	{
		super(position);
		this.weight = weight;
	}

	public Fruit(Fruit fruit)
	{
		this(fruit.position,fruit.weight);
	}


	public boolean eat(double time)
	{
		return super.visit(time);
	}

	public boolean eaten()
	{
		MyLogger.logln("Fruit " + this + " is being eaten");
		return super.visited();
	}

	@Override
	public Fruit clone()
	{
		return new Fruit(this);
	}

	@Override
	public String toString()
	{
		return "Fruit{" +
				"w " + weight +
				", p " + position +
				", v " + _visited +
				", t" + _timeVisited +
				'}';
	}
}
