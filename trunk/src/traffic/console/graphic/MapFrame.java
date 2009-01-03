package traffic.console.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import traffic.event.Event;
import traffic.event.EventDispatcher;
import traffic.event.EventListener;
import traffic.log.Log;
import traffic.map.entity.Map;

public class MapFrame extends JFrame implements ActionListener, EventListener {
	static public final long serialVersionUID = 1L;
	private static final int defaultWidth = 1024, defaultHeight = 768;

	JLabel statusLabel;
	JButton openButton, saveButton, setButton;
	MapDisplayPanel mapDisplay;
	ControlPanel controlPanel;

	EventDispatcher eventDispatcher = new EventDispatcher();

	// VehicleGenPanel vehiclegendialog;
	// BarrierGenPanel barriergendialog;

	public MapFrame() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		setTitle("Traffic Simulating System");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create Toolbar
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		openButton = new JButton(getImageIcon("open.gif"));
		openButton.setActionCommand("SetVehicle");
		openButton.addActionListener(this);
		toolbar.add(openButton);
		saveButton = new JButton(getImageIcon("save.gif"));
		saveButton.setActionCommand("SetBarrier");
		saveButton.addActionListener(this);
		// saveButton.setEnabled(false);
		toolbar.add(saveButton);
		/*
		 * setButton = new JButton( getImageIcon(baseDirectory +
		 * "/image/sset.gif")); setButton.setActionCommand("Set");
		 * setButton.addActionListener(this); setButton.setEnabled(false);
		 * toolbar.add(setButton); toolbar.addSeparator();
		 */

		statusLabel = new JLabel("Traffic simulating system started.");

		JPanel MainDisplayPanel = new JPanel();
		mapDisplay = new MapDisplayPanel();
		controlPanel = new ControlPanel();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		MainDisplayPanel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 6.0;
		c.weighty = 1.0;
		gridbag.setConstraints(mapDisplay, c);
		MainDisplayPanel.add(mapDisplay);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 2.0;
		c.weighty = 1.0;
		gridbag.setConstraints(controlPanel, c);
		MainDisplayPanel.add(controlPanel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(MainDisplayPanel, BorderLayout.CENTER);
		getContentPane().add(statusLabel, BorderLayout.SOUTH);
		getContentPane().add(toolbar, BorderLayout.NORTH);

		mapDisplay.setStatusLabel(statusLabel);
		setFocusable(true);
		addKeyListener(new MapKeyListener(mapDisplay));
		pack();

		setSize(defaultWidth, defaultHeight);
		centerize();
		// vehiclegendialog = new VehicleGenPanel();
		// vehiclegendialog.pack();
		// barriergendialog = new BarrierGenPanel();
		// barriergendialog.pack();

		mapDisplay.addEventListener(this);
	}

	private void centerize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width / 2;
		int width = getWidth();
		setLocation(screenWidth - width / 2, 0);
	}

	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		Log.getInstance().writeln(cmd.toString());
		// if (cmd.toString().equals("SetVehicle")) {
		// vehiclegendialog.setVisible(true);
		// }
		// if (cmd.toString().equals("SetBarrier")) {
		// barriergendialog.setVisible(true);
		// }
		if (cmd.toString().equals("Exit")) {
			System.exit(0);
		}
	}

	private ImageIcon getImageIcon(String name) {
		return new ImageIcon(ImageLoader.loadImageByName(name));
	}

	protected void handleCreate(Map map) {
		mapDisplay.paint(map);
	}

	protected void handleMove(Map map) {
		mapDisplay.paint(map);
	}

	public void addEventListener(EventListener listener) {
		eventDispatcher.addEventListener(listener);
	}

	@Override
	public void eventOccured(Event event) {
		eventDispatcher.dispatchEvent(event);
	}
}
