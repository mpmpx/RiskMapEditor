package risk.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import risk.controller.ReadFileController;
import risk.controller.SaveLoadGameController;
import risk.game.Game;
import risk.game.Player;
import risk.game.RiskMap;

/**
 * Main frame that contains all panels and components.
 */
public class MainFrame extends JFrame {
	
	public final static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width * 6 / 10;
	public final static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height * 9 / 10;
	public final static String MENU_PANEL = "Menu Panel";
	public final static String GAME_PANEL = "Game Panel";
	
	private static MainFrame mainFrame;
	private JPanel contentPanel;
	private HashMap<String, JPanel> panels;
	
	private Game module;
	private JButton startButton;
	private JButton tournamentButton;
	private JButton loadButton;
	
	private final String[] playerBehavior = {"None", "Human", "Aggressive", "Benevolent", "Random", "Cheater"};
	private JTextField startMapFilePath;
	private JTextField[] tournamentMapFilePath;
	private JComboBox[] startPlayersBox;
	private JCheckBox[] tournamentPlayersBox;
	private JSpinner maxTurnSpinner;
	private JSpinner gameNumSpinner;
	
	private File selectedFile;
	
	/**
	 * Gets the instance of the MainFrame.
	 * @return the instance of the MainFrame.
	 */
	public static MainFrame getInstance() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		
		return mainFrame;
	}
	/**
	 * Creates the main frame of this application.
	 */
	private MainFrame() {
		panels = new HashMap<String, JPanel>();
		
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk");
		setLocationRelativeTo(null);
		contentPanel = (JPanel) getContentPane();
		contentPanel.setLayout(new CardLayout());
		createMenu();	
		
		((CardLayout) contentPanel.getLayout()).show(contentPanel,  MENU_PANEL);
		setVisible(true);
		
		startMapFilePath = new JTextField();
		startMapFilePath.setEditable(false);
		startMapFilePath.setPreferredSize(new Dimension(100,20));
		
		tournamentMapFilePath = new JTextField[5];
		for (int i = 0; i < 5; i++) {
			tournamentMapFilePath[i] = new JTextField();
			tournamentMapFilePath[i].setEditable(false);
			tournamentMapFilePath[i].setPreferredSize(new Dimension(100,20));
		}
		
		startPlayersBox = new JComboBox[6];
		for (int i = 0; i < 6; i++) {
			startPlayersBox[i] = new JComboBox(playerBehavior);
		}
		
		tournamentPlayersBox = new JCheckBox[4];
		for (int i = 0; i < 4; i++) {
			tournamentPlayersBox[i] = new JCheckBox(playerBehavior[i + 2]);
		}
	}
	
	/**
	 * Creates the menu of this application which allows users to redirect to specific pages.
	 */
	public void createMenu() {
		JPanel menuPanel = new JPanel();
		// Set a layout of the menu panel.
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		tournamentButton = new JButton("Tournament");
		tournamentButton.addActionListener(new TournamentListener());
		loadButton = new JButton("Load");
		loadButton.addActionListener(new LoadListener());
		
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 10;
		menuPanel.add(startButton, c);
		
		c.gridx = 0;
		c.gridy = 1;
		menuPanel.add(tournamentButton, c);
		
		c.gridx = 0;
		c.gridy = 2;
		menuPanel.add(loadButton, c);
		
		panels.put(MENU_PANEL, menuPanel);
		addPanel(menuPanel, MENU_PANEL);

	}
	
	/**
	 * Adds a new panel to the main frame.
	 * @param panel a JPanel that is to be added to the main frame.
	 * @param name the name of the panel.
	 */
	public void addPanel(JPanel panel, String name) {
		panels.put(name, panel);
		contentPanel.add(panel, name);
	}
	
	/**
	 * Changes the current displaying panel according to the given panel's name.
	 * @param name the panel's name.
	 */
	public void setCurrentPanel(String name) {
		((CardLayout)contentPanel.getLayout()).show(contentPanel, name);
	}
	
	/**
	 * Sets the module of the main frame.
	 * @param module a Game that is to be the module of the main frame.
	 */
	public void setModule(Game module) {
		this.module = module;
	}
	
	/**
	 * Methods of the start button's action listener.
	 */
	private class StartListener implements ActionListener {
		RiskMap map;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton loadMapButton = new JButton("Load Map");
			loadMapButton.addActionListener(new LoadMapListener(startMapFilePath));

			Object[] message = { loadMapButton, startMapFilePath, 
					"player1", startPlayersBox[0], 
					"player2", startPlayersBox[1],
					"player3", startPlayersBox[2],
					"player4", startPlayersBox[3],
					"player5", startPlayersBox[4],
					"player6", startPlayersBox[5] };

			int playerDialogResult = JOptionPane.showConfirmDialog(null, message, "Setup", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			
			if (playerDialogResult == JOptionPane.OK_OPTION) {
				LinkedList<String> behaviors = new LinkedList<String>();
				for (int i = 0; i < 6; i++) {
					if ((String)startPlayersBox[i].getSelectedItem() != "None") {
						behaviors.add((String)startPlayersBox[i].getSelectedItem());
					}
				}
				
				if (behaviors.size() < 2 || startMapFilePath.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "Please select a map and more than 2 players.");
					return;
				}
				
				ReadFileController readFileController = new ReadFileController();
				try {
					map = readFileController.readFile(selectedFile.getAbsolutePath());
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage());
					exception.printStackTrace();
					return;
				}
				module.setMap(map);
				module.setPlayers(behaviors);
				module.distributeTerritories();
				((GamePanel) panels.get(GAME_PANEL)).initialize();
				module.start();
				((CardLayout) contentPanel.getLayout()).show(contentPanel, GAME_PANEL);
				
			} 
			
			startMapFilePath.setText(null);
			for (int i = 0; i < 6; i++) {
				startPlayersBox[i].setSelectedIndex(0);
			}
		}
	}

	/**
	 * Customized ActionListener for "Load Map" button.
	 */
	private class LoadMapListener implements ActionListener {
		
		private JTextField mapFilePath;
		
		public LoadMapListener(JTextField mapFilePath) {
			this.mapFilePath = mapFilePath;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			fileChooser.setDialogTitle("Select a map file");
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Map file", "map");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				mapFilePath.setText(selectedFile.getPath());
			}
		}
	}
	
	/**
	 * The action listener for tournament button.
	 */
	private class TournamentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SpinnerModel gameNumModel = new SpinnerNumberModel(1,  1, 5, 1);               
			SpinnerModel maxTurnModel = new SpinnerNumberModel(10, 10, 50, 1);
			
			gameNumSpinner = new JSpinner(gameNumModel);
			maxTurnSpinner = new JSpinner(maxTurnModel);
			
			JButton[] loadMapButton = new JButton[5];
			
			for (int i = 0; i <5; i++) {
				loadMapButton[i] = new JButton("Load Map" + (i+1) );
				loadMapButton[i].addActionListener(new LoadMapListener(tournamentMapFilePath[i]));
			}
			
			Object[] message = { loadMapButton[0], tournamentMapFilePath[0], 
					loadMapButton[1], tournamentMapFilePath[1],
					loadMapButton[2], tournamentMapFilePath[2],
					loadMapButton[3], tournamentMapFilePath[3],
					loadMapButton[4], tournamentMapFilePath[4],
					"Game number:", gameNumSpinner,
					"Max turns:", maxTurnSpinner,
					"player", tournamentPlayersBox[0], 
					tournamentPlayersBox[1],
					tournamentPlayersBox[2],
					tournamentPlayersBox[3]};

			int playerDialogResult = JOptionPane.showConfirmDialog(null, message, "Setup", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			
			if (playerDialogResult == JOptionPane.OK_OPTION) {
				LinkedList<String> playerBehaviors = new LinkedList<String>();
				for (int i = 0; i < 4; i++) {
					if (tournamentPlayersBox[i].isSelected()) {
						playerBehaviors.add(tournamentPlayersBox[i].getText());
					}
				}
				
				LinkedList<String> mapPathList = new LinkedList<String>();
				for (int i = 0; i < 5; i++) {
					if (tournamentMapFilePath[i].getText().length() > 0) {
						mapPathList.add(tournamentMapFilePath[i].getText());
					}
				}
				
				int gameNum = (int) gameNumSpinner.getValue();
				int maxTurnNum = (int) maxTurnSpinner.getValue();
				
				String[][] result = new String[mapPathList.size()][gameNum];
				
				if (mapPathList.size() == 0) {
					JOptionPane.showMessageDialog(null, "You must select at least one map");
					return;
				}
				
				if (playerBehaviors.size() < 2) {
					JOptionPane.showMessageDialog(null, "You must select at least two players");
				}
				
				for (int i = 0; i < mapPathList.size(); i++) {
					ReadFileController controller = new ReadFileController();
					RiskMap map = null;
					try {
						map = controller.readFile(mapPathList.get(i));
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(null, exception.getMessage());
						exception.printStackTrace();
						return;
					}
					
					for (int j = 0; j < gameNum; j++) {
						map.reset();
						Game game = new Game();
						game.setMap(map);
						game.setMaxTurn(maxTurnNum);
						game.setPlayers(playerBehaviors);
						game.distributeTerritories();
						game.start();
						
						Player winner = game.getWinner();
						if (winner == null) {
							result[i][j] = "DRAW";
						}
						else {
							result[i][j] = winner.getStrategy().getBehavior().toString();
						}
					}
				}
				
				Vector<String> columnNames = new Vector<String>();
				
				columnNames.add(" ");
				for (int i = 0; i < gameNum; i++) {
					columnNames.add("Game " + (i + 1));
				}
				
				Vector<Vector<String>> data = new Vector<Vector<String>>();
				data.add(columnNames);
				for (int i = 0; i < mapPathList.size(); i++) {
					Vector<String> row = new Vector<String>();
					row.add("Map" + (i + 1));
					for (int j = 0; j < gameNum; j++) {
						row.add(result[i][j]);
					}
					data.add(row);
				}
				
				JTable table = new JTable(data, columnNames);
				Object[] resultMessage = {"Result: ", table};
				JOptionPane.showMessageDialog(null, resultMessage);
				
			}
			
			for (int i = 0; i < 5; i++) {
				tournamentMapFilePath[i].setText(null);
			}
		
			for (int i = 0; i < 4; i++) {
				tournamentPlayersBox[i].setSelected(false);
			}
		}
		
	}
	
	/**
	 * The action listener for "Load" button.
	 */
	private class LoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			SaveLoadGameController controller = new SaveLoadGameController();
			Game game = null;
			
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			fileChooser.setDialogTitle("Select a saved file");
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Save file", "save");
			fileChooser.addChoosableFileFilter(filter);

			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				module = controller.loadGame(selectedFile.getPath());
				((GamePanel) panels.get(GAME_PANEL)).setModule(module);
				((GamePanel) panels.get(GAME_PANEL)).initialize();
				module.load();
				
				((CardLayout) contentPanel.getLayout()).show(contentPanel, GAME_PANEL);
			}
		}
	}
}
