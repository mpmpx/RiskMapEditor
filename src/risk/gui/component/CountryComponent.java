package risk.gui.component;

import risk.gui.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;



public class CountryComponent extends JButton{
	private Shape shape;
	private Point componentLocation;
	private Point centerLocation;
	private Color backgroundColor;
	private Color borderColor;
	public static final int Radius = 8;
	
	// Country country;
		
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
	  
	// Draw and paint the button
	protected void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;	
		backgroundColor = getBackground();

		g2D.setColor(backgroundColor);
		g2D.fillOval(0, 0, getSize().width-1, getSize().height-1);
		super.paintComponent(g);
	}
	
	// Draw and paint the border of the button
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

	public void setLocationAtPoint(Point location) {
		this.centerLocation.setLocation(location);
		this.componentLocation.setLocation(location.x - Radius, location.y - Radius);
		
		this.setLocation(this.componentLocation);
	}
	// Hit detection.

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		
		return shape.contains(x, y);
	}

	public Point getCenterLocation() {
		return centerLocation;
	}
	
	
}
