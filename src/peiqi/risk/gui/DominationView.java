package risk.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import risk.game.*;

/**
 * This class is the GUI for the Domination View Panel
 */
public class DominationView extends JPanel implements Observer{

	private Player[] players;
	private LinkedList<JTextArea> textAreaList;
	private RiskMap map;
	
	/**
	 * Constructor method
	 * 
	 */
	public DominationView() {
		players = new Player[6];
		textAreaList = new LinkedList<JTextArea>();
	}
	
	/**
	 * Initializes DominationView with a module.
	 * @param module a Game that is to be the module of this DominationView panel.
	 */
	public void initialize(Game module) {
		module.addObserver(this);
		map = module.getMap();
		setPreferredSize(new Dimension(MainFrame.WIDTH * 2 / 10, MainFrame.HEIGHT * 7 / 10));
		setLayout(new GridLayout(6, 1));
	}
	
	/**
	 * Method to update the phase view
	 * @param observable observable class
	 * @param object argument.
	 */
	@Override
	public void update(Observable observable, Object object) {
		Game obs = (Game) observable;
		players = obs.getPlayers();
		textAreaList.clear();
		for (Player player : players) {
			JTextArea textArea = new JTextArea();
			Border colorBorder = BorderFactory.createLineBorder(player.getColor(), 2);
			
			String content = "";
			content += player.getName() + ":\n";
			content += ("Map controlled: " + 
					player.getTerritoryMap().size() * 100 / map.getTerritoryMap().size() + "%\n");
			
			content += ("Continents controlled: \n");
			for (Continent continent : player.getControlledContinent().keySet()) {
				if (player.getControlledContinent().get(continent)) {
					content += "    " + continent.getName() + "\n";
				}
			}
			content += "\n";
			
			content += ("Total number of armies: " + player.getTotalArmy());
			
			textArea.setBorder(colorBorder);
			textArea.setText(content);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setEditable(false);		
			textAreaList.add(textArea);
		}
		
		updateView();
	}
	
	/**
	 * Method to update the domination view
	 */
	private void updateView() {
		removeAll();
		revalidate();
		for (JTextArea textArea : textAreaList) {
		    JScrollPane scroll = new JScrollPane (textArea);	
		    scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMinimum());
		    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		    add(scroll);
		}
		revalidate();
		repaint();
	}

}
