package Packman;

import Algorithms.TSP.MoveRecord;
import File_format.MyLogger;
import Geom.LLA;
import Geom.Point3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Elisha
 */
public class GamePanel extends JPanel
{
	static final Point3D packmanPercent = new Point3D(3, 3); // min (width/p.x,height/p.y)
	static final Point3D fruitPercent = new Point3D(2, 2); // min (width/p.x,height/p.y)
	static final Point weightPosition = new Point(10, 10);
	static final long finishedDurationMillis = 1000;
	static final long timeout = 100;
	private Game game;
	private Map map; // Resources/Packman/game_1543693822377.csv
	public boolean run = true;

	private enum Mode
	{
		None, View, AddPackman, AddFruit, Play, FinishedPlay
	}

	public Mode mode = Mode.None;
	public final Image image;

	public GamePanel(File image, LLA top_left, LLA bottom_right)
	{
		Image im;
		try
		{
			im = ImageIO.read(image);
		} catch (IOException e)
		{
			im = null;
			e.printStackTrace();
		}

		this.image = im;
		map = new Map(im, top_left, bottom_right);
		game = new Game();

		addMouseListener();
	}

	public void exit()
	{
		MyLogger.logln("Exiting...");
		run = false;
	}

	public void start(JFrame frame)
	{
		MyLogger.logln("Starting game...");
		mode = Mode.View;
		Thread thread = new Thread(() ->
		{
			while (run)
			{
				try
				{
					Thread.sleep(timeout);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				if (mode == Mode.Play || mode == Mode.FinishedPlay)
					repaint();
				else
				{
					try
					{
						Thread.sleep(timeout);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			MyLogger.logln("Exited game");
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		});

		thread.start();
	}

	public boolean saveCSV()
	{
		MyLogger.logln("Saving to CSV...");
		if (game == null)
		{
			MyLogger.logln("Nothing to save");
			return false;
		}

		File file = getFile("Save Path");
		if (file == null)
		{
			MyLogger.logln("Save to CSV canceled");
			return false;
		}

		game.saveToCSV(file);

		MyLogger.logln("Saved to CSV file " + file);

		return true;
	}

	public boolean saveKML()
	{
		MyLogger.logln("saving to KML...");

		if (game == null)
		{
			return false;
		}

		File file = getFile("Save Path");
		if (file == null)
		{
			MyLogger.logln("Save to KML canceled");
			return false;
		}

		Double eps = getDouble("Seconds per frame");
		if (eps == null)
		{
			MyLogger.logln("Save to KML canceled");
			return false;
		}

		Path2KML.path2KML(file, game.getPackmen(), game.getFruits(), eps, System.currentTimeMillis());

		MyLogger.logln("Saved to KML file " + file);

		return true;
	}

	public boolean loadGame()
	{
		MyLogger.logln("Loading game...");
		File file;
		do
		{
			file = getFile("Load Path");

			if (file == null)
			{
				MyLogger.logln("Load canceled");
				return false;
			}

		} while (!file.exists());

		game = new Game(file);

		repaint();

		MyLogger.logln("Loaded " + file);

		return true;
	}

	public void stop()
	{
		if (mode == Mode.Play || mode == Mode.FinishedPlay)
		{
			mode = Mode.View;
			MyLogger.logln("Play stopped");
		}
		repaint();
	}


	private ShortestPathAlgo algo;
	private double speed;
	private double currentTimeAlgo = 0;
	private long finishedTimeAlgo = 0;

	public boolean play()
	{
		MyLogger.logln("Playing...");

		Double speed = getDouble("Speed");
		if (speed == null)
			return false;

		if (game == null)
			return false;

		algo = new ShortestPathAlgo(game);
		this.speed = speed;
		currentTimeAlgo = 0;
		mode = Mode.Play;

		lastFrameTime = System.currentTimeMillis();
		repaint();

		return true;
	}


	private void paintPlay(Graphics g)
	{
		if (game != null && algo != null)
		{
			int weight = 0;

			for (Packman p : algo.packmen)
			{
				MoveRecord mr = p.getMoveAtTime(currentTimeAlgo);
				LLA position;
				if (mr != null)
				{
					position = mr.getPositionAtTime(currentTimeAlgo);
				} else
				{
					position = p.getPosition();
				}
				paintPackman(g, map.worldToPixel(position), p.getRadius());
				System.out.println("Packman " + position + " time " + currentTimeAlgo +
						", deltaTimeSeconds: " + deltaTimeSeconds + ", deltaTimeMillis: " + deltaTimeMillis +
						", pixels: " + map.worldToPixel(position));
			}
			for (Fruit f : algo.fruits)
			{
				if (f.wasVisited(currentTimeAlgo))
				{
					weight += f.weight;
				} else
				{
					paintFruit(g, map.worldToPixel(f.position));
				}
			}
			paintTimeAndWeight(g, weight);

			if (currentTimeAlgo > algo.getDurationSeconds())
			{
				mode = Mode.FinishedPlay;
				finishedTimeAlgo = System.currentTimeMillis();
				currentTimeAlgo = 0;
				MyLogger.logln("Play finished");
			}

			currentTimeAlgo += speed * deltaTimeSeconds;
		}
	}

	private void paintFinishedPlay(Graphics g)
	{
		if (currentFrameTime - finishedTimeAlgo <= finishedDurationMillis)
		{
			paintView(g);
			g.drawString("Finished!", getWidth() / 2, getHeight() / 2);
		} else
		{
			mode = Mode.View;
			paintView(g);
		}

	}

	private void addMouseListener()
	{
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() != MouseEvent.NOBUTTON)
				{
					MyLogger.logln("Mouse clicked " + e.getPoint());
					if (game == null)
					{
						game = new Game();
					}
					if (mode == Mode.AddPackman)
					{
						addPackman(e.getPoint());
					} else if (mode == Mode.AddFruit)
					{
						addFruit(e.getPoint());
					}
				}
			}
		});
	}

	double newPackmanSpeed = 1, newPackmanRadius = 1;

	public void setPackmanProperties()
	{
		MyLogger.logln("Setting Packman Properties...");

		int confirm;
		Double speed;
		Double radius;
		do
		{
			speed = getDouble("Speed");
			radius = getDouble("Radius");
			if (speed == null)
			{
				MyLogger.logln("Set Packman Properties canceled");
				return;
			}
			if (radius == null)
				radius = 1d;


			confirm = JOptionPane.showConfirmDialog(null,
					"New Packman speed " + speed + " and radius " + radius + "?");

		} while (confirm == 1);

		if (confirm == 0)
		{
			newPackmanSpeed = speed;
			newPackmanRadius = radius;

			MyLogger.logln("Packman Properties set. speed: " + newPackmanSpeed + ", raduis: " + newPackmanRadius);
		} else
		{
			MyLogger.logln("Set Packman Properties canceled");
		}

		mode = Mode.View;
		repaint();
	}

	double newFruitWeight = 1;

	public void setFruitProperties()
	{
		MyLogger.logln("Setting Fruit Properties...");
		Double weight;
		int confirm;
		do
		{
			weight = getDouble("New Fruit Weight:");
			if (weight == null)
			{
				MyLogger.logln("Set Fruit Properties canceled");
				return;
			}
			confirm = JOptionPane.showConfirmDialog(null,
					"New Fruit weight " + weight + "?");
		} while (confirm == 1);
		if (confirm == 0)
		{
			newFruitWeight = weight;

			MyLogger.logln("Fruit Properties set. weight: " + newFruitWeight);
		} else
		{
			MyLogger.logln("Set Fruit Properties canceled");
		}

		mode = Mode.View;
		repaint();
	}

	public void setAddPackman()
	{
		MyLogger.logln("Set to add Packman");
		mode = Mode.AddPackman;
	}

	public void setAddFruit()
	{
		MyLogger.logln("Set to add Fruit");
		mode = Mode.AddFruit;
	}

	public void setView()
	{
		MyLogger.logln("Set to view");
		mode = Mode.View;
	}

	public void addPackman(Point place)
	{
		MyLogger.logln("Adding Packman At " + place);
		game.addPackman(new Packman(map.pixelToWorld(place), newPackmanSpeed, newPackmanRadius));
		repaint();
	}

	public void addFruit(Point place)
	{
		MyLogger.logln("Adding Fruit At " + place);
		game.addFruit(new Fruit(map.pixelToWorld(place), newFruitWeight));
		repaint();
	}

	private void paintView(Graphics g)
	{
		if (game != null)
		{
			for (Packman p : game.packmen)
			{
				paintPackman(g, map.worldToPixel(p.getPosition()), p.getRadius());
			}
			for (Fruit f : game.fruits)
			{
				paintFruit(g, map.worldToPixel(f.position));
			}
		}
	}

	private long lastFrameTime = System.currentTimeMillis();
	private long currentFrameTime = System.currentTimeMillis();
	private long deltaTimeMillis;
	private double deltaTimeSeconds;

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		currentFrameTime = System.currentTimeMillis();
		deltaTimeMillis = (currentFrameTime - lastFrameTime);
		deltaTimeSeconds = deltaTimeMillis / 1000.0;
		lastFrameTime = currentFrameTime;

		map.updateScreenSize(this.getWidth(), this.getHeight());
		g.drawImage(map.map, 0, 0, this.getWidth(), this.getHeight(), null);

		switch (mode)
		{
			case View:
			case AddPackman:
			case AddFruit:
				paintView(g);
				break;
			case Play:
				paintPlay(g);
				break;
			case FinishedPlay:
				paintFinishedPlay(g);
				break;
		}
	}

	private void paintPackman(Graphics g, Point position, double radius)
	{
		int size = Integer.min((int) (getWidth() * radius * packmanPercent.x() / 100.0),
				(int) (getHeight() * radius * packmanPercent.y() / 100.0));

		g.setColor(Color.yellow);
		g.fillOval(position.x, position.y, size, size);
	}

	private void paintFruit(Graphics g, Point position)
	{
		int size = Integer.min((int) (getWidth() * fruitPercent.x() / 100),
				(int) (getHeight() * fruitPercent.y() / 100));

		g.setColor(Color.green);
		g.fillRect(position.x, position.y, size, size);
	}

	private void paintTimeAndWeight(Graphics g, int weight)
	{
		g.drawString("Weight: " + weight + " Time: " + currentTimeAlgo, weightPosition.x, weightPosition.y);
	}


	private File getFile(Object message)
	{
		File file = null;
		boolean found = false;
		do
		{
			String path = JOptionPane.showInputDialog(message);
			if (path == null)
			{
				return null;
			}

			file = new File(path);

		} while (file.isDirectory());

		return file;
	}

	private Double getDouble(Object message)
	{
		Double d = null;
		boolean found = false;
		while (!found)
		{
			String str = JOptionPane.showInputDialog(message);
			if (str == null)
			{
				return null;
			}

			try
			{
				d = Double.valueOf(str);
				found = true;
			} catch (NumberFormatException ignored)
			{

			}

		}
		return d;
	}
}
