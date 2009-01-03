package traffic.console.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import traffic.event.Event;
import traffic.event.EventDispatcher;
import traffic.event.EventListener;
import traffic.log.Log;
import traffic.map.entity.Map;
import traffic.simulation.kernel.Simulator;

public class MapFrame extends JFrame implements ActionListener, EventListener {
	static public final long serialVersionUID = 1L;
	private static final int defaultWidth = 1024, defaultHeight = 768;

	JLabel statusLabel;
	JButton openButton, saveButton, setButton;
	JPanel buttonPanel;
	JButton pauseButton, resumeButton, startButton, resetButton, stopButton;
	MapDisplayPanel mapDisplay;
	ControlPanel controlPanel;
	VehicleDisplayPanel vPanel;

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
		statusLabel.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		statusLabel.setToolTipText("current status");

		JPanel MainDisplayPanel = new JPanel();
		mapDisplay = new MapDisplayPanel();
		controlPanel = new ControlPanel();
		vPanel = new VehicleDisplayPanel();

		GridBagLayout gridbag0 = new GridBagLayout();
		GridBagConstraints c0 = new GridBagConstraints();
		controlPanel.setLayout(gridbag0);
		c0.fill = GridBagConstraints.BOTH;
		c0.gridx = 0;
		c0.gridy = 0;
		c0.weightx = 1.0;
		c0.weighty = 6.0;
		gridbag0.setConstraints(vPanel, c0);
		controlPanel.add(vPanel);

		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("System Button"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		buttonPanel.setLayout(new GridLayout(5, 1));

		pauseButton = new JButton(getImageIcon("open.gif"));
		pauseButton.setText("pause simulation");
		pauseButton.setActionCommand("pause");
		pauseButton.addActionListener(this);
		pauseButton.setToolTipText("pause");
		buttonPanel.add(pauseButton);

		resumeButton = new JButton(getImageIcon("open.gif"));
		resumeButton.setText("resume simulation");
		resumeButton.setActionCommand("resume");
		resumeButton.addActionListener(this);
		resumeButton.setToolTipText("resume");
		buttonPanel.add(resumeButton);

		startButton = new JButton(getImageIcon("open.gif"));
		startButton.setText("start simulation");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);
		startButton.setToolTipText("start");
		buttonPanel.add(startButton);

		stopButton = new JButton(getImageIcon("open.gif"));
		stopButton.setText("stop  simulation");
		stopButton.setActionCommand("stop");
		stopButton.addActionListener(this);
		stopButton.setToolTipText("stop");
		buttonPanel.add(stopButton);

		resetButton = new JButton(getImageIcon("open.gif"));
		resetButton.setText("reset simulation");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		pauseButton.setToolTipText("reset");
		buttonPanel.add(resetButton);

		c0.gridy = 1;
		c0.weightx = 1.0;
		c0.weighty = 2.0;
		gridbag0.setConstraints(buttonPanel, c0);
		controlPanel.add(buttonPanel);
		controlPanel.validate();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		MainDisplayPanel.setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 8.0;
		c.weighty = 1.0;
		gridbag.setConstraints(mapDisplay, c);
		MainDisplayPanel.add(mapDisplay);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.8;
		c.weighty = 1.0;
		gridbag.setConstraints(controlPanel, c);
		MainDisplayPanel.add(controlPanel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(MainDisplayPanel, BorderLayout.CENTER);
		getContentPane().add(statusLabel, BorderLayout.SOUTH);
		getContentPane().add(toolbar, BorderLayout.NORTH);

		mapDisplay.setVehicleDisplayPanel(vPanel);
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
			statusLabel.setText("system exit");
			Simulator.getInstance().exit();
		} else if (cmd.toString().equals("pause")) {
			statusLabel.setText("simulation pasued");
			Simulator.getInstance().pause();
		} else if (cmd.toCharArray().equals("resume")) {
			statusLabel.setText("simulation resumed");
			Simulator.getInstance().resume();
		} else if (cmd.toString().equals("start")) {
			statusLabel.setText("simulation stared");
			Simulator.getInstance().start();
		} else if (cmd.toString().equals("stop")) {
			statusLabel.setText("simulation stopped");
			Simulator.getInstance().stop();
		} else if (cmd.toString().equals("reset")) {
			statusLabel.setText("simulation resetted");
			Simulator.getInstance().reset();
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
