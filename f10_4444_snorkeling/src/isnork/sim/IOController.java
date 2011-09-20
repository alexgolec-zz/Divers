/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isnork.sim;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Satyajeet
 */
public class IOController {

    public ArrayList<String> getPlayerList()
    {
        ArrayList<String> PlayerNames = new ArrayList<String>();

        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream("Game.playerlist");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
              // Print the content on the console
              //GameEngine.println (strLine);
              PlayerNames.add(strLine);
            }
            //Close the input stream
            in.close();
            }catch (Exception e){//Catch exception if any
              GameEngine.println("Error: " + e.getMessage());
            }



        return PlayerNames;
    }

    public Player getPObject(String playername)
    {
        Player myplayer = null;
        try {
            Class<Player> pc_object = (Class<Player>) Class.forName(playername);
             myplayer = (Player) pc_object.newInstance();

        } catch (InstantiationException ex) {
            GameEngine.println("Player CLass " + playername + " could not be instantiated!");
        } catch (IllegalAccessException ex) {
            GameEngine.println("Player CLass " + playername + " Illegal Access!");
        } catch (ClassNotFoundException ex) {
            GameEngine.println("Player CLass " + playername + " was not found!");
        }
         return myplayer;
    }

}
