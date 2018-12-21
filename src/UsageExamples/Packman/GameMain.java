package UsageExamples.Packman;

import Geom.LLA;
import Packman.MyFrame;

import java.io.File;

/**
 * @author Elisha
 */
public class GameMain
{
	private static final File image = new File("Resources/Packman/Ariel1.png");
	private static final LLA top_left = new LLA(32.10559, 35.20234, 0);
	private static final LLA bottom_right = new LLA(32.10199, 35.21239, 0);

	public static void main(String[] args)
	{
		MyFrame frame = new MyFrame("Packman Game",image,top_left,bottom_right);
		frame.start();
	}
}
