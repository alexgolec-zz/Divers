/* 
 * 	$Id: GUI.java,v 1.4 2007/11/14 22:02:59 johnc Exp $
 * 
 * 	Programming and Problem Solving
 *  Copyright (c) 2007 The Trustees of Columbia University
 */
package isnork.sim.ui;

import isnork.sim.GameEngine;
import isnork.sim.GameListener;

import javax.swing.JTabbedPane;



/**
 * 
 * @author Bell
 * 
 */
public final class Text implements GameListener
{
	private GameEngine engine;
	private static final long serialVersionUID = 1L;
	private boolean longMode = false;
	private JTabbedPane tabPane;

	public void setLongMode(boolean longMode) {
		this.longMode = longMode;
	}
	public Text(GameEngine engine)
	{
		this.engine = engine;
		engine.addGameListener(this);
		
	}
	public void play()
	{
		if(engine.setUpGame())
			while(engine.step())
			{
				
			}
	}
	public void gameUpdated(GameUpdateType type)
	{
		switch (type)
		{
		case GAMEOVER:
		
			break;
		case MOVEPROCESSED:
			if(longMode)
				System.out.println("Time,Num_Caught,Num_Lights,Num_Mosquitos,Board_Name,Player_Name");
//			configPanel.updateScore(engine.getBoard().mosquitosCaught);
			break;
		case STARTING:
			System.out.println("Time,Num_Caught,Num_Lights,Num_Mosquitos,Board_Name,Player_Name");
			break;
		case MOUSEMOVED:
		default:
			// nothing.
		}
	}


}
