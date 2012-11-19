package undecided;

import java.awt.Color;

public class ColorData {
	int r;
	int g;
	int b;
	int a;

	public ColorData(int red, int green, int blue, int alpha) {
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}
	
	public Color getColor(){
		return new Color(r,g,b,a);
	}
}