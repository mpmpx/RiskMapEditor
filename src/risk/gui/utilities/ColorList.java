package risk.gui.utilities;

import java.awt.Color;
import java.util.Stack;

public class ColorList {
	private static int index = 0;
	private static final Color[] COLOR_LIST = {
			new Color(220,35,35), new Color(223,181,32), 
			new Color(166,224,31), new Color(24,232,220),
			new Color(26,107,230), new Color(153,26,230), 
			new Color(248,171,245), new Color(201,74,71),
			new Color(223,115,32), new Color(215,224,31), 
			new Color(94,224,31), new Color(24,174,231),
			new Color(35,24,231), new Color(231,24,225), 
			new Color(243,133,174), new Color(202,126,70),
			new Color(142,113,114), new Color(174,172,81), 
			new Color(77,179,135), new Color(77,128,179),
			new Color(84,77,179), new Color(152,74,181), 
			new Color(118,137,126), new Color(116,124,139),
			new Color(174,137,81), new Color(108,176,79), 
			new Color(78,172,177), new Color(75,106,180),
			new Color(122,74,181), new Color(181,74,165), 
			new Color(114,143,112), new Color(142,140,113),			
	};
	private static Stack<Color> stack;
	
	private ColorList() {
		stack = new Stack<Color>();
		
		for (int i = COLOR_LIST.length - 1; i >= 0; i--) {
			stack.push(COLOR_LIST[i]);
		}
	}
	
	public static Color get() {
		return stack.pop();
	}
	
	public static void put(Color color) {
		stack.push(color);
	}
}
