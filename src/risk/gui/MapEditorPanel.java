package risk.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class MapEditorPanel extends JPanel {

	JButton returnButton;
	
	public MapEditorPanel() {
		

		
		JButton button1 = new JButton("MapEditor");
		button1.setToolTipText("This is a button");
		this.add(button1);
		
		
		returnButton = new JButton("Return");
		ListenForButton lForButton = new ListenForButton();
		returnButton.addActionListener(lForButton);
		this.add(returnButton);
	}
	

	private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == returnButton) {
				MainFrame frame = MainFrame.getInstance();
				frame.SetCurrentPanel(MainFrame.MENUPANEL);
				frame.SetSize(500, 600);
			}
			
		}
		
	}
}
