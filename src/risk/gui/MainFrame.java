package risk.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import risk.gui.map_editor.MapEditorPanel;

/**
 * This class provides an instance of JFrame which is the main frame of the program.
 */
public class MainFrame extends JFrame {
	
	public static final String MENU_PANEL = "MenuPanel";
	public static final String MAP_EDITOR_PANEL = "MapEditorPanel";
	
	private static MainFrame mainFrame;
	private JPanel panelManager;
	private MenuPanel menuPanel;
	private MapEditorPanel mapEditorPanel;
		

	
	/**
	 * Constructor
	 */
	private MainFrame() {
		initFrame();
	}
	
	/**
	 * Initialize all class variables. Create MapEditor panel.
	 */
	private void initFrame() {
		setFrameSize(MenuPanel.WIDTH, MenuPanel.HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk");

		getContentPane().setBackground(Color.BLACK);
		this.setVisible(true);
		
		panelManager = new JPanel(new CardLayout());
		
		
		menuPanel = MenuPanel.getInstance();
		mapEditorPanel = new MapEditorPanel();
		panelManager.add(menuPanel, MENU_PANEL);

		setCurrentPanel(MENU_PANEL);	
		add(panelManager);
		panelManager.add(mapEditorPanel, MAP_EDITOR_PANEL);

	}
	
	/**
	 * Set the size and location of the frame.
	 * @param x is the width of the frame.
	 * @param y is the height of the frame.
	 */
	public void setFrameSize(int x, int y) {
		setSize(x, y);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
	
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		
		setLocation(xPos, yPos);
	}
	
	/**
	 * Set the current displaying panel of the frame.
	 * @param panelName is the name of panel to be shown.
	 */
	public void setCurrentPanel(String panelName) {
		CardLayout c = (CardLayout)(panelManager.getLayout());
		c.show(panelManager, panelName);

	}
	
	/**
	 * Get the instance of the frame.
	 * @return the instance of the frame.
	 */
	public static MainFrame getInstance() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		
		return mainFrame;
	}
	
}
