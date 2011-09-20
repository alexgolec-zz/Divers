package isnork.sim;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class GameObject {
	protected double x;
	protected double y;
	public enum Direction{
		E(0,false,1,0),NE(45,true,1,-1),N(90,false,0,-1),NW(135,true,-1,-1),W(180,false,-1,0),SW(225,true,-1,1),S(270,false,0,1),SE(315,true,1,1),STAYPUT(0,false,0,0);
		int deg;
		boolean diag;
		public int dx;
		public int dy;
		public int getDx() {
			return dx;
		}
		public int getDy() {
			return dy;
		}
		Direction(int degrees, boolean diag,int x,int y)
		{
			deg = degrees;
			this.diag = diag;
			this.dx=x;
			this.dy=y;
		}
		public int getDegrees() {
			return deg;
		}
		public boolean isDiag()
		{
			return diag;
		}
		public static ArrayList<Direction> allBut(Direction n)
		{
			ArrayList<Direction> r = new ArrayList<GameObject.Direction>();
			for(Direction d : Direction.values())
			{
				if(n == null || d.deg != n.deg )
					r.add(d);
			}
			return r;
		}
	}
	public Point2D getLocation()
	{
		return new Point2D.Double(x,y);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
}
