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
		if(msg.length() != 1)
			System.err.println("iSnork Received invalid message: " + msg);
		else if(msg.toLowerCase() != "a" &&
				msg.toLowerCase() != "b" &&
				msg.toLowerCase() != "c" &&
				msg.toLowerCase() != "d" &&
				msg.toLowerCase() != "e" &&
				msg.toLowerCase() != "f" &&
				msg.toLowerCase() != "g" &&
				msg.toLowerCase() != "h" &&
				msg.toLowerCase() != "i" &&
				msg.toLowerCase() != "j" &&
				msg.toLowerCase() != "k" &&
				msg.toLowerCase() != "l" &&
				msg.toLowerCase() != "m" &&
				msg.toLowerCase() != "n" &&
				msg.toLowerCase() != "o" &&
				msg.toLowerCase() != "p" &&
				msg.toLowerCase() != "q" &&
				msg.toLowerCase() != "r" &&
				msg.toLowerCase() != "s" &&
				msg.toLowerCase() != "t" &&
				msg.toLowerCase() != "u" &&
				msg.toLowerCase() != "v" &&
				msg.toLowerCase() != "w" &&
				msg.toLowerCase() != "x" &&
				msg.toLowerCase() != "y" &&
				msg.toLowerCase() != "z"
		)
			System.err.println("iSnork Received invalid message: " + msg);
		else
			this.msg= msg.toLowerCase();
	}
}
