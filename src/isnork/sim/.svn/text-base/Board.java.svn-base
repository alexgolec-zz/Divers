/* 
 * 	$Id: Board.java,v 1.6 2007/11/28 16:30:18 johnc Exp $
 * 
 * 	Programming and Problem Solving
 *  Copyright (c) 2007 The Trustees of Columbia University
 */
package isnork.sim;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Stack;


/**
 * @author Jon Bell
 * 
 */
public final class Board {
	public static final int pixels_per_meter = 160;
	public static double pixels_per_pixel = 10;
	private int width;
	private int height;
	ArrayList<SeaLifePrototype> sealife;
	public GameEngine engine;
	ArrayList<Player> snorkelers;
	Cell[][] cells;
	
	private double round(double n) {
		return Math.round(100 * n) / 100;
	}

	public static Point2D toScreenSpace(Point2D point2d) {
		Point2D clone = new Point2D.Double();
		clone.setLocation(toScreenSpace(point2d.getX()),
				toScreenSpace(point2d.getY()));
		return clone;
	}
	public ArrayList<SeaLifePrototype> getSealife() {
		return sealife;
	}

	public static double fromScreenSpace(double v) {
		return (v * pixels_per_pixel / pixels_per_meter) - GameConfig.d;
	}

	public static double toScreenSpace(double v) {
		return (GameConfig.d+ v) * pixels_per_meter / pixels_per_pixel;
	}
	public static double toScreenSpaceNoOffset(double v) {
		return (v) * pixels_per_meter / pixels_per_pixel;
	}
	public static Point2D fromScreenSpace(Point2D p) {
		Point2D r = new Point2D.Double();
		r.setLocation(fromScreenSpace(p.getX()), fromScreenSpace(p.getY()));
		return r;
	}

	public Board() {
		this(100,100);

	}

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		init();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void init() {

	}

	public boolean inBounds(int x, int y) {
		return (x >= 0 && x < width) && (y >= 0 && y < height);
	}

}
