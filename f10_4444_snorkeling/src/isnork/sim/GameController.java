/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isnork.sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import org.apache.log4j.Logger;
/**
 *
 * @author Satyajeet
 */
public class GameController {


    ArrayList<Boolean> isplayerdone;
    private Logger log = Logger.getLogger(GameController.class);


    // If gameover this function will return a (-1)
    public GameResult GamePlay(GameConfig gc_local)
    {
        isplayerdone = new ArrayList<Boolean>();
        GameResult gameresult = new GameResult(0, new ArrayList<Integer>());
        
       return gameresult;
    }


}