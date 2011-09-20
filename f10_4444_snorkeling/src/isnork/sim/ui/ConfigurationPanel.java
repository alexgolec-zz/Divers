/*
 * 	$Id: ConfigurationPanel.java,v 1.3 2007/11/14 22:00:22 johnc Exp $
 * 
 * 	Programming and Problem Solving
 *  Copyright (c) 2007 The Trustees of Columbia University
 */
package isnork.sim.ui;

import isnork.sim.GameConfig;
import isnork.sim.Player;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.image.RescaleOp;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class ConfigurationPanel extends JPanel implements ChangeListener,
		ItemListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private GameConfig config;

	static Font config_font = new Font("Arial", Font.PLAIN, 14);

	private JLabel roundLabel;
	private JSpinner rSpinner;

	private JLabel numAntsLabel;
	private JSpinner dSpinner;
	private JSpinner numDiversSpinner;

	private JList playerList;
	private JLabel score;
	private JButton remove;
	private JSpinner randomSeed;
	
	private JLabel playerLabel;
	private JComboBox playerBox;

	private JLabel boardLabel;
	private JComboBox boardBox;

	private Class<Player> selectedPlayer;

	private JSlider speedSlider;
	protected JLabel interactiveHelp;

	public ConfigurationPanel(GameConfig config) {
		this.config = config;

		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Configuration"));
		this.setPreferredSize(new Dimension(350, 1200));
		
		this.setLayout(new GridLayout(0, 2));



		numAntsLabel = new JLabel("D (Dimension): ");
		numAntsLabel.setFont(config_font);
		dSpinner = new JSpinner(new SpinnerNumberModel(
				GameConfig.d, 1, null, 1));
		dSpinner.setPreferredSize(new Dimension(100, 10));
		dSpinner.addChangeListener(this);

		add(numAntsLabel);
		add(dSpinner);


		JLabel lbl = new JLabel("R: ");
		lbl.setFont(config_font);
		rSpinner = new JSpinner(new SpinnerNumberModel(
				this.config.getR(), 1, null, 1));
		rSpinner.setPreferredSize(new Dimension(100, 10));
		rSpinner.addChangeListener(this);

		add(lbl);
		add(rSpinner);

		JLabel label = new JLabel("N (Number of Divers): ");
		label.setFont(config_font);
		numDiversSpinner = new JSpinner(new SpinnerNumberModel(this.config.getNumDivers(), 1, null,
				1));
		numDiversSpinner.setPreferredSize(new Dimension(100, 10));
		numDiversSpinner.addChangeListener(this);
		add(label);
		add(numDiversSpinner);

		randomSeed = new JSpinner(new SpinnerNumberModel(this.config.getRandomSeed(), new Long(1), null,
				new Long(1)));
		randomSeed.addChangeListener(this);
		add(new JLabel("Random seed"));
		add(randomSeed);

		playerLabel = new JLabel("Player:");
		add(playerLabel);
		// make player combo box
		playerBox = new JComboBox(config.getPlayerListModel());
		playerBox.addItemListener(this);
		playerBox.setRenderer(new ClassRenderer());
		add(playerBox);

		config.setPlayerClass((Class<Player>) playerBox.getSelectedItem());

		// board combo
		boardLabel = new JLabel("Board:");
		boardBox = new JComboBox(config.getBoardList());
		boardBox.addItemListener(this);
		add(boardLabel);
		add(boardBox);

		speedSlider = new JSlider(0, 1000);
		speedSlider.setValue(0);
		add(new JLabel("Delay (0 - 1000ms):"));
		add(speedSlider);

		mouseCoords = new JLabel("Mouse: N/A");
		score = new JLabel("Score: N/A");
		add(score);
		add(mouseCoords);

	}

	public void setMouseCoords(Point2D p) {
		if (p == null)
			mouseCoords.setText("Mouse: N/A");
		float v1 = (float) Math.round(p.getX() * 100) / 100;
		float v2 = (float) Math.round(p.getY() * 100) / 100;
		mouseCoords.setText(String.format("Mouse: %4.2f, %4.2f", v1, v2));
	}

	protected JLabel mouseCoords;

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		boardBox.setEnabled(enabled);
		numDiversSpinner.setEnabled(enabled);
		dSpinner.setEnabled(enabled);
		playerBox.setEnabled(enabled);
	}

	public void stateChanged(ChangeEvent arg0) {
		 if (arg0.getSource().equals(rSpinner))
		 config.setR(((Integer) ((JSpinner)
		 arg0.getSource()).getValue()).intValue());
		 else if(arg0.getSource().equals(dSpinner))
		 config.setD(((Integer) ((JSpinner)
		 arg0.getSource()).getValue()).intValue());
		 else if(arg0.getSource().equals(numDiversSpinner))
		 config.setNumDivers(((Integer) ((JSpinner)
		 arg0.getSource()).getValue()).intValue());
		 else if(arg0.getSource().equals(randomSeed))
			 config.setRandomSeed(((Long) ((JSpinner)
			 arg0.getSource()).getValue()).longValue());
		 else
		 throw new RuntimeException("Unknown State Changed Event!!");
	}

	public void itemStateChanged(ItemEvent arg0) {
		if (arg0.getSource().equals(playerBox)
				&& arg0.getStateChange() == ItemEvent.SELECTED) {
			// config.setActivePlayer((Class)arg0.getItem());
			selectedPlayer = (Class) arg0.getItem();
			config.setPlayerClass((Class<Player>) playerBox.getSelectedItem());

		}
		if (arg0.getSource().equals(boardBox)
				&& arg0.getStateChange() == ItemEvent.SELECTED) {
			config.setSelectedBoard((File) arg0.getItem());
		}
	}

	public void reloadBoards() {
		boardBox.removeAllItems();
		File[] files = config.getBoardList();
		for (int i = 0; i < files.length; i++)
			boardBox.addItem(files[i]);
	}

	public JSlider getSpeedSlider() {
		return speedSlider;
	}

	public void valueChanged(ListSelectionEvent e) {
	}

	public void updateScores(double s) {
		score.setText("Score: " + s);
	}
}
