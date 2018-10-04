package risk.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DetailsPanel extends JPanel {

	public DetailsPanel() {
		Dimension size = getPreferredSize();
		size.width = 250;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Personal Details"));
		
		JLabel nameLabel = new JLabel("Name: ");
		JLabel occupationLabel = new JLabel("Occupation: ");
		
		JTextField nameField = new JTextField(10);
		JTextField occupationField = new JTextField(10);
		
		JButton addBtn = new JButton("Add");
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		//// First column ////
		
		gc.anchor = GridBagConstraints.LINE_END;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;
		add(nameLabel, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		add(occupationLabel, gc);
		
		//// Second column ////
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 0;
		add(nameField, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		add(occupationField, gc);
		
		// Final row //
		
		gc.weighty = 10;
		
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 1;
		gc.gridy = 2;
		add(addBtn, gc);
		
	}
	
    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        Line2D lin = new Line2D.Float(0, 0, 50, 50);
        g2.draw(lin);
        g2.draw(new Line2D.Double(120, 50, 240, 50));
    }
}
