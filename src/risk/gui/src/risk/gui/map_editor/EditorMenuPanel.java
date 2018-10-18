package risk.gui.map_editor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import risk.contorller.MapEditorController;
import risk.gui.MainFrame;
import risk.gui.MenuPanel;

class EditorMenuPanel extends JPanel {
	
	private static final int HEIGHT = 200;
	private static final int WIDTH = 200;	
	private static EditorMenuPanel editorMenuPanel;
	private MapEditorPanel rootPanel;
	private JButton loadButton;
	private JButton saveButton;
	private JButton newButton;
	private JButton createCountryButton;
	private JButton backButton;
	private static MapEditorController controller;
	
	private EditorMenuPanel() {
		rootPanel = (MapEditorPanel) this.getParent();
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createTitledBorder("Menu"));
		this.setLayout(new GridLayout(5, 1));
		controller = MapEditorPanel.getController();
		
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
	
	public static EditorMenuPanel getInstance(){
		if (editorMenuPanel == null) {
			editorMenuPanel = new EditorMenuPanel();
		}
		
		return editorMenuPanel;
	}
	
//	public void updateUI() {
//	}
	
	private class ListenForButton implements ActionListener {
		MapEditorController controller = MapEditorPanel.getController();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton selectedBtn = (JButton) e.getSource();
			if (selectedBtn == newButton) {
				controller.createNewMap();
				((MapEditorPanel) editorMenuPanel.getParent()).enableMapDisplayPanel();
			} 
			else if (selectedBtn == loadButton) {
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
			else if (selectedBtn == saveButton) {
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
			else if (selectedBtn == createCountryButton) {
				controller.setAddCountryFlag(true);
				System.out.println("createCountry button pressed");
			}
			else if (selectedBtn == backButton) {
				MainFrame frame = MainFrame.getInstance();
				frame.setCurrentPanel(MainFrame.MENU_PANEL);
				frame.SetSize(MenuPanel.WIDTH, MenuPanel.HEIGHT);			
			}
		}
		
	}
}
