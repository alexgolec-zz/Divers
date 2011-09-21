package isnork.sim;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;



public class GenerateTournamentScript {

	static String tpl = "MAP;RADIUS;DIM;NUM;SEED";
	static void printVersions()
	{
		
		
		String[] maps = {
				"lochness.xml",
				"100.xml",
				"400.xml",
				"800.xml",
				"DiversityDay.xml",
				"DenseDiversityDay.xml",
				"g2Benchmark.xml",
				"HiddenTreasureWithMines.xml",
				"lochnessPlus.xml",
				"Piranha.xml",
				"g5_very_happy.xml",
				"g2Benchmark.xml",

		};
		String[] rs = {"5","8","15"};
		String[] ds = {"30","40", "60"};
		String[] ns = {"1","2","7","20","30"};
//		String[] rs = {"5"};
//		String[] ds = {"30","60"};
//		String[] ns = {"20"};
		
		long[] seeds = new long[20];
		Random rand  = new Random();
		for(int i = 0; i<seeds.length;i++)
		{
			seeds[i] = rand.nextLong();
		}
		for(int i =0;i<seeds.length;i++)
			for(String m : maps)
				
					for(String d : ds)
					{
						if(d.equals("60") && !m.equals("800.xml") && !m.equals("DenseDiversityDay.xml"))
							continue;
							for(String r : rs)
								for(String n : ns)
								{
								Statement s;
								try {
									s = conn.createStatement();	
									s.execute("INSERT INTO jobs (job) VALUES (\""+(tpl.replace("MAP", m).
											replace("RADIUS", r).replace("DIM", d).replace("NUM", ""+n).replace("SEED",""+seeds[i]))+"\")");
									} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
					}
	}
	static Connection conn = null;
	static void initDB(String filename)
	{
        try
        {
//            String url = "jdbc:sqlite:"+filename;
//            Class.forName ("org.sqlite.JDBC").newInstance ();
//            conn = DriverManager.getConnection (url);
        	String userName = "";
            String password = "";
            String url = "jdbc:mysql://localhost/4444p1";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            GameEngine.println ("Database connection established");
        }
        catch (Exception e)
        {
            GameEngine.println ("Cannot connect to database server");
        }

	}
	public static void main(String[] args) {
		initDB("results.db");
		printVersions();
//		generate();
//		make6OfSame();
//		make3v3();
//			generate2v2s();
//			enerate5v5s();
//			generate8p();
//			generate9p();
//			generate2v2v2v2();
//			generateSingles();
		
	}
}
