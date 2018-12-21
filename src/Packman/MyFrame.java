package Packman;

import File_format.MyLogger;
import Geom.LLA;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * @author Elisha
 */
public class MyFrame extends JFrame
{
	public static final File logFile = new File("log (" + System.currentTimeMillis() + ").txt");

	private GamePanel panel;

	public MyFrame(String title, GamePanel panel, JMenuBar menuBar)
	{
		super(title);
		add(panel);
		setJMenuBar(menuBar);
		setSize(panel.image.getWidth(null), panel.image.getHeight(null));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.panel = panel;
	}
	public MyFrame(String title, GamePanel panel)
	{
		this(title,panel,menu(panel));
	}

	public  MyFrame(String title,File image,LLA top_left,LLA bottom_right, JMenuBar menuBar)
	{
		this(title, new GamePanel(image, top_left, bottom_right),menuBar);
	}

	public MyFrame(String title,File image,LLA top_left,LLA bottom_right)
	{
		this(title,new GamePanel(image, top_left, bottom_right));
	}

	public void start()
	{
		panel.start(this);
	}

	public GamePanel getPanel()
	{
		return panel;
	}

	static JMenuBar menu(GamePanel panel)
	{


		JMenuBar menubar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem exitMenuItem = new JMenuItem("Save Log");
		exitMenuItem.setMnemonic(KeyEvent.VK_S);
		exitMenuItem.setToolTipText("Save log to file");
		exitMenuItem.addActionListener((event) -> MyLogger.save(logFile));
		fileMenu.add(exitMenuItem);

		JMenuItem loadMenuItem = new JMenuItem("Load");
		loadMenuItem.setMnemonic(KeyEvent.VK_L);
		loadMenuItem.setToolTipText("Load a CSV file");
		loadMenuItem.addActionListener((event) -> panel.loadGame());
		fileMenu.add(loadMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save to CSV");
		saveMenuItem.setMnemonic(KeyEvent.VK_C);
		saveMenuItem.setToolTipText("Save this Game to a CSV file");
		saveMenuItem.addActionListener((event) -> panel.saveCSV());
		fileMenu.add(saveMenuItem);

		JMenuItem saveKMLMenuItem = new JMenuItem("Save to KML");
		saveKMLMenuItem.setMnemonic(KeyEvent.VK_K);
		saveKMLMenuItem.setToolTipText("Save this Game to a KML file");
		saveKMLMenuItem.addActionListener((event) -> panel.saveKML());
		fileMenu.add(saveKMLMenuItem);

		menubar.add(fileMenu);

		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		JMenuItem addPackmanMenuItem = new JMenuItem("Set Packman properties");
		addPackmanMenuItem.setMnemonic(KeyEvent.VK_A);
		addPackmanMenuItem.setToolTipText("Set the properties for the new Packman");
		addPackmanMenuItem.addActionListener((event) -> panel.setPackmanProperties());
		editMenu.add(addPackmanMenuItem);

		JMenuItem addFruitMenuItem = new JMenuItem("Set Fruit properties");
		addFruitMenuItem.setMnemonic(KeyEvent.VK_R);
		addFruitMenuItem.setToolTipText("Set the properties for the new Fruit");
		addFruitMenuItem.addActionListener((event) -> panel.setFruitProperties());
		editMenu.add(addFruitMenuItem);

		editMenu.addSeparator();

		ButtonGroup group = new ButtonGroup();

		JRadioButtonMenuItem addPackmanRBMenuItem = new JRadioButtonMenuItem("Add Packman");
		addPackmanRBMenuItem.setSelected(false);
		addPackmanRBMenuItem.setMnemonic(KeyEvent.VK_P);
		addPackmanRBMenuItem.setToolTipText("Set mode to add Packman");
		addPackmanRBMenuItem.addActionListener((e -> panel.setAddPackman()));
		group.add(addPackmanRBMenuItem);
		editMenu.add(addPackmanRBMenuItem);

		JRadioButtonMenuItem addFruitRBMenuItem = new JRadioButtonMenuItem("Add Fruit");
		addFruitRBMenuItem.setMnemonic(KeyEvent.VK_F);
		addFruitRBMenuItem.setToolTipText("Set mode to add Fruit");
		addFruitRBMenuItem.setSelected(false);
		addFruitRBMenuItem.addActionListener((e -> panel.setAddFruit()));
		group.add(addFruitRBMenuItem);
		editMenu.add(addFruitRBMenuItem);

		JRadioButtonMenuItem dontAddRBMenuItem = new JRadioButtonMenuItem("Don't add");
		dontAddRBMenuItem.setMnemonic(KeyEvent.VK_F);
		dontAddRBMenuItem.setToolTipText("Set mode to view");
		dontAddRBMenuItem.setSelected(true);
		dontAddRBMenuItem.addActionListener((e -> panel.setView()));
		group.add(dontAddRBMenuItem);
		editMenu.add(dontAddRBMenuItem);

		menubar.add(editMenu);

		JMenu playMenu = new JMenu("Play");
		playMenu.setMnemonic(KeyEvent.VK_P);

		JMenuItem playMenuItem = new JMenuItem("Play");
		playMenuItem.setMnemonic(KeyEvent.VK_P);
		playMenuItem.setToolTipText("Play the game");
		playMenuItem.addActionListener((event) -> panel.play());
		playMenu.add(playMenuItem);

		JMenuItem stopMenuItem = new JMenuItem("Stop");
		stopMenuItem.setMnemonic(KeyEvent.VK_S);
		stopMenuItem.setToolTipText("Stop the game");
		stopMenuItem.addActionListener((event) -> panel.stop());
		playMenu.add(stopMenuItem);

		menubar.add(playMenu);

		return menubar;
	}

}
