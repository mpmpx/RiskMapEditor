package risk.gui.map_editor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import risk.contorller.MapEditorController;
import risk.gui.MainFrame;
import risk.gui.MenuPanel;

/**
 * EditorMenuPanel class is an instance of JPanel which is a sub-panel of MapEditor panel.
 * It contains buttons which enable user to 
 * 1. create a new map;
 * 2. load an existing .map file;
 * 3. save current working map into a .map file;
 * 4. create a new country on the current map;
 * 5. return to the menu.
 */

class EditorMenuPanel extends JPanel {
	
	private static final int HEIGHT = 200;
	private static final int WIDTH = 200;	
	private MapEditorPanel rootPanel;
	private JButton loadButton;
	private JButton saveButton;
	private JButton newButton;
	private JButton createCountryButton;
	private JButton backButton;
	private MapEditorController controller;
	
	/**
	 * Constructor of this class. Initialize all class variables and create
	 * all of its buttons.
	 * @param controller is the controller of the MapEditor panel.
	 */
	public EditorMenuPanel(MapEditorController controller) {
		rootPanel = (MapEditorPanel) this.getParent();
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createTitledBorder("Menu"));
		this.setLayout(new GridLayout(5, 1));
		this.controller = controller;
		
		newButton = new JButton("New");
		newButton.addActionListener(new ListenForButton());
		this.add(newButton);
		
		loadButton = new JButton("Load");
		loadButton.addActionListener(new ListenForButton());
		this.add(loadButton);		

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ListenForButton());
		this.add(saveButton);
		
		createCountryButton = new JButton("Create Country");
		createCountryButton.addActionListener(new ListenForButton());
		this.add(createCountryButton);
		
		backButton = new JButton("Back");
		backButton.addActionListener(new ListenForButton());
		this.add(backButton);
	}
	

	/**
	 * This function is called when the "New" button is clicked.
	 * Pop up a window to set the size of new map.
	 */
	private void createMap() {
		int width = 0;
		int height = 0;
		    
		JSpinner widthSpinner;
		JSpinner heightSpinner;
		SpinnerNumberModel widthSpinnerModel;
		SpinnerNumberModel heightSpinnerModel;
		widthSpinnerModel = new SpinnerNumberModel(0, 0, 5000, 1);
		heightSpinnerModel = new SpinnerNumberModel(0, 0, 5000, 1);
		widthSpinner = new JSpinner(widthSpinnerModel);
		heightSpinner = new JSpinner(heightSpinnerModel);
			
		JTextField widthField = new JTextField();
		JTextField heightField = new JTextField();
		Object[] message = {
				"Width: (0-5000)", widthSpinner ,
				"Height: (0-5000)", heightSpinner,
		};
				
		if (JOptionPane.showConfirmDialog(null, message,
				"Set the size of new map", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			width = (int) widthSpinner.getValue();
			height = (int) heightSpinner.getValue();
			controller.setMapSize(width, height);
			controller.createNewMap();
		} 
		else {
			return;
		}
	}
	
	/**
	 * This method is called when the "save" button is clicked.
	 * Pop up a file chooser window and load the selected .map file. 
	 */
	private void saveMap() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChooser.setDialogTitle("Create a map file");
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map file", "map");
		fileChooser.addChoosableFileFilter(filter);
			
		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String fileName = selectedFile.getAbsolutePath();
			if (!fileName.endsWith(".map")) {
				fileName += ".map";
			}
			
			controller.saveMap(fileName);
		}
	}
	
	/**
	 * This method is called when the "load" button is clicked.
	 * Pop up a file chooser window and load the selected .map file. 
	 */
	private void loadMap() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChooser.setDialogTitle("Select a map file");
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map file", "map");
		fileChooser.addChoosableFileFilter(filter);
		
		int returnValue = fileChooser.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();	
			controller.loadMap(selectedFile.getAbsolutePath());				
		}
	}

	/**
	 * A member class which provides methods of action listener for buttons.
	 */
	private class ListenForButton implements ActionListener {

		/**
		 * This class provides methods for all buttons of EditorMenu panel when it is clicked.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton selectedBtn = (JButton) e.getSource();
			if (selectedBtn == newButton) {
				if (controller.isMapSaved() == false) {
					int option = JOptionPane.showConfirmDialog(null, "Your current map is not saved. Do you want to save it?",
							"", JOptionPane.YES_NO_CANCEL_OPTION);
					
					if (option == JOptionPane.OK_OPTION) {
						saveMap();
					} 
					else if (option == JOptionPane.NO_OPTION){
						createMap();
						return;
					} 
				}
				
				if (controller.isMapSaved() == true) {
					createMap();
				}
			} 
			else if (selectedBtn == loadButton) {
				if (controller.isMapSaved() == false) {
					int option = JOptionPane.showConfirmDialog(null, "Your current map is not saved. Do you want to save it?",
							"", JOptionPane.YES_NO_CANCEL_OPTION);
					
					if (option == JOptionPane.OK_OPTION) {
						saveMap();
					} 
					else if (option == JOptionPane.NO_OPTION){
						loadMap();
						return;
					}
				}
				
				if (controller.isMapSaved() == true) {
					loadMap();
				}
			} 
			else if (selectedBtn == saveButton) {
				saveMap();
			}
			else if (selectedBtn == createCountryButton) {
				controller.setAddCountryFlag(true);
			}
			else if (selectedBtn == backButton) {
				if (controller.isMapSaved() == false) {
					int option = JOptionPane.showConfirmDialog(null, "Your current map is not saved. Do you want to save it?",
							"", JOptionPane.YES_NO_CANCEL_OPTION);
					
					if (option == JOptionPane.OK_OPTION) {
						saveMap();
					} 
					else if (option == JOptionPane.NO_OPTION){
						MainFrame frame = MainFrame.getInstance();
						frame.setCurrentPanel(MainFrame.MENU_PANEL);
						frame.setFrameSize(MenuPanel.WIDTH, MenuPanel.HEIGHT);		
						controller.clear();
					} 
				}
				
				if (controller.isMapSaved() == true) {
					MainFrame frame = MainFrame.getInstance();
					frame.setCurrentPanel(MainFrame.MENU_PANEL);
					frame.setFrameSize(MenuPanel.WIDTH, MenuPanel.HEIGHT);	
					controller.clear();
				}
	
			}
		}
	}
}

