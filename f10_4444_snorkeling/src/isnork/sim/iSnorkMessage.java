package isnork.sim;

import java.awt.geom.Point2D;

public class iSnorkMessage {
	String msg;
	Point2D location;
	int sender;
	public int getSender() {
		return sender;
	}
	public String getMsg() {
		return msg;
	}
	public Point2D getLocation() {
		return location;
	}
	public iSnorkMessage(String msg)

	{

	    if(msg == null)

	        this.msg = msg;

	    else if(msg.length() != 1)

	        GameEngine.println("iSnork Received invalid message: " + msg);

	    else if(!msg.toLowerCase().equals("a") &&

	            !msg.toLowerCase().equals("b") &&

	            !msg.toLowerCase().equals("c") &&

	            !msg.toLowerCase().equals("d") &&

	            !msg.toLowerCase().equals("e") &&

	            !msg.toLowerCase().equals("f") &&

	            !msg.toLowerCase().equals("g") &&

	            !msg.toLowerCase().equals("h") &&

	            !msg.toLowerCase().equals("i") &&

	            !msg.toLowerCase().equals("j") &&

	            !msg.toLowerCase().equals("k") &&

	            !msg.toLowerCase().equals("l") &&

	            !msg.toLowerCase().equals("m") &&

	            !msg.toLowerCase().equals("n") &&

	            !msg.toLowerCase().equals("o") &&

	            !msg.toLowerCase().equals("p") &&

	            !msg.toLowerCase().equals("q") &&

	            !msg.toLowerCase().equals("r") &&

	            !msg.toLowerCase().equals("s") &&

	            !msg.toLowerCase().equals("t") &&

	            !msg.toLowerCase().equals("u") &&

	            !msg.toLowerCase().equals("v") &&

	            !msg.toLowerCase().equals("w") &&

	            !msg.toLowerCase().equals("x") &&

	            !msg.toLowerCase().equals("y") &&

	            !msg.toLowerCase().equals("z")

	    )

	        GameEngine.println("iSnork Received invalid message: " + msg);

	    else

	        this.msg= msg.toLowerCase();
	}
}
