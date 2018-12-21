package UsageExamples.Packman;

import Packman.Game;
import Packman.Path2KML;
import Packman.ShortestPathAlgo;

import java.io.File;
import java.util.Date;

/**
 * @author Elisha
 */
public class Path2KML_Test
{
	public static void main(String[] args)
	{
		Game game = new Game(new File("src/UsageExamples/Packman/game_1543684662657.csv"));

		ShortestPathAlgo algo  = new ShortestPathAlgo(game);
		Path2KML.path2KML(new File("src/UsageExamples/Packman/game_1543684662657.kml"),
				algo.packmen,algo.fruits,2,System.currentTimeMillis());
	}
}
