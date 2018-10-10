package risk.gui.map_editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

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
import risk.gui.component.CountryComponent;


public class CountryEditPanel extends JPanel {
	private static final int HEIGHT = 500;
	private static final int WIDTH = 200;
	private static final int NAME_FIELD_HEIGHT = 60;
	private static final int CONTINENT_FIELD_HEIGHT = 60;
	private static final int LINKS_FIELD_HEIGHT = 400;
	private static final int SPACE_HEIGHT = 10;
	
	private static CountryEditPanel countryEditPanel;
	private MapEditorPanel rootPanel;
	
	private JPanel nameFieldPanel;
	private JPanel continentPanel;
	private JPanel linksPanel;
	private JButton addContinentBtn;
	private JButton deleteCountryBtn;
	private ArrayList<String> adjacentCountryList;
	private JComboBox<String> continentComboBox;
	private static MapEditorController controller;
	private HashMap<Point, CountryComponent> countryComponentHashMap;
	
	public CountryEditPanel() {
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createTitledBorder("Selected Territory"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		rootPanel = (MapEditorPanel) this.getParent();
		this.createNameFieldPanel();
		this.createContinentFieldPanel();
		this.createLinksPanel();
		this.createDeleteCountryBtn();
		controller = MapEditorPanel.getController();
		adjacentCountryList = new ArrayList<String>(10);
		
		
		/*for (int i = 0; i < 10; i ++) {
			this.addLink("i: " + i);
			adjacentCountryList.add("i: " + i);
		}*/
	}
	
	public static CountryEditPanel getInstance(){
		if (countryEditPanel == null) {
			countryEditPanel = new CountryEditPanel();
		}
		
		return countryEditPanel;
	}
	
	private void createNameFieldPanel() {
		this.add(Box.createRigidArea(new Dimension(WIDTH, SPACE_HEIGHT)));
		
		nameFieldPanel = new JPanel();
		nameFieldPanel.setMaximumSize(new Dimension(WIDTH, NAME_FIELD_HEIGHT));
		nameFieldPanel.setBorder(BorderFactory.createTitledBorder("Name"));
		JTextField nameField = new JTextField(15);
		nameFieldPanel.add(nameField);
		this.add(nameFieldPanel);		
	}
	
	private void createContinentFieldPanel() {
		this.add(Box.createRigidArea(new Dimension(WIDTH, SPACE_HEIGHT)));
		
		continentPanel = new JPanel();
		continentPanel.setMaximumSize(new Dimension(WIDTH, CONTINENT_FIELD_HEIGHT));
		continentPanel.setBorder(BorderFactory.createTitledBorder("Continent"));
		continentComboBox = new JComboBox<>();
		continentComboBox.setPreferredSize(new Dimension(130,20));
		continentPanel.add(continentComboBox);
		
		addContinentBtn = new JButton("+");
		addContinentBtn.setBorder(null);
		addContinentBtn.setPreferredSize(new Dimension(20, 20));
		addContinentBtn.addActionListener(new ListenForButton());
		continentPanel.add(addContinentBtn);
		this.add(continentPanel);
	}
	
	private void createLinksPanel() {
		this.add(Box.createRigidArea(new Dimension(WIDTH, SPACE_HEIGHT)));
		
		linksPanel = new JPanel();
		linksPanel.setLayout(new FlowLayout(FlowLayout.LEFT));		
		linksPanel.setMaximumSize(new Dimension(WIDTH, LINKS_FIELD_HEIGHT));
		linksPanel.setBorder(BorderFactory.createTitledBorder("Links to"));
		this.add(linksPanel);	
	}
	
	private void createDeleteCountryBtn() {
		deleteCountryBtn = new JButton("Delete this country");
		deleteCountryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteCountryBtn.addActionListener(new ListenForButton());
		this.add(deleteCountryBtn);
	}
		
	/* methods related to continentPanel */
	
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
				newContinentValue = Integer.valueOf(continentValueText = continentValueField.getText());
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
			updateContinentPanel();
			break;
		}		
	}

	private void updateContinentPanel() {
		ArrayList<String> continentList = controller.getContinentList();
		
		continentComboBox.removeAllItems();
		for (String continentName : continentList) {
			System.out.println(continentName);
			continentComboBox.addItem(continentName);
		}
		
		continentPanel.revalidate();
		continentPanel.repaint();
	}
	/* methods related to linksPanel */
	
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

	private void removeLink(JButton selectedBtn) {
		JPanel linkContent = (JPanel) selectedBtn.getParent();
		String targetCountry = ((JLabel) linkContent.getComponent(1)).getText();
		linksPanel.remove(selectedBtn.getParent());
		adjacentCountryList.remove(targetCountry);
		
		linksPanel.revalidate();
		linksPanel.repaint();		
	}	



	private class ListenForButton implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			JButton selectedBtn = (JButton) event.getSource();
			if (selectedBtn == addContinentBtn) {
				addContinent();
			}
			else if (selectedBtn == deleteCountryBtn){
				controller.deleteCountry();
			}
			else {
				removeLink(selectedBtn);
			}
		}
	}
}