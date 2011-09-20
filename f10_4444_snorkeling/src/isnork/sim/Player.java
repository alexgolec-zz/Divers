
package isnork.sim;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Jon Bell
 */
public abstract class Player {

	Point2D location;
	int id;
	double happiness = 0;
	public double getHappiness()
	{
		return happiness;
	}
	public int getId()
	{
		return id;
	}
    /**
     * Returns the name for this player
     */
    public abstract String getName();
    
    /**
     * Called on the player when it is instantiated
     * @param thisSeed 
     */
	public void Register(Random r)
	{
		//Do nothing is OK! You'll need the random though :)
		random = r;
	}
	public abstract void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty, int d, int r, int n);
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ID: "+id+", happiness: "+happiness;
	}
	protected Random random;
	public abstract String tick(Point2D myPosition, Set<Observation> whatYouSee, Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations);
	public abstract Direction getMove();
}

