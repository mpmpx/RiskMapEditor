package risk.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import risk.gui.map_editor.MapEditorPanel;


public class MainFrame extends JFrame {
	
	public static final String MENU_PANEL = "MenuPanel";
	public static final String MAP_EDITOR_PANEL = "MapEditorPanel";
	
	private static MainFrame mainFrame;
	private JPanel panelManager;
	private MenuPanel menuPanel;
	private MapEditorPanel mapEditorPanel;
		

	
	private MainFrame() {
		initFrame();
	}
	
	private void initFrame() {
		SetSize(MenuPanel.WIDTH, MenuPanel.HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk");

		getContentPane().setBackground(Color.BLACK);
		this.setVisible(true);
		
		panelManager = new JPanel(new CardLayout());
		
		
		menuPanel = MenuPanel.getInstance();
		mapEditorPanel = MapEditorPanel.getInstance();
		panelManager.add(menuPanel, MENU_PANEL);

		setCurrentPanel(MENU_PANEL);	
		add(panelManager);
		panelManager.add(mapEditorPanel, MAP_EDITOR_PANEL);

	}
	
	public void SetSize(int x, int y) {
		setSize(x, y);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
	
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		
		setLocation(xPos, yPos);
	}
	
	public void setCurrentPanel(String panelName) {
		CardLayout c = (CardLayout)(panelManager.getLayout());
		c.show(panelManager, panelName);

	}
	
	public static MainFrame getInstance() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		
		return mainFrame;
	}
	
}
