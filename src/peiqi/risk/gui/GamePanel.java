package risk.gui;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import risk.game.Game;

/**
 * The panel of the game
 *
 */
public class GamePanel extends JPanel {
    private MapDisplayPanel mapDisplayPanel;
    private PhaseView phaseView;
    private DominationView dominationView;
	private CardExchangeView cardExchangeView;
    	
	private Game module;
	
	/**
	 * Constructor method
	 */
	public GamePanel() {
		mapDisplayPanel = new MapDisplayPanel();
		phaseView = new PhaseView();
		dominationView = new DominationView();
		cardExchangeView = new CardExchangeView();

	}
	
	/**
	 * Initialize the contents of the panel
	 * 
	 */
	public void initialize() {
		cardExchangeView.initialize(module);
		phaseView.setCardExchangeView(cardExchangeView);
		mapDisplayPanel.initialize(module);
		phaseView.initialize(module);
		dominationView.initialize(module);

		JScrollPane displayScrollPanel = new JScrollPane(mapDisplayPanel);
		displayScrollPanel.setPreferredSize(new Dimension(MainFrame.WIDTH * 7 / 10, MainFrame.HEIGHT * 7 / 10));
		displayScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		displayScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		displayScrollPanel.getViewport().setViewPosition(new Point(200, 200));	
		
		add(displayScrollPanel);		
		add(dominationView);
		add(phaseView);

	}
	/**
	 * Return the current phase view
	 * 
	 * @return phaseView
	 */
	public PhaseView getPhaseView() {
		return phaseView;
	}
	/**
	 * Return the current map panel
	 * 
	 * @return mapDisplayPanel
	 */
	public MapDisplayPanel getMapDisplayPanel() {
		return mapDisplayPanel;
	}
	/**
	 * Return the current domination view
	 * 
	 * @return dominationView
	 */
	public DominationView getDominationView() {
		return dominationView;
	}
	/**
	 * Return the current card exchange view
	 * 
	 * @return cardExchangeView
	 */
	public CardExchangeView getCardExchangeView() {
		return cardExchangeView;
	}	
	
	/**
	 * Sets the module of the GamePanel.
	 * @param module a Game that is to be the module of this GamePanel.
	 */
	public void setModule(Game module) {
		this.module = module;
	}
}
