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

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

public final class GameEngine {
	private GameConfig config;
	private Board board;
	// private PlayerWrapper player;
	private int round;
	private ArrayList<GameListener> gameListeners;
	private Logger log;

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

	public Board getBoard() {
		return board;
	}

	HashMap<SeaLife, QueuedSeaLife> queuedSealife;
	HashMap<Player, QueuedPlayer> queuedPlayers;
	HashMap<Player,HashSet<SeaLife>> beenSeenById;
	HashMap<Player,HashMap<String, Integer>> beenSeenByName;
	public boolean step() {
		if(round > 480)
		{
			Point2D origin = new Point2D.Double(0, 0);
			for (Player pl : players) {
				if(!pl.location.equals(origin))
				{
					pl.happiness-=config.getPenalty();
					score-=config.getPenalty();
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
					int happy = 0;
					
					if(s.dangerous && s.getLocation().distance(pl.location) <= 1.5)
						happy = happy - 2*s.happiness;
					else if(beenSeenById.get(pl).contains(s))
						happy = 0;
					else if(!beenSeenByName.get(pl).containsKey(s.getName()))
					{
						happy = s.happiness;
						beenSeenByName.get(pl).put(s.getName(), 1);
					}
					else if(beenSeenByName.get(pl).get(s.getName()) == 1)
					{
						happy = s.happiness/2;
						beenSeenByName.get(pl).put(s.getName(), 2);
					}
					else if(beenSeenByName.get(pl).get(s.getName()) == 2)
					{
						happy = s.happiness/4;
						beenSeenByName.get(pl).put(s.getName(), 3);
					}
					
					beenSeenById.get(pl).add(s);
					
					Observation o = new Observation();
					o.id=s.getId();
					o.name=s.getName();
					o.location=(Point2D) s.getLocation().clone();
					o.happy=happy;
					o.danger=s.dangerous;
					observations.add(o);
					pl.happiness += happy;
					score += happy;
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
			
			String msg = pl.tick((Point2D) pl.location.clone(), observations, messages,locations);
			if (msg != null){
				iSnorkMessage m = new iSnorkMessage(msg);
				m.sender=pl.id;
				m.location=pl.location;
				newmsg.add(m);
			}
			
			if (!queuedPlayers.containsKey(pl)) {
				Direction d = pl.getMove();
				
				if (d != null) {
					Point2D p = new Point2D.Double(pl.location.getX() + d.dx,
							pl.location.getY() + d.dy);
					if (Math.abs(p.getX()) > GameConfig.d
							|| Math.abs(p.getY()) > GameConfig.d) {
						System.err.println("SIM>>Player " + pl.id
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
		System.err.println("Usage: GameEngine gui");
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

	public static final void main(String[] args) {
		
		if (args.length < 1 || args.length > 7) {
			printUsage();
			System.exit(-1);
		}
		GameEngine engine = new GameEngine();
		if (args[0].equalsIgnoreCase("text")) {
			// TextInterface ti = new TextInterface();
			// ti.register(engine);
			// ti.playGame();
			if (args.length < 6) {
				printUsage();
				System.exit(-1);
			}
			Text t = new Text(engine);
			engine.getConfig().setSelectedBoard(new File(args[2]));
			try {
				engine.getConfig().setPlayerClass(
						(Class<Player>) Class.forName(args[3]));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (args[6].equals("long"))
				t.setLongMode(true);
			if (args.length == 8)
				engine.getConfig().setMaxRounds(Integer.valueOf(args[7]));
			t.play();
			// throw new
			// RuntimeException("Text interface not implemented. Sorry.");
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
	int score;
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
			curPlayer.Register(new Random(thisSeed));
			curPlayer.id = 0-i;
			curPlayer.newGame(protos,config.getPenalty(),config.getD(),config.getR(),config.getNumDivers());
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

	public int getScores() {
		// TODO Auto-generated method stub
		return score;
	}
}
