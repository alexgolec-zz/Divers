/* 
 * 	$Id: CellComponent.java,v 1.1 2007/09/06 14:51:49 johnc Exp $
 * 
 * 	Programming and Problem Solving
 *  Copyright (c) 2007 The Trustees of Columbia University
 */
package isnork.sim;

import isnork.sim.Cell.CellListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public final class CellComponent extends JPanel implements CellListener {
	private Cell myCell;
	private static final long serialVersionUID = 1L;
	private static HashMap<String, Image> icons = new HashMap<String, Image>();
	private static int cacheWidth = 0;
	private static int cacheHeight = 0;
	// private Color color;

	public CellComponent(Cell cell) {
		myCell = cell;
		// this.color = color;
		this.setMinimumSize(new Dimension(10, 10));
		this.setPreferredSize(new Dimension(10, 10));
		updateToolTip();
	}

	public Cell getCell() {
		return myCell;
	}

	public void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();

		g.setColor(Color.white); // clear by painting white.
		g.fillRect(0, 0, w - 1, h - 1);
//
//		Graphics2D g2d = (Graphics2D) g;
//
//		// if (myCell.getNumExplorers() > 0)
//		// {
//		// // if(myCell.getNumExplorers() == 1)
//		// // g.setColor(myCell.getExplorers().get(0).getId())
//		// g.setColor(color);
//		// g.fillRect(0, 0, w - 1, h - 1);
//		// }
//
//		// Draw terrain
//		Color fillColor = Color.WHITE;
//
//		g.setColor(fillColor);
//		g.fillRect(0, 0, w, h);
//
//		// draw cell outline.
//		g.setColor(Color.black);
//		Rectangle2D rect = new Rectangle2D.Double(0, 0, w-.2, h-.2);
//		g2d.draw(rect);
//
//		if(w!= cacheWidth || h != cacheHeight)
//		{
//			cacheWidth =w;
//			cacheHeight = h;
//			icons = new HashMap<String,Image>();
//		}
//		if(myCell.sealife != null)
//		for(SeaLife s : myCell.sealife)
//		{
//			if(s.filename != null)
//			{
//				if(!icons.containsKey(s.filename))
//				{
//					BufferedImage img = null;
//				    try {
//				        img = ImageIO.read(new File("icons/nemo.png"));
//				        icons.put("nemo", img.getScaledInstance(w, h, BufferedImage.SCALE_FAST));
//				    } catch (IOException ioe) {
//				    	GameEngine.println("Unable to read image nemo");
//				    }
//				}
//				g2d.drawImage(icons.get(s.filename),
//						0, 0, null);
//			}
//			else
//				g2d.drawString(s.name.substring(0, 1), 0, 0);
//		}

	}

	public void CellUpdated(Cell source) {
		updateToolTip();
		repaint();
	}

	private void updateToolTip() {
		String tip = "Location: (" + myCell.getLocation().x + ", "
				+ myCell.getLocation().y + ")";
		// if (myCell.getNumExplorers() > 0)
		// {
		// for (int i = 0; i < myCell.getExplorers().size(); i++)
		// {
		// Explorer e = myCell.getExplorers().get(i);
		// tip += " | E" + e.getId();
		// }
		// }
		//
		// tip += " | " + TERRAIN_NAMES[myCell.getCellTerrain()];
		// if (myCell.isStartable())
		// tip += " | Startable";

		this.setToolTipText(tip);

	}

	// public static final void main(String []args){
	// JFrame f = new JFrame();
	// f.getContentPane().setLayout(new BorderLayout());
	// f.setPreferredSize(new Dimension(200, 100));
	// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	//
	// JPanel panel = new JPanel(new FlowLayout());
	// CellPrivate c1 = new CellPrivate(0,0,1d);
	// c1.setCellTerrain(WATER);
	// CellPrivate c2 = new CellPrivate(1,0,1d);
	// c2.setStartable(true);
	// c2.setCellTerrain(LAND);
	// panel.add(new CellComponent(c1, Color.YELLOW));
	// panel.add(new CellComponent(c2, Color.BLUE));
	// f.getContentPane().add(panel, BorderLayout.CENTER);
	//
	// f.pack();
	// f.setVisible(true);
	//
	// }
}
