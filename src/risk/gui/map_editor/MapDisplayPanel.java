package risk.gui.map_editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputListener;

import risk.contorller.MapEditorController;
import risk.game.Country;
import risk.gui.component.CountryComponent;

/**
 * MapDisplayPanel class is an instance of JPanl which provides a sub-panel of 
 * the MapEditor panel. It displays information of map in the panel. Circles represents countries
 * and their colors represent different continent. Lines between circles represent a link between
 * these two countries. Also, when click a circle, the border of the circle turns to black meaning
 * it is selected.
 */
class MapDisplayPanel extends JPanel implements MouseInputListener {

	public static final int WIDTH = 900;
	public static final int HEIGHT = 700;
	
	private int startX = 0;
	private int startY = 0;
	private int destX = 0;
	private int destY = 0;
	
	private JScrollPane scrollPane;
	private MapEditorController controller;	
	private Point cursorLocation;
	private HashMap<Point, Country> countryHashMap;
	private HashMap<String, Color> continentColorHashMap;
	private HashMap<Point, LinkedList<Point>> edgeList;
	
	/**
	 * Constructor of the class. Initialize all class variables, add listeners,
	 * and create a scroll panel to wrap the MapDisplay panel.
	 * @param controller is the controller of the MapEditor panel. 
	 */
	public MapDisplayPanel(MapEditorController controller) {
		this.setLayout(null);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));	
		this.controller = controller;
		this.cursorLocation = new Point(0,0);
		this.countryHashMap = controller.getCountryHashMap();
		this.continentColorHashMap = controller.getContinentColorHashMap();
		this.edgeList = controller.getEdgeHashMap();
		
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		scrollPane = new JScrollPane(this);
		scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));	
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
		
	}
	
	/**
	 * Get the scroll panel.
	 * @return the scroll panel.
	 */
	public JScrollPane getScrollPane(){

		return scrollPane;
	}
	
	/**
	 * Overwrite the paint method which paints all existing edges on the display area.
	 * @param g is the graph.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		edgeList = controller.getEdgeHashMap();		
		
		for (Point startLocation : edgeList.keySet()) {
			for (Point destLocation : edgeList.get(startLocation)) {
				g.drawLine(startLocation.x, startLocation.y, 
							destLocation.x, destLocation.y);
			}
		}
		
		// Draw a temporary line on the screen
		g.drawLine(startX, startY, destX, destY);
		
		if (controller.getAddCountryFlag() == true && this.isEnabled() == true) {
			g.drawOval(cursorLocation.x - CountryComponent.Radius, 
					cursorLocation.y - CountryComponent.Radius, 
					CountryComponent.Radius * 2, CountryComponent.Radius * 2);
		}
	}
	
	/**
	 * This method cleans up all things in the display area.
	 */
	public void clear() {
		this.removeAll();
		this.repaint();
	}
	
	/**
	 * Repaint and Update information of the MapDisplay 
	 * panel according to data stored in the controller.
	 */
	public void updateInfo() {
		countryHashMap = controller.getCountryHashMap();
		continentColorHashMap = controller.getContinentColorHashMap();
		edgeList = controller.getEdgeHashMap();
		
		Component[] buttons = this.getComponents();
		for (Component btn : buttons) {
			if (btn instanceof CountryComponent) {
				Country country = countryHashMap.get(((CountryComponent)btn).getCenterLocation());
				Color continentColor = continentColorHashMap.get(country.getContinentName());
				if (country.getContinentName() != null) {
					((CountryComponent)btn).setForeground(continentColor);					
					((CountryComponent)btn).setBackground(continentColor);
				}
				btn.revalidate();
				btn.repaint();
			}
		}		
		this.repaint();
	}
	
	/**
	 * Unselect all countries in the display area.
	 */
	public void unSelectCountry() {
		Component[] buttons = this.getComponents();
		for (Component btn : buttons) {
			if (btn instanceof CountryComponent) {
				((CountryComponent)btn).setSelected(false);
			}
		}
	}

	/**
	 * Add and draw a new circle representing a country on the display area.
	 * @param country
	 */
	public void addCountry(Country country) {
		CountryComponent countryComponent = new CountryComponent();
		countryComponent.addActionListener(new countryActionListener());
		countryComponent.setBackground(new Color(123,123,123));
		countryComponent.setForeground(new Color(123,123,123));
		countryComponent.setLocationAtPoint(country.getLocation());
					
		this.add(countryComponent);
		this.repaint();
		this.revalidate();
	}
	
	/**
	 * Erase and delete a circle based on given location on the display area.
	 * @param location is the location of the circle to be removed.
	 */
	public void removeCountry(Point location) {
		Component[] buttons = this.getComponents();
		for (Component btn : buttons) {
			if (btn instanceof CountryComponent) {
				if (((CountryComponent)btn).getCenterLocation().equals(location)){
					this.remove(btn);
				}
			}
		}
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Implements a function of the MouseListener which is called when the mouse is clicked
	 * on the display area. When creating a new country, this function creates and adds a new
	 * circle on the display area.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		countryHashMap = controller.getCountryHashMap();
		
		if (this.isEnabled() == true) {
			if (controller.getAddCountryFlag() == true) {
				if (countryHashMap.size() > 0) {
					for (Point point : countryHashMap.keySet()) {
						if (Math.hypot(point.getX() - e.getX(), point.getY()- e.getY()) < CountryComponent.Radius * 2){
							JOptionPane.showMessageDialog(null, "You cannot place a country here");
							return;
						}
					}
				}
				
				CountryComponent countryComponent = new CountryComponent();
				countryComponent.addActionListener(new countryActionListener());
				countryComponent.setBackground(new Color(123,123,123));
				countryComponent.setForeground(new Color(123,123,123));
				countryComponent.setLocationAtPoint(e.getPoint());
							
				this.add(countryComponent);
				controller.addCountry(countryComponent);
				this.repaint();
				this.revalidate();
			}

		}
	}

	/**
	 * Implements a function of the MouseListener which is called when the mouse is released.
	 * When dragging and creating a link between two circles, if the mouse is released inside another
	 * circle which has not connected to the selected circle, a new line will be created between 
	 * the selected circle and this circle.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {	
		Country selectedCounty = controller.getSelectedCountry();
		if (this.isEnabled() == true &&controller.getAddCountryFlag() == false && selectedCounty != null) {
			Component[] allComponents = this.getComponents();
			for (Component component : allComponents) {
				if (component instanceof CountryComponent) {
					CountryComponent button = (CountryComponent)component;
					if (Math.hypot(button.getCenterLocation().x - e.getX(),
							button.getCenterLocation().y - e.getY()) < CountryComponent.Radius) {
						destX = button.getCenterLocation().x;
						destY = button.getCenterLocation().y;				

						if (startX != destX && startY != destY) {
							controller.addLink(new Point(startX, startY), new Point(destX, destY));
						}
						startX = 0;
						startY = 0;
						destX = 0;
						destY = 0;
						this.repaint();
						return;
					}
				}
			}
		}
		
		startX = 0;
		startY = 0;
		destX = 0;
		destY = 0;
		this.repaint();
	}

	/**
	 * Implements a function of the MouseListener which is called when dragging the mouse. 
	 * When a circle is selected, dragging mouse on the display panel dynamically creates a line
	 * from the selected circle to the position of the mouse.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		countryHashMap = controller.getCountryHashMap();
		Country selectedCounty = controller.getSelectedCountry();
		if (this.isEnabled() == true && controller.getAddCountryFlag() == false && selectedCounty != null) {
			if (countryHashMap.containsValue(controller.getSelectedCountry())) {
				this.startX = controller.getSelectedCountry().getX();
				this.startY = controller.getSelectedCountry().getY();
				this.destX = e.getX();
				this.destY = e.getY();
				this.repaint();
			}
		}
	}

	/**
	 * Implements a function of the MouseListener which is called when moving the mouse. 
	 * Get the location of the mouse when moving it.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {		
		if (controller.getAddCountryFlag() == true) {
			cursorLocation.setLocation(e.getPoint());
			repaint();
		}
	}

	/**
	 * Unused methods of the MouseListener.
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	
	/**
	 * Unused methods of the MouseListener.
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Unused methods of the MouseListener.
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	/**
	 * This is an actionListener needs to be bind to country components
	 */
	private class countryActionListener implements ActionListener {
		
		/**
		 * This method is called when a circle is clicked. Set the clicked circle 
		 * as the selected country.
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			unSelectCountry();
			CountryComponent selectedCountryComponent = (CountryComponent) event.getSource();
			selectedCountryComponent.setSelected(true);
			controller.setSelectedCountry(selectedCountryComponent.getCenterLocation());
		}
	}




}