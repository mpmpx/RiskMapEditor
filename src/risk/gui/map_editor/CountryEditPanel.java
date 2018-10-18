package risk.gui.map_editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import risk.contorller.MapEditorController;
import risk.game.*;

/**
 * CountryEditPanal provides an instance of JPanel which is a sub-panel
 * of the MapEditor panel. This panel contains several areas to show and edit
 * information of a selected country including the name, continent and adjacent
 * countries.
 */
public class CountryEditPanel extends JPanel {
	public static final int HEIGHT = 500;
	public static final int WIDTH = 200;
	public static final int NAME_FIELD_HEIGHT = 60;
	public static final int CONTINENT_FIELD_HEIGHT = 60;
	public static final int LINKS_FIELD_HEIGHT = 400;
	public static final int SPACE_HEIGHT = 10;

	
	private JPanel nameFieldPanel;
	private JPanel continentPanel;
	private JPanel linksPanel;
	private JButton addContinentBtn;
	private JButton deleteCountryBtn;
	private JComboBox<String> continentComboBox;
	private MapEditorController controller;
	private HashMap<Point, Country> countryHashMap;
	private HashMap<Point, LinkedList<Point>> edgeHashMap;
	private Country selectedCountry;
	private LinkedList<Continent> continentList;
	private ListenForComboBox listenForComboBox;
	
	/**
	 * Constructor of this class. Initialize all class variables and create information area.
	 */
	public CountryEditPanel(MapEditorController controller) {
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createTitledBorder("Selected Territory"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.createNameFieldPanel();
		this.createContinentFieldPanel();
		this.createLinksPanel();
		this.createDeleteCountryBtn();
		this.controller = controller;
		
		this.selectedCountry = null;
		this.continentList = new LinkedList<Continent>();
		this.listenForComboBox = new ListenForComboBox();

	}
	
	/**
	 * This method creates a panel which contains a text field enabling users to edit
	 * the name of a selected country.
	 */
	private void createNameFieldPanel() {
		this.add(Box.createRigidArea(new Dimension(WIDTH, SPACE_HEIGHT)));	
		nameFieldPanel = new JPanel();
		nameFieldPanel.setMaximumSize(new Dimension(WIDTH, NAME_FIELD_HEIGHT));
		nameFieldPanel.setBorder(BorderFactory.createTitledBorder("Name"));
		JTextField nameField = new JTextField(15);
		if (this.selectedCountry != null) {
			nameField.setText(this.selectedCountry.getName());
		}
		nameFieldPanel.add(nameField);
		this.add(nameFieldPanel);		
	}
	
	/**
	 * This method creates a panel with a combo box and a button, which allow
	 * users to select an existing continent from the combo box and create a new
	 * continent by click the button.
	 */
	private void createContinentFieldPanel() {
		this.add(Box.createRigidArea(new Dimension(WIDTH, SPACE_HEIGHT)));
		
		continentPanel = new JPanel();
		continentPanel.setMaximumSize(new Dimension(WIDTH, CONTINENT_FIELD_HEIGHT));
		continentPanel.setBorder(BorderFactory.createTitledBorder("Continent"));
		continentComboBox = new JComboBox<>();
		continentComboBox.addItemListener(listenForComboBox);
		continentComboBox.setPreferredSize(new Dimension(130,20));
		continentPanel.add(continentComboBox);
		
		addContinentBtn = new JButton("+");
		addContinentBtn.setBorder(null);
		addContinentBtn.setPreferredSize(new Dimension(20, 20));
		addContinentBtn.addActionListener(new ListenForButton());
		continentPanel.add(addContinentBtn);
		this.add(continentPanel);
	}
	
	/**
	 * This method creates a panel with a series of links which indicate adjacent countries
	 * of a selected country.
	 */
	private void createLinksPanel() {
		this.add(Box.createRigidArea(new Dimension(WIDTH, SPACE_HEIGHT)));
		
		linksPanel = new JPanel();
		linksPanel.setLayout(new FlowLayout(FlowLayout.LEFT));		
		linksPanel.setMaximumSize(new Dimension(WIDTH, LINKS_FIELD_HEIGHT));
		linksPanel.setBorder(BorderFactory.createTitledBorder("Links to"));
		this.add(linksPanel);	
	}
	
	/**
	 * This methods create a "Delete this country" button which allows users to delete
	 * the selected country from the map.
	 */
	private void createDeleteCountryBtn() {
		deleteCountryBtn = new JButton("Delete this country");
		deleteCountryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteCountryBtn.addActionListener(new ListenForButton());
		this.add(deleteCountryBtn);
	}
	
	/**
	 * This method cleans up all information of the CountryEdit panel.
	 */
	public void clear() {
		((JTextField) nameFieldPanel.getComponent(0)).setText("");
		continentComboBox.removeAllItems();
		linksPanel.removeAll();
		this.selectedCountry = null;
		this.revalidate();
	}
	
	/**
	 * Update the information of CountryEdit panel according to data stored in the controller.
	 */
	public void updateInfo() {
		Component[] components = nameFieldPanel.getComponents();
		if (this.selectedCountry != null) {
			this.selectedCountry.setName(((JTextField)components[0]).getText());
			this.selectedCountry.setContinentName((String)continentComboBox.getSelectedItem());
			controller.updateCountryInfo(this.selectedCountry);
		}
		this.selectedCountry = controller.getSelectedCountry();
		this.continentList = controller.getContinentList();
		
		((JTextField) components[0]).setText(this.selectedCountry.getName());
		this.updateContinentPanel();
		this.updateLinkPanel();
	}
	
	/**
	 * Update information of continent panel according to data stored in the controller.
	 */
	private void updateContinentPanel() {
		continentComboBox.removeItemListener(listenForComboBox);
		
		continentList = controller.getContinentList();
		continentComboBox.getItemCount();
		LinkedList<String> comboBoxContent = new LinkedList<String>();
		for (int i = 0; i < continentComboBox.getItemCount(); i++) {
			comboBoxContent.add(continentComboBox.getItemAt(i));
		}
		for (Continent continent : continentList) {
			if (!comboBoxContent.contains(continent.getName())) {
				continentComboBox.addItem(continent.getName());
			}
		}
		
		continentComboBox.setSelectedItem(this.selectedCountry.getContinentName());
		continentPanel.revalidate();
		continentPanel.repaint();
		continentComboBox.addItemListener(listenForComboBox);
	}	
	
	/**
	 * Update information of the link panel according to data stored in the controller.
	 */
	private void updateLinkPanel() {
		this.linksPanel.removeAll();
		selectedCountry = controller.getSelectedCountry();
		edgeHashMap = controller.getEdgeHashMap();
		countryHashMap = controller.getCountryHashMap();
		if (!edgeHashMap.isEmpty() && selectedCountry != null) {
			LinkedList<Point> pointList = edgeHashMap.get(selectedCountry.getLocation());
			if (pointList != null) {
				for (Point location : pointList) {
					addLink(countryHashMap.get(location).getName());
				}
			}
		}
		
		this.linksPanel.revalidate();
		this.linksPanel.repaint();
	}
	
	/**
	 * This method is called when the button in the continent panel is clicked.
	 * Add a new continent and assign a name and a value to it.
	 */
	private void addContinent() {
		String newContinentName;
		String continentValueText;
		int newContinentValue = 0;
		JTextField continentNameField = new JTextField();
		JTextField continentValueField = new JTextField();
		Object[] message = {
				"Continent name:", continentNameField,
				"Continent value:", continentValueField,
		};
			
		while (JOptionPane.showConfirmDialog(null, message,
				"New continent details", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			newContinentName = continentNameField.getText();
			continentValueText = continentValueField.getText();
			
			if (newContinentName.length() == 0) {
				JOptionPane.showMessageDialog(null, "The continent name cannot be blank.");
				continue;
			}
			
			if (controller.isContinentDuplicate(newContinentName)) {
				JOptionPane.showMessageDialog(null, "This continent name already exists.");
				continue;
			}
			
			if (controller.isMaxContinent()) {
				JOptionPane.showMessageDialog(null, "You cannot create more continent.");
				continue;
			}
								
			try {
				newContinentValue = Integer.valueOf(continentValueText);
				if (newContinentValue < 0) {
					JOptionPane.showMessageDialog(null, "Continent value should be a positive integer.");
					continue;
				}
			}
			catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(null, "The continent value should be an integer.");
				continue;
			}
			
			controller.addContinent(newContinentName, newContinentValue);
			this.updateContinentPanel();
			//this.continentComboBox.addItem(newContinentName);
			//this.continentComboBox.getModel().setSelectedItem(null);
			break;
		}		
	}


	/**
	 * This method creates a panel with a button and a label, and add this panel in the link panel.
	 * The button is used to delete the corresponding link of the selected country and the label shows
	 * an adjacent country the link links to.
	 * @param newCountryName is the name of an adjacent country.
	 */
	private void addLink(String newCountryName) {
		JPanel linkContent = new JPanel();
		linkContent.setPreferredSize(new Dimension(170,20));
		linkContent.setLayout(null);
		
		JButton deleteLinkBtn = new JButton("x");
		deleteLinkBtn.setBorder(null);
		deleteLinkBtn.setBounds(0,0,15,15);
		deleteLinkBtn.addActionListener(new ListenForButton());
		linkContent.add(deleteLinkBtn);
		
		JLabel countryNameLabel = new JLabel(newCountryName);
		countryNameLabel.setBounds(20, 0, 200, 20);
		linkContent.add(countryNameLabel);
		
		linksPanel.add(linkContent);
	}

	/**
	 * This function is called when a button of the link panel is clicked.
	 * Remove the corresponding link of the selected panel.
	 * @param selectedBtn is the button being clicked.
	 */
	private void removeLink(JButton selectedBtn) {
		JPanel linkContent = (JPanel) selectedBtn.getParent();
		String targetCountry = ((JLabel) linkContent.getComponent(1)).getText();
		linksPanel.remove(selectedBtn.getParent());
		
		linksPanel.revalidate();
		linksPanel.repaint();		
	}	

	/**
	 * This class is an action listener for buttons.
	 *
	 */
	private class ListenForButton implements ActionListener {
		
		/**
		 * Listen to a button and react when the button is clicked.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton selectedBtn = (JButton) e.getSource();
			if (selectedBtn == addContinentBtn) {
				addContinent();
			}
			else if (selectedBtn == deleteCountryBtn){
				controller.deleteCountry(controller.getSelectedCountry());
			}
			else {
				JLabel label = (JLabel) selectedBtn.getParent().getComponent(1);
				controller.removeLink(label.getText());
			}
		}
	}

	/**
	 * The item listener of the combo box.
	 */
	private class ListenForComboBox implements ItemListener {

		/**
		 * React when users select an item in the combo box.
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String continentName = (String)e.getItem();
				selectedCountry = controller.getSelectedCountry();
				selectedCountry.setContinentName(continentName);
				controller.updateCountryInfo(selectedCountry);
		    }
		}
	}
	
}