package traffic.console.graphic;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import traffic.map.entity.Map;

public class MapFrame extends JFrame implements ActionListener {
	static public final long serialVersionUID = 1L;

	JLabel statusLabel;
	JMenuItem openMenu, saveMenu, setMenu;
	JButton openButton, saveButton, setButton;
	MapDisplayPanel mapDisplay;
	String baseDirectory = "./";
	GenerateConsole generatedialog;

	public MapFrame() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		setTitle("Traffic Simulating System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create Menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// System Menu
		JMenu menu = new JMenu("System");
		menuBar.add(menu);
		JMenuItem item = new JMenuItem("New Window");
		item.setActionCommand("NewWin");
		item.addActionListener(this);
		menu.add(item);
		item = new JMenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(this);
		menu.add(item);

		// File Menu
		menu = new JMenu("File");
		menuBar.add(menu);
		openMenu = new JMenuItem("Open");
		openMenu.setIcon(getImageIcon(baseDirectory + "/image/open.gif"));
		openMenu.setActionCommand("Open");
		openMenu.addActionListener(this);
		menu.add(openMenu);
		saveMenu = new JMenuItem("Save");
		saveMenu.setIcon(getImageIcon(baseDirectory + "/image/save.gif"));
		saveMenu.setActionCommand("Save");
		saveMenu.addActionListener(this);
		saveMenu.setEnabled(false);
		menu.add(saveMenu);
		/*		setMenu = new JMenuItem("Set");
		 setMenu.setIcon(getImageIcon(baseDirectory + "/image/sset.gif"));
		 setMenu.setActionCommand("Set");
		 setMenu.addActionListener(this);
		 setMenu.setEnabled(false);
		 menu.add(setMenu);*/

		// View menu
		menu = new JMenu("View");
		menuBar.add(menu);

		// Create Toolbar
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		openButton = new JButton(
				getImageIcon(baseDirectory + "/image/open.gif"));
		openButton.setActionCommand("Set");
		openButton.addActionListener(this);
		toolbar.add(openButton);
		saveButton = new JButton(
				getImageIcon(baseDirectory + "/image/save.gif"));
		saveButton.setActionCommand("Save");
		saveButton.addActionListener(this);
		saveButton.setEnabled(false);
		toolbar.add(saveButton);
		/*		setButton = new JButton(
		 getImageIcon(baseDirectory + "/image/sset.gif"));
		 setButton.setActionCommand("Set");
		 setButton.addActionListener(this);
		 setButton.setEnabled(false);
		 toolbar.add(setButton);
		 toolbar.addSeparator();*/

		statusLabel = new JLabel("Traffic simulating system started.");

		JLayeredPane MainDisplayPanel = new JLayeredPane();
		mapDisplay = new MapDisplayPanel();
		MainDisplayPanel.add(mapDisplay, JLayeredPane.DEFAULT_LAYER);
		// MainDisplayPanel.add(new VehicleDisplayPanel(),
		// JLayeredPane.PALETTE_LAYER);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(MainDisplayPanel, BorderLayout.CENTER);
		getContentPane().add(statusLabel, BorderLayout.SOUTH);

		getContentPane().add(toolbar, BorderLayout.NORTH);

		generatedialog = new GenerateConsole();
		generatedialog.pack();
		
	}

	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		System.out.println(cmd.toString());
		if (cmd.toString().equals("Set")) {
			generatedialog.setVisible(true);
		}
		if (cmd.toString().equals("Exit"))
			this.dispose();
	}

	private ImageIcon getImageIcon(String path) {
		return new ImageIcon(this.getClass().getResource(path));
	}

	protected void handleCreate(Map map) {
		mapDisplay.paint(map);
	}

	protected void handleMove(Map map) {
		mapDisplay.paint(map);
	}
}
