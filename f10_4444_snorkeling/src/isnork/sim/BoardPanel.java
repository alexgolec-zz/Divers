/* 
 * 	$Id: BoardPanel.java,v 1.1 2007/09/06 14:51:49 johnc Exp $
 * 
 * 	Programming and Problem Solving
 *  Copyright (c) 2007 The Trustees of Columbia University
 */
package isnork.sim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class BoardPanel extends JPanel implements MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Point2D MouseCoords;

	private Board board;

	private GameEngine engine;
	private static HashMap<String, Image> icons = new HashMap<String, Image>();
	private static int cacheWidth = 0;
	Cursor curCursor;

	public BoardPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.setBackground(Color.white);
		addMouseMotionListener(this);

	}

	Rectangle2D boardBox = null;
	public static Line2D debugLine = null;

	final Color default_color = new Color(0, 65, 18);
	final Color default_danger_color = new Color(255, 0, 0);
	final Color ocrean_color = new Color(205, 221, 229);
	final Color visible_color = new Color(205,0,229);
	public void paint(Graphics g) {
		if (board == null)
			return;
		int w = (int) Board.toScreenSpaceNoOffset(1);
		if (w != cacheWidth) {
			cacheWidth = w;
			icons = new HashMap<String, Image>();
		}
		int[][] counts = new int[GameConfig.d * 2 + 1][GameConfig.d * 2 + 1];
		for (int i = 0; i < counts.length; i++) {
			for (int j = 0; j < counts.length; j++)
				counts[i][j] = 0;
		}
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(ocrean_color);
		boardBox = new Rectangle2D.Double(
				Board.toScreenSpace(GameConfig.d * -1),
				Board.toScreenSpace(GameConfig.d * -1),
				Board.toScreenSpace(GameConfig.d + 1),
				Board.toScreenSpace(GameConfig.d + 1));
		g2D.fill(boardBox);
		g2D.setColor(Color.black);
		g2D.fillOval((int) Board.toScreenSpace(0),
				(int) Board.toScreenSpace(0),
				(int) Board.toScreenSpaceNoOffset(1),
				(int) Board.toScreenSpaceNoOffset(1));
		
		if(onPlayer)
		{
			g2D.setColor(visible_color);
			double tx = Board.toScreenSpace(MouseCoords.getX() - engine.getConfig().getR());
			double ty = Board.toScreenSpace(MouseCoords.getY()- engine.getConfig().getR() );
			Ellipse2D visibleBox = new Ellipse2D.Double(tx,ty, Board.toScreenSpaceNoOffset(engine.getConfig().getR()*2 + 1) , Board.toScreenSpaceNoOffset(engine.getConfig().getR()*2 + 1) );
			g2D.fill(visibleBox);
		}
		g2D.setColor(Color.black);
		drawGrid(g2D);

		if (engine != null && engine.getConfig() != null
				&& engine.getConfig().creatures != null) {
			for (SeaLife s : engine.getConfig().creatures) {
				counts[(int) (s.getLocation().getX() + GameConfig.d)][(int) (s
						.getLocation().getY() + GameConfig.d)]++;
				if (s.filename != null) {

					if (!icons.containsKey(s.filename)) {
						BufferedImage img = null;
						try {
							img = ImageIO.read(new File("boards/icons/" + s.filename));
							icons.put(s.filename, img.getScaledInstance(w, w,
									BufferedImage.SCALE_FAST));
						} catch (IOException ioe) {
							GameEngine.println("Unable to read image icons/"
									+ s.filename);
							ioe.printStackTrace();
						}
					}
					g2D.drawImage(icons.get(s.filename),
							(int) Board.toScreenSpace(s.getLocation().getX()),
							(int) Board.toScreenSpace(s.getLocation().getY()),
							null);

				} else {
					Ellipse2D d = new Ellipse2D.Double(Board.toScreenSpace(s
							.getLocation().getX()), Board.toScreenSpace(s
							.getLocation().getY()),
							Board.toScreenSpaceNoOffset(1),
							Board.toScreenSpaceNoOffset(1));
					g2D.fill(d);
				}
			}
			for (Player p : engine.players) {
				counts[(int) (p.location.getX() + GameConfig.d)][(int) (p.location
						.getY() + GameConfig.d)]++;
				BufferedImage img = null;
				try {
					img = ImageIO.read(new File("boards/icons/diver.png"));
					icons.put("diver", img.getScaledInstance(w, w,
							BufferedImage.SCALE_FAST));
				} catch (IOException ioe) {
					ioe.printStackTrace();
					GameEngine.println("Unable to read image icons/diver.png");
				}
				g2D.drawImage(icons.get("diver"),
						(int) Board.toScreenSpace(p.location.getX()),
						(int) Board.toScreenSpace(p.location.getY()), null);

			}
			for (int i = 0; i < counts.length; i++) {
				for (int j = 0; j < counts.length; j++)
					if (counts[i][j] > 1) {
						g2D.drawString("" + counts[i][j],
								(float) Board.toScreenSpaceNoOffset(i),
								(float) Board.toScreenSpaceNoOffset(j) + w);
					}
			}
		}
	}

	private void drawGrid(Graphics2D g2d) {
		g2d.setColor(Color.black);
		for (int i = -GameConfig.d; i <= GameConfig.d + 1; i++) {
			Line2D line = new Line2D.Double(0, Board.toScreenSpace(i),
					Board.toScreenSpace(GameConfig.d + 1),
					Board.toScreenSpace(i));
			g2d.draw(line);
			line = new Line2D.Double(Board.toScreenSpace(i), 0,
					Board.toScreenSpace(i),
					Board.toScreenSpace(GameConfig.d + 1));
			g2d.draw(line);
		}
	}

	public BoardPanel(GameEngine eng) {
		setEngine(eng);
		setBoard(engine.getBoard());
		addMouseMotionListener(this);
	}

	public void setEngine(GameEngine eng) {
		engine = eng;
	}

	public void setBoard(Board b) {
		board = b;
		// this.setPreferredSize(new Dimension((int) Board
		// .toScreenSpace(GameConfig.d), (int) Board
		// .toScreenSpace(GameConfig.d)));

		repaint();
		revalidate();
	}

	Line2D selectedLine = null;

	private int floorAbs(double v) {
		int r = (int) Math.abs(v);
		if (v < 0)
			r = 0 - r - 1;
		return r;
	}
	boolean onPlayer = false;
	Point mouseLoc = null;
	public void mouseMoved(MouseEvent e) {
		MouseCoords = Board.fromScreenSpace(e.getPoint());
		MouseCoords = new Point2D.Double(floorAbs(MouseCoords.getX()),
				floorAbs(MouseCoords.getY()));
		if (engine != null)
			engine.mouseChanged();
		String tip = "<html><b>Location:</b> (" + MouseCoords.getX() + ", " + MouseCoords.getY() + ")"+(MouseCoords.getX()==MouseCoords.getY() && MouseCoords.getX()==0 ? " <b>BOAT</B>" : "")+"<br><b>Sealife here:</b><br>";
		int n=0;
		if(engine != null && engine.getConfig() != null)
		{
			for(SeaLife s : engine.getConfig().creatures)
			{
				if(s.getLocation().equals(MouseCoords))
				{
					tip+=s.toString()+"<br>";
					n++;
				}
			}
			if(n==0)
				tip+="<i>No sealife here</i><br>";
			n =0;
			tip +="<b>Players here:</b><br>";
			onPlayer = false;
			for(Player p : engine.players)
			{
				if(p.location.equals(MouseCoords))
				{
					onPlayer = true;
					tip+=p.toString()+"<br>";
					n++;
				}
			}
			if(n==0)
				tip+="<i>No players here</i><br>";
		}
		tip += "</html>";
		this.setToolTipText(tip);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void recalculateDimensions() {
		int my_w = this.getWidth();
		int my_h = this.getHeight();
		int d = Math.min(my_w, my_h);
		d -= 10;
		if (d > 0)
			Board.pixels_per_pixel = ((double) GameConfig.d * 2 + 1)
					* Board.pixels_per_meter / d;
		repaint();
	}
	public void recalculateDimensions(int my_w, int my_h) {
		int d = Math.min(my_w, my_h);
		d -= 10;
		if (d > 0)
			Board.pixels_per_pixel = ((double) GameConfig.d * 2 + 1)
					* Board.pixels_per_meter / d;
		repaint();
	}
}
