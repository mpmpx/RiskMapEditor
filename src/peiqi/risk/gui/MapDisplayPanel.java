package risk.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import risk.game.*;

/**
 * The main entrance of the game
 *
 */
public class MapDisplayPanel extends JPanel implements Observer{

	private Player currentPlayer;
	private int currentPhase;
	private Game module;
	private RiskMap map;
	private Territory selectedTerritory;
	private Territory attackerTerritory;
	private Territory defenderTerritory;
	private BufferedImage image;
	private HashMap<String, Territory> territoryMap;
	private HashMap<Point, Territory> locationMap;
	private HashMap<String, LinkedList<Territory>> reachableMap;	
	private HashMap<String, LinkedList<Territory>> attackableMap;
	
	
	/**
	 * Create the application.
	 * The constructor method.
	 */
	public MapDisplayPanel() {
		currentPhase = Phase.STARTUP;
		territoryMap = new HashMap<String, Territory>();
		locationMap = new HashMap<Point, Territory>();
	}

	/**
	 * Initializes MapDisplayPanel with a module.
	 * @param module a Game that is to be the module of this MapDisplayPanel.
	 */
	public void initialize(Game module) {
		this.module = module;
		module.addObserver(this);
		addMouseListener(new Listener());
		
		map = module.getMap();
		territoryMap = map.getTerritoryMap();
		for (Territory territory : territoryMap.values()) {
			locationMap.put(territory.getLocation(), territory);
		}
		
		image = map.getImage();
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		
		paintImage();
		repaint();
	}
	
	/**
	 * Method to update the MapDisplayPanel view
	 * @param observable observable class
	 * @param arg argument.
	 */
	@Override
	public void update(Observable observable, Object arg) {
		Game obs = (Game) observable;
		
		currentPhase = obs.getCurrentPhase();
		currentPlayer = obs.getCurrentPlayer();
		
		if (currentPhase == Phase.FORTIFICATION) {
			reachableMap = obs.getCurrentPlayer().getReachableMap();
		}
		
		if (currentPhase != Phase.FORTIFICATION) {
			if (selectedTerritory != null) {
				selectedTerritory = null;
				paintImage();
			}
		}
		
		if (currentPhase == Phase.ATTACK) {
			attackableMap = obs.getCurrentPlayer().getAttackableMap();
			attackerTerritory = obs.getAttacker();
			defenderTerritory = obs.getDefender();
			
			if (attackerTerritory == null && defenderTerritory == null) {
				paintImage();
			}
		} 
		repaint();
	}
	/**
	 * Method to paint the image
	 */
	private void paintImage() {
		for (Territory territory : territoryMap.values()) {
			paintTerritory(territory, territory.getColor());
		}
	}
	
	/**
	 * Method to paint the territory
	 *
	 * @param territory
	 *		the selected territory that will be painted 
	 * @param color
	 *		the color that selected territory will be painted 
	 */
	private void paintTerritory(Territory territory, Color color) {
		for (Point point : territory.getShape()) {
			image.setRGB(point.x, point.y, color.getRGB());
		}
	}
	
	/**
	 * Method to paint the componet
	 *
	 * @param g
	 *	the selected component to be painted with Graphics type
	 */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if (image != null) {
        	g.drawImage(image, 0, 0, null);
        	drawArmyNumber(g);
        }
    }	
    
	/**
	 * Method to draw the number of army
	 *
	 * @param g with Graphic type
	 */
	private void drawArmyNumber(Graphics g) {
		for (Territory territory : territoryMap.values()) {
			g.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 12));
			g.setColor(Color.white);
			g.drawString(territory.getArmy() + "", territory.getX(), territory.getY());
		}
	}
		
	/**
	 * select a territory based on give xy-coordinator.
	 * @param x x coordinate of the territory
	 * @param y y coordinate of the territory
	 * @return a territory based on give xy-coordinator.
	 */
	private Territory selectTerritory(int x, int y) {
		for (Territory territory : territoryMap.values()) {
			if (territory.contains(new Point(x, y))) {
				return territory;
			}
		}
		
		return null;
	}
	
	/**
	 * Listener method, in order to switch to different phase 
	 *
	 */
	private class Listener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Territory territory = selectTerritory(e.getX(), e.getY());
			
			switch (currentPhase) {
				case Phase.STARTUP : {
					startupClick(territory);
					break;
				}
				case Phase.REINFORCEMENT : {
					reinforcementClick(territory);
					break;
				}
				case Phase.ATTACK : {
					attackClick(territory);
					break;
				}
				case Phase.FORTIFICATION : {
					fortificationClick(territory);
					break;
				}
			}
		}
		
		
		/**
		 * Click when in the startup phase.
		 * @param territory selected territory
		 */
		private void startupClick(Territory territory) {
			if (territory != null && currentPlayer.getTerritoryMap().containsValue(territory)) {
				
				SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, currentPlayer.getUnassignedArmy(), 1);
				JSpinner spinner = new JSpinner(spinnerModel);
				Object[] message = {
						"Set armies to " + territory.getName() + "(0 - " + currentPlayer.getUnassignedArmy() + ")", 
						spinner
				};
				
				int result = JOptionPane.showConfirmDialog(null, message, "Set armies", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					module.reinforce(territory, (int) spinner.getValue());
					return;
				}
				else {
					return;
				}
			}
		}
			
		
		/**
		 * Click when in the reinforcement phase.
		 * @param territory selected territory
		 */
		private void reinforcementClick(Territory territory) {
			startupClick(territory);
		}

		/**
		 * Click when in the attack phase.
		 * @param territory selected territory
		 */
		private void attackClick(Territory territory) {
			if (territory != null) {
				// select attacker territory
				if (attackerTerritory == null) {	
					if (currentPlayer.getTerritoryMap().containsValue(territory)) {
						
						// Territory with only one army or without adjacent enemy's territory is not able to be selected.
						if (territory.getArmy() == 1 || attackableMap.get(territory.getName()).isEmpty()) {
							return;
						} 
					
						attackerTerritory = territory;
						paintTerritory(territory, territory.getColor().darker());
						repaint();
					}
				}
				// Select defender territory
				else if (defenderTerritory == null) {
					// Click on an territory which cannot be attacked by attacker territory.
					if (!attackableMap.get(attackerTerritory.getName()).contains(territory)) {
						attackerTerritory = null;
						paintImage();
						repaint();
					}
					// Set defender territory
					else {
						defenderTerritory = territory;
						paintTerritory(territory, territory.getColor().darker());
						repaint();
					}
				}
			}
			else {
				if (defenderTerritory != null) { 
					return;
				}
				
			  	if (attackerTerritory != null) {
					attackerTerritory = null;
					paintImage();
					repaint();
				}
			}
			
			module.setupAttack(attackerTerritory , defenderTerritory);
		}
		
		/**
		 * Click when in the fortification phase.
		 * @param territory selected territory
		 */
		private void fortificationClick(Territory territory) {
			if (territory != null && currentPlayer.getTerritoryMap().containsValue(territory)) {
				
				// Select departure territory.
				if (selectedTerritory == null) {
				    // Territory with only one army is not able to be selected.
					// Territory with no reachable territories is not able to be selected.
					if (territory.getArmy() == 1 || reachableMap.get(territory.getName()).isEmpty()) {
						return;
					}
					
					selectedTerritory = territory;
					paintTerritory(territory, territory.getColor().darker());
					repaint();
				}
				// Select arrival country.
				else {
					
					// if select departure country itself.
					if (selectedTerritory == territory || !reachableMap.get(selectedTerritory.getName()).contains(territory)) {
						selectedTerritory = null;
						paintImage();
						repaint();
					}
					else {
						SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, selectedTerritory.getArmy() - 1, 1);;
						JSpinner spinner = new JSpinner(spinnerModel);
						Object[] message = {
								selectedTerritory.getName() + "(" + selectedTerritory.getArmy() + ")  >>>>  "
										+ territory.getName() + "(" + territory.getArmy() +")", 
								"Move armies (1 - " + (selectedTerritory.getArmy() - 1) +")",
								spinner
						};
						
						int result = JOptionPane.showConfirmDialog(null, message, "Fortification", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							module.fortify(selectedTerritory, territory, (int) spinner.getValue());
							paintTerritory(selectedTerritory, selectedTerritory.getColor());
							selectedTerritory = null;
							module.nextPhase();
						}
					}
					
				}
			}
			else {
				if (selectedTerritory != null) {
					selectedTerritory = null;
					paintImage();
					repaint();
				}
			}
		}
		
		
		@Override
		public void mouseEntered(MouseEvent arg0) {	
			return;
		}

		@Override
		public void mouseExited(MouseEvent arg0) {			
			return;
		}

		@Override
		public void mousePressed(MouseEvent arg0) {			
			return;
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {			
			return;
		}
		
	}
}
