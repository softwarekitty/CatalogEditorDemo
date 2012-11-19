package facade;

import gui.widget.SaveButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;



//since there can be multiple views of a version, they must all synch with each other as modified.
//That is to say, the variables in this facade should always be identical to those in the views, and visa versa.
//This facade is meant to make that and other tasks more straightforward
public class VersionFacade {
	//private Element version;
	private ArrayList<String> facadeNames;
	private HashMap<String,StringFacade> facades;
	public static final String[] names = {"TITLE","DESCRIPTION","PREREQ"};

	public VersionFacade(Element version, SaveButton saveButton) {
		
		facadeNames = new ArrayList<String>(Arrays.asList(names));
		
		//this.version = version;
		facades = new HashMap<String,StringFacade>();
		for(String name:facadeNames){
			facades.put(name, new StringFacade(version.getChild(name),saveButton,this));
		}
		
		// set up at first to match
//		title = new StringFacade(version.getChild("TITLE"), saveButton);
//		prereq = new StringFacade(version.getChild("PREREQ"),saveButton);
//		description = new StringFacade(version.getChild("DESCRIPTION"),saveButton);
		// description = version.getChildText("DESCRIPTION");
		// prereq = version.getChildText("PREREQ");
	}
	
	public ArrayList<String> getFacadeNames(){
		return facadeNames;
	}
	
	public StringFacade getFacade(String key){
		return facades.get(key);
	}
	
	public void removeListeners(String prefix){
		Collection<StringFacade> list = facades.values();
		for(StringFacade sf: list){
			sf.removeListenersWithPrefix(prefix);
		}
	}

//	public StringFacade getTitle() {
//		return title;
//	}

	// private boolean experimental;
	// private boolean sfOnly;
//	private StringFacade title;
	// private ArrayList<String> crossDesignators;
	// private String dual;//improve?
	// private int primaryContactHours;
	// private int secondaryContactHours;
	// private boolean arranged;
	// private boolean required;
	// private boolean repeatable;
	// private int maxCreditCount;
	// private int minHours;
	// private int maxHours;
	// private ArrayList<Pair<String,String>> offered;
//	private StringFacade prereq;
//	private StringFacade description;
	// private boolean nonMajorGraduateCredit;
	// private ArrayList<String> notes;
	// private ArrayList<String> comments;
	//
	//
	//
	// private ArrayList<Boolean> booleanAttributes;

//	public StringFacade getPrereq() {
//		return prereq;
//	}
//
//	public StringFacade getDescription() {
//		return description;
//	}

	// <VERSION lastEditorID="0" lastEdited="01/01/2011" catalogYear="2012"
	// accepted="true" experimental="false" sfOnly="false">
	// <TITLE>Intro to Computerzzzz</TITLE>
	// <CROSS>
	// <PROGRAM designator="Cpr E" />
	// </CROSS>
	// <DUAL>300,400</DUAL>
	// <CONTACTHOURS>
	// <PRIMARY>3</PRIMARY>
	// <SECONDARY>0</SECONDARY>
	// </CONTACTHOURS>
	// <CREDIT arranged="false" required="false" repeatable="true"
	// maxCreditCount="9">
	// <MINHOURS>3</MINHOURS>
	// <MAXHOURS>3</MAXHOURS>
	// </CREDIT>
	// <OFFERED>
	// <TERM yearsOffered="all">F</TERM>
	// <TERM yearsOffered="even">S</TERM>
	// <TERM yearsOffered="odd">SS</TERM>
	// </OFFERED>
	// <PREREQ>blah blah blah...any String here</PREREQ>
	// <DESCRIPTION>Students learn to think about binary signals</DESCRIPTION>
	// <NOTES nonMajorGraduateCredit="false">
	// <NOTE>Field Trip</NOTE>
	// </NOTES>
	// <COMMENTS />
	// </VERSION>
}
