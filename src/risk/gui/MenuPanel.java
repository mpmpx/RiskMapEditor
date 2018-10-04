package risk.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

	private static MenuPanel menuPanel;
	
	JButton button2;
	private MenuPanel() {
		this.setLayout(null );
		//GridBagConstraints c = new GridBagConstraints();
		

		JButton button1 = new JButton("Play");
		button1.setToolTipText("This is a button");
		button1.setBounds(175,100,150,60);
		this.add(button1);
		//this.setLocation(0,0);


		button2 = new JButton("MapEditor");
		ListenForButton lForButton = new ListenForButton();
		button2.addActionListener(lForButton);
		button2.setBounds(175,300,150,60);
		this.add(button2);
		
		
	}
	
	
	public static MenuPanel getInstance() {
		if (menuPanel == null) {
			menuPanel = new MenuPanel();
		}
		return menuPanel;
	}
	
	private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == button2) {
				MainFrame frame = MainFrame.getInstance();
				frame.SetCurrentPanel(MainFrame.MAPEDITORPANEL);

				frame.SetSize(800, 800);
			}
			
		}
		
	}
}
