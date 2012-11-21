package undecided;

import java.awt.Color;


/**
 * The Class ColorData is a simple rgb representation of a color.
 */
public class ColorData {
	
	/** The r. */
	int r;
	
	/** The g. */
	int g;
	
	/** The b. */
	int b;
	
	/** The a. */
	int a;

	/**
	 * Instantiates a new color data.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 * @param alpha the alpha
	 */
	public ColorData(int red, int green, int blue, int alpha) {
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor(){
		return new Color(r,g,b,a);
	}
}