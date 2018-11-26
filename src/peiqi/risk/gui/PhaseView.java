package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.CardLayout;

import risk.game.*;
/**
 * This class is the GUI for the Phase View Panel, showing the current phase of
 * conquest game and current player
 */
public class PhaseView extends JPanel implements Observer{

	private int currentPhase;
	private Game module;
	private Territory attacker;
	private Territory defender;
	
	private CardExchangeView cardExchangeView;
	private JButton nextButton;
	private Player currentPlayer;
	private JLabel remainingArmyLabel;
	private JLabel reinforcementInfo;
	private JLabel attackerLabel;
	private JLabel defenderLabel;
	private JLabel fortificationLabel;
	private JSpinner attackerSpinner;
	private JSpinner defenderSpinner;
	private JLabel attackerDiceLabel;
	private JLabel defenderDiceLabel;
	private JDialog exchangeDialog;
	
	/**
	 * constructor method initialize phase view panel
	 * 
	 */
	public PhaseView() {
		setLayout(new GridBagLayout());
		
		nextButton = new JButton("Next");
		remainingArmyLabel = new JLabel();
		reinforcementInfo = new JLabel();
		attackerLabel = new JLabel();
		defenderLabel = new JLabel();
		fortificationLabel = new JLabel();
		attackerSpinner = new JSpinner();
		defenderSpinner = new JSpinner();
		attackerDiceLabel = new JLabel();
		defenderDiceLabel = new JLabel();
		
		attacker = null;
		defender = null;
		
		setPreferredSize(new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT * 2 / 10));
		nextButton.addActionListener(new ButtonListener());
	}

	/**
	 * Initializes this PhaseView with module.
	 * @param module a Game that is to be the module of this PhaseView panel.
	 */
	public void initialize(Game module) {
		this.module = module;
		module.addObserver(this);
		
		exchangeDialog = new JDialog(MainFrame.getInstance(), "Card Exchange", true);	
		exchangeDialog.setPreferredSize(new Dimension(600, 300));
		exchangeDialog.setLocationRelativeTo(null);
		exchangeDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		exchangeDialog.setResizable(false);
		exchangeDialog.setContentPane(cardExchangeView);
		exchangeDialog.pack();
	}
	
	/**
	 * Sets the CardExchangeView of the PhaseView panel.
	 * @param cardExchangeView a cardExchangeView that is to be set.
	 */
	public void setCardExchangeView(CardExchangeView cardExchangeView) {
		this.cardExchangeView = cardExchangeView;

	}
	/**
	 * Method to update the phase view
	 * @param observable observable class
	 * @param arg argument.
	 */
	@Override
	public void update(Observable observable, Object arg) {
		Game obs = (Game) observable;
		
		currentPhase = obs.getCurrentPhase();
		currentPlayer = obs.getCurrentPlayer();
		attacker = obs.getAttacker();
		defender = obs.getDefender();

		if (attacker != null) {
			attackerLabel.setText("attacker: " + attacker.getName());
		} 
		else {
			attackerLabel.setText("attacker: ");
		}
		
		if (defender != null) {
			defenderLabel.setText("defender: " + defender.getName());
		}
		else {
			defenderLabel.setText("defender: ");
		}
		
		updateView();
	}
	/**
	 * Method to update the phase view
	 */
	private void updateView() {
		removeAll();
		switch (currentPhase) {
			case Phase.STARTUP : {
				updateStartupPhaseView();
				break;
			}
			case Phase.REINFORCEMENT : {
				updateReinforcementPhaseView();
				break;
			}
			case Phase.ATTACK : {
				updateAttackPhaseView();
				break;
			}
			case Phase.FORTIFICATION : {
				updateFortificationPhaseView();
				break;
			}
		}
		
		revalidate();
		repaint();	
	}
	/**
	 * Method to update the start up phase view
	 */
	private void updateStartupPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);

		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() + 
				" - Startup Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		remainingArmyLabel.setText("Remaining unassgined armies: " + currentPlayer.getUnassignedArmy());
					
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(remainingArmyLabel, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(nextButton, c);		
	}
	/**
	 * Method to update the reinforcement phase view
	 */
	private void updateReinforcementPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);

		JButton exchangeButton = new JButton("Exchange Cards");
		HashMap<Continent, Boolean> controlledContinent = currentPlayer.getControlledContinent();
		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() +
				" - Reinforcement Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		

		exchangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				exchangeDialog.setVisible(true);
			}
			
		});
		remainingArmyLabel.setText("Remaining unassgined armies: " + currentPlayer.getUnassignedArmy());

		String reinforcementInfoMsg = "Free armies: " + currentPlayer.getFreeArmy() + " ";
		for (Continent continent : controlledContinent.keySet()) {
			if (controlledContinent.get(continent)) {
				reinforcementInfoMsg += "Own " + continent.getName() + ": " + continent.getValue() + " ";
			}
		}
		reinforcementInfo.setText(reinforcementInfoMsg);
		
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(remainingArmyLabel, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(exchangeButton, c);
		
		c.gridx = 1;
		c.gridy = 2;
		add(nextButton, c);
		

	}
	/**
	 * Method to update the attack phase view
	 */
	private void updateAttackPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);
		
		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() +
				" - Attack Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		JButton rollButton = new JButton("roll");
		JButton alloutButton = new JButton("all-out");
		JButton stopButton = new JButton("stop");
		
		rollButton.addActionListener(new AttackPhaseButtonListener());
		alloutButton.addActionListener(new AttackPhaseButtonListener());
		stopButton.addActionListener(new AttackPhaseButtonListener());
		
		String attackerDiceResult = "<html>";
		String defenderDiceResult = "<html>";
		
		PriorityQueue<Integer> attackerDice = new PriorityQueue<Integer>(Collections.reverseOrder());
		attackerDice.addAll(module.getAttackerDice());
		PriorityQueue<Integer> defenderDice = new PriorityQueue<Integer>(Collections.reverseOrder());
		defenderDice.addAll(module.getDefenderDice());
		
		while (!attackerDice.isEmpty() && !defenderDice.isEmpty()) {
			if (attackerDice.peek() > defenderDice.peek()) {
				attackerDiceResult += ("<font color='red'>"+ attackerDice.peek().toString()+ "</font>   ");
				defenderDiceResult += defenderDice.peek() + "   ";
			} 
			else {
				defenderDiceResult += ("<font color='red'>"+ defenderDice.peek().toString()+ "</font>   ");
				attackerDiceResult += attackerDice.peek() + "   ";
			}
			attackerDice.remove();
			defenderDice.remove();
		}
		
		while (!attackerDice.isEmpty()) {
			attackerDiceResult += attackerDice.remove() + "   ";
		}
		attackerDiceResult += "</html>";
		
		while (!defenderDice.isEmpty()) {
			defenderDiceResult += defenderDice.remove() + "   ";
		}
		defenderDiceResult += "</html>";
		
		attackerDiceLabel.setText(attackerDiceResult);
		defenderDiceLabel.setText(defenderDiceResult);
		
		
		if (attacker != null && defender != null) {
			
			if (attacker.getArmy() == 1) {
				attackerSpinner.setEnabled(false);
				defenderSpinner.setEnabled(false);
				
				rollButton.setEnabled(false);
				alloutButton.setEnabled(false);
				stopButton.setEnabled(false);
				module.setupAttack(null,  null);
				
				if (module.checkAttackPhase() == false) {
					attackerDiceLabel.setText(null);
					defenderDiceLabel.setText(null);
					return;
				}
			} 
			else {
				SpinnerNumberModel attackerSpinnerModel = new SpinnerNumberModel(Math.min(3, attacker.getArmy() - 1), 
																			1, Math.min(3, attacker.getArmy() - 1), 1);
				SpinnerNumberModel defenderSpinnerModel = new SpinnerNumberModel(Math.min(2, defender.getArmy()), 
																			1, Math.min(2, defender.getArmy()), 1);
				attackerSpinner.setModel(attackerSpinnerModel);
				defenderSpinner.setModel(defenderSpinnerModel);
				attackerSpinner.setEnabled(true);
				defenderSpinner.setEnabled(true);
			
				rollButton.setEnabled(true);
				alloutButton.setEnabled(true);
				stopButton.setEnabled(true);
			}
		} else {
			attackerSpinner.setEnabled(false);
			defenderSpinner.setEnabled(false);
			
			rollButton.setEnabled(false);
			alloutButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
		
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 0;
		c.gridy = 0;
		add(attackerSpinner, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(defenderSpinner, c);
		
		c.weightx = 1;
		c.weighty = 2;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		add(attackerLabel, c);
		
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 1;
		add(defenderLabel, c);
		
		c.gridx = 2;
		c.gridy = 0;
		add(attackerDiceLabel, c);

		c.gridx = 2;
		c.gridy = 1;
		add(defenderDiceLabel, c);

		
		c.weightx = 1;
		c.weighty = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		add(rollButton, c);
		
		c.weightx = 1;
		c.weighty = 3;
		c.gridx = 1;
		c.gridy = 2;
		add(alloutButton, c);
		
		c.weightx = 1;
		c.weighty = 3;
		c.gridx = 2;
		c.gridy = 2;
		add(stopButton, c);
		
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 3;
		add(nextButton, c);
	}
	/**
	 * Method to update the fortification phase view
	 */
	private void updateFortificationPhaseView() {
		GridBagConstraints c = new GridBagConstraints();
		Border colorBorder = BorderFactory.createLineBorder(currentPlayer.getColor(), 10);

		Border titleBorder = BorderFactory.createTitledBorder(colorBorder, currentPlayer.getName() + 
				" - Fortification Phase", TitledBorder.CENTER, TitledBorder.TOP);
		setBorder(titleBorder);
		
		fortificationLabel.setText("Move armies from one of your countries to another reachable one.");
		
		c.weightx = 10;
		c.weighty = 10;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		add(fortificationLabel, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 1;
		add(nextButton, c);		
	}

	
	/**
	 * Method to add attack phase button listener
	 */
	private class AttackPhaseButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonText = ((JButton) e.getSource()).getText();
			
			if (buttonText.equals("roll")) {
				int attackerDiceNum = (int) attackerSpinner.getValue();
				int defenderDiceNum = (int) defenderSpinner.getValue();
				
				attack(attacker, defender, attackerDiceNum, defenderDiceNum);			
			} else if (buttonText.equals("all-out")) {
				while(attacker != null && defender != null) {
					attack(attacker, defender, Math.min(3, attacker.getArmy() - 1), Math.min(2, defender.getArmy()));
				}
				
			} else if (buttonText.equals("stop")) {
				attackerDiceLabel.setText(null);
				defenderDiceLabel.setText(null);
				module.setupAttack(null, null);
			}
			
		}
		
		private void attack(Territory attacker, Territory defender, int attackerDiceNum, int defenderDiceNum) {
			Territory attackerCopy = attacker;
			Territory defenderCopy = defender;
			
			module.attack(attackerDiceNum, defenderDiceNum);

			if (defenderCopy.getColor().equals(attackerCopy.getColor())) {
				SpinnerNumberModel spinnerModel = new SpinnerNumberModel(attackerCopy.getArmy() - 1, 
						attackerDiceNum, attackerCopy.getArmy() - 1, 1);
				
				JSpinner spinner = new JSpinner(spinnerModel);
				Object[] message = {
						"You conquered " + defenderCopy.getName(),
						"Set armies in the conquered territory (" + attackerDiceNum + " - " + (attackerCopy.getArmy() - 1) + ")",
						spinner
				};
				

			    Object[] options = {"OK"};
				int result = JOptionPane.showOptionDialog(null, message, "Conquered Territory", 
						JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
				if (result == JOptionPane.OK_OPTION) {
					attackerDiceLabel.setText(null);
					defenderDiceLabel.setText(null);
					module.conquer(attackerCopy, defenderCopy, (int) spinner.getValue());
				} else {
					attackerDiceLabel.setText(null);
					defenderDiceLabel.setText(null);
					module.conquer(attackerCopy, defenderCopy, (int) spinner.getValue());
				}
			}
			
			if (currentPlayer.getTerritoryMap().size() == module.getMap().getTerritoryMap().size()) {
				JOptionPane.showMessageDialog(null, currentPlayer.getName() + " won the game.");
				System.exit(0);
			}
		}
	}
	/**
	 * Method add button listener
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (currentPhase) {
				case Phase.STARTUP : {
					if (currentPlayer.getUnassignedArmy() != 0) {
						JOptionPane.showMessageDialog(null, "You have to set all unassigned armies.");
						return;
					} 
					else {
						module.nextPhase();
					}
					
					break;
				}
				case Phase.REINFORCEMENT : {
					if (currentPlayer.getUnassignedArmy() != 0) {
						JOptionPane.showMessageDialog(null, "You have to set all unassigned armies.");
						return;
					} 
					else {
						module.nextPhase();
					}
					break;
				}
				case Phase.ATTACK : {
					int result = JOptionPane.showConfirmDialog(null, "Do you want to skip attack phase?", null, JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						module.setupAttack(null, null);
						attackerDiceLabel.setText(null);
						defenderDiceLabel.setText(null);
						module.nextPhase();
					}	
					break;
				}
				case Phase.FORTIFICATION : {
					int result = JOptionPane.showConfirmDialog(null, "Do you want to skip this phase?", null, JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						module.nextPhase();
					}	
					break;
				}
			}
			
		}
		
	}


}
