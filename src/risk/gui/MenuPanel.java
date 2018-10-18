package risk.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import risk.gui.map_editor.MapEditorPanel;

/**
 * This class is a panel which contains a button allowing users to navigate to other page by clicking.
 */
public class MenuPanel extends JPanel implements ActionListener{
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;
	
	private static MenuPanel menuPanel;
	JButton mapEditorBtn;
	
	/**
	 * Constructor initializes all class variables and create buttons.
	 */
	private MenuPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();


		mapEditorBtn = new JButton("MapEditor");
		mapEditorBtn.addActionListener(this);
		mapEditorBtn.setPreferredSize(new Dimension(200, 60));
		this.add(mapEditorBtn, c);
	}
	
	/**
	 * Get the instance of the MenuPanel.
	 * @return the instance of the MenuPanel.
	 */
	public static MenuPanel getInstance() {
		if (menuPanel == null) {
			menuPanel = new MenuPanel();
		}
		return menuPanel;
	}

	/**
	 * Implements a method of ActionListener. This method is call when a button
	 * is clicked and then navigate and display the corresponding page.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mapEditorBtn) {
			MainFrame frame = MainFrame.getInstance();
			frame.setCurrentPanel(MainFrame.MAP_EDITOR_PANEL);
			frame.setFrameSize(MapEditorPanel.WIDTH, MapEditorPanel.HEIGHT);
		}
	}
		
}
