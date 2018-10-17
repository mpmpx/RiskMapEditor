package risk.gui.map_editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputListener;

import risk.contorller.MapEditorController;
import risk.game.Country;
import risk.gui.component.CountryComponent;

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
	
	
	public JScrollPane getScrollPane(){

		return scrollPane;
	}

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
	
	public void clear() {
		this.removeAll();
		this.repaint();
	}
	
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
	
	public void unSelectCountry() {
		Component[] buttons = this.getComponents();
		for (Component btn : buttons) {
			if (btn instanceof CountryComponent) {
				((CountryComponent)btn).setSelected(false);
			}
		}
	}

	public void addCountry(Country country) {
		CountryComponent countryComponent = new CountryComponent();
		countryComponent.addActionListener(new countryActionListener());
		countryComponent.addMouseListener(new countryMouseListener());
		countryComponent.setBackground(new Color(123,123,123));
		countryComponent.setForeground(new Color(123,123,123));
		countryComponent.setLocationAtPoint(country.getLocation());
					
		this.add(countryComponent);
		this.repaint();
		this.revalidate();
	}
	
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
				countryComponent.addMouseListener(new countryMouseListener());
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

	@Override
	public void mouseReleased(MouseEvent e) {	
		//System.out.println("display panel: released");
		//System.out.println("display panel release at " + e.getX() + ", " + e.getY());
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

	@Override
	public void mouseDragged(MouseEvent e) {
		countryHashMap = controller.getCountryHashMap();
		Country selectedCounty = controller.getSelectedCountry();
		if (this.isEnabled() == true && controller.getAddCountryFlag() == false && selectedCounty != null) {
			if (countryHashMap.containsValue(controller.getSelectedCountry())) {
				this.startX = controller.getSelectedCountry().getX();
				this.startY = controller.getSelectedCountry().getY();
				
				//System.out.println(this.startX + ", " + this.startY);
				this.destX = e.getX();
				this.destY = e.getY();
				this.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		/* TODO Auto-generated method stub
			System.out.println(e.getX() + ", " + e.getY());
		*/
		
		if (controller.getAddCountryFlag() == true) {
			cursorLocation.setLocation(e.getPoint());
			repaint();
		}
	}
	

	
	private class countryActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			unSelectCountry();
			CountryComponent selectedCountryComponent = (CountryComponent) event.getSource();
			selectedCountryComponent.setSelected(true);
			controller.setSelectedCountry(selectedCountryComponent.getCenterLocation());
		}
	}
	
	private class countryMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//System.out.println("entered");
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("release");
			//System.out.println("button release at " + e.getX() + ", " + e.getY());
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}