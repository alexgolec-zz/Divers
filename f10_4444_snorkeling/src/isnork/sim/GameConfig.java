/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isnork.sim;

import java.awt.Point;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;


import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class GameConfig {

	int gameDelay = 500;
	int number_of_rounds;
	int current_round;
	int penalty = 0;
	
	public int getPenalty() {
		return penalty;
	}
	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}
	public long getRandomSeed() {
		return randomSeed;
	}
	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
		random = new Random(randomSeed);
	}
	public static int d = 20;
	int num_divers = 20;
	long randomSeed = System.currentTimeMillis();
	
	public int getNumDivers() {
		return num_divers;
	}
	public void setNumDivers(int num_divers) {
		this.num_divers = num_divers;
	}
	public void setD(int d) {
		
		GameConfig.d = d;
	}

	public int getD() {
		return d;
	}

	int r = 5;

	public void setR(int r) {
		this.r = r;
	}

	public int getR() {
		return r;
	}

	String selectedBoard = null;
	int max_rounds = -1;
	private ArrayList<Class<Player>> availablePlayers;
	private Class<Player> playerClass;
	public static Random random;
	private ArrayList<File> availableBoards;

	ArrayList<SeaLifePrototype> sealife;
	private Logger log = Logger.getLogger(this.getClass());
	private File boardFile;
	int num_lights = 5;

	public Class<Player> getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(Class<Player> playerClass) {
		this.playerClass = playerClass;
	}

	public static final int max_rounds_max = 4000;

	public void setSelectedBoard(File f) {
		boardFile = f;
	}

	/**
	 * Obtain the list of all valid boards in the location specified by the xml
	 * configuration file.
	 * 
	 * @return An array of valid board files.
	 */
	public File[] getBoardList() {
		File[] ret = new File[availableBoards.size()];
		return availableBoards.toArray(ret);
	}

	public void setMaxRounds(int v) {
		this.max_rounds = v;
	}

	public int getMaxRounds() {
		return max_rounds;
	}

	public GameConfig() {
		

		availablePlayers = new ArrayList<Class<Player>>();
		availableBoards = new ArrayList<File>();
		readBoards();
		readPlayers();
	}

	private void readPlayers() {
		try {
			File f = new File("playerClasses.txt");
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				String t = s.nextLine();
					try {
						availablePlayers.add((Class<Player>) Class
								.forName(t));
					} catch (ClassNotFoundException e) {
						log.error("[Configuration] Class not found: "
								+ t);
					}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File sourceFolder = new File("bin"+System.getProperty("file.separator")+"isnork"+System.getProperty("file.separator"));
		for(File f : sourceFolder.listFiles())
		{

			if(f.getName().length() == 2 && f.getName().substring(0,1).equals("g"))
			{
				for(File c : f.listFiles())
				{
					if(c.getName().endsWith(".class") ){
						String className = c.toString().replace(System.getProperty("file.separator"),".").replace("bin.","");						className = className.substring(0, className.length() - 6);
						 Class theClass = null;
				          try{
				            theClass = Class.forName(className, false,this.getClass().getClassLoader());
				            if(theClass.getSuperclass() != null && theClass.getSuperclass().toString().equals("class isnork.sim.Player"))
				            {
				            	if(!availablePlayers.contains((Class<Player>) theClass))
				            		availablePlayers.add((Class<Player>) theClass);
				            }
				          }catch(NoClassDefFoundError e){
				            continue;
				          } catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							continue;
						}

					}
					else if(c.isDirectory())
					{
						for(File ca : c.listFiles())
						{
							if(ca.getName().endsWith(".class") ){
								String className = ca.toString().replace(c.toString(),"").replaceAll("/", ".");
								className = className.substring(0, className.length() - 6);
								 Class theClass = null;
						          try{
						            theClass = Class.forName(className, false,this.getClass().getClassLoader());
						            if(theClass.getSuperclass() != null && theClass.getSuperclass().toString().equals("class isnork.sim.Player"))
						            {
						            	if(!availablePlayers.contains((Class<Player>) theClass))
						            		availablePlayers.add((Class<Player>) theClass);
						            }
						          }catch(NoClassDefFoundError e){
						            continue;
						          } catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									continue;
								}

							}
							else if(c.isDirectory())
							{
								
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Read in configuration file.
	 * 
	 * @param file
	 */
	public void load() {
		
		File f = getSelectedBoard();
		try {
			XMLDecoder d = new XMLDecoder(new FileInputStream(f));
			sealife = new ArrayList<SeaLifePrototype>();
			SeaLifePrototype n = (SeaLifePrototype) d.readObject();
			while (n != null) {
				sealife.add(n);
				try {
					n = (SeaLifePrototype) d.readObject();
				} catch (ArrayIndexOutOfBoundsException e) {
					n = null;
				}
			}
		} catch (IOException e) {
			GameEngine.println("Error reading configuration file:"
					+ e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		initSeaCreatures();
	}

	ArrayList<SeaLife> creatures;

	public void initSeaCreatures() {
		GameConfig.random = new Random(randomSeed);
		creatures = new ArrayList<SeaLife>();
		int n = 0;
		for (SeaLifePrototype p : sealife) {
			int min = p.getMinCount();
			int max = p.getMaxCount() + 1;
			int actual = GameConfig.random.nextInt(max - min) + min;
			for (int i = 0; i < actual; i++) {
				SeaLife s = new SeaLife(p);
				s.setLocation(new Point(GameConfig.random.nextInt(d * 2+1) - d,
						GameConfig.random.nextInt(d * 2+1) - d));
				s.setId(n);
				creatures.add(s);
				n++;
			}
		}
	}

	/**
	 * Read all xml files from the board directory. Accept them only if valid.
	 * 
	 */
	public void readBoards() {
		availableBoards.clear();
		String s = "boards";

		File dir = new File(s);

		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().toLowerCase().endsWith(".xml");
			}
		});
		/* Board b = new Board(1,1); */
		for (int i = 0; i < files.length; i++) {
			/*
			 * try{ b.load(files[i]); availableBoards.add(files[i]);
			 * }catch(IOException e){ log.error("Problem loading board file " +
			 * files[i]); } catch(BoardSanityException e){
			 * log.error("Sanity problem loading board file " +files[i]+". " +
			 * e); }
			 */
			availableBoards.add(files[i]);
		}
		if (availableBoards.size() > 0)
			boardFile = availableBoards.get(0);
		else
			boardFile = null;
	}

	public File getSelectedBoard() {
		return boardFile;
	}

	public ComboBoxModel getPlayerListModel() {
		DefaultComboBoxModel m = new DefaultComboBoxModel();
		for (Class c : availablePlayers) {
			m.addElement(c);
		}
		return m;
	}

	public ArrayList<SeaLifePrototype> getSeaLifePrototypes() {
		// TODO Auto-generated method stub
		return sealife;
	}

}
