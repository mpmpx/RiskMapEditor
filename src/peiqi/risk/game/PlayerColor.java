package risk.game;

import java.awt.Color;


/**
 * PlayerColor class contains a set of colors to be used for representing players on the map.
 */
public class PlayerColor {

	private static final int size = 6;
	private static int index = 0;
	
	private static Color[] colors = new Color[] {	Color.red,
													Color.blue,
													Color.green,
													Color.orange,
													Color.pink,
													Color.lightGray
												};
	
	
	/**
	 * Returns next color in the list.
	 * @return a color
	 */
	public static Color nextColor() {
		if (index >= size) {
			return null;
		} else {
			return colors[index++];
		}
	}
	
	/**
	 * Reset the color list.
	 */
	public static void reset() {
		index = 0;
	}
}
