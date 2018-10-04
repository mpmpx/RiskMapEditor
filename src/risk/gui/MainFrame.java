package risk.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private static MainFrame mainFrame;
	private JPanel panelManager;
	private MenuPanel menuPanel;
	private MapEditorPanel mapEditorPanel;
	
	
	public static final String MENUPANEL = "MenuPanel";
	public static final String MAPEDITORPANEL = "MapEditorPanel";
	
	private MainFrame() {
		InitFrame();
	}
	
	private void InitFrame() {
		this.SetSize(500, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Risk");
		this.setVisible(true);
		
		panelManager = new JPanel(new CardLayout());
		
		
		menuPanel = MenuPanel.getInstance();
		mapEditorPanel = new MapEditorPanel();
		panelManager.add(menuPanel, MENUPANEL);
		panelManager.add(mapEditorPanel, MAPEDITORPANEL);
		
		this.add(panelManager);


	}
	
	public void SetSize(int x, int y) {
		this.setSize(x, y);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
	
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		
		this.setLocation(xPos, yPos);
	}
	
	public void SetCurrentPanel(String panelName) {
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
