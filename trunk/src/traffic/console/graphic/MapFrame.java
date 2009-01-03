package traffic.console.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
	JButton addButton, setButton, exitButton;
	JPanel buttonPanel;
	JButton pauseButton, resumeButton, resetButton;
	MapDisplayPanel mapDisplay;
	ControlPanel controlPanel;
	VehicleDisplayPanel vPanel;

	EventDispatcher eventDispatcher = new EventDispatcher();

	VehicleGenPanel vehiclegendialog;
	BarrierGenPanel barriergendialog;

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
		toolbar.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		toolbar.setFloatable(false);
		addButton = new JButton(getImageIcon("img_add.png"));
		addButton.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		addButton.setActionCommand("SetVehicle");
		addButton.addActionListener(this);
		toolbar.add(addButton);
		setButton = new JButton(getImageIcon("img_set.png"));
		setButton.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		setButton.setActionCommand("SetBarrier");
		setButton.addActionListener(this);
		toolbar.add(setButton);
		exitButton = new JButton(getImageIcon("img_exit.png"));
		exitButton.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		exitButton.setActionCommand("exit");
		exitButton.addActionListener(this);
		toolbar.add(exitButton);

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
		buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("System Button"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		buttonPanel.setLayout(new GridLayout(3, 1));

		pauseButton = new JButton(getImageIcon("img_pause.png"));
		pauseButton
				.setText("<html>&nbsp;&nbsp;pause&nbsp;&nbsp;&nbsp;simulation<br></html>");
		pauseButton.setActionCommand("pause");
		pauseButton.addActionListener(this);
		pauseButton.setToolTipText("pause");
		pauseButton.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		buttonPanel.add(pauseButton);

		resumeButton = new JButton(getImageIcon("img_resume.png"));
		resumeButton
				.setText("<html>&nbsp;&nbsp;resume&nbsp;simulation<br></html>");
		resumeButton.setActionCommand("resume");
		resumeButton.addActionListener(this);
		resumeButton.setToolTipText("resume");
		resumeButton.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		buttonPanel.add(resumeButton);

		resetButton = new JButton(getImageIcon("img_reset.png"));
		resetButton
				.setText("<html>&nbsp;&nbsp;reset&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;simulation<br></html>");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		resetButton.setToolTipText("reset");
		resetButton.setBorder(BorderFactory.createLineBorder(Color
				.decode("#B8CFE5")));
		buttonPanel.add(resetButton);

		c0.gridy = 1;
		c0.weightx = 1.0;
		c0.weighty = 1.0;
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

		vehiclegendialog = new VehicleGenPanel();
		vehiclegendialog.pack();
		barriergendialog = new BarrierGenPanel();
		barriergendialog.pack();

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
		if (cmd.toString().equals("SetVehicle")) {
			vehiclegendialog.setVisible(true);
		}
		if (cmd.toString().equals("SetBarrier")) {
			barriergendialog.setVisible(true);
		}
		if (cmd.toString().equals("exit")) {
			statusLabel.setText("system exit");
			Simulator.getInstance().exit();
		} else if (cmd.toString().equals("pause")) {
			statusLabel.setText("simulation pasued");
			Simulator.getInstance().pause();
		} else if (cmd.toString().equals("resume")) {
			statusLabel.setText("simulation resumed");
			Simulator.getInstance().resume();
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
