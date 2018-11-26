package risk.controller;

import risk.game.Game;
import risk.gui.GamePanel;
import risk.gui.MainFrame;

/**
 * The controller used to start the application.
 */
public class StartController {
	
	/**
	 * Main method to startup  controller.
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		MainFrame frame = MainFrame.getInstance();
		Game game = new Game();
		frame.setModule(game);
		
		GamePanel gamePanel = new GamePanel();
		gamePanel.setModule(game);
		
		frame.addPanel(gamePanel, MainFrame.GAME_PANEL);
		frame.setCurrentPanel(MainFrame.MENU_PANEL);
		
		
		
	}
}
