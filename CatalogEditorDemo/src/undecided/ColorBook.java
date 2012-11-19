package undecided;

import facade.VersionFacade;

import java.awt.Color;
import java.util.HashMap;

@SuppressWarnings("serial")
public class ColorBook extends HashMap<String,ColorData>{
	private final String TRANSPARENT = "TRANSPARENT";
	private final String HIGHLIGHT = "HIGHLIGHT";
	private final String BACKGROUND = "BACKGROUND";
	
	private int[][] values = {{0,0,225},{216,165,32},{0,128,0}};
	private String[] names = {"BLUE","ORANGE","GREEN"};
	private HashMap<String,String> elementNameMap;
	
	public ColorBook(){
		super();
		int nNames = names.length;
		for(int i=0;i<nNames;i++){
			put(TRANSPARENT+names[i],new ColorData(values[i][0],values[i][1],values[i][2],20));
			put(HIGHLIGHT + names[i],new ColorData(values[i][0],values[i][1],values[i][2],80));
			put(BACKGROUND+names[i],new ColorData(values[i][0],values[i][1],values[i][2],200));

		}
		initElementNameMap();
	}
	
	private void initElementNameMap(){
		elementNameMap = new HashMap<String,String>();
		String[] facadeNames = VersionFacade.names;
		int length = facadeNames.length;
		for(int i=0;i<length;i++){
			elementNameMap.put(facadeNames[i], names[i]);
		}
	}
	
	public Color getTransparent(String facadeName){
		return get(TRANSPARENT+elementNameMap.get(facadeName)).getColor();
	}
	
	public Color getHighlight(String facadeName){
		return get(HIGHLIGHT+elementNameMap.get(facadeName)).getColor();
	}
	
	public Color getBackground(String facadeName){
		return get(BACKGROUND+elementNameMap.get(facadeName)).getColor();
	}
}
