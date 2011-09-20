/* 
 * 	$Id: GameEngine.java,v 1.6 2007/11/28 16:30:47 johnc Exp $
 * 
 * 	Programming and Problem Solving
 *  Copyright (c) 2007 The Trustees of Columbia University
 */
package isnork.sim;

import isnork.sim.GameListener.GameUpdateType;

import isnork.sim.GameObject.Direction;
import isnork.sim.ui.GUI;
import isnork.sim.ui.Text;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
class PlayerMover implements Runnable
{
	Player p;
	Direction d;
	public PlayerMover(Player p)
	{
		this.p=p;
	}
	@Override
	public void run() {
		d = p.getMove();
	}
	public Direction getD() {
		return d;
	}
}
class PlayerRunner implements Runnable
{
//	public abstract String tick(Point2D myPosition, Set<Observation> whatYouSee, Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations);
	Player p;
	Point2D pt;
	Set<Observation> see;
	Set<iSnorkMessage> incoming;
	Set<Observation> locs;
	String msg;
	public PlayerRunner(Player p, Set<Observation> see, Set<iSnorkMessage> incoming, Set<Observation> locs)
	{
		this.p=p;
		this.see=see;
		this.incoming = incoming;
		this.locs = locs;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		msg = p.tick((Point2D) p.location.clone(), see, incoming,locs);

	}
	public String getMsg() {
		return msg;
	}
	
}
class PlayerInit implements Runnable
{
	HashSet<SeaLifePrototype> protos;
	GameConfig config;
	Player p;
	Random r;
	public PlayerInit(Player pl,HashSet<SeaLifePrototype> p, GameConfig c,Random r)
	{
		protos = p;
		config = c;
		this.p = pl;
		this.r= r;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		p.Register(r);
		p.newGame(protos, 0, config.getD(), config.getR(), config.getNumDivers());
	}
	
}
class TournamentRunner implements Runnable{
	Text t;
	public TournamentRunner(Text t)
	{
		this.t=t;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		t.play();
	}
	
}
public final class GameEngine {
	private GameConfig config;
	private Board board;
	// private PlayerWrapper player;
	private boolean exceptioned = false;
	private int round;
	private ArrayList<GameListener> gameListeners;
	private Logger log;
	public static void println(Object o)
	{
		
	}
	public static void println(String s)
	{
	}
	public static void println(double s)
	{
	}
	public GameEngine() {
		config = new GameConfig();
		gameListeners = new ArrayList<GameListener>();
		board = new Board(10, 10);
		board.engine = this;

		log = Logger.getLogger(GameController.class);
	}

	public void addGameListener(GameListener l) {
		gameListeners.add(l);
	}

	public int getCurrentRound() {
		return round;
	}

	public GameConfig getConfig() {
		return config;
	}

	private Player curPlayer;
public Player getCurPlayer() {
	return curPlayer;
}
	public Board getBoard() {
		return board;
	}

	HashMap<SeaLife, QueuedSeaLife> queuedSealife;
	HashMap<Player, QueuedPlayer> queuedPlayers;
	HashMap<Player,HashSet<SeaLife>> beenSeenById;
	HashMap<Player,HashMap<String, Integer>> beenSeenByName;
	int dsq = 0;
	public int getDsq() {
		return dsq;
	}
	public boolean step() {
		if(round > 480)
		{
			Point2D origin = new Point2D.Double(0, 0);
			dsq = 0;
			for (Player pl : players) {
				if(!pl.location.equals(origin))
				{
					pl.happiness-=config.getPenalty();
					score-=config.getPenalty();
					dsq++;
				}
			}
			notifyListeners(GameUpdateType.GAMEOVER);
			return false;
		}
		HashSet<SeaLife> dequeue = new HashSet<SeaLife>();
		for (QueuedSeaLife qs : queuedSealife.values()) {
			qs.ttl--;
			if (qs.ttl == 0) {
				SeaLife s = qs.sealife;
				Direction d = qs.d;
				int dist = 1;
				// Do the move.
				Point2D p = new Point2D.Double(s.getLocation().getX() + d.dx, s
						.getLocation().getY() + d.dy);
				s.setLocation(p);
				s.setDirection(d);
				dequeue.add(qs.sealife);
			}
		}
		for (SeaLife s : dequeue)
			queuedSealife.remove(s);

		HashSet<Player> dequeuep = new HashSet<Player>();
		for (QueuedPlayer qs : queuedPlayers.values()) {
			qs.ttl--;
			if (qs.ttl == 0) {
				Player pl = qs.player;
				Direction d = qs.d;
				int dist = 1;
				// Do the move.
				Point2D p = new Point2D.Double(pl.location.getX() + d.dx,
						pl.location.getY() + d.dy);
				pl.location = p;
				dequeuep.add(qs.player);
			}
		}
		for (Player s : dequeuep)
			queuedPlayers.remove(s);

		for (SeaLife s : config.creatures) {
			if (s.speed > 0 && !queuedSealife.containsKey(s)) {
				// Do the move thing.
				Direction d = s.getNewDirection();
				Point2D p = new Point2D.Double(s.getLocation().getX() + d.dx, s
						.getLocation().getY() + d.dy);
				while (Math.abs(p.getX()) > GameConfig.d
						|| Math.abs(p.getY()) > GameConfig.d) {
					d = s.getNewDirection();
					p = new Point2D.Double(s.getLocation().getX() + d.dx, s
							.getLocation().getY() + d.dy);
				}
				QueuedSeaLife q = new QueuedSeaLife();
				q.d = d;
				q.sealife = s;
				if (d.diag)
					q.ttl = 3;
				else
					q.ttl = 2;
				queuedSealife.put(s, q);
			}
		}
		HashSet<iSnorkMessage> newmsg = new HashSet<iSnorkMessage>();
		for (Player pl : players) {
			HashSet<Observation> observations = new HashSet<Observation>();
			for (SeaLife s : config.creatures) {
				if(s.getLocation().distance(pl.location) <= config.r)
				{
					if(!beenSeenById.containsKey(pl))
						beenSeenById.put(pl, new HashSet<SeaLife>());
					if(!beenSeenByName.containsKey(pl))
						beenSeenByName.put(pl, new HashMap<String, Integer>());
					double happy = 0;
					
					if(!pl.location.equals(new Point2D.Double(0, 0))) { // if on the boat, doesn't affect
					    if(s.dangerous && s.getLocation().distance(pl.location) <= 1.5)
					        happy = happy - s.happiness*2;
					    else if(beenSeenById.get(pl).contains(s))
					        happy = 0;
					    else if(!beenSeenByName.get(pl).containsKey(s.getName()))
					    {
					        happy = s.happiness;
					        beenSeenByName.get(pl).put(s.getName(), 1);
					    }
					    else if(beenSeenByName.get(pl).get(s.getName()) == 1)
					    {
					        if(!pl.location.equals(new Point2D.Double(0, 0))) {
					            happy = s.happiness/2;
					            beenSeenByName.get(pl).put(s.getName(), 2);					        
					        }
					    }
					    else if(beenSeenByName.get(pl).get(s.getName()) == 2 && !pl.location.equals(new Point2D.Double(0, 0)))
					    {
					        happy = s.happiness/4;
					        beenSeenByName.get(pl).put(s.getName(), 3);
					    }

					    beenSeenById.get(pl).add(s);
					    pl.happiness += happy;
					    score += happy;
					}
					
					
					Observation o = new Observation();
					o.id=s.getId();
					o.name=s.getName();
					o.dir=s.getDirection();
					o.location=(Point2D) s.getLocation().clone();
					o.happy=happy;
					o.danger=s.dangerous;
					observations.add(o);

				}
			}
			HashSet<Observation> locations = new HashSet<Observation>();
			for (Player pp : players) {
				if(pp != pl && pp.location.distance(pl.location) <= config.r)
				{
					Observation o = new Observation();
					o.id=pp.getId();
					o.name=pp.getName();
					o.location=(Point2D) pp.location.clone();
					o.happy=0;
					o.danger=false;
					observations.add(o);
				}
				Observation o = new Observation();
				o.id=pp.getId();
				o.name=pp.getName();
				o.location=(Point2D) pp.location.clone();
				o.happy=0;
				o.danger=false;
				locations.add(o);
			}
			
			String msg = null;
			try
			{

				msg = pl.tick((Point2D) pl.location.clone(), observations, messages, locations);
			}
			catch(Exception e)
			{
				exceptioned = true;
				round = 481;
				return true;
			}
			
			if (msg != null){
				iSnorkMessage m = new iSnorkMessage(msg);
				m.sender=pl.id;
				m.location=pl.location;
				newmsg.add(m);
			}
			
			if (!queuedPlayers.containsKey(pl)) {
				Direction d = null;
				try
				{
					d = pl.getMove();
				}
				catch(Exception e)
				{
					System.err.println("Timeout/exception");
					exceptioned = true;
					round = 481;
					return true;
				}
				if (d != null) {
					Point2D p = new Point2D.Double(pl.location.getX() + d.dx,
							pl.location.getY() + d.dy);
					if (Math.abs(p.getX()) > GameConfig.d
							|| Math.abs(p.getY()) > GameConfig.d) {
						GameEngine.println("SIM>>Player " + pl.id
								+ " returned an invalid move");
					} else {
						QueuedPlayer q = new QueuedPlayer();
						q.player = pl;
						q.d = d;
						q.ttl = (d.diag ? 3 : 2);
						queuedPlayers.put(pl, q);
					}
				}
			}
		}
		messages = newmsg;
		notifyListeners(GameUpdateType.MOVEPROCESSED);
		round++;
		return true;
	}

	private HashSet<iSnorkMessage> messages;

	private final static void printUsage() {
		GameEngine.println("Usage: GameEngine gui");
		System.err
				.println("Usage: GameEngine text <board> <playerclass> <num mosquitos> <num lights> <long|short> {max rounds}");
	}

	public void removeGameListener(GameListener l) {
		gameListeners.remove(l);
	}

	private void notifyListeners(GameUpdateType type) {
		Iterator<GameListener> it = gameListeners.iterator();
		while (it.hasNext()) {
			it.next().gameUpdated(type);
		}
	}
	public static HashMap<Integer,Class<Player>> playerList;
	public static final void main(String[] args) {
		
		if (args.length < 1 || args.length > 7) {
			printUsage();
			System.exit(-1);
		}
		GameEngine engine = new GameEngine();
		if (args[0].equalsIgnoreCase("text")) {
			Random r = new Random();
			try {
				Thread.sleep(r.nextInt(300));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initDB();
	    	try {
	    		if(playerList == null)
	    		{
	    			playerList = new HashMap<Integer, Class<Player>>();
		    		Statement sp = GameEngine.conn.createStatement();
		    		ResultSet rs2 = sp.executeQuery("SELECT id,class from player");
		    		while(rs2.next())
		    		{
		    			playerList.put(rs2.getInt(1), (Class<Player>) Class.forName(rs2.getString(2)));
		    		}
	    		}
				Statement s = GameEngine.conn.createStatement();
				ResultSet rs1 = s.executeQuery("SELECT COUNT(id) FROM jobs where owned is null ");
				rs1.next();
				int n = rs1.getInt(1);
				int c = 0;
				while(n > 0)
				{
					ResultSet rs = s.executeQuery("SELECT job,id from jobs where owned is null  limit 1 FOR UPDATE");
					if(rs.next())
					{
						
						int nz = rs.getInt(2);
						String job = (rs.getString(1));
						Statement s2 = GameEngine.conn.createStatement();
						int zz = s2.executeUpdate("UPDATE jobs set owned=1, started=NOW() where id="+rs.getInt(2) + " and started is null");
						rs.close();
						if(zz == 0)
						{
							continue;
						}
						String[] args2 = job.split(";");
						runTourn(args2,nz);
						s2.executeUpdate("UPDATE jobs set ended=NOW() where id="+nz);

					}
					else
						break;
//						n--;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (args[0].equalsIgnoreCase("gui")) {

			new GUI(engine);
		} else if (args[0].equalsIgnoreCase("tournament")) {
			// runTournament(args, engine);
		} else {
			printUsage();
			System.exit(-1);
		}
	}

	HashSet<Player> players;
	double score;
	public boolean setUpGame() {
		players = new HashSet<Player>();
		score =0;
		messages = new HashSet<iSnorkMessage>();
		queuedSealife = new HashMap<SeaLife, GameEngine.QueuedSeaLife>();
		queuedPlayers = new HashMap<Player, GameEngine.QueuedPlayer>();
		beenSeenById = new HashMap<Player, HashSet<SeaLife>>();
		beenSeenByName = new HashMap<Player, HashMap<String,Integer>>();
		round = 0;
		config.load();
		HashSet<SeaLifePrototype> protos = new HashSet<SeaLifePrototype>();
		for(SeaLifePrototype t : config.sealife)
		{
			protos.add((SeaLifePrototype)t.clone());
		}
		for (int i = 0; i < config.getNumDivers(); i++) {
			long thisSeed = config.randomSeed + i * 3;
			try {
				curPlayer = config.getPlayerClass().newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			curPlayer.location = new Point2D.Double(0, 0);

			curPlayer.id = 0-i;
			Random r= new Random(thisSeed);

			try {
				curPlayer.Register(r);
				curPlayer.newGame(protos, config.getPenalty(), config.getD(), config.getR(), config.getNumDivers());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.exceptioned = true;
				return false;
			}
			players.add(curPlayer);
		}
		
		round = 0;
		notifyListeners(GameUpdateType.STARTING);
		return true;
	}

	public void mouseChanged() {
		notifyListeners(GameUpdateType.MOUSEMOVED);
	}

	class QueuedSeaLife {
		SeaLife sealife;
		Direction d;
		int ttl;
	}

	class QueuedPlayer {
		Player player;
		Direction d;
		int ttl;
	}

	public double getScores() {
		// TODO Auto-generated method stub
		return score;
	}
	public static int tourn_id = -1;
	public static int tourn_player_id = -1;
	private static void runTourn(String args[], int jid)
	{
		Statement s;
		java.net.InetAddress localMachine = null;
		try {
			localMachine = java.net.InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//static String tpl = "MAP;RADIUS;DIM;NUM;SEED";
    	String hostname =  localMachine.getHostName();
    	int tournament_id =-1;
		try {
			
			s = conn.createStatement();	
			s.execute("INSERT INTO tournament (start,source,map,r,d,n,seed,job_id)" +
					" VALUES (NOW(),\""+hostname+"\",\""+args[0]+"\",\""+args[1]+"\",\""+args[2]+"\",\""+args[3]+"\",\""+args[4]+"\","+jid+")",Statement.RETURN_GENERATED_KEYS);
			ResultSet rszz= s.getGeneratedKeys();
			rszz.next();
			tournament_id = rszz.getInt(1);
			tourn_id = tournament_id;
			GameEngine.println("ID " + tournament_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Integer pId: playerList.keySet())
		{
			Class<Player> p = playerList.get(pId);
			GameEngine engine = new GameEngine();
			long start = System.currentTimeMillis();
			Text t = new Text(engine);
			engine.getConfig().setPlayerClass(p);
			engine.getConfig().setSelectedBoard(new File("boards/"+args[0]));
			engine.getConfig().setR(Integer.valueOf(args[1]));
			engine.getConfig().setD(Integer.valueOf(args[2]));
			engine.getConfig().setNumDivers(Integer.valueOf(args[3]));
			engine.getConfig().setRandomSeed(Long.valueOf(args[4]));
			System.out.println("Starting tourn " + tournament_id + " on player " + pId);
			try
			{
				TournamentRunner tr = new TournamentRunner(t);
				Thread th = new Thread(tr);
				th.start();
				th.join(140000);
				if(th.isAlive())
				{
					th.interrupt();
					engine.exceptioned = true;
					System.err.println("Exception in Tournament " + tournament_id + ", player " + pId);
				}
			}
			catch(Exception e)
			{
				engine.exceptioned = true;
				System.err.println("Exception in Tournament " + tournament_id + ", player " + pId);
				e.printStackTrace();
			}
			try {
				s = conn.createStatement();
				int invalid;
				if(engine.getDsq() > 0)
					invalid = engine.getDsq();
				else
					invalid = 0;
				double score = engine.getScores();
				if(engine.exceptioned)
					score = -1;
				long time = System.currentTimeMillis() - start;
				time = time/1000;
				s.execute("INSERT INTO tournament_player (tournament_id,player_id,score,non_return,time) VALUES ("+tournament_id+", \""+pId+"\", " + score + ", "+invalid+", "+time+")");	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			engine = null;
			System.gc();
		}
		try {
			s = conn.createStatement();
			s.execute("UPDATE tournament SET end=NOW() where id="+tournament_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static Connection conn = null;
	static void initDB()
	{
        try
        {
//            String url = "jdbc:sqlite:"+filename;
//            Class.forName ("org.sqlite.JDBC").newInstance ();
//            conn = DriverManager.getConnection (url);
        	String userName = "ppsf09";
            String password = "ppsf09";
            String url = "jdbc:mysql://projects.seas.columbia.edu/4444p1";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
        }
        catch (Exception e)
        {
            GameEngine.println ("Cannot connect to database server");
        }

	}
}
