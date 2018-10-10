package risk.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import risk.gui.map_editor.MapEditorPanel;

public class MenuPanel extends JPanel {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;
	
	private static MenuPanel menuPanel;
	
	JButton button2;
	private MenuPanel() {
		this.setLayout(null);
		//GridBagConstraints c = new GridBagConstraints();

		JButton button1 = new JButton("Play");
		button1.setToolTipText("This is a button");
		button1.setBounds(175,100,150,60);
		this.add(button1);
		//this.setLocation(0,0);


		button2 = new JButton("MapEditor");
		button2.addActionListener(new ListenForButton());
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
				frame.setCurrentPanel(MainFrame.MAP_EDITOR_PANEL);

				frame.SetSize(MapEditorPanel.WIDTH, MapEditorPanel.HEIGHT);
			}
			
		}
		
	}
}
