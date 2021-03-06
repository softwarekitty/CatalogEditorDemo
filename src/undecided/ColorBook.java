package undecided;

import java.awt.Color;
import java.util.HashMap;

import facade.AbstractFacade;
import gui.HeaderEditorPane;


/**
 * The Class ColorBook is a convenience for mapping colors to facades.
 */
@SuppressWarnings("serial")
public class ColorBook extends HashMap<String,ColorData>{
	
	/** The TRANSPARENT. */
	private final String TRANSPARENT = "TRANSPARENT";
	
	/** The HIGHLIGHT. */
	private final String HIGHLIGHT = "HIGHLIGHT";
	
	/** The BACKGROUND. */
	private final String BACKGROUND = "BACKGROUND";
	
	/** The values. */
	private int[][] values = {{255,165,0},{173,255,47},{220,20,60},{139,69,19},{53,50,204},{255,255,0},{255,105,180},
	{222,184,135},{255,69,0},{135,206,250},{255,0,255},{34,139,34},{46,139,87},{0,0,225},{106,90,205},{178,34,34},{107,142,35},{255,0,0}};
	
	/** The names. */
	private String[] names = {"ORANGE","GREENYELLOW","CRIMSON","SADDLE_BROWN","DARK_ORCHID","YELLOW","HOT_PINK","BURLYWOOD","ORANGERED",
			"LIGHTSKYBLUE","MAGENTA","FORESTGREEN","SEAGREEN","BLUE","SLATEBLUE","FIREBRICK","OLIVEDRAB","RED"};
	
	/** The element id map. */
	private HashMap<Integer,String> elementIDMap;
	
	/**
	 * Instantiates a new color book.
	 */
	public ColorBook(){
		super();
		int nNames = names.length;
		for(int i=0;i<nNames;i++){
			put(TRANSPARENT+names[i],new ColorData(values[i][0],values[i][1],values[i][2],20));
			put(HIGHLIGHT + names[i],new ColorData(values[i][0],values[i][1],values[i][2],80));
			put(BACKGROUND+names[i],new ColorData(values[i][0],values[i][1],values[i][2],200));

		}
		initElementIDMap();
	}
	
	/**
	 * Inits the element id map.
	 */
	private void initElementIDMap(){
		elementIDMap = new HashMap<Integer,String>();
		int i=0;
		for(int a:AbstractFacade.allConstants){
			elementIDMap.put(a, names[i++]);
		}
		elementIDMap.put(HeaderEditorPane.HEADER, "RED");
	}
	
	/**
	 * Gets the transparent.
	 *
	 * @param ID the ID
	 * @return the transparent
	 */
	public Color getTransparent(int ID){
		ColorData cd = get(TRANSPARENT+elementIDMap.get(ID));
		return cd==null?Color.RED:cd.getColor();
	}
	
	/**
	 * Gets the highlight.
	 *
	 * @param ID the ID
	 * @return the highlight
	 */
	public Color getHighlight(int ID){
		ColorData cd = get(HIGHLIGHT+elementIDMap.get(ID));
		return cd==null?Color.RED:cd.getColor();
	}
	
	/**
	 * Gets the background.
	 *
	 * @param ID the ID
	 * @return the background
	 */
	public Color getBackground(int ID){
		ColorData cd = get(BACKGROUND+elementIDMap.get(ID));
		return cd==null?Color.RED:cd.getColor();
	}
}
