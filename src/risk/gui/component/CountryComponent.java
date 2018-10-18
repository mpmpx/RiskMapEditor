package risk.gui.component;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

/**
 * This class defines a circular component which represent a 
 * city showing on the MapDisplay panel.
 */
public class CountryComponent extends JButton{
	private Shape shape;
	private Point componentLocation;
	private Point centerLocation;
	private Color backgroundColor;
	private Color borderColor;
	public static final int Radius = 8;
	
	/**
	 * Constructor of this class. Initialize all class variables.
	 */
	public CountryComponent() {
		setSize(Radius * 2, Radius * 2);	
		backgroundColor = this.getBackground();
		borderColor = this.getForeground();
		componentLocation = new Point();
		centerLocation = new Point();		

		// This call makes the JButton not to paint 
		// the background, which allows us to paint 
		// a round background.
		setContentAreaFilled(false);
	}
	  
	/**
	 * Draw and paint buttons and edges.
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;	
		backgroundColor = getBackground();

		g2D.setColor(backgroundColor);
		g2D.fillOval(0, 0, getSize().width-1, getSize().height-1);
		super.paintComponent(g);
	}
	
	/**
	 * Draw and paint the border of the button.
	 * @param g is the graph.
	 */
	protected void paintBorder(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		borderColor = getForeground();
		
		if (getModel().isRollover() || getModel().isSelected()) {
			g2D.setColor(Color.black);
		}
		else {
			g2D.setColor(borderColor);
		}
		g2D.setStroke(new BasicStroke(2)); // set the thickness of the border
	    g2D.drawOval(0, 0, getSize().width - 1, getSize().height -1);
	}

	/**
	 * Set the center location of the component.
	 * @param location is the location of component.
	 */
	public void setLocationAtPoint(Point location) {
		this.centerLocation.setLocation(location);
		this.componentLocation.setLocation(location.x - Radius, location.y - Radius);
		
		this.setLocation(this.componentLocation);
	}
	

	/**
	 * Check whether a point is inside the component.
	 * @param x is the x coordinate.
	 * @param y is the y coordinate.
	 */
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		
		return shape.contains(x, y);
	}

	/**
	 * Get the center location of the component instead of the real location of the component.
	 * @return the center location of the component.
	 */
	public Point getCenterLocation() {
		return centerLocation;
	}
	
	
}
