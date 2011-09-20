package isnork.sim.ui;


import isnork.sim.Board;
import isnork.sim.BoardPanel;
import isnork.sim.GameEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class BoardFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	protected JLabel playerLabel;
	protected JLabel round;
	protected BoardPanel bp;
	public BoardFrame(Board b){
		this.setPreferredSize(new Dimension(640, 480));

		this.bp = new BoardPanel();
		JScrollPane boardScroller = new JScrollPane();
		boardScroller.getViewport().add(bp);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(boardScroller, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("Explorers");
		
		//TODO: SETUP UP LABELS ON BOARD PANEL FOR PLAYER COLORS
		//Set up labels...
		playerLabel = new JLabel("Player Name");
		round = new JLabel("Round: 0");
		JPanel panel = new JPanel(new GridLayout(1,2));
		panel.add(playerLabel);
		panel.add(round);
		this.getContentPane().add(panel, BorderLayout.NORTH);
		
		this.pack();
		this.setVisible(false);
	}
	public void setEngine(GameEngine eng)
	{
		bp.setEngine(eng);
		this.repaint();
	}
	public void setBoard(Board board) {
		bp.setBoard(board);
		this.repaint();
	}
}
