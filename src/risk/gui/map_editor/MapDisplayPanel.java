package risk.gui.map_editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputListener;

import risk.contorller.MapEditorController;
import risk.gui.component.CountryComponent;

class MapDisplayPanel extends JPanel implements MouseInputListener {

	private static MapDisplayPanel mapDisplayPanel;	
	private JScrollPane scrollPane;
	private static final int WIDTH = 900;
	private static final int HEIGHT = 700;
	private static MapEditorController controller;	

	
	
	private Point cursorLocation;

	
	private MapDisplayPanel() {
		this.setLayout(null);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(2000,2000));	
		controller = MapEditorPanel.getController();
		cursorLocation = new Point(0,0);
		HashMap<Point, CountryComponent> countryComponentHashMap = controller.getCountryComponentHashMap();
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		if (!countryComponentHashMap.isEmpty()) {
			for (CountryComponent countryComponent : countryComponentHashMap.values()) {
				add(countryComponent);
			}
		}
	}
	
	public static MapDisplayPanel getInstance(){
		if (mapDisplayPanel == null) {
			mapDisplayPanel = new MapDisplayPanel();
		}
		return mapDisplayPanel;
	}
	
	public JScrollPane getScrollPane(){
		if (mapDisplayPanel == null) {
			mapDisplayPanel = new MapDisplayPanel();
		}
		
		scrollPane = new JScrollPane(mapDisplayPanel);
		scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));	
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
		return scrollPane;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (controller.getAddCountryFlag() == true && this.isEnabled() == true) {
			g.drawOval(cursorLocation.x - CountryComponent.Radius, 
					cursorLocation.y - CountryComponent.Radius, 
					CountryComponent.Radius * 2, CountryComponent.Radius * 2);
		}
	}
	
	public void addButton() {
		
	}


	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		HashMap<Point, CountryComponent> countryComponentHashMap = controller.getCountryComponentHashMap();
		
		if (this.isEnabled() == true) {
			if (controller.getAddCountryFlag() == true) {
				for (Point point : countryComponentHashMap.keySet()) {
					if ((int)Math.hypot(point.getX() - e.getX(), point.getY()- e.getY()) < CountryComponent.Radius * 2){
						JOptionPane.showMessageDialog(null, "You cannot place a country here");
						return;
					}
				}
			
				CountryComponent countryComponent = new CountryComponent();
				countryComponent.addActionListener(new countryActionListener());
				countryComponent.setBackground(new Color(123,123,123));
				countryComponent.setForeground(new Color(123,123,123));
				countryComponent.setLocationAtPoint(e.getPoint());
				this.add(countryComponent);
				controller.addCountry(countryComponent);
			
				this.revalidate();
				this.repaint();
			}
		
		
		


			for (CountryComponent countryComponent : countryComponentHashMap.values()) {
				Point location = countryComponent.getLocation();
				System.out.println(location.x+", "+location.y);
				//		if (countryComponent.contains(e.getPoint())){
		//			((MapEditorPanel)this.getParent().getParent().getParent()).enableEditPanel();
		//		}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		System.out.println("mouse entered display panel");
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("mouse left display panel");
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (controller.getAddCountryFlag() == true) {
			cursorLocation.setLocation(e.getPoint());
			repaint();
		}
	}
	
	private class countryActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			 CountryComponent countryComponent = (CountryComponent) event.getSource();
		//	 countryComponent.setForeground(Color.black);
			 countryComponent.setSelected(true);
			 controller.setSelectedCountry(countryComponent);
			 MapEditorPanel rootPanel = (MapEditorPanel) mapDisplayPanel.getParent().getParent().getParent();//countryComponent.getParent().getParent().getParent().getParent();
			 
			 System.out.println(rootPanel.getClass().getName());
			 
			 rootPanel.updateAll();
		}

		private MapEditorPanel getParent() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
}